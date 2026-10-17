import java.util.PriorityQueue;

/**
 * EmergencyService.java
 * ---------------------------------------------------------------------
 * Manages emergency patients using a Java PriorityQueue so that the
 * most CRITICAL patients are always served first, regardless of the
 * order they were added.
 * ---------------------------------------------------------------------
 */
public class EmergencyService {

    private final PriorityQueue<EmergencyPatient> emergencyQueue = new PriorityQueue<>();

    public void addEmergencyPatient(EmergencyPatient patient) {
        emergencyQueue.offer(patient);
    }

    /**
     * Displays patients in priority order without permanently removing
     * them from the queue - achieved by iterating over a temporary copy.
     */
    public void viewEmergencyQueue() {
        if (emergencyQueue.isEmpty()) {
            System.out.println("Emergency queue is empty.");
            return;
        }
        PriorityQueue<EmergencyPatient> copy = new PriorityQueue<>(emergencyQueue);
        int position = 1;
        while (!copy.isEmpty()) {
            EmergencyPatient ep = copy.poll();
            System.out.println("Priority Position #" + position);
            ep.display();
            position++;
        }
    }

    /** Removes and returns the highest priority patient (used if needed). */
    public EmergencyPatient treatNextPatient() {
        return emergencyQueue.poll();
    }

    public boolean isEmpty() {
        return emergencyQueue.isEmpty();
    }
}
