# Hospital Management System - Detailed Class Specification

## **ENUMS**

```java
enum UserRole {
  PATIENT, DOCTOR, RECEPTIONIST, ADMINISTRATOR
}

enum AppointmentStatus {
  SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
}

enum PatientType {
  INPATIENT, OUTPATIENT
}

enum ReportType {
  PATIENT_STATISTICS, DEPARTMENT_REPORT, REVENUE_REPORT
}

enum FeedbackStatus {
  PENDING, REVIEWED, RESPONDED, CLOSED
}

enum BackupStatus {
  PENDING, IN_PROGRESS, COMPLETED, FAILED, RESTORED
}
```

---

## **DATA TYPES MAPPING**

| Concept | C++ | Java | Python |
|---------|-----|------|--------|
| Integer ID | `int` | `int` | `int` |
| Name/Text | `string` | `String` | `str` |
| Date | `Date` class | `LocalDate` | `datetime.date` |
| DateTime | `DateTime` class | `LocalDateTime` | `datetime.datetime` |
| Time | `Time` class | `LocalTime` | `datetime.time` |
| Money | `double` | `BigDecimal` | `Decimal` |
| Boolean | `bool` | `boolean` | `bool` |
| Collection | `vector<T*>` | `List<T>` | `List[T]` |
| Duration | `Duration` class | `Duration` | `timedelta` |

---

## **CLASS SPECIFICATIONS**

### **1. User (ABSTRACT CLASS)**

#### **Attributes:**
```
- userId: int                          // Primary key, auto-increment
- username: String                     // Unique, required
- password: String                     // Hashed, required
- email: String                        // Unique, required
- phone: String                        // Format: +X-XXX-XXX-XXXX
- role: UserRole                       // ENUM (PATIENT, DOCTOR, RECEPTIONIST, ADMIN)
- isActive: boolean                    // Default: true
- registrationDate: LocalDateTime      // Auto-generated on creation
- lastLoginDate: LocalDateTime         // Nullable, updated on each login
- accountStatus: String                // "Active", "Suspended", "Deactivated"
- createdBy: int (Administrator ID)    // Foreign key, nullable (nullable for self-registration)
- createdAt: LocalDateTime             // Auto-generated
- updatedAt: LocalDateTime             // Auto-updated
```

#### **Methods:**
```
+ login(username: String, password: String): boolean
+ logout(): void
+ changePassword(oldPassword: String, newPassword: String): boolean
+ updateProfile(email: String, phone: String): void
+ getAccountStatus(): String
+ setAccountStatus(status: String): void
+ displayInfo(): void
```

---

### **2. Patient (extends User)**

#### **Attributes:**
```
- patientId: int                       // Unique, auto-increment
- patientRecord: PatientRecord         // COMPOSITION (1:1, owns)
- dateOfBirth: LocalDate               // Required
- gender: String                       // "M", "F", "Other"
- address: String                      // Full address, required
- phoneNumber: String                  // Contact number
- emergencyContact: String             // Name of emergency contact
- emergencyPhoneNumber: String         // Contact number
- bloodGroup: String                   // "A+", "A-", "B+", etc.
- allergies: List<String>              // List of allergies
- medicalHistory: String               // Free text
- type: PatientType                    // ENUM (INPATIENT, OUTPATIENT)
- registrationDate: LocalDate          // Date of registration
- appointments: List<Appointment>      // AGGREGATION (1:0..*, has)
- medicalRecords: List<MedicalRecord>  // AGGREGATION (1:0..*, has)
- feedback: List<Feedback>             // AGGREGATION (1:0..*, submits)
- doctors: List<Doctor>                // MANY-TO-MANY (0..*:0..*, treatedBy)
- registeredBy: Receptionist           // ASSOCIATION (who registered)
- registrationReceptionist: int        // Foreign key to Receptionist
```

#### **Methods:**
```
+ bookAppointment(doctor: Doctor, date: LocalDate, time: LocalTime): Appointment
+ cancelAppointment(appointmentId: int): boolean
+ rescheduleAppointment(appointmentId: int, newDate: LocalDate, newTime: LocalTime): boolean
+ viewAppointments(): List<Appointment>
+ viewMedicalHistory(): List<MedicalRecord>
+ submitFeedback(message: String, rating: int): Feedback
+ viewFeedbackStatus(feedbackId: int): String
+ getAvailableSlots(doctorId: int, date: LocalDate): List<TimeSlot>
+ updateContactInfo(address: String, phone: String, emergency: String): void
+ getPatientRecord(): PatientRecord
```

