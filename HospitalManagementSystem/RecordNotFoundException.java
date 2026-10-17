/**
 * RecordNotFoundException.java
 * ---------------------------------------------------------------------
 * Thrown when a search/update/delete operation cannot locate the
 * requested Patient, Doctor, or Appointment record.
 * ---------------------------------------------------------------------
 */
public class RecordNotFoundException extends HMSException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
