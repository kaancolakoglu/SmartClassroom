import React, {useEffect, useState} from 'react';
import './BlockSelectionPage.css';
import ApiService from '../services/apiConfig';

const BlockSelectionPage = ({ onBlockSelect }) => {
  const [blocks, setBlocks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBlocks = async () => {
      try {
        const data = await ApiService.getAllBuildings();
        // Blokları alfabetik olarak sırala
        const sortedData = [...data].sort((a, b) => a.name.localeCompare(b.name));
        setBlocks(sortedData);
        setLoading(false);
      } catch (error) {
        console.error('Block data fetch error:', error);
        setError('Block data could not be loaded. Please try again later.');
        setLoading(false);
      }
    };

    fetchBlocks();
  }, []);

  const blockColors = {
    'A': {
      bg: 'bg-gradient-to-br from-[#ff5f6d] to-[#fca65c]', // Kırmızıdan turuncuya
      hover: 'hover:from-[#ff6f7d] hover:to-[#fcb06b]',
      border: 'border-[#ff8c6d]'
    },
    'B': {
      bg: 'bg-gradient-to-br from-[#fca65c] to-[#fdd835]', // Turuncudan sarıya
      hover: 'hover:from-[#fcb06b] hover:to-[#fde340]',
      border: 'border-[#ffb347]'
    },
    'C': {
      bg: 'bg-gradient-to-br from-[#fdd835] to-[#aed581]', // Sarıdan açık yeşile
      hover: 'hover:from-[#fde340] hover:to-[#b7e18d]',
      border: 'border-[#cddc39]'
    },
    'G': {
      bg: 'bg-gradient-to-br from-[#aed581] to-[#4fc3f7]', // Açık yeşilden açık maviye
      hover: 'hover:from-[#b7e18d] hover:to-[#64bdf6]',
      border: 'border-[#81c784]'
    },
    'H': {
      bg: 'bg-gradient-to-br from-[#4fc3f7] to-[#9575cd]', // Açık maviden mora
      hover: 'hover:from-[#64bdf6] hover:to-[#a784db]',
      border: 'border-[#64b5f6]'
    },
    'I': {
      bg: 'bg-gradient-to-br from-[#9575cd] to-[#d4e157]', // Mordan lime yeşiline (gökkuşağı sırasını tamamlamak için)
      hover: 'hover:from-[#a784db] hover:to-[#dce775]',
      border: 'border-[#7e57c2]'
    }
  };

  // Default color
  const defaultColor = {
    bg: 'from-neutral-100 to-neutral-200',
    hover: 'hover:from-neutral-200 hover:to-neutral-300',
    border: 'border-neutral-300'
  };

  // Get block color, use default if not found
  const getBlockColor = (block) => {
    return blockColors[block] || defaultColor;
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

        {loading ? (
            <div className="w-full text-center">
              <p className="text-xl">Loading...</p>
            </div>
        ) : error ? (
            <div className="w-full text-center">
              <p className="text-xl text-red-500">{error}</p>
            </div>
        ) : (
            <div className="grid grid-cols-2 gap-8 w-full max-w-5xl px-4 mx-auto">
              {blocks.map((block) => {
                const blockColor = getBlockColor(block.name);
                return (
                    <button
                        key={block.id}
                        onClick={() => onBlockSelect(block.id, block.name)}
                        className={`
                  relative overflow-hidden
                  py-8 rounded-2xl
                  text-3xl font-bold text-gray-700
                  bg-gradient-to-r ${blockColor.bg}
                  ${blockColor.hover}
                  transform hover:scale-105
                  transition-all duration-300 ease-in-out
                  shadow-lg hover:shadow-2xl
                  border-2 ${blockColor.border}
                `}
                    >
                      <span className="relative z-10">BLOCK {block.name}</span>
                    </button>
                );
              })}
            </div>
        )}
      </div>
  );
};

export default BlockSelectionPage;