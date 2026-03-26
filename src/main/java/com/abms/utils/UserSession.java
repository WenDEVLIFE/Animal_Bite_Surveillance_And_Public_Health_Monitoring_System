package com.abms.utils;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String role;

    private UserSession(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public static void login(String username, String role) {
        instance = new UserSession(username, role);
    }

    public static void logout() {
        instance = null;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
