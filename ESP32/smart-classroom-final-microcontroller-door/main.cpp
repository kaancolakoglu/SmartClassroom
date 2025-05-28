<#include <Arduino.h>
#include "WiFi.h"
#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "secrets.h"

#define MQTT_MAX_PACKET_SIZE 1024

#define AWS_IOT_PUBLISH_TOPIC   "topic_smart_classroom"
#define AWS_IOT_SUBSCRIBE_TOPIC "topic_smart_classroom"

#define CLASSROOM_NAME "G-203"
#define FLOOR_NUMBER 2
#define BLOCK_NAME "G"
#define CLASSROOM_CAPACITY 31

#define TRIG1 21   // Outer sensor
#define ECHO1 19
#define TRIG2 25   // Middle sensor
#define ECHO2 26
#define TRIG3 12   // Inner sensor 
#define ECHO3 14

#define MESAFE_ESIK 100
#define OLCUM_BEKLE 9
#define FILTRE_ORNEK_SAYISI 3
#define GECIS_ZAMAN_ASIMI 3000
#define MIN_GECIS_SURESI 150
#define SENSOR_BEKLE 1200
#define SENSOR_SOGUMA 1800
#define STABILITE_BEKLEME 2000
#define VAZGECME_SURESI 2000

#define BOSTA 0
#define S1_TETIKLENDI 1
#define S2_TETIKLENDI 2
#define S3_TETIKLENDI 3
#define S1_S2_SIRASI 4
#define S2_S3_SIRASI 5
#define S3_S2_SIRASI 6
#define S2_S1_SIRASI 7
#define GIRIS_DEVAM 8
#define CIKIS_DEVAM 9

int giris_sayisi = 0;
int cikis_sayisi = 0;
int currentOccupancy = 0;
int lastPublishedOccupancy = -1;
bool initialPublishDone = false;

float s1_degerler[FILTRE_ORNEK_SAYISI] = {0};
float s2_degerler[FILTRE_ORNEK_SAYISI] = {0};
float s3_degerler[FILTRE_ORNEK_SAYISI] = {0};
int okuma_indeksi = 0;

unsigned long s1_tetiklenme_zamani = 0;
unsigned long s2_tetiklenme_zamani = 0;
unsigned long s3_tetiklenme_zamani = 0;
unsigned long son_gecis_zamani = 0;
int durum = BOSTA;
int onceki_durum = BOSTA;
bool gecis_tamamlandi = false;
bool sensor_soguma_aktif = false;
unsigned long soguma_baslangic_zamani = 0;
bool s1_aktif = false, s2_aktif = false, s3_aktif = false;
bool onceki_s1_aktif = false, onceki_s2_aktif = false, onceki_s3_aktif = false;

WiFiClientSecure net = WiFiClientSecure();
PubSubClient client(net);

float mesafe_olc(int trig_pin, int echo_pin);
float filtrele(float degerler[], int boyut);
void gecis_algilama(float mesafe1, float mesafe2, float mesafe3);
void zaman_asimi_kontrol();
void updateOccupancy();
void publishMessage();
void connectAWS();
void messageHandler(char* topic, byte* payload, unsigned int length);

void setup() {
  pinMode(TRIG1, OUTPUT);
  pinMode(ECHO1, INPUT);
  pinMode(TRIG2, OUTPUT);
  pinMode(ECHO2, INPUT);
  pinMode(TRIG3, OUTPUT);
  pinMode(ECHO3, INPUT);
  
  delay(STABILITE_BEKLEME);
  
  for (int i = 0; i < FILTRE_ORNEK_SAYISI; i++) {
    s1_degerler[okuma_indeksi] = mesafe_olc(TRIG1, ECHO1);
    delayMicroseconds(500);
    s2_degerler[okuma_indeksi] = mesafe_olc(TRIG2, ECHO2);
    delayMicroseconds(500);
    s3_degerler[okuma_indeksi] = mesafe_olc(TRIG3, ECHO3);
  }
  
  connectAWS();
}

