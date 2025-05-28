#include "secrets.h"
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include "WiFi.h"

#define MQTT_MAX_PACKET_SIZE 1024
#include <PubSubClient.h>

#define AWS_IOT_PUBLISH_TOPIC   "topic_smart_classroom"
#define AWS_IOT_SUBSCRIBE_TOPIC "topic_smart_classroom"

#define CLASSROOM_NAME "G-203"
#define FLOOR_NUMBER 2
#define BLOCK_NAME "G"
#define CLASSROOM_CAPACITY 31

#define LED_PIN 2

WiFiClientSecure net = WiFiClientSecure();
PubSubClient client(net);

void publishStatus();

void printClientState(int state) {
  switch(state) {
    case -4: Serial.println("MQTT_CONNECTION_TIMEOUT"); break;
    case -3: Serial.println("MQTT_CONNECTION_LOST"); break;
    case -2: Serial.println("MQTT_CONNECT_FAILED"); break;
    case -1: Serial.println("MQTT_DISCONNECTED"); break;
    case 0: Serial.println("MQTT_CONNECTED"); break;
    default: Serial.println("MQTT_UNKNOWN_STATE"); break;
  }
}

void messageHandler(char* topic, byte* payload, unsigned int length) {
  Serial.print("Gelen mesaj - Topic: ");
  Serial.print(topic);
  Serial.print(" - Payload: ");
  
  char payloadStr[length + 1];
  memcpy(payloadStr, payload, length);
  payloadStr[length] = '\0';
  
  Serial.println(payloadStr);

  StaticJsonDocument<256> doc;
  DeserializationError error = deserializeJson(doc, payloadStr);
  
  if (error) {
    Serial.print("JSON parse hatası: ");
    Serial.println(error.c_str());
    return;
  }

  const char* classroomName = doc["classroomName"];
  int occupancy = doc["occupancy"];
  
  if (classroomName && strcmp(classroomName, CLASSROOM_NAME) == 0) {
    Serial.print("G-203 için kişi sayısı güncellendi: ");
    Serial.println(occupancy);
    
    if (occupancy > 0) {
      digitalWrite(LED_PIN, HIGH);
      Serial.println("LED AÇILDI - Sınıfta kişi var");
    } else {
      digitalWrite(LED_PIN, LOW);
      Serial.println("LED KAPANDI - Sınıf boş");
    }
  } else {
    Serial.println("Mesaj başka bir sınıf için, göz ardı ediliyor.");
  }
}

void publishStatus() {
  StaticJsonDocument<256> doc;
  
  doc["classroomName"] = CLASSROOM_NAME;
  doc["floorNumber"] = FLOOR_NUMBER;
  doc["blockName"] = BLOCK_NAME;
  doc["status"] = "online";
  doc["deviceType"] = "led_controller";
  
  char jsonBuffer[512];
  serializeJson(doc, jsonBuffer);

  Serial.print("Durum mesajı gönderiliyor: ");
  Serial.println(jsonBuffer);
  
  bool publishSuccess = client.publish(AWS_IOT_PUBLISH_TOPIC, jsonBuffer);
  Serial.print("Gönderim başarılı: ");
  Serial.println(publishSuccess ? "Evet" : "Hayır");
}

void connectAWS() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  Serial.println("Wi-Fi'ye bağlanılıyor...");

  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.print("WiFi bağlandı, IP: ");
  Serial.println(WiFi.localIP());

  net.setCACert(AWS_CERT_CA);
  net.setCertificate(AWS_CERT_CRT);
  net.setPrivateKey(AWS_CERT_PRIVATE);

  client.setServer(AWS_IOT_ENDPOINT, 8883);
  
  Serial.print("AWS IoT Endpoint: ");
  Serial.println(AWS_IOT_ENDPOINT);
  
  client.setCallback(messageHandler);

  Serial.print("AWS IoT'ye bağlanılıyor...");

  Serial.print("Thing Name: ");
  Serial.println(THINGNAME);

  while (!client.connected()) {
    Serial.print("MQTT bağlantısı deneniyor...");
    
    if (client.connect(THINGNAME)) {
      bool subSuccess = client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC);
      Serial.println("AWS IoT Bağlandı!");
      Serial.print("Topic'e abone olundu: ");
      Serial.print(AWS_IOT_SUBSCRIBE_TOPIC);
      Serial.print(" - Başarılı: ");
      Serial.println(subSuccess ? "Evet" : "Hayır");
      
      publishStatus();
      break;
    } else {
      Serial.print("Başarısız, rc=");
      Serial.print(client.state());
      printClientState(client.state());
      Serial.println(" 2 saniye sonra tekrar denenecek");
      delay(2000);
    }
  }

  if(!client.connected()){
    Serial.println("AWS IoT Timeout!");
    return;
  }
}

void setup() {
  Serial.begin(115200);
  delay(2000);
  
  Serial.println("\n\n=== ESP32 AWS IoT LED Kontrol - G-203 ===");
  Serial.println("Başlatılıyor...");
  
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  Serial.println("LED GPIO2'e konfigüre edildi");
  
  connectAWS();
}

void loop() {
  if (!client.connected()) {
    Serial.println("Bağlantı koptu! Yeniden bağlanılıyor...");
    connectAWS();
  }
  
  client.loop();
  
  static unsigned long lastCheck = 0;
  if (millis() - lastCheck > 10000) {
    lastCheck = millis();
    Serial.print("Bağlantı durumu: ");
    Serial.println(client.connected() ? "Bağlı" : "Bağlı değil");
    Serial.print("WiFi durumu: ");
    Serial.println(WiFi.status() == WL_CONNECTED ? "Bağlı" : "Bağlı değil");
  }
  
  delay(100);
}