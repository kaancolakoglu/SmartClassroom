# Smart Classroom: Occupancy Monitoring System

## Website
http://smart-classroom-frontend.s3-website-us-east-1.amazonaws.com/

## Project Overview
Smart Classroom is a solution designed to help students find available empty classrooms for study spaces when libraries are full. Using sensors and IoT technology, the system tracks classroom occupancy and displays this information on an accessible platform, redirecting students to available spaces.

## Features
- **Real-time Occupancy Detection**: Uses LD2410 sensors to detect and count people in classrooms
- **Web-based Dashboard**: Shows classroom availability status for students
- **Automatic Lighting Control**: Energy-saving feature that turns lights on/off based on room occupancy

## Technology Stack

### Hardware Components
- **LD2410 Sensor**: Motion and presence detection for counting occupants
- **ESP32**: Main microcontroller for processing sensor data and wireless communication
- **Lifepo4 Battery**: Provides reliable power supply
- **LED Lights**: For visual indicators and lighting

### Software & Cloud Infrastructure
- **Frontend**: Vite.js for building the user interface
- **Backend**: Java Spring for server-side operations
- **AWS Services**:
  - **AWS IoT Core**: For managing and communicating with IoT devices
  - **AWS RDS**: For storing and managing occupancy data

## System Architecture
```
┌───────────────┐       ┌──────────────────┐       ┌─────────────────┐
│  ESP32 with   │       │                  │       │    Vite Web     │
│ LD2410 Sensor ├──────►│  AWS IoT Core    ├──────►│    Dashboard    │
└───────────────┘       │                  │       └─────────────────┘
                        │  AWS RDS         │
┌───────────────┐       │                  │       ┌─────────────────┐
│   ESP32 Cam   ├──────►│  Java Spring     ├──────►│ Admin Interface │
└───────────────┘       │                  │       └─────────────────┘
                        └──────────────────┘
```

## Installation & Setup
### Prerequisites
- Java JDK 17 or higher
- Node.js and npm
- AWS account with appropriate permissions
- ESP32 development environment (Arduino IDE or PlatformIO)
- Required libraries for ESP32:
  - ESP32 Board Support Package
  - WiFi library
  - PubSubClient for MQTT
  - LD2410 sensor libraries

### Hardware Setup
- Connect the LD2410 sensor to ESP32 according to pinout specifications
- Connect relay module to ESP32 for lighting control
- Ensure proper power supply connection for all components

### Software Setup
Detailed software setup instructions will be provided once development is complete.

## Usage
Students can:
1. Access the Smart Classroom web dashboard to view available classrooms in real-time
2. Filter classrooms by building or floor
3. See occupancy status with visual indicators
4. Find and utilize available study spaces efficiently
5. Benefit from automatic lighting in occupied rooms
