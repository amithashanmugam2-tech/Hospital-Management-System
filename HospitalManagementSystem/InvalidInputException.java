/**
 * InvalidInputException.java
 * ---------------------------------------------------------------------
 * Thrown when user-supplied data fails validation rules (empty fields,
 * invalid age, invalid phone number, unavailable appointment slot etc).
 * ---------------------------------------------------------------------
 */
public class InvalidInputException extends HMSException {
    public InvalidInputException(String message) {
        super(message);
    }
}
