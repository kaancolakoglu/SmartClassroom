import React, { useState } from 'react';
import './BlockSelectionPage.css';

const BlockSelectionPage = ({ onBlockSelect }) => {
  const [blocks] = useState(['A', 'B', 'C', 'H', 'I']); // Test için daha fazla blok ekledim

  const blockColors = {
    'A': {
      bg: 'from-purple-200 to-pink-200',
      hover: 'hover:from-purple-300 hover:to-pink-300',
      border: 'border-purple-300'
    },
    'B': {
      bg: 'from-blue-200 to-cyan-200',
      hover: 'hover:from-blue-300 hover:to-cyan-300',
      border: 'border-blue-300'
    },
    'C': {
      bg: 'from-emerald-200 to-teal-200',
      hover: 'hover:from-emerald-300 hover:to-teal-300',
      border: 'border-emerald-300'
    },
    'H': {
      bg: 'from-orange-200 to-amber-200',
      hover: 'hover:from-orange-300 hover:to-amber-300',
      border: 'border-orange-300'
    },
    'I': {
      bg: 'from-rose-200 to-red-200',
      hover: 'hover:from-rose-300 hover:to-red-300',
      border: 'border-rose-300'
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-start justify-start p-8 bg-gradient-to-br from-green-100 to-blue-100 relative">
      <div className="fixed inset-0 flex items-center justify-center pointer-events-none z-0 opacity-40">
        <img src="/gsu-logo.png" alt="GSU Logo" className="w-[600px] h-[600px] object-contain" />
      </div>
      <div className="w-full flex justify-center mb-6">
        <div className="backdrop-blur-lg bg-gradient-to-r from-yellow-500 via-red-500 to-red-600 rounded-3xl p-6 shadow-2xl border border-white/40 transform hover:scale-105 transition-all duration-500 inline-block">
          <h1 className="text-4xl font-extrabold mb-2 text-center text-gray-200" style={{ fontFamily: '"Poppins", sans-serif' }}>GALATASARAY</h1>
          <h1 className="text-4xl font-extrabold text-center text-gray-200" style={{ fontFamily: '"Poppins", sans-serif' }}>UNIVERSITY</h1>
        </div>
      </div>

      <div className="text-center w-full mb-8 relative z-10">
        <h2 className="text-2xl font-bold text-gray-700 text-shadow">Select Block</h2>
      </div>
      
      <div className="grid grid-cols-2 gap-8 w-full max-w-5xl px-4 mx-auto">
        {blocks.map((block) => (
          <button
            key={block}
            onClick={() => onBlockSelect(block)}
            className={`
              relative overflow-hidden
              py-8 rounded-2xl
              text-3xl font-bold text-gray-700
              bg-gradient-to-r ${blockColors[block].bg}
              ${blockColors[block].hover}
              transform hover:scale-105
              transition-all duration-300 ease-in-out
              shadow-lg hover:shadow-2xl
              border-2 ${blockColors[block].border}
            `}
          >
            <span className="relative z-10">BLOCK {block}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

export default BlockSelectionPage;