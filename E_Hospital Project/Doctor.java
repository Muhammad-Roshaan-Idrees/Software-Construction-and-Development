import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private String doctorId;
    private String specialization;
    private String licenseNumber;
    private String department;  // Department name
    private double consultationFee;

    // BIDIRECTIONAL ASSOCIATIONS
    private List<Patient> patients;          // 1:* with Patient
    private List<Appointment> appointments;  // 1:* with Appointment
    private List<Prescription> prescriptions; // 1:* with Prescription
    private List<MedicalRecord> medicalRecords; // 1:* with MedicalRecord
    private List<Nurse> supervisingNurses;   // 1:* with Nurse

    // ASSOCIATION with Department
    private Department departmentObj;  // Doctor belongs to a Department

    public Doctor(int userId, String username, String password, String email, String phone,
                  String doctorId, String specialization, String licenseNumber,
                  String department, double consultationFee) {
        super(userId, username, password, email, phone);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.department = department;
        this.consultationFee = consultationFee;

        // Initialize associations
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
        this.supervisingNurses = new ArrayList<>();
        this.departmentObj = null;
    }

    // Department Association method
    public void assignToDepartment(Department department) {
        this.departmentObj = department;
        if (department != null) {
            department.addDoctor(this);  // Bidirectional setup
        }
    }

    // Other methods remain the same...
    public void addPatient(Patient patient) {
        if (!patients.contains(patient)) {
            patients.add(patient);
            patient.setPrimaryDoctor(this);  // Bidirectional setup
        }
    }

    public Prescription writePrescription(Patient patient, String medicines, String dosage, String instructions) {
        Prescription prescription = new Prescription(patient, this, medicines, dosage, instructions);
        prescriptions.add(prescription);
        patient.addPrescription(prescription);  // Bidirectional setup
        return prescription;
    }

    public MedicalRecord createMedicalRecord(Patient patient, String diagnosis, String treatment) {
        MedicalRecord record = patient.createMedicalRecord(diagnosis, treatment); // Uses patient's composition
        record.setDoctor(this);  // Set doctor reference
        medicalRecords.add(record);  // Add to doctor's list
        return record;
    }

    public void scheduleAppointment(Patient patient, String date, String time, String reason) {
        Appointment appointment = new Appointment(patient, this, date, time, reason);
        appointments.add(appointment);
        patient.getAppointments().add(appointment);  // Bidirectional setup
    }

    public void addSupervisingNurse(Nurse nurse) {
        if (!supervisingNurses.contains(nurse)) {
            supervisingNurses.add(nurse);
            nurse.setSupervisingDoctor(this);  // Bidirectional setup
        }
    }

    // Getters
    public String getDoctorId() { return doctorId; }
    public String getSpecialization() { return specialization; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getDepartment() { return department; }
    public double getConsultationFee() { return consultationFee; }
    public List<Patient> getPatients() { return patients; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public List<Nurse> getSupervisingNurses() { return supervisingNurses; }
    public Department getDepartmentObj() { return departmentObj; }

    // Setters
    public void setDepartment(String department) { this.department = department; }
    public void setDepartmentObj(Department departmentObj) { this.departmentObj = departmentObj; }

    public List<String> getAvailableTimeSlots(String date) {
        return null;
    }
}