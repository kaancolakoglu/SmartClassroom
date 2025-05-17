// src/services/apiConfig.js

// Use the Elastic Beanstalk URL for all API calls
const API_BASE_URL = 'https://production.eba-nhxxj3xh.us-east-1.elasticbeanstalk.com';

// API endpoints
const ENDPOINTS = {
    ALL_BUILDINGS: '/api/v1/buildings/all',
    CLASSROOMS_BY_BUILDING: (buildingId) => `/api/v1/classrooms/building/${buildingId}`,
};

// API service functions
const ApiService = {
    // Fetch all building blocks
    async getAllBuildings() {
        try {
            const response = await fetch(`${API_BASE_URL}${ENDPOINTS.ALL_BUILDINGS}`);
            if (!response.ok) {
                throw new Error('Failed to fetch buildings');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching buildings:', error);
            // Return default blocks in case of failure
            return [
                { id: 1, name: 'A' },
                { id: 2, name: 'B' },
                { id: 3, name: 'C' },
                { id: 4, name: 'G' },
                { id: 5, name: 'H' },
                { id: 6, name: 'I' }
            ];
        }
    },

    // Fetch classrooms by building ID
    async getClassroomsByBuildingId(buildingId) {
        try {
            const response = await fetch(`${API_BASE_URL}${ENDPOINTS.CLASSROOMS_BY_BUILDING(buildingId)}`);
            if (!response.ok) {
                throw new Error('Failed to fetch classrooms');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching classrooms:', error);
            throw error;
        }
    }
};

export default ApiService;