---

### **3. Doctor (extends User)**

#### **Attributes:**
```
- doctorId: int                        // Unique, auto-increment
- specialization: String               // "Cardiology", "Neurology", etc.
- qualification: String                // "MD", "MBBS", etc.
- licenseNumber: String                // Medical license, required
- consultationFee: BigDecimal          // Decimal with 2 places
- department: Department               // ASSOCIATION (belongs to, 0..*:1)
- departmentId: int                    // Foreign key
- schedule: Schedule                   // COMPOSITION (1:1, has)
- appointments: List<Appointment>      // ASSOCIATION (1:0..*, conducts)
- medicalRecords: List<MedicalRecord>  // ASSOCIATION (1:0..*, creates)
- timeSlots: List<TimeSlot>            // AGGREGATION (1:0..*, manages via Schedule)
- patients: List<Patient>              // MANY-TO-MANY (0..*:0..*, treats)
- yearsOfExperience: int               // Integer years
- certifications: List<String>         // List of certifications
- availability: String                 // "Available", "On Leave", "Off Duty"
- startTime: LocalTime                 // Daily work start time
- endTime: LocalTime                   // Daily work end time
- workDays: List<String>               // ["Monday", "Tuesday", ...]
```

#### **Methods:**
```
+ viewTodayAppointments(): List<Appointment>
+ viewAppointmentsByDate(date: LocalDate): List<Appointment>
+ viewPatientHistory(patientId: int): List<MedicalRecord>
+ createMedicalRecord(patientId: int, diagnosis: String, notes: String): MedicalRecord
+ addClinicalNotes(recordId: int, notes: String): void
+ addDiagnosis(recordId: int, diagnosis: String): void
+ createPrescription(recordId: int, medication: String, dosage: String, duration: String): Prescription
+ getAvailableSlots(date: LocalDate): List<TimeSlot>
+ confirmAppointment(appointmentId: int): void
+ completeAppointment(appointmentId: int): void
+ addExperience(yearsToAdd: int, certification: String): void
+ getSchedule(): Schedule
+ setAvailability(status: String): void
+ updateConsultationFee(newFee: BigDecimal): void
```

---

### **4. Receptionist (extends User)**

#### **Attributes:**
```
- receptionistId: int                  // Unique, auto-increment
- shift: String                        // "Morning", "Afternoon", "Night"
- deskNumber: int                      // Desk/Counter number
- department: String                   // Department assigned to, nullable
- patientRecords: List<PatientRecord>  // ASSOCIATION (manages, 0..*:0..*)
- appointmentsHandled: int             // Count of appointments scheduled
```

#### **Methods:**
```
+ registerPatient(name: String, dob: LocalDate, gender: String, 
                 address: String, phone: String, emergencyContact: String, 
                 allergies: String, bloodGroup: String): Patient
+ searchPatient(searchTerm: String, searchType: String): List<Patient>
+ // searchType: "NAME", "PATIENT_ID", "PHONE"
+ viewPatientRecord(patientId: int): PatientRecord
+ updatePatientRecord(patientId: int, updates: Map): void
+ scheduleAppointment(patientId: int, doctorId: int, date: LocalDate, time: LocalTime): Appointment
+ viewDoctorSchedule(doctorId: int): Schedule
+ checkDoctorAvailability(doctorId: int, date: LocalDate): List<TimeSlot>
+ updateAppointmentStatus(appointmentId: int, status: AppointmentStatus): void
+ cancelAppointment(appointmentId: int, reason: String): boolean
+ checkInPatient(patientId: int): void
+ printPatientLabel(patientId: int): void
+ getShift(): String
+ setShift(newShift: String): void
```

---

### **5. Administrator (extends User)**

#### **Attributes:**
```
- adminId: int                         // Unique, auto-increment
- adminLevel: String                   // "Super Admin", "Department Admin", "Billing Admin"
- permissions: List<String>            // List of permissions
- departments: List<Department>        // ASSOCIATION (oversees, 1:0..*)
- announcements: List<Announcement>    // COMPOSITION (publishes, 1:0..*)
- reports: List<Report>                // AGGREGATION (generates, 1:0..*)
- backups: List<Backup>                // AGGREGATION (creates, 1:0..*)
- billingSettings: List<BillingSetting> // ASSOCIATION (configures, 1:0..*)
- feedbackResponses: Map<int, String>  // feedbackId -> response mapping
- systemAlerts: List<String>           // System notifications/alerts
- lastActivityDate: LocalDateTime      // Track admin activity
```

