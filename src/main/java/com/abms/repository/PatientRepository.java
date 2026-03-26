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

    public List<com.abms.model.Patient> getAllPatients() throws SQLException {
        List<com.abms.model.Patient> patients = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, age, gender, address, contact_number FROM patients";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(new com.abms.model.Patient(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("contact_number")
                ));
            }
        }
        return patients;
    }

    public com.abms.model.Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new com.abms.model.Patient(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("contact_number")
                    );
                }
            }
        }
        return null;
    }

    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updatePatient(com.abms.model.Patient patient) throws SQLException {
        String sql = "UPDATE patients SET first_name = ?, last_name = ?, age = ?, gender = ?, address = ?, contact_number = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getGender());
            pstmt.setString(5, patient.getAddress());
            pstmt.setString(6, patient.getContactNumber());
            pstmt.setInt(7, patient.getId());
            pstmt.executeUpdate();
        }
    }
}
