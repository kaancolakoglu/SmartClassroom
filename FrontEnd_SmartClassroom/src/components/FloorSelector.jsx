import React from 'react';

const FloorSelector = ({ floors, selectedFloor, onFloorSelect }) => {
  const getColorByFloor = (floor) => {
    const colors = {
      0: '#2196F3', // Blue
      1: '#4CAF50', // Green
      2: '#FFC107', // Amber
      3: '#FF9800', // Orange
      4: '#673AB7', // Deep Purple
    };
    return colors[floor];
  };

  return (
    <div className="flex gap-2 justify-center -mb-2 flex-wrap">
      {floors.map((floor) => (
        <button
          key={floor}
          onClick={() => onFloorSelect(floor)}
          className={`px-6 py-2 rounded-full transition-all duration-300 
            ${selectedFloor === floor ? 'shadow-md text-white' : 'border-2'}`}
          style={{
            backgroundColor: selectedFloor === floor ? getColorByFloor(floor) : 'transparent',
            borderColor: getColorByFloor(floor),
          }}
        >
          {floor === 0 ? 'Ground' : `Floor ${floor}`}
        </button>
      ))}
    </div>
  );
};

export default FloorSelector;