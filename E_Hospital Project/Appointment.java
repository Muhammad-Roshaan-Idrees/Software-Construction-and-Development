import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int appointmentId;
    private String date;
    private String time;
    private String status;
    private String reason;
    private String notes;

    // BIDIRECTIONAL ASSOCIATIONS
    private Patient patient;  // 1:1
    private Doctor doctor;    // 1:1
    private Room room;        // 1:1

    private static int nextId = 1;

    public Appointment(Patient patient, Doctor doctor, String date, String time, String reason) {
        this.appointmentId = nextId++;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = "Scheduled";
        this.notes = "";
    }

    public boolean schedule() {
        this.status = "Scheduled";
        System.out.println("Appointment #" + appointmentId + " scheduled");
        return true;
    }

    public boolean cancel() {
        this.status = "Cancelled";
        System.out.println("Appointment #" + appointmentId + " cancelled");
        return true;
    }

    public boolean reschedule(String newDate, String newTime) {
        this.date = newDate;
        this.time = newTime;
        System.out.println("Appointment #" + appointmentId + " rescheduled to " + newDate + " " + newTime);
        return true;
    }

    public void complete() {
        this.status = "Completed";
    }

    public boolean isConfirmed() {
        return "Scheduled".equals(status) || "Confirmed".equals(status);
    }

    public void sendReminder() {
        System.out.println("Reminder: Appointment with Dr. " + doctor.getUsername() +
                " on " + date + " at " + time);
    }

    public int getDuration() {
        return 30; // Default 30 minutes
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public String getNotes() { return notes; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public void setStatus(String status) { this.status = status; }
}