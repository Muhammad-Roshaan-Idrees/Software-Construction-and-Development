import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private String patientId;
    private String bloodGroup;
    private String allergies;
    private String insuranceNumber;
    private String emergencyContact;

    // COMPOSITION with MedicalRecord (MedicalRecord is PART OF Patient)
    private List<MedicalRecord> medicalRecords;

    // BIDIRECTIONAL ASSOCIATIONS
    private Doctor primaryDoctor;                // 1:1 with Doctor
    private List<Appointment> appointments;      // 1:* with Appointment
    private List<Prescription> prescriptions;    // 1:* with Prescription
    private Room assignedRoom;                   // 1:1 with Room

    // UNIDIRECTIONAL ASSOCIATION
    private List<Bill> bills;                    // 1:* with Bill

    public Patient(int userId, String username, String password, String email, String phone,
                   String patientId, String bloodGroup, String allergies,
                   String insuranceNumber, String emergencyContact) {
        super(userId, username, password, email, phone);
        this.patientId = patientId;
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
        this.insuranceNumber = insuranceNumber;
        this.emergencyContact = emergencyContact;

        // Initialize composition and associations
        this.medicalRecords = new ArrayList<>();   // Composition
        this.appointments = new ArrayList<>();     // Association
        this.prescriptions = new ArrayList<>();    // Association
        this.bills = new ArrayList<>();           // Association
    }

    // ========== COMPOSITION METHODS ==========
    // MedicalRecord is CREATED BY and BELONGS TO Patient
    public MedicalRecord createMedicalRecord(String diagnosis, String treatment) {
        MedicalRecord record = new MedicalRecord(this, diagnosis, treatment);
        medicalRecords.add(record);  // Composition: record is part of patient
        return record;
    }

    // ========== ASSOCIATION METHODS ==========
    // BIDIRECTIONAL with Doctor (1:1)
    public void setPrimaryDoctor(Doctor doctor) {
        if (this.primaryDoctor != null) {
            this.primaryDoctor.getPatients().remove(this);
        }
        this.primaryDoctor = doctor;
        if (doctor != null && !doctor.getPatients().contains(this)) {
            doctor.getPatients().add(this);  // Bidirectional setup
        }
    }

    // BIDIRECTIONAL with Appointment (1:*)
    public void bookAppointment(Doctor doctor, String date, String time, String reason) {
        Appointment appointment = new Appointment(this, doctor, date, time, reason);
        appointments.add(appointment);
        doctor.getAppointments().add(appointment);  // Bidirectional setup
    }

    // BIDIRECTIONAL with Prescription (1:*)
    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
        // Prescription already has reference to this patient (set in constructor)
    }

    // BIDIRECTIONAL with Room (1:1)
    public void assignRoom(Room room) {
        if (this.assignedRoom != null) {
            this.assignedRoom.setPatient(null);  // Remove from old room
        }
        this.assignedRoom = room;
        if (room != null) {
            room.setPatient(this);  // Bidirectional setup
        }
    }

    // UNIDIRECTIONAL with Bill (1:*)
    public void addBill(Bill bill) {
        bills.add(bill);
        // Bill knows patient, but patient doesn't need to know bill
    }

    // ========== PATIENT METHODS ==========
    public void viewMedicalHistory() {
        System.out.println("\n=== Medical History for " + getUsername() + " ===");
        for (MedicalRecord record : medicalRecords) {
            System.out.println("Date: " + record.getDate() +
                    ", Diagnosis: " + record.getDiagnosis() +
                    ", Doctor: " + record.getDoctor().getUsername());
        }
    }

    public void payBill(int billId, double amount) {
        for (Bill bill : bills) {
            if (bill.getBillId() == billId) {
                bill.makePayment(amount);
                System.out.println("Payment of $" + amount + " made for bill #" + billId);
                return;
            }
        }
        System.out.println("Bill #" + billId + " not found.");
    }

    public void viewUpcomingAppointments() {
        System.out.println("\n=== Upcoming Appointments ===");
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals("Scheduled")) {
                System.out.println("Date: " + appointment.getDate() +
                        ", Time: " + appointment.getTime() +
                        ", Doctor: " + appointment.getDoctor().getUsername());
            }
        }
    }

    // Getters
    public String getPatientId() { return patientId; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAllergies() { return allergies; }
    public String getInsuranceNumber() { return insuranceNumber; }
    public String getEmergencyContact() { return emergencyContact; }
    public Doctor getPrimaryDoctor() { return primaryDoctor; }
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public List<Bill> getBills() { return bills; }
    public Room getAssignedRoom() { return assignedRoom; }
}