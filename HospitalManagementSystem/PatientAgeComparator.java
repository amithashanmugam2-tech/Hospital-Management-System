import java.util.Comparator;

/**
 * PatientAgeComparator.java
 * ---------------------------------------------------------------------
 * External Comparator used to sort patients by age (ascending), used
 * alongside Patient's own Comparable (by name) to demonstrate both
 * Comparable and Comparator in the Java Collections Framework.
 * ---------------------------------------------------------------------
 */
public class PatientAgeComparator implements Comparator<Patient> {
    @Override
    public int compare(Patient p1, Patient p2) {
        return Integer.compare(p1.getAge(), p2.getAge());
    }
}
