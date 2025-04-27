import React from 'react';
import RoomCard from './RoomCard';

const RoomGrid = ({ rooms }) => {
  const getGridColumns = () => {
    if (rooms.length <= 6) return 'grid-cols-2';
    return 'grid-cols-3';
  };

  return (
    <div className={`grid ${getGridColumns()} gap-4 max-h-[600px] overflow-y-auto p-4`}>
      {rooms.map(room => (
        <RoomCard key={room.id} room={room} />
      ))}
    </div>
  );
};

export default RoomGrid;