void loop() {
  unsigned long simdiki_zaman = millis();
  
  if (!client.connected()) {
    connectAWS();
  }
  
  client.loop();
  
  if (!initialPublishDone && client.connected()) {
    publishMessage();
    initialPublishDone = true;
  }
  
  if (sensor_soguma_aktif) {
    if (simdiki_zaman - soguma_baslangic_zamani > SENSOR_SOGUMA) {
      sensor_soguma_aktif = false;
    } else {
      delay(50);
      return;
    }
  }
  
  s1_degerler[okuma_indeksi] = mesafe_olc(TRIG1, ECHO1);
  delay(OLCUM_BEKLE);
  s2_degerler[okuma_indeksi] = mesafe_olc(TRIG2, ECHO2);
  delay(OLCUM_BEKLE);
  s3_degerler[okuma_indeksi] = mesafe_olc(TRIG3, ECHO3);
  
  okuma_indeksi = (okuma_indeksi + 1) % FILTRE_ORNEK_SAYISI;
  
  float mesafe1 = filtrele(s1_degerler, FILTRE_ORNEK_SAYISI);
  float mesafe2 = filtrele(s2_degerler, FILTRE_ORNEK_SAYISI);
  float mesafe3 = filtrele(s3_degerler, FILTRE_ORNEK_SAYISI);
  
  onceki_s1_aktif = s1_aktif;
  onceki_s2_aktif = s2_aktif;
  onceki_s3_aktif = s3_aktif;
  
  s1_aktif = (mesafe1 < MESAFE_ESIK && mesafe1 > 0);
  s2_aktif = (mesafe2 < MESAFE_ESIK && mesafe2 > 0);
  s3_aktif = (mesafe3 < MESAFE_ESIK && mesafe3 > 0);
  
  gecis_algilama(mesafe1, mesafe2, mesafe3);
  
  zaman_asimi_kontrol();
  
  updateOccupancy();
  publishMessage();
  
  delay(25);
}

void connectAWS() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED){
    delay(500);
  }

  net.setCACert(AWS_CERT_CA);
  net.setCertificate(AWS_CERT_CRT);
  net.setPrivateKey(AWS_CERT_PRIVATE);

  client.setServer(AWS_IOT_ENDPOINT, 8883);
  client.setCallback(messageHandler);

  while (!client.connected()) {
    if (client.connect(THINGNAME, NULL, NULL, NULL, 0, 0, NULL, 1)) {
      client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC);
      break;
    } else {
      delay(2000);
    }
  }
}


void updateOccupancy() {
  int newOccupancy = giris_sayisi - cikis_sayisi;
  
  if (newOccupancy < 0) {
    newOccupancy = 0;
    giris_sayisi = 0;
    cikis_sayisi = 0;
  }
  
  if (newOccupancy > CLASSROOM_CAPACITY) {
    newOccupancy = CLASSROOM_CAPACITY;
  }
  
  currentOccupancy = newOccupancy;
}

void publishMessage() {
  if (currentOccupancy != lastPublishedOccupancy) {
    StaticJsonDocument<256> doc;
    
    doc["classroomName"] = CLASSROOM_NAME;
    doc["floorNumber"] = FLOOR_NUMBER;
    doc["blockName"] = BLOCK_NAME;
    doc["occupancy"] = currentOccupancy;
    doc["classroomCapacity"] = CLASSROOM_CAPACITY;
    
    char jsonBuffer[512];
    serializeJson(doc, jsonBuffer);
    
    client.publish(AWS_IOT_PUBLISH_TOPIC, jsonBuffer);
    lastPublishedOccupancy = currentOccupancy;
  }
}

float mesafe_olc(int trig_pin, int echo_pin) {
  digitalWrite(trig_pin, LOW);
  delayMicroseconds(2);
  
  digitalWrite(trig_pin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig_pin, LOW);
  
  long sure = pulseIn(echo_pin, HIGH, 23200);
  
  if (sure == 0) {
    return 400.0;
  }
  
  float mesafe = (sure * 0.034) / 2;
  
  if (mesafe < 2.0) {
    return 400.0;
  }
  
  return mesafe;
}

float filtrele(float degerler[], int boyut) {
  float toplam = 0;
  int gecerli_sayisi = 0;
  
  for (int i = 0; i < boyut; i++) {
    if (degerler[i] > 0 && degerler[i] < 400) {
      toplam += degerler[i];
      gecerli_sayisi++;
    }
  }
  
  if (gecerli_sayisi > 0) {
    return toplam / gecerli_sayisi;
  } else {
    return 400.0;
  }
}

