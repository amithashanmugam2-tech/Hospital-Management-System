/**
 * HMSException.java
 * ---------------------------------------------------------------------
 * Base checked exception for all Hospital Management System specific
 * errors. Extending Exception (not RuntimeException) forces calling
 * code to handle these situations explicitly, which is used throughout
 * the project to demonstrate proper Exception Handling.
 * ---------------------------------------------------------------------
 */
public class HMSException extends Exception {
    public HMSException(String message) {
        super(message);
    }
}
