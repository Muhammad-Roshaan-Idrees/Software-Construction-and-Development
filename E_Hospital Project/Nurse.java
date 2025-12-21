import java.util.ArrayList;
import java.util.List;

public class Nurse extends User {
    private String nurseId;
    private String licenseNumber;
    private String department;
    private String shift;
    private String ward;

    // BIDIRECTIONAL ASSOCIATIONS
    private List<Patient> assignedPatients;  // *:* with Patient
    private Doctor supervisingDoctor;        // 1:1 with Doctor
    private List<Room> assignedRooms;        // *:* with Room

    // ASSOCIATION with Department
    private Department departmentObj;  // Nurse belongs to a Department

    public Nurse(int userId, String username, String password, String email, String phone,
                 String nurseId, String licenseNumber, String department,
                 String shift, String ward) {
        super(userId, username, password, email, phone);
        this.nurseId = nurseId;
        this.licenseNumber = licenseNumber;
        this.department = department;
        this.shift = shift;
        this.ward = ward;

        this.assignedPatients = new ArrayList<>();
        this.assignedRooms = new ArrayList<>();
        this.departmentObj = null;
    }

    // Department Association method
    public void assignToDepartment(Department department) {
        this.departmentObj = department;
        if (department != null) {
            department.addNurse(this);  // Bidirectional setup
        }
    }

    // Other methods remain the same...
    public void assignPatient(Patient patient) {
        if (!assignedPatients.contains(patient)) {
            assignedPatients.add(patient);
            // Patient doesn't maintain list of nurses, so this is one-way from Nurse to Patient
        }
    }

    public void setSupervisingDoctor(Doctor doctor) {
        this.supervisingDoctor = doctor;
        // Doctor will add this nurse to its list in addSupervisingNurse method
    }

    // Getters
    public String getNurseId() { return nurseId; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getDepartment() { return department; }
    public String getShift() { return shift; }
    public String getWard() { return ward; }
    public List<Patient> getAssignedPatients() { return assignedPatients; }
    public Doctor getSupervisingDoctor() { return supervisingDoctor; }
    public List<Room> getAssignedRooms() { return assignedRooms; }
    public Department getDepartmentObj() { return departmentObj; }
}