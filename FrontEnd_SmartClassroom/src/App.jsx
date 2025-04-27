import React, { useState, useEffect } from 'react';
import BlockSelectionPage from './components/BlockSelectionPage';
import BlockHeader from './components/BlockHeader';
import FloorSelector from './components/FloorSelector';
import RoomGrid from './components/RoomGrid';

function App() {
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [selectedFloor, setSelectedFloor] = useState(0);
  const [roomData, setRoomData] = useState(null);

  // Blok seçildiğinde çağrılacak fonksiyon
  const handleBlockSelect = async (block) => {
    setSelectedBlock(block);
    // Örnek veri kullanarak test edelim
    const mockData = {
      floors: [0, 1, 2],
      rooms: [
        { id: 1, name: "101", current: 15, capacity: 30 },
        { id: 2, name: "102", current: 20, capacity: 30 },
        { id: 3, name: "103", current: 10, capacity: 30 }
      ]
    };
    setRoomData(mockData);
  };

  // Kat değiştiğinde çağrılacak fonksiyon
  const handleFloorChange = async (floor) => {
    setSelectedFloor(floor);
    if (selectedBlock) {
      // Örnek veri kullanarak test edelim
      const mockData = {
        floors: [0, 1, 2],
        rooms: [
          { id: 1, name: `${floor}01`, current: 15, capacity: 30 },
          { id: 2, name: `${floor}02`, current: 20, capacity: 30 },
          { id: 3, name: `${floor}03`, current: 10, capacity: 30 }
        ]
      };
      setRoomData(mockData);
    }
  };

  // Geri dönüş butonu için fonksiyon
  const handleBack = () => {
    setSelectedBlock(null);
    setSelectedFloor(0);
    setRoomData(null);
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
            <BlockHeader block={selectedBlock} floor={selectedFloor} />
            <FloorSelector 
              floors={roomData?.floors || []}
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