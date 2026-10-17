/**
 * Billing.java
 * ---------------------------------------------------------------------
 * Represents a generated hospital bill. All monetary calculations
 * (subtotal, GST, total) are performed programmatically inside the
 * constructor using plain Java arithmetic - the user never enters GST
 * or the final amount manually.
 *
 * The GST rate is a single configurable constant (GST_RATE) so it can
 * be changed in exactly one place as required by the specification.
 * ---------------------------------------------------------------------
 */
public class Billing implements Displayable {

    /** Configurable GST rate in percent. Change this single constant to alter GST everywhere. */
    public static final double GST_RATE = 18.0;

    // Simple incrementing counter shared by all Billing instances to auto-generate bill IDs
    private static int billCounter = 0;

    private final String billId;
    private final String patientName;
    private final String doctorName;
    private final double consultationFee;
    private final double medicineCharges;
    private final double roomCharges;
    private final double labCharges;
    private final double otherCharges;
    private final double subtotal;
    private final double gstAmount;
    private final double totalAmount;

    public Billing(String patientName, String doctorName, double consultationFee,
                    double medicineCharges, double roomCharges, double labCharges, double otherCharges) {
        billCounter++;
        this.billId = "BILL" + String.format("%03d", billCounter);
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.consultationFee = consultationFee;
        this.medicineCharges = medicineCharges;
        this.roomCharges = roomCharges;
        this.labCharges = labCharges;
        this.otherCharges = otherCharges;

        // ---- All calculations performed programmatically ----
        this.subtotal = consultationFee + medicineCharges + roomCharges + labCharges + otherCharges;
        this.gstAmount = (subtotal * GST_RATE) / 100.0;
        this.totalAmount = subtotal + gstAmount;
    }

    public String getBillId() {
        return billId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void display() {
        System.out.println("============= HOSPITAL BILL =============");
        System.out.println("Bill ID           : " + billId);
        System.out.println("Patient Name      : " + patientName);
        System.out.println("Doctor Name       : Dr. " + doctorName);
        System.out.println("Consultation Fee  : " + String.format("%.2f", consultationFee));
        System.out.println("Medicine Charges  : " + String.format("%.2f", medicineCharges));
        System.out.println("Room Charges      : " + String.format("%.2f", roomCharges));
        System.out.println("Lab Charges       : " + String.format("%.2f", labCharges));
        System.out.println("Other Charges     : " + String.format("%.2f", otherCharges));
        System.out.println("-----------------------------------------");
        System.out.println("Subtotal          : " + String.format("%.2f", subtotal));
        System.out.println("GST (" + (int) GST_RATE + "%)         : " + String.format("%.2f", gstAmount));
        System.out.println("-----------------------------------------");
        System.out.println("Total Amount      : " + String.format("%.2f", totalAmount));
        System.out.println("===========================================");
    }
}
