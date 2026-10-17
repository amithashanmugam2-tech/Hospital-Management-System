/**
 * Person.java
 * ---------------------------------------------------------------------
 * Abstract parent class representing a generic person in the Hospital
 * Management System. Both Patient and Doctor inherit from this class.
 *
 * Demonstrates:
 *  - Abstraction (abstract class + abstract method getDetails())
 *  - Encapsulation (private fields with public getters/setters)
 *  - Inheritance (Patient and Doctor extend this class)
 * ---------------------------------------------------------------------
 */
public abstract class Person {

    // Encapsulated fields - accessible only through getters/setters
    private String id;
    private String name;
    private int age;
    private String gender;
    private String phone;

    /**
     * Constructor to initialize the common attributes of a person.
     */
    public Person(String id, String name, int age, String gender, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    // ---------------- Getters and Setters (Encapsulation) ----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Abstract method that every subclass must implement in its own way.
     * This is the core of run-time Polymorphism/Abstraction in this project.
     */
    public abstract String getDetails();
}