void gecis_algilama(float mesafe1, float mesafe2, float mesafe3) {
  unsigned long simdiki_zaman = millis();
  
  if (simdiki_zaman - son_gecis_zamani < SENSOR_BEKLE && son_gecis_zamani > 0) {
    return;
  }
  
  switch(durum) {
    case BOSTA:
      if (s1_aktif && !s2_aktif && !s3_aktif) {
        durum = S1_TETIKLENDI;
        s1_tetiklenme_zamani = simdiki_zaman;
        gecis_tamamlandi = false;
      }
      else if (s3_aktif && !s2_aktif && !s1_aktif) {
        durum = S3_TETIKLENDI;
        s3_tetiklenme_zamani = simdiki_zaman;
        gecis_tamamlandi = false;
      }
      break;
      
    case S1_TETIKLENDI:
      if (s2_aktif && !s3_aktif) {
        if (simdiki_zaman - s1_tetiklenme_zamani < GECIS_ZAMAN_ASIMI && 
            simdiki_zaman - s1_tetiklenme_zamani > MIN_GECIS_SURESI) {
          durum = GIRIS_DEVAM;
          s2_tetiklenme_zamani = simdiki_zaman;
        }
      }
      else if (s3_aktif) {
        durum = BOSTA;
      }
      else if (!s1_aktif && !s2_aktif && !s3_aktif && 
               simdiki_zaman - s1_tetiklenme_zamani > VAZGECME_SURESI) {
        durum = BOSTA;
      }
      break;
      
    case S3_TETIKLENDI:
      if (s2_aktif && !s1_aktif) {
        if (simdiki_zaman - s3_tetiklenme_zamani < GECIS_ZAMAN_ASIMI && 
            simdiki_zaman - s3_tetiklenme_zamani > MIN_GECIS_SURESI) {
          durum = CIKIS_DEVAM;
          s2_tetiklenme_zamani = simdiki_zaman;
        }
      }
      else if (s1_aktif) {
        durum = BOSTA;
      }
      else if (!s1_aktif && !s2_aktif && !s3_aktif && 
               simdiki_zaman - s3_tetiklenme_zamani > VAZGECME_SURESI) {
        durum = BOSTA;
      }
      break;
    
    case GIRIS_DEVAM:
      if (s3_aktif) {
        if (simdiki_zaman - s2_tetiklenme_zamani < GECIS_ZAMAN_ASIMI && 
            simdiki_zaman - s2_tetiklenme_zamani > MIN_GECIS_SURESI) {
          giris_sayisi++;
          gecis_tamamlandi = true;
          durum = BOSTA;
          son_gecis_zamani = simdiki_zaman;
          sensor_soguma_aktif = true;
          soguma_baslangic_zamani = simdiki_zaman;
        }
      }
      else if (s1_aktif && !s2_aktif && !s3_aktif) {
        durum = BOSTA;
      }
      break;
    
    case CIKIS_DEVAM:
      if (s1_aktif) {
        if (simdiki_zaman - s2_tetiklenme_zamani < GECIS_ZAMAN_ASIMI && 
            simdiki_zaman - s2_tetiklenme_zamani > MIN_GECIS_SURESI) {
          cikis_sayisi++;
          gecis_tamamlandi = true;
          durum = BOSTA;
          son_gecis_zamani = simdiki_zaman;
          sensor_soguma_aktif = true;
          soguma_baslangic_zamani = simdiki_zaman;
        }
      }
      else if (s3_aktif && !s2_aktif && !s1_aktif) {
        durum = BOSTA;
      }
      break;
  }
}

void zaman_asimi_kontrol() {
  unsigned long simdiki_zaman = millis();
  
  switch(durum) {
    case S1_TETIKLENDI:
      if (simdiki_zaman - s1_tetiklenme_zamani > GECIS_ZAMAN_ASIMI && !gecis_tamamlandi) {
        durum = BOSTA;
      }
      break;
      
    case S3_TETIKLENDI:
      if (simdiki_zaman - s3_tetiklenme_zamani > GECIS_ZAMAN_ASIMI && !gecis_tamamlandi) {
        durum = BOSTA;
      }
      break;
      
    case GIRIS_DEVAM:
      if (simdiki_zaman - s2_tetiklenme_zamani > GECIS_ZAMAN_ASIMI && !gecis_tamamlandi) {
        durum = BOSTA;
      }
      break;
      
    case CIKIS_DEVAM:
      if (simdiki_zaman - s2_tetiklenme_zamani > GECIS_ZAMAN_ASIMI && !gecis_tamamlandi) {
        durum = BOSTA;
      }
      break;
  }
}