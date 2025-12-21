import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billId;
    private String date;
    private String dueDate;
    private double totalAmount;
    private double paidAmount;
    private double tax;
    private double discount;
    private String status;
    private String paymentMethod;
    private List<String> items;

    // UNIDIRECTIONAL ASSOCIATION (Bill knows Patient, Patient doesn't know Bill)
    private Patient patient;

    private static int nextId = 1;

    public Bill(Patient patient) {
        this.billId = nextId++;
        this.date = LocalDateTime.now().toString();
        this.patient = patient;
        this.dueDate = LocalDateTime.now().plusDays(30).toString();
        this.totalAmount = 0.0;
        this.paidAmount = 0.0;
        this.tax = 0.0;
        this.discount = 0.0;
        this.status = "Pending";
        this.paymentMethod = "";
        this.items = new ArrayList<>();
    }

    public void calculateTotal() {
        totalAmount = totalAmount + (totalAmount * tax / 100) - discount;
    }

    public void addItem(String description, double amount) {
        items.add(description + ": $" + amount);
        totalAmount += amount;
        calculateTotal();
    }

    public boolean makePayment(double amount) {
        if (amount <= 0) return false;

        paidAmount += amount;
        if (paidAmount >= totalAmount) {
            status = "Paid";
        } else if (paidAmount > 0) {
            status = "Partial";
        }
        return true;
    }

    public void applyDiscount(double percentage) {
        discount = totalAmount * percentage / 100;
        calculateTotal();
    }

    public String generateInvoice() {
        return "Invoice #" + billId + "\n" +
                "Patient: " + patient.getUsername() + "\n" +
                "Date: " + date + "\n" +
                "Items:\n" + String.join("\n", items) + "\n" +
                "Total: $" + totalAmount + "\n" +
                "Paid: $" + paidAmount + "\n" +
                "Status: " + status;
    }

    public boolean isOverdue() {
        LocalDateTime due = LocalDateTime.parse(dueDate);
        return LocalDateTime.now().isAfter(due) && !status.equals("Paid");
    }

    public double getBalance() {
        return totalAmount - paidAmount;
    }

    // Getters
    public int getBillId() { return billId; }
    public String getDate() { return date; }
    public String getDueDate() { return dueDate; }
    public double getTotalAmount() { return totalAmount; }
    public double getPaidAmount() { return paidAmount; }
    public double getTax() { return tax; }
    public double getDiscount() { return discount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<String> getItems() { return items; }
    public Patient getPatient() { return patient; }
    public void setTax(double tax) { this.tax = tax; }
    public void setPaymentMethod(String method) { this.paymentMethod = method; }
}
