import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentService.java
 * ---------------------------------------------------------------------
 * Handles booking, viewing and cancelling appointments, including a
 * validation check to make sure the same doctor is not double-booked
 * for the same date and time.
 * ---------------------------------------------------------------------
 */
public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();
    private int counter = 0;

    /**
     * Books a new appointment after checking the doctor's availability
     * at the requested date/time.
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, String date, String time)
            throws InvalidInputException {

        if (!InputValidator.isNonEmpty(date) || !InputValidator.isNonEmpty(time)) {
            throw new InvalidInputException("Date and time must not be empty.");
        }

        for (Appointment a : appointments) {
            if (a.getStatus().equals("Booked")
                    && a.getDoctor().getId().equals(doctor.getId())
                    && a.getDate().equalsIgnoreCase(date)
                    && a.getTime().equalsIgnoreCase(time)) {
                throw new InvalidInputException(
                        "Dr. " + doctor.getName() + " is already booked at " + date + " " + time + ".");
            }
        }

        counter++;
        String appointmentId = "APT" + String.format("%03d", counter);
        Appointment appointment = new Appointment(appointmentId, patient, doctor, date, time);
        appointments.add(appointment);
        return appointment;
    }

    public void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        for (Appointment a : appointments) {
            a.display();
        }
    }

    /** Marks an appointment as cancelled (linear search by ID). */
    public void cancelAppointment(String appointmentId) throws RecordNotFoundException {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                if (a.getStatus().equals("Cancelled")) {
                    System.out.println("Appointment is already cancelled.");
                    return;
                }
                a.setStatus("Cancelled");
                System.out.println("Appointment " + appointmentId + " cancelled successfully.");
                return;
            }
        }
        throw new RecordNotFoundException("No appointment found with ID: " + appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}
