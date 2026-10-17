import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PatientService.java
 * ---------------------------------------------------------------------
 * Handles all business logic related to Patient Management: add, view,
 * search, update, delete and sorting.
 *
 * Demonstrates:
 *  - HashMap for O(1) lookup by Patient ID (efficient searching)
 *  - ArrayList for ordered storage / iteration of all records
 *  - Linear search algorithms (search by name)
 *  - Collections.sort() with both Comparable and Comparator
 *  - Single Responsibility Principle (SOLID) - this class only deals
 *    with patient-related operations.
 * ---------------------------------------------------------------------
 */
public class PatientService {

    private final Map<String, Patient> patientMap = new HashMap<>();
    private final List<Patient> patientList = new ArrayList<>();

    /**
     * Adds a new patient. Throws DuplicateIdException if the ID is
     * already registered (prevents duplicate Patient IDs).
     */
    public void addPatient(Patient patient) throws DuplicateIdException {
        if (patientMap.containsKey(patient.getId())) {
            throw new DuplicateIdException("A patient with ID '" + patient.getId() + "' already exists.");
        }
        patientMap.put(patient.getId(), patient);
        patientList.add(patient);
    }

    /** Displays every patient currently stored. */
    public void viewAllPatients() {
        if (patientList.isEmpty()) {
            System.out.println("No patient records found.");
            return;
        }
        for (Patient p : patientList) {
            p.display();
        }
    }

    /**
     * Fast lookup by ID using the HashMap. Throws RecordNotFoundException
     * if no such patient exists.
     */
    public Patient searchById(String id) throws RecordNotFoundException {
        Patient p = patientMap.get(id);
        if (p == null) {
            throw new RecordNotFoundException("No patient found with ID: " + id);
        }
        return p;
    }

    /**
     * Internal helper (no exception) used by other services that only
     * need to check existence without failing the whole operation.
     */
    public Patient findById(String id) {
        return patientMap.get(id);
    }

    /**
     * Linear search through the ArrayList looking for a (partial,
     * case-insensitive) name match. Demonstrates a manual search
     * algorithm over the Java Collections Framework.
     */
    public List<Patient> searchByName(String name) {
        List<Patient> results = new ArrayList<>();
        for (Patient p : patientList) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Updates an existing patient's mutable fields.
     */
    public void updatePatient(String id, int age, String phone, String bloodGroup,
                               String disease, String address) throws RecordNotFoundException {
        Patient p = patientMap.get(id);
        if (p == null) {
            throw new RecordNotFoundException("No patient found with ID: " + id);
        }
        p.setAge(age);
        p.setPhone(phone);
        p.setBloodGroup(bloodGroup);
        p.setDisease(disease);
        p.setAddress(address);
    }

    /** Removes a patient from both the map and the list. */
    public void deletePatient(String id) throws RecordNotFoundException {
        Patient p = patientMap.remove(id);
        if (p == null) {
            throw new RecordNotFoundException("No patient found with ID: " + id);
        }
        patientList.remove(p);
    }

    /** Sorts patients by name using Patient's natural ordering (Comparable). */
    public void sortByName() {
        Collections.sort(patientList);
    }

    /** Sorts patients by age using an external Comparator. */
    public void sortByAge() {
        Collections.sort(patientList, new PatientAgeComparator());
    }

    public List<Patient> getAllPatients() {
        return patientList;
    }

    public boolean isEmpty() {
        return patientList.isEmpty();
    }
}