#### **Methods:**
```
+ createUser(userType: String, userData: Map): User
+ // userType: "PATIENT", "DOCTOR", "RECEPTIONIST", "ADMINISTRATOR"
+ deactivateUser(userId: int, reason: String): boolean
+ suspendUser(userId: int, suspensionDays: int): boolean
+ activateUser(userId: int): boolean
+ manageUserProfile(userId: int, updates: Map): boolean
+ viewAllUsers(userType: String): List<User>
+ viewDepartments(): List<Department>
+ createDepartment(name: String, description: String, location: String): Department
+ updateDepartment(departmentId: int, updates: Map): void
+ removeDepartment(departmentId: int): boolean
+ assignDoctorToDepartment(doctorId: int, departmentId: int): boolean
+ removeDoctorFromDepartment(doctorId: int, departmentId: int): boolean
+ generateReport(reportType: ReportType, dateRange: String): Report
+ // dateRange format: "2024-01-01 to 2024-12-31"
+ generateStatistics(type: String, period: String): Map<String, Object>
+ createBackup(backupType: String): Backup
+ // backupType: "FULL", "INCREMENTAL", "DIFFERENTIAL"
+ restoreBackup(backupId: int): boolean
+ sendAnnouncement(title: String, message: String, targetAudience: String): Announcement
+ updateAnnouncement(announcementId: int, title: String, message: String): void
+ viewFeedback(): List<Feedback>
+ respondToFeedback(feedbackId: int, response: String): void
+ viewSystemAlerts(): List<String>
+ configureSystemSettings(key: String, value: String): void
+ manageBillingSettings(feeName: String, amount: BigDecimal, paymentOptions: String): BillingSetting
+ viewAuditLogs(startDate: LocalDate, endDate: LocalDate): List<String>
+ exportData(format: String): File
```

---

### **6. PatientRecord (COMPOSITION - owned by Patient)**

#### **Attributes:**
```
- recordId: int                        // Unique, auto-increment
- patientId: int                       // Foreign key (Patient)
- registrationDate: LocalDate          // When patient registered
- fullName: String                     // Full name
- dateOfBirth: LocalDate               // DOB
- gender: String                       // "M", "F", "Other"
- address: String                      // Current address
- phoneNumber: String                  // Primary contact
- email: String                        // Email address
- emergencyContact: String             // Emergency contact name
- emergencyPhone: String               // Emergency contact phone
- allergies: String                    // Comma-separated or list
- bloodGroup: String                   // "A+", "B-", etc.
- patientType: PatientType             // ENUM
- maritalStatus: String                // "Single", "Married", etc.
- occupation: String                   // Job/profession
- nationality: String                  // Country
- idType: String                       // "Passport", "Driver License", etc.
- idNumber: String                     // ID number
- insuranceProvider: String            // Insurance company name
- insurancePolicyNumber: String        // Policy number
- createdAt: LocalDateTime             // Record creation time
- updatedAt: LocalDateTime             // Last update time
- isActive: boolean                    // Active record flag
```

#### **Methods:**
```
+ updateRecord(): void
+ updateContactInfo(address: String, phone: String): void
+ updateEmergencyContact(name: String, phone: String): void
+ updateInsuranceInfo(provider: String, policyNumber: String): void
+ getRecordInfo(): Map<String, Object>
+ displayInfo(): void
+ archiveRecord(): void
+ restoreRecord(): void
```

---

### **7. Appointment (ASSOCIATION)**

