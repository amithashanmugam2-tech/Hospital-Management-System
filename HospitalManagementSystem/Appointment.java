/**
 * Appointment.java
 * ---------------------------------------------------------------------
 * Represents a booked appointment linking a Patient with a Doctor at a
 * given date and time.
 * ---------------------------------------------------------------------
 */
public class Appointment implements Displayable {

    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private String date; // format: dd-mm-yyyy
    private String time; // format: hh:mm AM/PM
    private String status; // "Booked" or "Cancelled"

    public Appointment(String appointmentId, Patient patient, Doctor doctor, String date, String time) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = "Booked";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void display() {
        System.out.println("-----------------------------------------");
        System.out.println("Appointment ID : " + appointmentId);
        System.out.println("Patient        : " + patient.getName() + " (" + patient.getId() + ")");
        System.out.println("Doctor         : Dr. " + doctor.getName() + " (" + doctor.getId() + ")");
        System.out.println("Date           : " + date);
        System.out.println("Time           : " + time);
        System.out.println("Status         : " + status);
        System.out.println("-----------------------------------------");
    }
}
