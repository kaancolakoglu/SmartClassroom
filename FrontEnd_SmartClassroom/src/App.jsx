import React, {useState} from 'react';
import BlockSelectionPage from './components/BlockSelectionPage';
import BlockHeader from './components/BlockHeader';
import FloorSelector from './components/FloorSelector';
import RoomGrid from './components/RoomGrid';
import ApiService from './services/apiConfig';

function App() {
  const [selectedBlock, setSelectedBlock] = useState(null);
  const [selectedFloor, setSelectedFloor] = useState(0);
  const [roomData, setRoomData] = useState(null);
  const [totalCapacity, setTotalCapacity] = useState(0);
  const [availableFloors, setAvailableFloors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Block selection handler
  const handleBlockSelect = async (blockId, blockName) => {
    setLoading(true);
    setError(null);
    setSelectedBlock({id: blockId, name: blockName});

    try {
      // Fetch classrooms for the selected block using the API service
      const data = await ApiService.getClassroomsByBuildingId(blockName);

      // Extract available floors
      const floors = [...new Set(data.map(room => room.floorNumber))].sort();
      setAvailableFloors(floors);

      // Select first floor
      const firstFloor = floors.length > 0 ? floors[0] : 0;
      setSelectedFloor(firstFloor);

      // Filter rooms for the selected floor
      const roomsForFloor = data.filter(room => room.floorNumber === firstFloor);

      // Calculate total capacity
      const totalCap = roomsForFloor.reduce((sum, room) => sum + room.classroomCapacity, 0);
      setTotalCapacity(totalCap);

      // Set room data
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
      console.error('Error fetching data:', error);
      setError('Failed to load classroom data. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  // Floor change handler
  const handleFloorChange = async (floor) => {
    setSelectedFloor(floor);
    if (selectedBlock) {
      setLoading(true);
      setError(null);

      try {
        const data = await ApiService.getClassroomsByBuildingId(selectedBlock.name);

        // Filter rooms for the selected floor
        const roomsForFloor = data.filter(room => room.floorNumber === floor);

        // Calculate total capacity
        const totalCap = roomsForFloor.reduce((sum, room) => sum + room.classroomCapacity, 0);
        setTotalCapacity(totalCap);

        // Set room data
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
        console.error('Error fetching data:', error);
        setError('Failed to load classroom data. Please try again later.');
      } finally {
        setLoading(false);
      }
    }
  };

  // Back button handler
  const handleBack = () => {
    setSelectedBlock(null);
    setSelectedFloor(0);
    setRoomData(null);
    setTotalCapacity(0);
    setAvailableFloors([]);
    setError(null);
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

              {loading && (
                  <div className="flex justify-center items-center h-64">
                    <div className="text-xl">Loading classroom data...</div>
                  </div>
              )}

              {error && (
                  <div className="flex justify-center items-center h-64">
                    <div className="text-xl text-red-500">{error}</div>
                  </div>
              )}

              {!loading && !error && roomData && <RoomGrid rooms={roomData.rooms} />}
            </div>
        )}
      </div>
  );
}

export default App;