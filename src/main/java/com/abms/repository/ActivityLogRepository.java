package com.abms.repository;

import com.abms.model.ActivityLog;
import com.abms.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogRepository {

    public void log(String username, String action, String details) {
        String sql = "INSERT INTO system_activity_logs (username, action, details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ActivityLog> getAllLogs() throws SQLException {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM system_activity_logs ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(new ActivityLog(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getString("details"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return logs;
    }
}
