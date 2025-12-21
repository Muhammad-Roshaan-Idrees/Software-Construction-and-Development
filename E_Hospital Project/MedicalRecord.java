import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private int recordId;
    private String date;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private List<String> prescribedMedicines;
    private List<String> testResults;
    private String notes;
    private String followUpDate;

    // BIDIRECTIONAL ASSOCIATIONS
    private Patient patient;      // 1:1 with Patient (also part of composition)
    private Doctor doctor;        // 1:1 with Doctor
    private Nurse nurse;          // 1:1 with Nurse

    private static int nextId = 1;

    // Composition constructor - requires Patient
    public MedicalRecord(Patient patient, String diagnosis, String treatment) {
        this.recordId = nextId++;
        this.date = LocalDateTime.now().toString();
        this.patient = patient;  // Composition: cannot exist without Patient
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.symptoms = "";
        this.prescribedMedicines = new ArrayList<>();
        this.testResults = new ArrayList<>();
        this.notes = "";
        this.followUpDate = "";
    }

    // Association method for Doctor
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        // Doctor adds this record to its list separately
    }

    // Association method for Nurse
    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public void addTestResult(String testName, String result) {
        testResults.add(testName + ": " + result);
    }

    public void updateTreatment(String newTreatment) {
        this.treatment = newTreatment;
    }

    public void addPrescribedMedicine(String medicine) {
        prescribedMedicines.add(medicine);
    }

    public void addNote(String note) {
        this.notes += "\n" + note;
    }

    public void scheduleFollowUp(String date) {
        this.followUpDate = date;
    }

    public boolean isFollowUpRequired() {
        return followUpDate != null && !followUpDate.isEmpty();
    }

    // Getters
    public int getRecordId() { return recordId; }
    public String getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getSymptoms() { return symptoms; }
    public String getTreatment() { return treatment; }
    public List<String> getPrescribedMedicines() { return prescribedMedicines; }
    public List<String> getTestResults() { return testResults; }
    public String getNotes() { return notes; }
    public String getFollowUpDate() { return followUpDate; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public Nurse getNurse() { return nurse; }

    // Setters
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setNotes(String notes) { this.notes = notes; }
}