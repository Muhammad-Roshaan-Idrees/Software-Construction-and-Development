import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Admin extends User {
    private String adminId;
    private String department;
    private int accessLevel;
    private List<String> systemLogs;

    // UNIDIRECTIONAL ASSOCIATION with User (Admin manages all users)
    private List<User> managedUsers;

    public Admin(int userId, String username, String password, String email, String phone,
                 String adminId, String department, int accessLevel) {
        super(userId, username, password, email, phone);
        this.adminId = adminId;
        this.department = department;
        this.accessLevel = accessLevel;
        this.systemLogs = new ArrayList<>();
        this.managedUsers = new ArrayList<>();
        logAction("Admin account created");
    }

    public User addUser(String userType, Map<String, Object> details) {
        User newUser = null;
        int userId = 1000 + managedUsers.size() + 1;
        String username = (String) details.get("username");
        String password = (String) details.get("password");
        String email = (String) details.get("email");
        String phone = (String) details.get("phone");

        switch (userType.toLowerCase()) {
            case "doctor":
                newUser = new Doctor(userId, username, password, email, phone,
                        (String) details.get("doctorId"),
                        (String) details.get("specialization"),
                        (String) details.get("licenseNumber"),
                        (String) details.get("department"),
                        (double) details.get("consultationFee"));
                break;
            case "patient":
                newUser = new Patient(userId, username, password, email, phone,
                        (String) details.get("patientId"),
                        (String) details.get("bloodGroup"),
                        (String) details.get("allergies"),
                        (String) details.get("insuranceNumber"),
                        (String) details.get("emergencyContact"));
                break;
            case "nurse":
                newUser = new Nurse(userId, username, password, email, phone,
                        (String) details.get("nurseId"),
                        (String) details.get("licenseNumber"),
                        (String) details.get("department"),
                        (String) details.get("shift"),
                        (String) details.get("ward"));
                break;
            case "receptionist":
                newUser = new Receptionist(userId, username, password, email, phone,
                        (String) details.get("receptionistId"),
                        (String) details.get("deskNumber"),
                        (String) details.get("shift"));
                break;
            case "pharmacist":
                newUser = new Pharmacist(userId, username, password, email, phone,
                        (String) details.get("pharmacistId"),
                        (String) details.get("licenseNumber"),
                        (String) details.get("shift"));
                break;
        }

        if (newUser != null) {
            managedUsers.add(newUser);
            logAction("Added " + userType + ": " + username);
        }
        return newUser;
    }

    public boolean removeUser(int userId) {
        for (User user : managedUsers) {
            if (user.getUserId() == userId) {
                user.setActive(false);
                managedUsers.remove(user);
                logAction("Removed user: " + user.getUsername());
                return true;
            }
        }
        return false;
    }

    public boolean deactivateUser(int userId) {
        for (User user : managedUsers) {
            if (user.getUserId() == userId) {
                user.setActive(false);
                logAction("Deactivated user: " + user.getUsername());
                return true;
            }
        }
        return false;
    }

    public String generateReport(String reportType) {
        String report = "Report: " + reportType + "\n";
        report += "Generated: " + LocalDateTime.now() + "\n";
        report += "Total Users: " + managedUsers.size() + "\n";

        int activeUsers = 0;
        for (User user : managedUsers) {
            if (user.isActive()) activeUsers++;
        }
        report += "Active Users: " + activeUsers + "\n";

        logAction("Generated report: " + reportType);
        return report;
    }

    public List<String> viewSystemLogs() {
        return systemLogs;
    }

    public void backupDatabase() {
        logAction("Database backup initiated");
        System.out.println("Database backup completed successfully.");
    }

    public boolean restoreDatabase(String backupFile) {
        logAction("Database restore from: " + backupFile);
        System.out.println("Database restored from " + backupFile);
        return true;
    }

    public String resetUserPassword(int userId) {
        for (User user : managedUsers) {
            if (user.getUserId() == userId) {
                String newPassword = "TempPass" + System.currentTimeMillis() % 1000;
                user.setPassword(newPassword);
                logAction("Password reset for user: " + user.getUsername());
                return newPassword;
            }
        }
        return null;
    }

    public void updateSystemSettings(Map<String, Object> settings) {
        logAction("System settings updated");
        System.out.println("System settings updated successfully.");
    }

    private void logAction(String action) {
        String log = LocalDateTime.now() + " - " + getUsername() + " - " + action;
        systemLogs.add(log);
    }

    // Getters
    public String getAdminId() { return adminId; }
    public String getDepartment() { return department; }
    public int getAccessLevel() { return accessLevel; }
    public List<String> getSystemLogs() { return systemLogs; }
    public List<User> getManagedUsers() { return managedUsers; }
}
