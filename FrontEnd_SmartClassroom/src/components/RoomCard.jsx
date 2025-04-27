import React from 'react';

const RoomCard = ({ room }) => {
  const getColorByOccupancy = () => {
    const percentage = (room.current / room.capacity) * 100;
    if (percentage <= 20) return '#4CAF50';  // Açık yeşil - Çok müsait
    if (percentage <= 40) return '#8BC34A';  // Yeşil - Müsait
    if (percentage <= 60) return '#FFB300';  // Amber - Orta doluluk
    if (percentage <= 80) return '#FB8C00';  // Turuncu - Yüksek doluluk
    if (percentage < 100) return '#F44336';  // Kırmızı - Çok yüksek doluluk
    return '#B71C1C';  // Koyu kırmızı - Tamamen dolu
  };

  return (
    <div 
      className="rounded-full p-4 border-2 transition-all duration-300 hover:shadow-lg"
      style={{ borderColor: getColorByOccupancy() }}
    >
      <div className="text-center">
        <div className="text-xl font-bold mb-2">{room.id}</div>
        <div className="text-lg mb-2">{room.name}</div>
        <div className="text-sm">
          {room.current}/{room.capacity}
        </div>
      </div>
    </div>
  );
};

export default RoomCard;