import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private int inventoryId;
    private Map<String, Integer> medicines;  // Medicine name -> Quantity
    private Map<String, Double> prices;      // Medicine name -> Price
    private Map<String, String> suppliers;   // Medicine name -> Supplier
    private int lowStockThreshold;
    private LocalDateTime lastUpdated;

    // UNIDIRECTIONAL ASSOCIATION with Pharmacist (Inventory doesn't know Pharmacist)

    private static int nextId = 1;

    public Inventory() {
        this.inventoryId = nextId++;
        this.medicines = new HashMap<>();
        this.prices = new HashMap<>();
        this.suppliers = new HashMap<>();
        this.lowStockThreshold = 10;
        this.lastUpdated = LocalDateTime.now();

        // Add sample medicines
        addMedicine("Paracetamol", 100, 5.0, "Pharma Inc");
        addMedicine("Amoxicillin", 50, 15.0, "Med Corp");
        addMedicine("Ibuprofen", 75, 8.0, "Health Ltd");
    }

    public void addMedicine(String name, int quantity, double price, String supplier) {
        int currentQty = medicines.getOrDefault(name, 0);
        medicines.put(name, currentQty + quantity);
        prices.put(name, price);
        suppliers.put(name, supplier);
        lastUpdated = LocalDateTime.now();
    }

    public boolean removeMedicine(String name, int quantity) {
        if (!medicines.containsKey(name) || medicines.get(name) < quantity) {
            return false;
        }
        medicines.put(name, medicines.get(name) - quantity);
        lastUpdated = LocalDateTime.now();
        return true;
    }

    public int checkStock(String name) {
        return medicines.getOrDefault(name, 0);
    }

    public void updatePrice(String name, double newPrice) {
        if (prices.containsKey(name)) {
            prices.put(name, newPrice);
            lastUpdated = LocalDateTime.now();
        }
    }

    public List<String> checkLowStock() {
        List<String> lowStock = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : medicines.entrySet()) {
            if (entry.getValue() <= lowStockThreshold) {
                lowStock.add(entry.getKey() + ": " + entry.getValue() + " remaining");
            }
        }
        return lowStock;
    }

    public void reorderMedicine(String name, int quantity) {
        System.out.println("Reorder request: " + quantity + " units of " + name);
    }

    public double getMedicinePrice(String name) {
        return prices.getOrDefault(name, 0.0);
    }

    public String generateStockReport() {
        StringBuilder report = new StringBuilder();
        report.append("Inventory Report - ").append(lastUpdated).append("\n");
        report.append("================================\n");
        for (String medicine : medicines.keySet()) {
            report.append(medicine).append(": ")
                    .append(medicines.get(medicine)).append(" units, $")
                    .append(prices.get(medicine)).append("\n");
        }
        return report.toString();
    }

    // Getters
    public int getInventoryId() { return inventoryId; }
    public Map<String, Integer> getMedicines() { return medicines; }
    public Map<String, Double> getPrices() { return prices; }
    public Map<String, String> getSuppliers() { return suppliers; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
