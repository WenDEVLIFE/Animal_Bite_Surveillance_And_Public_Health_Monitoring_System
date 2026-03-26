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

    public java.util.List<com.abms.model.BiteIncident> getAllIncidents() throws SQLException {
        java.util.List<com.abms.model.BiteIncident> incidents = new java.util.ArrayList<>();
        String sql = "SELECT b.*, p.first_name, p.last_name FROM bite_incidents b " +
                     "JOIN patients p ON b.patient_id = p.id ORDER BY b.bite_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                incidents.add(new com.abms.model.BiteIncident(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getDate("bite_date").toLocalDate(),
                    rs.getString("animal_type"),
                    rs.getString("bite_site"),
                    rs.getString("animal_status"),
                    rs.getString("exposure_type"),
                    rs.getString("remarks")
                ));
            }
        }
        return incidents;
    }

    public com.abms.model.BiteIncident getIncidentById(int id) throws SQLException {
        String sql = "SELECT b.*, p.first_name, p.last_name FROM bite_incidents b " +
                     "JOIN patients p ON b.patient_id = p.id WHERE b.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new com.abms.model.BiteIncident(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getDate("bite_date").toLocalDate(),
                        rs.getString("animal_type"),
                        rs.getString("bite_site"),
                        rs.getString("animal_status"),
                        rs.getString("exposure_type"),
                        rs.getString("remarks")
                    );
                }
            }
        }
        return null;
    }

    public void deleteIncident(int id) throws SQLException {
        String sql = "DELETE FROM bite_incidents WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updateIncident(com.abms.model.BiteIncident incident) throws SQLException {
        String sql = "UPDATE bite_incidents SET bite_date = ?, animal_type = ?, bite_site = ?, animal_status = ?, exposure_type = ?, remarks = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(incident.getBiteDate()));
            pstmt.setString(2, incident.getAnimalType());
            pstmt.setString(3, incident.getBiteSite());
            pstmt.setString(4, incident.getAnimalStatus());
            pstmt.setString(5, incident.getExposureType());
            pstmt.setString(6, incident.getRemarks());
            pstmt.setInt(7, incident.getId());
            pstmt.executeUpdate();
        }
    }

    public int getTodayIncidentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bite_incidents WHERE bite_date = CURRENT_DATE";
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
