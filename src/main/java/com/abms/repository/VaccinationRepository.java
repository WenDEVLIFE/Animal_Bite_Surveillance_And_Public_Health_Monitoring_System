package com.abms.repository;

import com.abms.utils.DatabaseConnection;

import java.sql.*;

public class VaccinationRepository {

    public void addVaccinationSchedule(int incidentId, int doseNumber, Date scheduledDate) throws SQLException {
        String sql = "INSERT INTO vaccinations (incident_id, dose_number, scheduled_date, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, incidentId);
            pstmt.setInt(2, doseNumber);
            pstmt.setDate(3, scheduledDate);
            
            pstmt.executeUpdate();
        }
    }

    public void updateVaccinationStatus(int id, Date actualDate, String status, int administeredBy) throws SQLException {
        String sql = "UPDATE vaccinations SET actual_date = ?, status = ?, administered_by = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, actualDate);
            pstmt.setString(2, status);
            pstmt.setInt(3, administeredBy);
            pstmt.setInt(4, id);
            
            pstmt.executeUpdate();
        }
    }
}
