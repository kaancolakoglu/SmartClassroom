import React, {useState} from 'react';
import BlockSelectionPage from './components/BlockSelectionPage';
import BlockHeader from './components/BlockHeader';
import FloorSelector from './components/FloorSelector';
import RoomGrid from './components/RoomGrid';

function App() {
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [selectedFloor, setSelectedFloor] = useState(0);
  const [roomData, setRoomData] = useState(null);
  const [totalCapacity, setTotalCapacity] = useState(0);
  const [availableFloors, setAvailableFloors] = useState([]);

  const API_BASE_URL = 'https://production.eba-nhxxj3xh.us-east-1.elasticbeanstalk.com';
  // Blok seçildiğinde çağrılacak fonksiyon
  const handleBlockSelect = async (blockId, blockName) => {
    setSelectedBlock({id: blockId, name: blockName});
    try {
      // Blok ID'sini kullanarak API'den sınıfları çek
      const response = await fetch(`${API_BASE_URL}/api/v1/classrooms/building/${blockId}`);
      if (!response.ok) {
        throw new Error('Veri çekilemedi');
      }
      const data = await response.json();
      
      // Mevcut katları belirle
      const floors = [...new Set(data.map(room => room.floorNumber))].sort();
      setAvailableFloors(floors);
      
      // İlk katı seç
      const firstFloor = floors.length > 0 ? floors[0] : 0;
      setSelectedFloor(firstFloor);
      
      // Seçilen kata ait odaları filtrele
      const roomsForFloor = data.filter(room => room.floorNumber === firstFloor);
      
      // Toplam kapasiteyi hesapla
      const totalCap = roomsForFloor.reduce((sum, room) => sum + room.classroomCapacity, 0);
      setTotalCapacity(totalCap);
      
      // Oda verilerini ayarla
      setRoomData({
        floors: floors,
        rooms: roomsForFloor.map(room => ({
          id: room.id,
          name: room.classroomName,
          current: room.occupancy,
          capacity: room.classroomCapacity
        }))
      });
    } catch (error) {
      console.error('Veri çekme hatası:', error);
      // Hata durumunda kullanıcıya bilgi verilebilir
    }
  };

  // Kat değiştiğinde çağrılacak fonksiyon
  const handleFloorChange = async (floor) => {
    setSelectedFloor(floor);
    if (selectedBlock) {
      try {
        const response = await fetch(`${API_BASE_URL}/api/v1/classrooms/building/${selectedBlock.id}`);
        if (!response.ok) {
          throw new Error('ERROR');
        }
        const data = await response.json();
        
        // Seçilen kata ait odaları filtrele
        const roomsForFloor = data.filter(room => room.floorNumber === floor);
        
        // Toplam kapasiteyi hesapla
        const totalCap = roomsForFloor.reduce((sum, room) => sum + room.classroomCapacity, 0);
        setTotalCapacity(totalCap);
        
        // Oda verilerini ayarla
        setRoomData({
          floors: availableFloors,
          rooms: roomsForFloor.map(room => ({
            id: room.id,
            name: room.classroomName,
            current: room.occupancy,
            capacity: room.classroomCapacity
          }))
        });
      } catch (error) {
        console.error('Veri çekme hatası:', error);
      }
    }
  };

  // Geri dönüş butonu için fonksiyon
  const handleBack = () => {
    setSelectedBlock(null);
    setSelectedFloor(0);
    setRoomData(null);
    setTotalCapacity(0);
    setAvailableFloors([]);
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-green-100 to-blue-100">
      {!selectedBlock ? (
        <BlockSelectionPage onBlockSelect={handleBlockSelect} />
      ) : (
        <div className="container mx-auto px-4 py-4">
          <div className="sticky top-0 p-4 mb-8 z-10">
            <button
              onClick={handleBack}
              className="absolute left-4 top-4 px-4 py-2 rounded-full border-2 border-gray-600 hover:bg-gray-100 transition-all duration-300"
            >
              ← Back
            </button>
            <BlockHeader block={selectedBlock.name} floor={selectedFloor} totalCapacity={totalCapacity} />
            <FloorSelector 
              floors={availableFloors}
              selectedFloor={selectedFloor}
              onFloorSelect={handleFloorChange}
            />
          </div>
          {roomData && <RoomGrid rooms={roomData.rooms} />}
        </div>
      )}
    </div>
  );
}

export default App;