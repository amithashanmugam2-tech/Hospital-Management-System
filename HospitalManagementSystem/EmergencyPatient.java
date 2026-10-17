/**
 * EmergencyPatient.java
 * ---------------------------------------------------------------------
 * Lightweight record used inside the Emergency PriorityQueue. Implements
 * Comparable so that Java's PriorityQueue can automatically order
 * patients with CRITICAL emergencies ahead of LOW ones.
 * ---------------------------------------------------------------------
 */
public class EmergencyPatient implements Displayable, Comparable<EmergencyPatient> {

    private String id;
    private String name;
    private EmergencyLevel level;

    public EmergencyPatient(String id, String name, EmergencyLevel level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmergencyLevel getLevel() {
        return level;
    }

    /**
     * Enum's compareTo is based on ordinal (CRITICAL=0 ... LOW=3), which
     * gives us exactly the priority ordering we need for a min-heap.
     */
    @Override
    public int compareTo(EmergencyPatient other) {
        return this.level.compareTo(other.level);
    }

    @Override
    public void display() {
        System.out.println("-----------------------------------------");
        System.out.println("Emergency Patient ID : " + id);
        System.out.println("Name                 : " + name);
        System.out.println("Emergency Level      : " + level);
        System.out.println("-----------------------------------------");
    }
}