#### **Attributes:**
```
- appointmentId: int                   // Unique, auto-increment
- patientId: int                       // Foreign key (Patient) - ASSOCIATION
- patient: Patient                     // Reference to Patient
- doctorId: int                        // Foreign key (Doctor) - ASSOCIATION
- doctor: Doctor                       // Reference to Doctor
- timeSlotId: int                      // Foreign key (TimeSlot) - AGGREGATION
- timeSlot: TimeSlot                   // Reference to TimeSlot (booked)
- date: LocalDate                      // Appointment date
- time: LocalTime                      // Appointment time
- status: AppointmentStatus            // ENUM
- reason: String                       // Reason for visit
- notes: String                        // Additional notes
- duration: Duration                   // Expected duration (e.g., 30 minutes)
- medicalRecordId: int                 // Foreign key (MedicalRecord) - nullable
- medicalRecord: MedicalRecord         // AGGREGATION (generates, 0..1)
- createdAt: LocalDateTime             // When appointment was booked
- updatedAt: LocalDateTime             // Last update
- cancelReason: String                 // Reason if cancelled, nullable
- completedAt: LocalDateTime           // When appointment was completed, nullable
```

#### **Methods:**
```
+ confirm(): void
+ cancel(reason: String): boolean
+ reschedule(newDate: LocalDate, newTime: LocalTime): boolean
+ complete(): void
+ noShow(): void
+ updateNotes(notes: String): void
+ generateMedicalRecord(): MedicalRecord
+ getAppointmentInfo(): Map<String, Object>
+ sendReminderNotification(): void
+ displayInfo(): void
```

---

### **8. Schedule (COMPOSITION - owned by Doctor)**

#### **Attributes:**
```
- scheduleId: int                      // Unique, auto-increment
- doctorId: int                        // Foreign key (Doctor)
- weeklySlots: Map<DayOfWeek, List<TimeSlot>>  // Monday->Slots, Tuesday->Slots, etc.
- timeOffDates: List<LocalDate>        // Dates doctor is off
- startTime: LocalTime                 // Daily start time
- endTime: LocalTime                   // Daily end time
- slotDuration: Duration               // Default slot duration (e.g., 30 min)
- timeOffReasons: Map<LocalDate, String> // Reason for time off
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### **Methods:**
```
+ getAvailableSlots(date: LocalDate): List<TimeSlot>
+ isAvailable(date: LocalDate, time: LocalTime): boolean
+ addTimeOff(date: LocalDate, reason: String): void
+ removeTimeOff(date: LocalDate): void
+ addWeeklySlots(dayOfWeek: String, startTime: LocalTime, endTime: LocalTime, slotDuration: Duration): void
+ getScheduleInfo(): Map<String, Object>
+ updateWorkingHours(startTime: LocalTime, endTime: LocalTime): void
+ generateWeeklySchedule(startDate: LocalDate): Map<String, List<TimeSlot>>
```

---

### **9. TimeSlot (COMPOSITION - owned by Schedule)**

#### **Attributes:**
```
- slotId: int                          // Unique, auto-increment
- scheduleId: int                      // Foreign key (Schedule)
- date: LocalDate                      // Date of slot
- startTime: LocalTime                 // Start time
- endTime: LocalTime                   // End time
- duration: Duration                   // Calculated duration
- isAvailable: boolean                 // Available flag
- isBooked: boolean                    // Booked flag
- appointmentId: int                   // Foreign key if booked, nullable
- createdAt: LocalDateTime
```

#### **Methods:**
```
+ overlaps(other: TimeSlot): boolean
+ getDuration(): Duration
+ bookSlot(appointmentId: int): void
+ releaseSlot(): void
+ isOverlapingWith(startTime: LocalTime, endTime: LocalTime): boolean
+ displayInfo(): void
```

---

### **10. MedicalRecord (COMPOSITION - owned by Patient, but AGGREGATION for Appointment)**

#### **Attributes:**
```
- recordId: int                        // Unique, auto-increment
- patientId: int                       // Foreign key (Patient)
- patient: Patient                     // COMPOSITION owner
- appointmentId: int                   // Foreign key (Appointment), nullable
- appointment: Appointment             // AGGREGATION reference
- doctorId: int                        // Foreign key (Doctor)
- doctor: Doctor                       // ASSOCIATION (creates)
- visitDate: LocalDate                 // Date of visit
- time: LocalTime                      // Time of visit
- diagnosis: String                    // Medical diagnosis
- symptoms: String                     // Reported symptoms
- clinicalNotes: String                // Doctor's clinical notes
- prescriptions: List<Prescription>    // COMPOSITION (contains)
- followUpDate: LocalDate              // Next follow-up date, nullable
- followUpNotes: String                // Notes for follow-up
- testResults: String                  // Lab test results, nullable
- vitals: Map<String, String>          // {"BP": "120/80", "HR": "75", "Temp": "98.6"}
- treatmentPlan: String                // Proposed treatment
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### **Methods:**
```
+ updateDiagnosis(diagnosis: String): void
+ addPrescription(medication: String, dosage: String, duration: String): Prescription
+ addClinicalNote(note: String): void
+ updateVitalSigns(vitals: Map<String, String>): void
+ setFollowUpDate(date: LocalDate, notes: String): void
+ addTestResults(results: String): void
+ setTreatmentPlan(plan: String): void
+ getPatientHistory(): String
+ displayInfo(): void
+ generateReport(): String
+ exportPDF(): File
```

