public class Main {
    public static void main(String[] args) {
        System.out.println("=== HOSPITAL MANAGEMENT SYSTEM  ===\n");

        // Create Departments
        Department cardiologyDept = new Department("DEPT001", "Cardiology",
                "Heart and cardiovascular diseases department", "Building A, Floor 3", "Ext. 300");

        Department pediatricsDept = new Department("DEPT002", "Pediatrics",
                "Children's health department", "Building B, Floor 1", "Ext. 100");

        // Create Doctors
        Doctor cardiologist = new Doctor(1001, "smith", "doc123",
                "smith@hospital.com", "111-111-1111", "DOC001", "Cardiologist",
                "MED12345", "Cardiology", 200.0);

        Doctor pediatrician = new Doctor(1002, "jones", "doc456",
                "jones@hospital.com", "222-222-2222", "DOC002", "Pediatrician",
                "MED67890", "Pediatrics", 150.0);

        // Assign Doctors to Departments
        cardiologist.assignToDepartment(cardiologyDept);
        pediatrician.assignToDepartment(pediatricsDept);

        // Set Head Doctors
        cardiologyDept.setHeadDoctor(cardiologist);
        pediatricsDept.setHeadDoctor(pediatrician);

        // Create Nurses
        Nurse cardiologyNurse = new Nurse(2001, "nurse_mary", "nur123",
                "mary@hospital.com", "333-333-3333", "NUR001", "NUR12345",
                "Cardiology", "Morning", "ICU");

        Nurse pediatricsNurse = new Nurse(2002, "nurse_john", "nur456",
                "john@hospital.com", "444-444-4444", "NUR002", "NUR67890",
                "Pediatrics", "Evening", "Ward 5");

        // Assign Nurses to Departments
        cardiologyNurse.assignToDepartment(cardiologyDept);
        pediatricsNurse.assignToDepartment(pediatricsDept);

        // Create Rooms
        Room icuRoom = new Room("ROOM001", "301", "ICU", 500.0);
        Room privateRoom = new Room("ROOM002", "302", "Private", 300.0);
        Room generalRoom = new Room("ROOM003", "101", "General", 150.0);

        // Add Rooms to Departments
        cardiologyDept.addRoom(icuRoom);
        cardiologyDept.addRoom(privateRoom);
        pediatricsDept.addRoom(generalRoom);

        // Create Patient
        Patient patient = new Patient(3001, "alice", "pat123",
                "alice@email.com", "555-555-5555", "PAT001", "O+", "None",
                "INS001", "John Doe (555-123-4567)");

        // Assign primary doctor
        patient.setPrimaryDoctor(cardiologist);

        // Create Medical Record (COMPOSITION with Patient)
        MedicalRecord record = patient.createMedicalRecord(
                "Hypertension",
                "Lifestyle changes and medication"
        );
        record.setDoctor(cardiologist);  // ASSOCIATION with Doctor
        record.setNurse(cardiologyNurse); // ASSOCIATION with Nurse
        record.addTestResult("Blood Pressure", "140/90 mmHg");
        record.addPrescribedMedicine("Lisinopril 10mg");
        record.addNote("Patient needs to reduce salt intake");
        record.scheduleFollowUp("2024-02-15");

        // Display Department Information
        System.out.println("\n" + cardiologyDept.getDepartmentInfo());
        System.out.println("\n" + pediatricsDept.getDepartmentInfo());

        // Display Patient Medical History
        patient.viewMedicalHistory();

        // Check available rooms
        System.out.println("\nAvailable rooms in Cardiology:");
        for (Room room : cardiologyDept.getAvailableRooms()) {
            System.out.println(" - Room " + room.getRoomNumber() + " (" + room.getType() + ")");
        }

        // Assign patient to room
        icuRoom.assignPatient(patient);

    }
}