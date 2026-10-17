/**
 * Doctor.java
 * ---------------------------------------------------------------------
 * Represents a hospital doctor. Extends the abstract Person class.
 *
 * Demonstrates Inheritance, Polymorphism (overridden getDetails()),
 * Interfaces (Displayable), and Method Overloading (two constructors).
 * ---------------------------------------------------------------------
 */
public class Doctor extends Person implements Displayable {

    private String specialization;
    private int experience; // in years
    private double consultationFee;

    /** Full constructor. */
    public Doctor(String id, String name, int age, String gender, String phone,
                   String specialization, int experience, double consultationFee) {
        super(id, name, age, gender, phone);
        this.specialization = specialization;
        this.experience = experience;
        this.consultationFee = consultationFee;
    }

    /**
     * Overloaded constructor - consultationFee is optional and defaults
     * to a standard rate of 500.0 when omitted (Method Overloading).
     */
    public Doctor(String id, String name, int age, String gender, String phone,
                   String specialization, int experience) {
        this(id, name, age, gender, phone, specialization, experience, 500.0);
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public String getDetails() {
        return "Doctor [ID=" + getId() + ", Name=" + getName() + ", Specialization=" + specialization
                + ", Experience=" + experience + " yrs, Fee=" + String.format("%.2f", consultationFee) + "]";
    }

    @Override
    public void display() {
        System.out.println("-----------------------------------------");
        System.out.println("Doctor ID       : " + getId());
        System.out.println("Name            : Dr. " + getName());
        System.out.println("Specialization  : " + specialization);
        System.out.println("Experience      : " + experience + " years");
        System.out.println("Consultation Fee: " + String.format("%.2f", consultationFee));
        System.out.println("-----------------------------------------");
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
