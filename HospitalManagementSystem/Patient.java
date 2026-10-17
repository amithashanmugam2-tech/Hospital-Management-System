/**
 * Patient.java
 * ---------------------------------------------------------------------
 * Represents a hospital patient. Extends the abstract Person class and
 * adds patient-specific attributes.
 *
 * Demonstrates:
 *  - Inheritance (extends Person)
 *  - Polymorphism (overrides getDetails() and toString())
 *  - Interfaces (implements Displayable)
 *  - Comparable (natural ordering by name, used for "Sort by Name")
 *  - Method Overloading (two constructors)
 * ---------------------------------------------------------------------
 */
public class Patient extends Person implements Displayable, Comparable<Patient> {

    private String bloodGroup;
    private String disease;
    private String address;

    /**
     * Full constructor - all fields supplied.
     */
    public Patient(String id, String name, int age, String gender, String phone,
                    String bloodGroup, String disease, String address) {
        super(id, name, age, gender, phone);
        this.bloodGroup = bloodGroup;
        this.disease = disease;
        this.address = address;
    }

    /**
     * Overloaded constructor - address is optional and defaults to
     * "Not Provided" when omitted. Demonstrates Method Overloading.
     */
    public Patient(String id, String name, int age, String gender, String phone,
                    String bloodGroup, String disease) {
        this(id, name, age, gender, phone, bloodGroup, disease, "Not Provided");
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Overridden abstract method from Person (Runtime Polymorphism).
     */
    @Override
    public String getDetails() {
        return "Patient [ID=" + getId() + ", Name=" + getName() + ", Age=" + getAge()
                + ", Gender=" + getGender() + ", Phone=" + getPhone()
                + ", BloodGroup=" + bloodGroup + ", Disease=" + disease
                + ", Address=" + address + "]";
    }

    /**
     * Implementation of Displayable - prints a neatly formatted patient card.
     */
    @Override
    public void display() {
        System.out.println("-----------------------------------------");
        System.out.println("Patient ID   : " + getId());
        System.out.println("Name         : " + getName());
        System.out.println("Age          : " + getAge());
        System.out.println("Gender       : " + getGender());
        System.out.println("Phone        : " + getPhone());
        System.out.println("Blood Group  : " + bloodGroup);
        System.out.println("Disease      : " + disease);
        System.out.println("Address      : " + address);
        System.out.println("-----------------------------------------");
    }

    /**
     * Natural ordering by name - used by Collections.sort() for the
     * "Sort Patients by Name" menu option (Comparable).
     */
    @Override
    public int compareTo(Patient other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
