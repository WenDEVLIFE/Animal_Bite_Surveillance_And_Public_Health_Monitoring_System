# Project Reference: Animal Bite Surveillance System

This document provides a quick reference for the project structure and architectural components of the Animal Bite Surveillance and Public Health Monitoring System.

## Architecture Patterns

### Repository Layer
- **Location**: `com.abms.repository`
- **Responsibility**: Direct interaction with MySQL using JDBC.

### Service Layer
- **Location**: `com.abms.service`
- **Responsibility**: Business logic, data transformation, and coordination between repositories and UI.

### UI Layer (JavaFX)
- **Location**: `com.abms.ui`
- **Responsibility**: User interaction, form display, and displaying data using visual components like tables and buttons.

### Utilities
- **Location**: `com.abms.utils`
- **Responsibility**: Common helper functions, database connection management, and data formatting.

## Core Features
- Patient and Incident Recording
- Vaccination Schedule Monitoring
- Follow-up Reminders
- Case Report Generation
- User Authentication & Management

## Technologies
- **Language**: Java 11
- **Framework**: JavaFX (UI)
- **Persistence**: MySQL
- **Build Tool**: Maven
