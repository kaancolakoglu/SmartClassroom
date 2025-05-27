import React from 'react';
import RoomCard from './RoomCard';

const RoomGrid = ({ rooms }) => {
  const getGridColumns = () => {
    if (rooms.length <= 6) return 'grid-cols-1 md:grid-cols-2';
    return 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3';
  };

  const sortedRooms = [...rooms].sort((a, b) => {
    const numA = parseInt(a.name.replace(/\D/g, ''));
    const numB = parseInt(b.name.replace(/\D/g, ''));
    return numA - numB;
  });

  return (
    <div 
      className={`grid ${getGridColumns()} gap-4 p-3 mt-1 overflow-y-auto flex-grow auto-rows-fr`} 
      style={{ 
        scrollbarWidth: 'thin',
        scrollbarColor: '#4CAF50 transparent',
        gridAutoRows: '100px',
      }}
    >
      {sortedRooms.map(room => (
        <RoomCard key={room.id} room={room} />
      ))}
    </div>
  );
};

export default RoomGrid;