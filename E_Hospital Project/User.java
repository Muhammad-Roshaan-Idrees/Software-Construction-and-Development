import java.time.LocalDateTime;
import java.util.*;

public abstract class User {
    protected int userId;
    protected String username;
    protected String password;
    protected String email;
    protected String phone;
    protected boolean isActive;
    protected LocalDateTime registrationDate;

    public User(int userId, String username, String password, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isActive = true;
        this.registrationDate = LocalDateTime.now();
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println(username + " logged out successfully.");
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            System.out.println("Password changed successfully.");
            return true;
        }
        return false;
    }

    public void updateProfile(String email, String phone) {
        this.email = email;
        this.phone = phone;
        System.out.println("Profile updated successfully.");
    }

    public void displayInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Registered: " + registrationDate);
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public void setPassword(String password) { this.password = password; }
}