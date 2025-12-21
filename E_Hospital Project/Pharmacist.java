import java.util.ArrayList;
import java.util.List;

public class Pharmacist extends User {
    private String pharmacistId;
    private String licenseNumber;
    private String shift;
    private Inventory inventory;
    private int prescriptionsFilled;

    // UNIDIRECTIONAL ASSOCIATION with Inventory (Pharmacist manages Inventory)

    public Pharmacist(int userId, String username, String password, String email, String phone,
                      String pharmacistId, String licenseNumber, String shift) {
        super(userId, username, password, email, phone);
        this.pharmacistId = pharmacistId;
        this.licenseNumber = licenseNumber;
        this.shift = shift;
        this.inventory = new Inventory();  // Creates/manages inventory
        this.prescriptionsFilled = 0;
    }

    public boolean dispenseMedicine(Prescription prescription) {
        if (!prescription.isValid()) {
            System.out.println("Cannot dispense - prescription is invalid or expired");
            return false;
        }

        // Check if medicines are in stock
        for (String medicine : prescription.getMedicines()) {
            if (inventory.checkStock(medicine) <= 0) {
                System.out.println("Medicine out of stock: " + medicine);
                return false;
            }
            inventory.removeMedicine(medicine, 1);
        }

        prescription.dispense();
        prescriptionsFilled++;
        System.out.println("Medicines dispensed for prescription #" + prescription.getPrescriptionId());
        return true;
    }

    public int checkMedicineStock(String medicineName) {
        return inventory.checkStock(medicineName);
    }

    public void orderMedicine(String medicineName, int quantity) {
        inventory.reorderMedicine(medicineName, quantity);
    }

    public void updateMedicinePrice(String medicineName, double newPrice) {
        inventory.updatePrice(medicineName, newPrice);
        System.out.println("Price updated for " + medicineName + " to $" + newPrice);
    }

    public List<String> checkExpiredMedicines() {
        // Simplified - in real system would check expiry dates
        System.out.println("Checking for expired medicines...");
        return new ArrayList<>();
    }

    public void generateMedicineReport() {
        System.out.println(inventory.generateStockReport());
    }

    public boolean validatePrescription(Prescription prescription) {
        return prescription.isValid() &&
                prescription.getDoctor() != null &&
                prescription.getPatient() != null;
    }

    // Getters
    public String getPharmacistId() { return pharmacistId; }
    public String getLicenseNumber() { return licenseNumber; }
    public String getShift() { return shift; }
    public Inventory getInventory() { return inventory; }
    public int getPrescriptionsFilled() { return prescriptionsFilled; }
}
