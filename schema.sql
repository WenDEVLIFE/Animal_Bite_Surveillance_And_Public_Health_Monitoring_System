-- Animal Bite Surveillance and Public Health Monitoring System
-- Database Schema Design

-- Drop database if exists and create it
DROP DATABASE IF EXISTS animal_bite_db;
CREATE DATABASE animal_bite_db;
USE animal_bite_db;

-- Users table for authentication
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, HEALTH_WORKER
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Patients table
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    age INT,
    gender ENUM('Male', 'Female', 'Other'),
    address TEXT,
    contact_number VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Animal Bite Incidents table
CREATE TABLE bite_incidents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    bite_date DATE NOT NULL,
    animal_type ENUM('Dog', 'Cat', 'Other') NOT NULL,
    bite_site VARCHAR(50), -- Head, Hand, Leg, etc.
    animal_status ENUM('Alive', 'Dead', 'Unknown', 'Observation') DEFAULT 'Unknown',
    exposure_type ENUM('Category I', 'Category II', 'Category III') NOT NULL, -- WHO categories
    remarks TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Vaccinations table
CREATE TABLE vaccinations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT NOT NULL,
    dose_number INT NOT NULL, -- 1, 2, 3, 4, 5 (or Day 0, 3, 7, 14, 28)
    scheduled_date DATE NOT NULL,
    actual_date DATE NULL,
    status ENUM('Pending', 'Completed', 'Missed') DEFAULT 'Pending',
    administered_by INT,
    FOREIGN KEY (incident_id) REFERENCES bite_incidents(id) ON DELETE CASCADE,
    FOREIGN KEY (administered_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert default admin user (password: admin123 - in real app should be hashed)
INSERT INTO users (username, password, role, full_name) 
VALUES ('admin', 'admin123', 'ADMIN', 'System Administrator');