---

### **11. Prescription (COMPOSITION - owned by MedicalRecord)**

#### **Attributes:**
```
- prescriptionId: int                  // Unique, auto-increment
- recordId: int                        // Foreign key (MedicalRecord)
- medication: String                   // Medicine name
- dosage: String                       // Dosage amount (e.g., "100mg", "2 tablets")
- frequency: String                    // "Once daily", "Twice daily", etc.
- duration: String                     // Duration (e.g., "7 days", "30 days")
- instructions: String                 // Special instructions
- sideEffects: String                  // Known side effects
- contraindications: String            // When not to take
- prescribedDate: LocalDate            // Date prescribed
- expiryDate: LocalDate                // Prescription validity
- refillsAllowed: int                  // Number of refills
- refillsUsed: int                     // Refills already used
- quantity: int                        // Total quantity prescribed
```

#### **Methods:**
```
+ generatePrescription(): String
+ refillPrescription(): boolean
+ canRefill(): boolean
+ getRefillsRemaining(): int
+ isExpired(): boolean
+ displayInfo(): void
+ printPrescription(): void
```

---

### **12. Feedback (AGGREGATION - Patient submits, ASSOCIATION - Admin responds)**

#### **Attributes:**
```
- feedbackId: int                      // Unique, auto-increment
- patientId: int                       // Foreign key (Patient)
- patient: Patient                     // AGGREGATION reference
- rating: int                          // 1-5 rating
- category: String                     // "Staff", "Service", "Facilities", "Overall"
- comments: String                     // Feedback message
- submitDate: LocalDate                // When submitted
- submitTime: LocalTime                // Time submitted
- status: FeedbackStatus               // ENUM
- adminId: int                         // Foreign key (Administrator), nullable
- administrator: Administrator        // ASSOCIATION (responds)
- adminResponse: String                // Response text, nullable
- responseDate: LocalDate              // When responded, nullable
- isAnonymized: boolean                // Anonymized flag
```

#### **Methods:**
```
+ submit(): void
+ anonymize(): void
+ respond(adminId: int, response: String): void
+ updateStatus(status: FeedbackStatus): void
+ displayInfo(): void
+ getRating(): int
+ getCategory(): String
```

---

### **13. Department (AGGREGATION - contains Doctors, ASSOCIATION - Admin oversees)**

#### **Attributes:**
```
- departmentId: int                    // Unique, auto-increment
- name: String                         // Department name
- description: String                  // Department description
- location: String                     // Physical location/building
- headDoctorId: int                    // Head of department, nullable
- headDoctor: Doctor                   // Reference to head doctor
- doctors: List<Doctor>                // AGGREGATION (contains)
- phone: String                        // Department phone
- email: String                        // Department email
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- isActive: boolean                    // Active flag
```

#### **Methods:**
```
+ addDoctor(doctorId: int): boolean
+ removeDoctor(doctorId: int): boolean
+ getDoctors(): List<Doctor>
+ getDoctorCount(): int
+ setHeadDoctor(doctorId: int): void
+ updateInfo(updates: Map): void
+ getDepartmentStats(): Map<String, Object>
+ displayInfo(): void
```

---

### **14. Report (AGGREGATION - Admin generates, COMPOSITION - contains ReportData)**

#### **Attributes:**
```
- reportId: int                        // Unique, auto-increment
- type: ReportType                     // ENUM
- generatedBy: int                     // Foreign key (Administrator)
- administrator: Administrator        // AGGREGATION reference
- generatedAt: LocalDateTime           // When report generated
- period: YearMonth                    // Period covered (e.g., 2024-01)
- dateRange: String                    // "2024-01-01 to 2024-01-31"
- reportData: ReportData               // COMPOSITION (contains)
- title: String                        // Report title
- description: String                 // Description
- format: String                       // "PDF", "CSV", "JSON", "XLSX"
- fileSize: long                       // File size in bytes
- filePath: String                     // Storage path
- content: String                      // JSON content
- isExported: boolean                  // Export flag
```

