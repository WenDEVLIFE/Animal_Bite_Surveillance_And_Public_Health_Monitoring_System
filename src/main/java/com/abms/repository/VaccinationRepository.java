package com.abms.repository;

import com.abms.utils.DatabaseConnection;

import com.abms.service.VaccinationRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<VaccinationRecord> getVaccinationsByStatus(String status) throws SQLException {
        List<VaccinationRecord> list = new ArrayList<>();
        String sql = "SELECT v.id as vid, p.first_name, p.last_name, v.dose_number, v.scheduled_date, v.status, v.actual_date " +
                     "FROM vaccinations v " +
                     "JOIN bite_incidents b ON v.incident_id = b.id " +
                     "JOIN patients p ON b.patient_id = p.id " +
                     "WHERE v.status = ? " +
                     "ORDER BY v.scheduled_date ASC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                    Date actualDate = rs.getDate("actual_date");
                    String dateStr = rs.getDate("scheduled_date").toString();
                    if (actualDate != null) {
                        dateStr += " (Done: " + actualDate.toString() + ")";
                    }
                    list.add(new VaccinationRecord(
                        rs.getInt("vid"),
                        fullName,
                        rs.getInt("dose_number"),
                        dateStr,
                        rs.getString("status")
                    ));
                }
            }
        }
        return list;
    }

    public List<VaccinationRecord> getAllVaccinations() throws SQLException {
        List<VaccinationRecord> list = new ArrayList<>();
        String sql = "SELECT v.id as vid, p.first_name, p.last_name, v.dose_number, v.scheduled_date, v.status, v.actual_date " +
                     "FROM vaccinations v " +
                     "JOIN bite_incidents b ON v.incident_id = b.id " +
                     "JOIN patients p ON b.patient_id = p.id " +
                     "ORDER BY v.scheduled_date ASC";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                Date actualDate = rs.getDate("actual_date");
                String dateStr = rs.getDate("scheduled_date").toString();
                if (actualDate != null) {
                    dateStr += " (Done: " + actualDate.toString() + ")";
                }
                list.add(new VaccinationRecord(
                    rs.getInt("vid"),
                    fullName,
                    rs.getInt("dose_number"),
                    dateStr,
                    rs.getString("status")
                ));
            }
        }
        return list;
    }
    public int getPendingVaccinationCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vaccinations WHERE status = 'Pending'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getCompletedDoseCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vaccinations WHERE status = 'Completed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
