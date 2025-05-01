import React from 'react';

const BlockHeader = ({ block, floor, totalCapacity }) => {
  const getFloorName = (floor) => {
    const names = ['GROUND', 'FIRST', 'SECOND', 'THIRD', 'FOURTH'];
    return `${names[floor]} FLOOR`;
  };

  return (
    <div className="text-center mb-8">
      <h1 className="text-4xl font-bold mb-2">BLOCK {block}</h1>
      <h2 className="text-2xl text-gray-700">{getFloorName(floor)}</h2>
      <p className="text-lg text-gray-600 mt-2">
        Total Capacity: <span className="font-semibold">{totalCapacity}</span>
      </p>
    </div>
  );
};

export default BlockHeader;