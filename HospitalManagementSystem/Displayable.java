/**
 * Displayable.java
 * ---------------------------------------------------------------------
 * Interface implemented by any entity that can print a formatted view
 * of itself to the console (Patient, Doctor, Appointment, Billing,
 * EmergencyPatient).
 *
 * Demonstrates: Interfaces, Abstraction, and Polymorphism (each class
 * implements display() differently).
 * ---------------------------------------------------------------------
 */
public interface Displayable {
    void display();
}