#### **Methods:**
```
+ generate(): void
+ export(format: String): File
+ toJson(): String
+ toCsv(): String
+ toPdf(): File
+ toXlsx(): File
+ emailReport(recipients: List<String>): void
+ scheduleReport(cronExpression: String): void
+ displayInfo(): void
```

---

### **15. ReportData (COMPOSITION - owned by Report)**

#### **Attributes:**
```
- dataId: int                          // Unique, auto-increment
- reportId: int                        // Foreign key (Report)
- metrics: Map<String, Object>         // {"totalPatients": 150, "appointments": 500, ...}
- details: List<Map<String, Object>>   // List of detailed rows
- summary: Map<String, Integer>        // Summary statistics
- timestamps: LocalDateTime            // When data was compiled
```

#### **Methods:**
```
+ addMetric(key: String, value: Object): void
+ addDetail(row: Map<String, Object>): void
+ calculateSummary(): void
+ getMetrics(): Map<String, Object>
+ getDetails(): List<Map<String, Object>>
+ getSummary(): Map<String, Integer>
```

---

### **16. Announcement (COMPOSITION - Admin publishes)**

#### **Attributes:**
```
- announcementId: int                  // Unique, auto-increment
- adminId: int                         // Foreign key (Administrator)
- administrator: Administrator        // COMPOSITION owner
- title: String                        // Announcement title
- message: String                      // Full message
- publishDate: LocalDate               // Publication date
- publishTime: LocalTime               // Publication time
- targetAudience: String               // "All", "Patients", "Staff", "Doctors"
- category: String                     // "Hospital News", "Emergency", "Maintenance", etc.
- priority: String                     // "Low", "Medium", "High", "Urgent"
- isActive: boolean                    // Active flag
- expiryDate: LocalDate                // When announcement expires, nullable
- viewCount: int                       // Number of views
```

#### **Methods:**
```
+ publish(): void
+ edit(title: String, message: String): void
+ deactivate(): void
+ reactivate(): void
+ updateAudience(audience: String): void
+ displayInfo(): void
+ incrementViewCount(): void
+ isExpired(): boolean
```

---

### **17. Backup (AGGREGATION - Admin creates)**

#### **Attributes:**
```
- backupId: int                        // Unique, auto-increment
- createdBy: int                       // Foreign key (Administrator)
- administrator: Administrator        // AGGREGATION reference
- backupDate: LocalDateTime            // When backup created
- backupType: String                   // "FULL", "INCREMENTAL", "DIFFERENTIAL"
- status: BackupStatus                 // ENUM
- fileSize: long                       // Backup size in bytes
- filePath: String                     // Storage location
- duration: Duration                   // Backup duration
- compression: String                  // Compression type (e.g., "gzip")
- encryptionKey: String                // Encryption key, sensitive
- restorePoints: List<LocalDateTime>   // Available restore points
- lastRestoredAt: LocalDateTime        // Last restore time, nullable
- failureReason: String                // Error message if failed, nullable
```

#### **Methods:**
```
+ createBackup(): boolean
+ restoreBackup(): boolean
+ verifyBackup(): boolean
+ deleteBackup(): void
+ getBackupInfo(): Map<String, Object>
+ getRestorePoints(): List<LocalDateTime>
+ restoreToPoint(dateTime: LocalDateTime): boolean
+ scheduleBackup(cronExpression: String): void
+ displayInfo(): void
```

---

### **18. BillingSetting (ASSOCIATION - Admin configures)**

#### **Attributes:**
```
- settingId: int                       // Unique, auto-increment
- configuredBy: int                    // Foreign key (Administrator)
- administrator: Administrator        // ASSOCIATION reference
- feeName: String                      // "Consultation Fee", "Lab Test", etc.
- amount: BigDecimal                   // Fee amount with 2 decimal places
- currency: String                     // "USD", "INR", "EUR", etc.
- description: String                  // Fee description
- paymentOptions: List<String>         // ["Cash", "Card", "Insurance", "Online"]
- isActive: boolean                    // Active fee flag
- effectiveDate: LocalDate             // When fee becomes effective
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- taxPercentage: BigDecimal            // Tax rate if applicable
- discountAvailable: boolean           // Discount available flag
- discountPercentage: BigDecimal       // Discount rate, nullable
```

