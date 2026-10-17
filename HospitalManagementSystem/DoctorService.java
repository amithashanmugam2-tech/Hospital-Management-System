import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DoctorService.java
 * ---------------------------------------------------------------------
 * Handles all business logic related to Doctor Management: add, view
 * and search. Mirrors the design of PatientService (HashMap + ArrayList)
 * to keep the codebase consistent and maintainable.
 * ---------------------------------------------------------------------
 */
public class DoctorService {

    private final Map<String, Doctor> doctorMap = new HashMap<>();
    private final List<Doctor> doctorList = new ArrayList<>();

    /** Adds a new doctor. Prevents duplicate Doctor IDs. */
    public void addDoctor(Doctor doctor) throws DuplicateIdException {
        if (doctorMap.containsKey(doctor.getId())) {
            throw new DuplicateIdException("A doctor with ID '" + doctor.getId() + "' already exists.");
        }
        doctorMap.put(doctor.getId(), doctor);
        doctorList.add(doctor);
    }

    public void viewAllDoctors() {
        if (doctorList.isEmpty()) {
            System.out.println("No doctor records found.");
            return;
        }
        for (Doctor d : doctorList) {
            d.display();
        }
    }

    /** Fast HashMap lookup by ID. */
    public Doctor searchById(String id) throws RecordNotFoundException {
        Doctor d = doctorMap.get(id);
        if (d == null) {
            throw new RecordNotFoundException("No doctor found with ID: " + id);
        }
        return d;
    }

    /** Internal helper used by other services - returns null instead of throwing. */
    public Doctor findById(String id) {
        return doctorMap.get(id);
    }

    /** Linear search by (partial, case-insensitive) name. */
    public List<Doctor> searchByName(String name) {
        List<Doctor> results = new ArrayList<>();
        for (Doctor d : doctorList) {
            if (d.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(d);
            }
        }
        return results;
    }

    public List<Doctor> getAllDoctors() {
        return doctorList;
    }

    public boolean isEmpty() {
        return doctorList.isEmpty();
    }
}
