/**
 * DuplicateIdException.java
 * ---------------------------------------------------------------------
 * Thrown when an attempt is made to add a Patient or Doctor whose ID
 * already exists in the system (prevents duplicate IDs as required).
 * ---------------------------------------------------------------------
 */
public class DuplicateIdException extends HMSException {
    public DuplicateIdException(String message) {
        super(message);
    }
}
