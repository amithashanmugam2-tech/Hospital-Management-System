import java.util.ArrayList;
import java.util.List;

/**
 * BillingService.java
 * ---------------------------------------------------------------------
 * Generates Billing objects for patients. The doctor's consultation
 * fee is retrieved automatically from the Doctor object - the user
 * only supplies the variable charges (medicine, room, lab, other).
 * ---------------------------------------------------------------------
 */
public class BillingService {

    private final List<Billing> bills = new ArrayList<>();

    /**
     * Creates, stores and returns a new Billing record. All arithmetic
     * (subtotal, GST, total) happens inside the Billing constructor.
     */
    public Billing generateBill(String patientName, String doctorName, double consultationFee,
                                 double medicineCharges, double roomCharges,
                                 double labCharges, double otherCharges) {
        Billing bill = new Billing(patientName, doctorName, consultationFee,
                medicineCharges, roomCharges, labCharges, otherCharges);
        bills.add(bill);
        return bill;
    }

    public List<Billing> getAllBills() {
        return bills;
    }
}
