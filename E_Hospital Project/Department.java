import java.util.ArrayList;
import java.util.List;

public class Department {
    private String departmentId;
    private String name;
    private String description;
    private Doctor headDoctor;  // 1:1 Association with Doctor
    private String location;
    private String phoneExtension;

    // AGGREGATION relationships (Department has Doctors/Nurses/Rooms)
    private List<Doctor> doctors;  // 1:* Aggregation with Doctor
    private List<Nurse> nurses;    // 1:* Aggregation with Nurse
    private List<Room> rooms;      // 1:* Aggregation with Room

    public Department(String departmentId, String name, String description,
                      String location, String phoneExtension) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.phoneExtension = phoneExtension;
        this.headDoctor = null;
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
        this.rooms = new ArrayList<>();
    }

    // AGGREGATION methods - weak lifecycle (objects exist independently)
    public void addDoctor(Doctor doctor) {
        if (doctor != null && !doctors.contains(doctor)) {
            doctors.add(doctor);
            System.out.println("Dr. " + doctor.getUsername() + " added to " + name + " department");
        }
    }

    public boolean removeDoctor(String doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                doctors.remove(doctor);
                System.out.println("Doctor " + doctorId + " removed from " + name + " department");
                return true;
            }
        }
        return false;
    }

    public void addNurse(Nurse nurse) {
        if (nurse != null && !nurses.contains(nurse)) {
            nurses.add(nurse);
            System.out.println("Nurse " + nurse.getUsername() + " added to " + name + " department");
        }
    }

    public boolean removeNurse(String nurseId) {
        for (Nurse nurse : nurses) {
            if (nurse.getNurseId().equals(nurseId)) {
                nurses.remove(nurse);
                System.out.println("Nurse " + nurseId + " removed from " + name + " department");
                return true;
            }
        }
        return false;
    }

    public void addRoom(Room room) {
        if (room != null && !rooms.contains(room)) {
            rooms.add(room);
            System.out.println("Room " + room.getRoomNumber() + " added to " + name + " department");
        }
    }

    // ASSOCIATION method for Head Doctor (1:1)
    public void setHeadDoctor(Doctor doctor) {
        this.headDoctor = doctor;
        if (doctor != null) {
            // Ensure doctor is in department
            addDoctor(doctor);
            System.out.println("Dr. " + doctor.getUsername() + " appointed as head of " + name + " department");
        }
    }

    public int getStaffCount() {
        return doctors.size() + nurses.size();
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                available.add(room);
            }
        }
        return available;
    }

    public String getDepartmentInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== Department Information ===\n");
        info.append("ID: ").append(departmentId).append("\n");
        info.append("Name: ").append(name).append("\n");
        info.append("Description: ").append(description).append("\n");
        info.append("Location: ").append(location).append("\n");
        info.append("Phone: ").append(phoneExtension).append("\n");
        if (headDoctor != null) {
            info.append("Head Doctor: Dr. ").append(headDoctor.getUsername()).append("\n");
        }
        info.append("Total Staff: ").append(getStaffCount()).append("\n");
        info.append("Doctors: ").append(doctors.size()).append("\n");
        info.append("Nurses: ").append(nurses.size()).append("\n");
        info.append("Rooms: ").append(rooms.size()).append("\n");
        info.append("Available Rooms: ").append(getAvailableRooms().size()).append("\n");
        return info.toString();
    }

    public void displayDepartmentInfo() {
        System.out.println(getDepartmentInfo());
    }

    // Getters
    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getPhoneExtension() { return phoneExtension; }
    public Doctor getHeadDoctor() { return headDoctor; }
    public List<Doctor> getDoctors() { return new ArrayList<>(doctors); } // Return copy
    public List<Nurse> getNurses() { return new ArrayList<>(nurses); }   // Return copy
    public List<Room> getRooms() { return new ArrayList<>(rooms); }      // Return copy
}