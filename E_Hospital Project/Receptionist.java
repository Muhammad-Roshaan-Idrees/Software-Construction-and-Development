import java.util.ArrayList;
import java.util.List;

public class Receptionist extends User {
    private String receptionistId;
    private String deskNumber;
    private String shift;
    private int appointmentsProcessed;
    private int patientsRegistered;

    // UNIDIRECTIONAL ASSOCIATION with Patient
    private List<Patient> registeredPatients;

    public Receptionist(int userId, String username, String password, String email, String phone,
                        String receptionistId, String deskNumber, String shift) {
        super(userId, username, password, email, phone);
        this.receptionistId = receptionistId;
        this.deskNumber = deskNumber;
        this.shift = shift;
        this.appointmentsProcessed = 0;
        this.patientsRegistered = 0;
        this.registeredPatients = new ArrayList<>();
    }

    public Patient registerPatient(String name, String email, String phone, String address,
                                   String bloodGroup, String allergies, String insuranceNumber, String emergencyContact) {
        int userId = 1000 + patientsRegistered + 1;
        String username = "patient" + userId;
        String password = "temp123";
        String patientId = "PAT" + String.format("%04d", userId);

        Patient patient = new Patient(userId, username, password, email, phone,
                patientId, bloodGroup, allergies, insuranceNumber, emergencyContact);

        registeredPatients.add(patient);  // Unidirectional association
        patientsRegistered++;
        System.out.println("Patient registered: " + patientId + " - " + name);
        return patient;
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor, String date, String time, String reason) {
        Appointment appointment = new Appointment(patient, doctor, date, time, reason);
        patient.bookAppointment(doctor, date, time, reason);  // Uses patient's method
        appointmentsProcessed++;
        System.out.println("Appointment booked for " + patient.getUsername() + " with Dr. " + doctor.getUsername());
        return appointment;
    }

    public Bill generateBill(Patient patient, double consultationFee, double medicineFee, double roomFee) {
        Bill bill = new Bill(patient);
        bill.addItem("Consultation", consultationFee);
        bill.addItem("Medicines", medicineFee);
        if (roomFee > 0) {
            bill.addItem("Room Charges", roomFee);
        }
        bill.calculateTotal();
        return bill;
    }

    public boolean cancelAppointment(Appointment appointment) {
        return appointment.cancel();
    }

    public boolean rescheduleAppointment(Appointment appointment, String newDate, String newTime) {
        return appointment.reschedule(newDate, newTime);
    }

    public List<String> checkDoctorAvailability(Doctor doctor, String date) {
        return doctor.getAvailableTimeSlots(date);
    }

    public void processPayment(Bill bill, double amount) {
        bill.makePayment(amount);
        System.out.println("Payment processed for bill #" + bill.getBillId());
    }

    // Getters
    public String getReceptionistId() { return receptionistId; }
    public String getDeskNumber() { return deskNumber; }
    public String getShift() { return shift; }
    public int getAppointmentsProcessed() { return appointmentsProcessed; }
    public int getPatientsRegistered() { return patientsRegistered; }
    public List<Patient> getRegisteredPatients() { return registeredPatients; }
}