#### **Methods:**
```
+ updateSetting(amount: BigDecimal, options: List<String>): void
+ getFeeAmount(): BigDecimal
+ calculateTax(amount: BigDecimal): BigDecimal
+ calculateDiscount(amount: BigDecimal): BigDecimal
+ getFinalAmount(baseAmount: BigDecimal): BigDecimal
+ activateFee(): void
+ deactivateFee(): void
+ displayInfo(): void
```

---

## **RELATIONSHIP SUMMARY TABLE**

| Relationship | From Class | To Class | Type | Cardinality | Ownership |
|--------------|-----------|---------|------|-------------|-----------|
| Inheritance | User | Patient, Doctor, Receptionist, Admin | IS-A | - | - |
| Patient → MedicalRecord | Patient | MedicalRecord | COMPOSITION | 1:0..* | Patient owns |
| Doctor → Schedule | Doctor | Schedule | COMPOSITION | 1:1 | Doctor owns |
| Schedule → TimeSlot | Schedule | TimeSlot | COMPOSITION | 1:0..* | Schedule owns |
| MedicalRecord → Prescription | MedicalRecord | Prescription | COMPOSITION | 1:0..* | MedicalRecord owns |
| Report → ReportData | Report | ReportData | COMPOSITION | 1:1 | Report owns |
| Admin → Announcement | Admin | Announcement | COMPOSITION | 1:0..* | Admin publishes |
| Department → Doctor | Department | Doctor | AGGREGATION | 1:0..* | Weak ownership |
| Patient ← → Appointment | Patient | Appointment | ASSOCIATION | 1:0..* | Independent |
| Doctor ← → Appointment | Doctor | Appointment | ASSOCIATION | 1:0..* | Independent |
| Appointment → MedicalRecord | Appointment | MedicalRecord | AGGREGATION | 1:0..1 | Weak ownership |
| Appointment → TimeSlot | Appointment | TimeSlot | AGGREGATION | 1:1 | Weak ownership |
| Patient → Feedback | Patient | Feedback | AGGREGATION | 1:0..* | Patient submits |
| Admin → Feedback | Admin | Feedback | ASSOCIATION | 0..*:0..* | Admin responds |
| Admin → Backup | Admin | Backup | AGGREGATION | 1:0..* | Admin creates |
| Admin → Report | Admin | Report | AGGREGATION | 1:0..* | Admin generates |
| Admin → Department | Admin | Department | ASSOCIATION | 1:0..* | Admin oversees |
| Admin → BillingSetting | Admin | BillingSetting | ASSOCIATION | 1:0..* | Admin configures |
| Doctor → Department | Doctor | Department | ASSOCIATION | 0..*:1 | Doctor belongs to |
| Patient ← → Doctor | Patient | Doctor | MANY-TO-MANY | 0..*:0..* | Mutual reference |
| Receptionist → Patient | Receptionist | Patient | ASSOCIATION | 0..*:0..* | Receptionist registers |

---

## **KEY DATA TYPE RULES**

### **For Java Implementation:**
- Use `int` for IDs (auto-increment in database)
- Use `String` for text, but limit sizes (e.g., VARCHAR(255))
- Use `LocalDate` for dates, `LocalTime` for times, `LocalDateTime` for timestamps
- Use `BigDecimal` for financial amounts (NOT `double` or `float`)
- Use `Duration` for time intervals
- Use `List<T>` from java.util for collections
- Use `Map<K,V>` for key-value pairs
- Use enums for fixed value sets
- Use `boolean` for flags
- Use `LocalDateTime` with timezone awareness for multi-region systems

### **For C++ Implementation:**
- Use `int` for IDs
- Use `std::string` for text
- Use custom Date/Time classes or `std::chrono`
- Use `double` carefully for money (use integer cents instead)
- Use `std::vector<T*>` for collections
- Use `std::map<K,V>` for key-value pairs
- Use `enum class` for type safety
- Use `bool` for flags

### **For Python Implementation:**
- Use `int` for IDs
- Use `str` for text
- Use `datetime.date`, `datetime.time`, `datetime.datetime`
- Use `Decimal` from decimal module for money
- Use `List[T]` for collections
- Use `Dict[K, V]` for key-value pairs
- Use `Enum` from enum module
- Use `bool` for flags

