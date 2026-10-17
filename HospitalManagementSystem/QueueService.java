import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * QueueService.java
 * ---------------------------------------------------------------------
 * Manages the normal (non-emergency) patient waiting line using the
 * Java Queue interface (FIFO ordering), backed by a LinkedList.
 * ---------------------------------------------------------------------
 */
public class QueueService {

    private final Queue<Patient> waitingQueue = new LinkedList<>();

    public void addToQueue(Patient patient) {
        waitingQueue.offer(patient);
    }

    public void viewQueue() {
        if (waitingQueue.isEmpty()) {
            System.out.println("Waiting queue is empty.");
            return;
        }
        int position = 1;
        for (Patient p : waitingQueue) {
            System.out.println("Position " + position + ": " + p.getName() + " (ID: " + p.getId() + ")");
            position++;
        }
    }

    /** Removes and returns the patient at the front of the queue (FIFO). */
    public Patient serveNextPatient() {
        return waitingQueue.poll();
    }

    /** Removes a specific patient from anywhere in the queue by ID. */
    public void removeFromQueue(String patientId) throws RecordNotFoundException {
        Iterator<Patient> it = waitingQueue.iterator();
        while (it.hasNext()) {
            Patient p = it.next();
            if (p.getId().equalsIgnoreCase(patientId)) {
                it.remove();
                return;
            }
        }
        throw new RecordNotFoundException("Patient with ID " + patientId + " is not in the waiting queue.");
    }

    public boolean isEmpty() {
        return waitingQueue.isEmpty();
    }
}
