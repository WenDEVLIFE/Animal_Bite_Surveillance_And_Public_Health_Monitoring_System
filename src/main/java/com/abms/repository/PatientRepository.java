package com.abms.repository;

import com.abms.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    public int addPatient(String firstName, String lastName, int age, String gender, String address, String contactNumber) throws SQLException {
        String sql = "INSERT INTO patients (first_name, last_name, age, gender, address, contact_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, address);
            pstmt.setString(6, contactNumber);
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating patient failed, no ID obtained.");
                }
            }
        }
    }

    public List<String> getAllPatientNames() throws SQLException {
        List<String> names = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name FROM patients";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                names.add(rs.getInt("id") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }
        return names;
    }
}
