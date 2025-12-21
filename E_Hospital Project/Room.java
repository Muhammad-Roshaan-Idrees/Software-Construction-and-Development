import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomId;
    private String roomNumber;
    private String type;
    private String status;
    private double ratePerDay;
    private List<String> facilities;
    private String cleaningSchedule;

    // BIDIRECTIONAL ASSOCIATIONS
    private Patient patient;            // 1:1 with Patient
    private List<Nurse> assignedNurses; // *:* with Nurse

    public Room(String roomId, String roomNumber, String type, double ratePerDay) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.ratePerDay = ratePerDay;
        this.status = "Available";
        this.facilities = new ArrayList<>();
        this.assignedNurses = new ArrayList<>();

        // Add default facilities based on room type
        if (type.equals("ICU")) {
            facilities.add("Ventilator");
            facilities.add("Monitor");
            facilities.add("Oxygen Supply");
        } else if (type.equals("Private")) {
            facilities.add("TV");
            facilities.add("Private Bathroom");
            facilities.add("WiFi");
        } else {
            facilities.add("Basic Bed");
            facilities.add("Shared Bathroom");
        }
    }

    public boolean assignPatient(Patient patient) {
        if (!isAvailable()) {
            System.out.println("Room " + roomNumber + " is not available");
            return false;
        }
        this.patient = patient;
        this.status = "Occupied";
        if (patient != null) {
            patient.assignRoom(this);  // Bidirectional setup
        }
        return true;
    }

    public void vacateRoom() {
        if (patient != null) {
            patient.assignRoom(null);  // Bidirectional cleanup
        }
        this.patient = null;
        this.status = "Available";
        System.out.println("Room " + roomNumber + " vacated");
    }

    public boolean isAvailable() {
        return "Available".equals(status);
    }

    public boolean needsCleaning() {
        return "Occupied".equals(status) &&
                LocalDateTime.now().getHour() >= 10 &&
                LocalDateTime.now().getHour() <= 16;
    }

    public void scheduleCleaning(String date, String time) {
        this.cleaningSchedule = date + " " + time;
        System.out.println("Cleaning scheduled for room " + roomNumber + " on " + date + " at " + time);
    }

    public double calculateStayCost(int days) {
        return ratePerDay * days;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    // Getters and Setters
    public String getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public double getRatePerDay() { return ratePerDay; }
    public List<String> getFacilities() { return facilities; }
    public String getCleaningSchedule() { return cleaningSchedule; }
    public Patient getPatient() { return patient; }
    public List<Nurse> getAssignedNurses() { return assignedNurses; }
    public void setPatient(Patient patient) { this.patient = patient; }
}

