import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prescription {
    private int prescriptionId;
    private String date;
    private String expiryDate;
    private List<String> medicines;
    private Map<String, String> dosage;
    private String instructions;
    private int refillsAllowed;
    private int refillsUsed;
    private String status;

    // BIDIRECTIONAL ASSOCIATIONS
    private Patient patient;  // 1:1
    private Doctor doctor;    // 1:1

    private static int nextId = 1;

    public Prescription(Patient patient, Doctor doctor, String medicines, String dosage, String instructions) {
        this.prescriptionId = nextId++;
        this.date = LocalDateTime.now().toString();
        this.patient = patient;
        this.doctor = doctor;
        this.medicines = new ArrayList<>();
        this.medicines.add(medicines);
        this.dosage = new HashMap<>();
        this.dosage.put(medicines, dosage);
        this.instructions = instructions;
        this.refillsAllowed = 3;
        this.refillsUsed = 0;
        this.status = "Active";
        this.expiryDate = LocalDateTime.now().plusMonths(3).toString();
    }

    public void addMedicine(String medicine, String dosage) {
        medicines.add(medicine);
        this.dosage.put(medicine, dosage);
    }

    public boolean dispense() {
        if (!isValid()) {
            System.out.println("Cannot dispense invalid prescription");
            return false;
        }
        System.out.println("Dispensing medicines: " + medicines);
        return true;
    }

    public boolean refill() {
        if (refillsUsed < refillsAllowed && !isExpired()) {
            refillsUsed++;
            System.out.println("Prescription refilled. Remaining refills: " + getRemainingRefills());
            return true;
        }
        return false;
    }

    public boolean isValid() {
        return !isExpired() && refillsUsed <= refillsAllowed;
    }

    public boolean isExpired() {
        LocalDateTime expiry = LocalDateTime.parse(expiryDate);
        return LocalDateTime.now().isAfter(expiry);
    }

    public int getRemainingRefills() {
        return refillsAllowed - refillsUsed;
    }

    public String printPrescription() {
        return "Prescription #" + prescriptionId + "\n" +
                "Patient: " + patient.getUsername() + "\n" +
                "Doctor: " + doctor.getUsername() + "\n" +
                "Medicines: " + medicines + "\n" +
                "Instructions: " + instructions;
    }

    // Getters
    public int getPrescriptionId() { return prescriptionId; }
    public String getDate() { return date; }
    public String getExpiryDate() { return expiryDate; }
    public List<String> getMedicines() { return medicines; }
    public Map<String, String> getDosage() { return dosage; }
    public String getInstructions() { return instructions; }
    public int getRefillsAllowed() { return refillsAllowed; }
    public int getRefillsUsed() { return refillsUsed; }
    public String getStatus() { return status; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
}
