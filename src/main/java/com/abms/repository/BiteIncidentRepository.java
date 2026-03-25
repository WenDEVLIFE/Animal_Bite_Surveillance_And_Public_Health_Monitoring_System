package com.abms.repository;

import com.abms.utils.DatabaseConnection;
import java.sql.*;

public class BiteIncidentRepository {

    public int addIncident(int patientId, Date biteDate, String animalType, String biteSite, String animalStatus, String exposureType, String remarks) throws SQLException {
        String sql = "INSERT INTO bite_incidents (patient_id, bite_date, animal_type, bite_site, animal_status, exposure_type, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, patientId);
            pstmt.setDate(2, biteDate);
            pstmt.setString(3, animalType);
            pstmt.setString(4, biteSite);
            pstmt.setString(5, animalStatus);
            pstmt.setString(6, exposureType);
            pstmt.setString(7, remarks);
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating incident failed, no ID obtained.");
                }
            }
        }
    }
}
