import java.util.List;
import java.util.Scanner;

/**
 * HospitalManagementSystem.java
 * ---------------------------------------------------------------------
 * The central controller of the application, implemented as a Singleton
 * so that exactly one instance manages all shared services (Patient,
 * Doctor, Appointment, Emergency, Queue, Billing) for the lifetime of
 * the program.
 *
 * INPUT VALIDATION STRATEGY (important):
 *  - Every field the user types is validated for the CORRECT TYPE
 *    (e.g. Name/Gender must be letters only, Age/Phone/IDs must be
 *    numeric, Blood Group must match a real blood group, Phone must be
 *    a valid Indian mobile number).
 *  - When invalid input is entered, the program prints an "Invalid
 *    input" message and RE-PROMPTS THE SAME FIELD in a loop - it never
 *    exits, crashes, or aborts the whole operation just because one
 *    field was typed wrong.
 *  - For fields that reference existing records (Patient ID, Doctor ID
 *    while booking/billing/searching), typing "0" lets the user cancel
 *    that single operation cleanly and return to the main menu, instead
 *    of being stuck in an infinite loop if they don't know a valid ID.
 *
 * Demonstrates:
 *  - Singleton Design Pattern
 *  - SOLID Principles:
 *      S - Each *Service class has a single responsibility.
 *      O - New menu options / services can be added without modifying
 *          existing service classes.
 *      L - Patient/Doctor can substitute Person anywhere it is used.
 *      I - Displayable is a small, focused interface.
 *      D - This controller depends on service abstractions/behaviour
 *          rather than duplicating their internal logic.
 *  - Modular programming (all logic delegated to dedicated services)
 * ---------------------------------------------------------------------
 */
public class HospitalManagementSystem {

    // The single shared instance (Singleton pattern)
    private static HospitalManagementSystem instance;

    // Core services
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final EmergencyService emergencyService;
    private final QueueService queueService;
    private final BillingService billingService;

    /**
     * Private constructor prevents external instantiation - the only
     * way to get an instance is through getInstance().
     */
    private HospitalManagementSystem() {
        patientService = new PatientService();
        doctorService = new DoctorService();
        appointmentService = new AppointmentService();
        emergencyService = new EmergencyService();
        queueService = new QueueService();
        billingService = new BillingService();
    }

    /** Global access point to the single instance (Singleton pattern). */
    public static HospitalManagementSystem getInstance() {
        if (instance == null) {
            instance = new HospitalManagementSystem();
        }
        return instance;
    }

    /**
     * Runs the main menu loop until the user chooses to exit.
     */
    public void run(Scanner sc) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 20.\n");
                continue;
            }

            try {
                switch (choice) {
                    case 1: addPatient(sc); break;
                    case 2: viewPatients(); break;
                    case 3: searchPatient(sc); break;
                    case 4: updatePatient(sc); break;
                    case 5: deletePatient(sc); break;
                    case 6: addDoctor(sc); break;
                    case 7: viewDoctors(); break;
                    case 8: searchDoctor(sc); break;
                    case 9: bookAppointment(sc); break;
                    case 10: viewAppointments(); break;
                    case 11: cancelAppointment(sc); break;
                    case 12: addEmergencyPatient(sc); break;
                    case 13: viewEmergencyQueue(); break;
                    case 14: addToWaitingQueue(sc); break;
                    case 15: viewWaitingQueue(); break;
                    case 16: serveNextPatient(); break;
                    case 17: generateBill(sc); break;
                    case 18: sortPatientsByName(); break;
                    case 19: sortPatientsByAge(); break;
                    case 20:
                        running = false;
                        System.out.println("Thank you for using the Hospital Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 20.");
                }
            } catch (HMSException e) {
                // Safety net for any business-rule exception (e.g. a duplicate
                // slipping through). The program still never crashes - it just
                // reports the error and the menu loop continues.
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                // Safety net for any unexpected runtime issue
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("=========================================");
        System.out.println("      HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("=========================================");
        System.out.println("1.  Add Patient");
        System.out.println("2.  View Patients");
        System.out.println("3.  Search Patient");
        System.out.println("4.  Update Patient");
        System.out.println("5.  Delete Patient");
        System.out.println("6.  Add Doctor");
        System.out.println("7.  View Doctors");
        System.out.println("8.  Search Doctor");
        System.out.println("9.  Book Appointment");
        System.out.println("10. View Appointments");
        System.out.println("11. Cancel Appointment");
        System.out.println("12. Add Emergency Patient");
        System.out.println("13. View Emergency Queue");
        System.out.println("14. Add Patient to Waiting Queue");
        System.out.println("15. View Waiting Queue");
        System.out.println("16. Serve Next Patient");
        System.out.println("17. Generate Bill");
        System.out.println("18. Sort Patients by Name");
        System.out.println("19. Sort Patients by Age");
        System.out.println("20. Exit");
        System.out.println("=========================================");
        System.out.print("Enter your choice: ");
    }

    // ==================== PATIENT MANAGEMENT ====================

    private void addPatient(Scanner sc) throws HMSException {
        String id = readNewPatientId(sc);
        if (id == null) {
            System.out.println("Add Patient cancelled.");
            return;
        }

        String name = readAlphaText(sc, "Enter Name (letters only): ");
        int age = readValidAge(sc);
        String gender = readGender(sc);
        String phone = readValidPhone(sc);
        String bloodGroup = readBloodGroup(sc);
        String disease = readNonEmptyText(sc, "Enter Disease: ");

        System.out.print("Enter Address: ");
        String address = sc.nextLine().trim();
        if (!InputValidator.isNonEmpty(address)) {
            address = "Not Provided";
        }

        Patient patient = new Patient(id, name, age, gender, phone, bloodGroup, disease, address);
        patientService.addPatient(patient);
        System.out.println("Patient added successfully.");
    }

    private void viewPatients() {
        System.out.println("\n--- Patient List ---");
        patientService.viewAllPatients();
    }

    private void searchPatient(Scanner sc) {
        System.out.println("Search by: 1. ID   2. Name");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            Patient p = readExistingPatient(sc, "Enter Patient ID (or 0 to cancel): ");
            if (p == null) {
                System.out.println("Search cancelled.");
                return;
            }
            p.display();
        } else if (choice.equals("2")) {
            System.out.print("Enter Patient Name (or part of it): ");
            String name = sc.nextLine().trim();
            List<Patient> results = patientService.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No patients found matching: " + name);
            } else {
                for (Patient p : results) {
                    p.display();
                }
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void updatePatient(Scanner sc) {
        Patient existing = readExistingPatient(sc, "Enter Patient ID to update (or 0 to cancel): ");
        if (existing == null) {
            System.out.println("Update cancelled.");
            return;
        }

        int age = readValidAge(sc);
        String phone = readValidPhone(sc);
        String bloodGroup = readBloodGroup(sc);
        String disease = readNonEmptyText(sc, "Enter new Disease: ");

        System.out.print("Enter new Address: ");
        String address = sc.nextLine().trim();
        if (!InputValidator.isNonEmpty(address)) {
            address = existing.getAddress();
        }

        try {
            patientService.updatePatient(existing.getId(), age, phone, bloodGroup, disease, address);
            System.out.println("Patient updated successfully.");
        } catch (RecordNotFoundException e) {
            // Defensive only - we already confirmed the record exists above.
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deletePatient(Scanner sc) {
        Patient existing = readExistingPatient(sc, "Enter Patient ID to delete (or 0 to cancel): ");
        if (existing == null) {
            System.out.println("Delete cancelled.");
            return;
        }
        try {
            patientService.deletePatient(existing.getId());
            System.out.println("Patient deleted successfully.");
        } catch (RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void sortPatientsByName() {
        patientService.sortByName();
        System.out.println("Patients sorted by name:");
        patientService.viewAllPatients();
    }

    private void sortPatientsByAge() {
        patientService.sortByAge();
        System.out.println("Patients sorted by age:");
        patientService.viewAllPatients();
    }

    // ==================== DOCTOR MANAGEMENT ====================

    private void addDoctor(Scanner sc) throws HMSException {
        String id = readNewDoctorId(sc);
        if (id == null) {
            System.out.println("Add Doctor cancelled.");
            return;
        }

        String name = readAlphaText(sc, "Enter Name (letters only): ");
        int age = readDoctorAge(sc);
        String gender = readGender(sc);
        String phone = readValidPhone(sc);
        String specialization = readAlphaText(sc, "Enter Specialization (letters only): ");
        int experience = readValidExperience(sc, age);
        double fee = readValidDouble(sc, "Enter Consultation Fee: ");

        Doctor doctor = new Doctor(id, name, age, gender, phone, specialization, experience, fee);
        doctorService.addDoctor(doctor);
        System.out.println("Doctor added successfully.");
    }

    private void viewDoctors() {
        System.out.println("\n--- Doctor List ---");
        doctorService.viewAllDoctors();
    }

    private void searchDoctor(Scanner sc) {
        System.out.println("Search by: 1. ID   2. Name");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            Doctor d = readExistingDoctor(sc, "Enter Doctor ID (or 0 to cancel): ");
            if (d == null) {
                System.out.println("Search cancelled.");
                return;
            }
            d.display();
        } else if (choice.equals("2")) {
            System.out.print("Enter Doctor Name (or part of it): ");
            String name = sc.nextLine().trim();
            List<Doctor> results = doctorService.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No doctors found matching: " + name);
            } else {
                for (Doctor d : results) {
                    d.display();
                }
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // ==================== APPOINTMENT MANAGEMENT ====================

    private void bookAppointment(Scanner sc) {
        Patient patient = readExistingPatient(sc, "Enter Patient ID (or 0 to cancel): ");
        if (patient == null) {
            System.out.println("Booking cancelled.");
            return;
        }

        Doctor doctor = readExistingDoctorByName(sc);
        if (doctor == null) {
            System.out.println("Booking cancelled.");
            return;
        }

        // Loop so that a doctor-already-booked conflict just asks for a
        // different date/time instead of throwing the user back to the menu.
        while (true) {
            String date = readValidDate(sc);
            String time = readValidTime(sc);
            try {
                Appointment appointment = appointmentService.bookAppointment(patient, doctor, date, time);
                System.out.println("Appointment booked successfully!");
                appointment.display();
                return;
            } catch (InvalidInputException e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try a different date/time.");
            }
        }
    }

    private void viewAppointments() {
        System.out.println("\n--- Appointment List ---");
        appointmentService.viewAppointments();
    }

    private void cancelAppointment(Scanner sc) {
        while (true) {
            System.out.print("Enter Appointment ID to cancel (or 0 to cancel this action): ");
            String id = sc.nextLine().trim();
            if (id.equals("0")) {
                System.out.println("Action cancelled.");
                return;
            }
            try {
                appointmentService.cancelAppointment(id);
                return;
            } catch (RecordNotFoundException e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }
    }

    // ==================== EMERGENCY MANAGEMENT ====================

    private void addEmergencyPatient(Scanner sc) {
        String id = readNumericIdFormat(sc, "Enter Emergency Patient ID (numbers only): ");
        String name = readAlphaText(sc, "Enter Name (letters only): ");
        EmergencyLevel level = readEmergencyLevel(sc);

        emergencyService.addEmergencyPatient(new EmergencyPatient(id, name, level));
        System.out.println("Emergency patient added successfully.");
    }

    private void viewEmergencyQueue() {
        System.out.println("\n--- Emergency Queue (Priority Order) ---");
        emergencyService.viewEmergencyQueue();
    }

    // ==================== WAITING QUEUE MANAGEMENT ====================

    private void addToWaitingQueue(Scanner sc) {
        Patient p = readExistingPatient(sc, "Enter Patient ID to add to waiting queue (or 0 to cancel): ");
        if (p == null) {
            System.out.println("Action cancelled.");
            return;
        }
        queueService.addToQueue(p);
        System.out.println(p.getName() + " added to the waiting queue.");
    }

    private void viewWaitingQueue() {
        System.out.println("\n--- Waiting Queue ---");
        queueService.viewQueue();
    }

    private void serveNextPatient() {
        Patient p = queueService.serveNextPatient();
        if (p == null) {
            System.out.println("Waiting queue is empty. No patient to serve.");
        } else {
            System.out.println("Now serving: " + p.getName() + " (ID: " + p.getId() + ")");
        }
    }

    // ==================== BILLING ====================

    private void generateBill(Scanner sc) {
        Patient patient = readExistingPatient(sc, "Enter Patient ID (or 0 to cancel): ");
        if (patient == null) {
            System.out.println("Billing cancelled.");
            return;
        }

        Doctor doctor = readExistingDoctor(sc, "Enter Doctor ID (or 0 to cancel): ");
        if (doctor == null) {
            System.out.println("Billing cancelled.");
            return;
        }

        // Consultation fee is retrieved automatically - not entered by the user
        double consultationFee = doctor.getConsultationFee();

        double medicine = readValidDouble(sc, "Enter Medicine Charges: ");
        double room = readValidDouble(sc, "Enter Room Charges: ");
        double lab = readOptionalDouble(sc, "Enter Laboratory/Test Charges (press Enter to skip): ");
        double other = readOptionalDouble(sc, "Enter Other Charges (press Enter to skip): ");

        Billing bill = billingService.generateBill(
                patient.getName(), doctor.getName(), consultationFee, medicine, room, lab, other);

        System.out.println("\nBill generated successfully!\n");
        bill.display();
    }

    // ==================== INPUT HELPER METHODS (retry-loop validation) ====================
    // Every method below re-prompts on invalid input instead of throwing an
    // exception that would abort the whole operation. The user simply gets
    // an "Invalid input" message and is asked for the same field again.

    /** Loops until the user enters a non-empty string. */
    private String readNonEmptyText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!InputValidator.isNonEmpty(value)) {
                System.out.println("Invalid input: this field cannot be empty. Please try again.");
                continue;
            }
            return value;
        }
    }

    /** Loops until the user enters letters/spaces only (rejects digits/symbols). */
    private String readAlphaText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!InputValidator.isAlpha(value)) {
                System.out.println("Invalid input: only letters are allowed here (no numbers/symbols). Please try again.");
                continue;
            }
            return value;
        }
    }

    /** Loops until the user enters exactly Male, Female, or Transgender (case-insensitive). */
    private String readGender(Scanner sc) {
        while (true) {
            System.out.print("Enter Gender (Male / Female / Transgender): ");
            String value = sc.nextLine().trim();
            if (!InputValidator.isValidGender(value)) {
                System.out.println("Invalid input: Gender must be Male, Female, or Transgender. Please try again.");
                continue;
            }
            // Normalize to a consistent, nicely capitalized form for storage/display
            String upper = value.trim().toUpperCase();
            if (upper.equals("MALE")) {
                return "Male";
            } else if (upper.equals("FEMALE")) {
                return "Female";
            } else {
                return "Transgender";
            }
        }
    }

    /** Loops until a valid positive whole-number age is entered (no upper limit). */
    private int readValidAge(Scanner sc) {
        while (true) {
            System.out.print("Enter Age: ");
            String input = sc.nextLine().trim();
            try {
                int age = Integer.parseInt(input);
                if (!InputValidator.isValidAge(age)) {
                    System.out.println("Invalid input: Age must be a positive whole number. Please try again.");
                    continue;
                }
                return age;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Age must be a valid whole number, not text. Please try again.");
            }
        }
    }

    /**
     * Loops until a valid doctor age is entered. A doctor must be at
     * least InputValidator.MIN_DOCTOR_AGE years old (no upper limit) -
     * this is what makes the later experience check meaningful.
     */
    private int readDoctorAge(Scanner sc) {
        while (true) {
            System.out.print("Enter Age: ");
            String input = sc.nextLine().trim();
            try {
                int age = Integer.parseInt(input);
                if (!InputValidator.isValidDoctorAge(age)) {
                    System.out.println("Invalid input: A doctor must be at least "
                            + InputValidator.MIN_DOCTOR_AGE + " years old. Please try again.");
                    continue;
                }
                return age;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Age must be a valid whole number, not text. Please try again.");
            }
        }
    }

    /**
     * Loops until a valid years-of-experience is entered for the given
     * doctor age. Experience cannot exceed (age - MIN_DOCTOR_AGE) -
     * this is exactly the check that rejects an impossible combination
     * like "age 19, experience 75".
     */
    private int readValidExperience(Scanner sc, int age) {
        int maxPossible = age - InputValidator.MIN_DOCTOR_AGE;
        while (true) {
            System.out.print("Enter Years of Experience (max possible for this age: " + maxPossible + "): ");
            String input = sc.nextLine().trim();
            try {
                int experience = Integer.parseInt(input);
                if (!InputValidator.isValidExperience(age, experience)) {
                    System.out.println("Invalid input: A " + age + "-year-old doctor cannot have " + experience
                            + " years of experience. Maximum possible is " + maxPossible
                            + " years (assuming practice started around age " + InputValidator.MIN_DOCTOR_AGE
                            + "). Please try again.");
                    continue;
                }
                return experience;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Experience must be a valid whole number, not text. Please try again.");
            }
        }
    }

    /** Loops until a valid INDIAN 10-digit mobile number (starting with 6-9) is entered. */
    private String readValidPhone(Scanner sc) {
        while (true) {
            System.out.print("Enter Phone Number (10-digit Indian mobile number, starts with 6-9): ");
            String phone = sc.nextLine().trim();
            if (!InputValidator.isValidPhone(phone)) {
                System.out.println("Invalid input: Phone number must be exactly 10 digits and start with 6, 7, 8, or 9 (valid Indian mobile number). Please try again.");
                continue;
            }
            return phone;
        }
    }

    /** Loops until a valid blood group (A+, A-, B+, B-, AB+, AB-, O+, O-, A1+, A1-, Bombay) is entered. */
    private String readBloodGroup(Scanner sc) {
        while (true) {
            System.out.print("Enter Blood Group (e.g. A+, O-, AB+, A1+, Bombay): ");
            String bloodGroup = sc.nextLine().trim().toUpperCase();
            if (!InputValidator.isValidBloodGroup(bloodGroup)) {
                System.out.println("Invalid input: Blood group must be one of A+, A-, B+, B-, AB+, AB-, O+, O-, A1+, A1-, Bombay. Please try again.");
                continue;
            }
            return bloodGroup;
        }
    }

    /** Loops until a valid non-negative decimal value is entered (required field). */
    private double readValidDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (!InputValidator.isNonNegative(value)) {
                    System.out.println("Invalid input: Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a valid numeric value, not text. Please try again.");
            }
        }
    }

    /** Loops until a valid non-negative decimal value is entered, OR blank (defaults to 0.0). */
    private double readOptionalDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return 0.0;
            }
            try {
                double value = Double.parseDouble(input);
                if (!InputValidator.isNonNegative(value)) {
                    System.out.println("Invalid input: Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a valid numeric value, or press Enter to skip. Please try again.");
            }
        }
    }

    /** Loops until the ID is numeric (no uniqueness requirement - used for Emergency Patients). */
    private String readNumericIdFormat(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = sc.nextLine().trim();
            if (!InputValidator.isNumericId(id)) {
                System.out.println("Invalid input: ID must be a positive whole number. Please try again.");
                continue;
            }
            return id;
        }
    }

    /** Loops until a NEW, numeric, non-duplicate Patient ID is entered ("0" cancels). */
    private String readNewPatientId(Scanner sc) {
        while (true) {
            System.out.print("Enter Patient ID (numbers only, e.g. 101, or 0 to cancel): ");
            String id = sc.nextLine().trim();
            if (id.equals("0")) {
                return null;
            }
            if (!InputValidator.isNumericId(id)) {
                System.out.println("Invalid input: Patient ID must be a positive whole number. Please try again.");
                continue;
            }
            if (patientService.findById(id) != null) {
                System.out.println("Invalid input: Patient ID '" + id + "' already exists. Please choose another.");
                continue;
            }
            return id;
        }
    }

    /** Loops until a NEW, numeric, non-duplicate Doctor ID is entered ("0" cancels). */
    private String readNewDoctorId(Scanner sc) {
        while (true) {
            System.out.print("Enter Doctor ID (numbers only, e.g. 501, or 0 to cancel): ");
            String id = sc.nextLine().trim();
            if (id.equals("0")) {
                return null;
            }
            if (!InputValidator.isNumericId(id)) {
                System.out.println("Invalid input: Doctor ID must be a positive whole number. Please try again.");
                continue;
            }
            if (doctorService.findById(id) != null) {
                System.out.println("Invalid input: Doctor ID '" + id + "' already exists. Please choose another.");
                continue;
            }
            return id;
        }
    }

    /** Loops asking for a Patient ID until an existing patient is found ("0" cancels). */
    private Patient readExistingPatient(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = sc.nextLine().trim();
            if (id.equals("0")) {
                return null;
            }
            Patient p = patientService.findById(id);
            if (p == null) {
                System.out.println("Invalid input: No patient found with ID '" + id + "'. Please try again (or 0 to cancel).");
                continue;
            }
            return p;
        }
    }

    /** Loops asking for a Doctor ID until an existing doctor is found ("0" cancels). */
    private Doctor readExistingDoctor(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = sc.nextLine().trim();
            if (id.equals("0")) {
                return null;
            }
            Doctor d = doctorService.findById(id);
            if (d == null) {
                System.out.println("Invalid input: No doctor found with ID '" + id + "'. Please try again (or 0 to cancel).");
                continue;
            }
            return d;
        }
    }

    /**
     * Loops asking for a Doctor's NAME (instead of ID) until a doctor is
     * selected. Handles three cases without ever exiting the operation:
     *   - No match at all              -> re-ask for a name
     *   - Exactly one match            -> use it directly
     *   - Multiple doctors share it    -> list them and let the user pick
     * Typing "0" at any prompt cancels the whole booking.
     */
    private Doctor readExistingDoctorByName(Scanner sc) {
        while (true) {
            System.out.print("Enter Doctor Name (or part of it, or 0 to cancel): ");
            String name = sc.nextLine().trim();
            if (name.equals("0")) {
                return null;
            }
            if (!InputValidator.isNonEmpty(name)) {
                System.out.println("Invalid input: Doctor name cannot be empty. Please try again.");
                continue;
            }

            List<Doctor> matches = doctorService.searchByName(name);

            if (matches.isEmpty()) {
                System.out.println("Invalid input: No doctor found matching '" + name + "'. Please try again (or 0 to cancel).");
                continue;
            }

            if (matches.size() == 1) {
                Doctor onlyMatch = matches.get(0);
                System.out.println("Found: Dr. " + onlyMatch.getName() + " (" + onlyMatch.getSpecialization() + ")");
                return onlyMatch;
            }

            // Multiple doctors matched the name - show a numbered list and let the user choose
            System.out.println("Multiple doctors matched '" + name + "':");
            for (int i = 0; i < matches.size(); i++) {
                Doctor d = matches.get(i);
                System.out.println((i + 1) + ". Dr. " + d.getName() + " - " + d.getSpecialization()
                        + " (ID: " + d.getId() + ")");
            }

            Doctor selected = selectFromList(sc, matches);
            if (selected != null) {
                return selected;
            }
            // selected == null means the user chose to search by name again
        }
    }

    /**
     * Lets the user pick one doctor from an already-displayed numbered
     * list. Returns null if the user types "0", which sends control back
     * to readExistingDoctorByName() to search again with a different name.
     */
    private Doctor selectFromList(Scanner sc, List<Doctor> matches) {
        while (true) {
            System.out.print("Enter the number of the doctor you want (or 0 to search again): ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > matches.size()) {
                    System.out.println("Invalid input: Please enter a number between 1 and " + matches.size() + ". Please try again.");
                    continue;
                }
                return matches.get(choice - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a valid whole number, not text. Please try again.");
            }
        }
    }

    /**
     * Loops until a valid, real calendar date in dd-mm-yyyy format is
     * entered, AND that date is today or later - a past date (e.g.
     * "01-01-2020") is rejected and the user is asked again.
     */
    private String readValidDate(Scanner sc) {
        while (true) {
            System.out.print("Enter Appointment Date (dd-mm-yyyy, e.g. 21-07-2026): ");
            String date = sc.nextLine().trim();
            if (!InputValidator.isValidDate(date)) {
                System.out.println("Invalid input: Date must be a real date in dd-mm-yyyy format (e.g. 21-07-2026). Please try again.");
                continue;
            }
            if (!InputValidator.isNotPastDate(date)) {
                System.out.println("Invalid input: Appointment date cannot be in the past. Please enter today's date or a future date.");
                continue;
            }
            return date;
        }
    }

    /**
     * Loops until a valid time in "hh:mm AM/PM" format is entered, AND
     * that time falls within regular OPD consultation hours (9:00 AM -
     * 6:00 PM). This restriction applies ONLY to regular appointments
     * booked here - Emergency Patients (menu option 12) are added
     * straight to the priority queue with no date/time at all, so they
     * are never subject to this window and can be reported any time.
     */
    private String readValidTime(Scanner sc) {
        while (true) {
            System.out.print("Enter Appointment Time (hh:mm AM/PM, between 9:00 AM - 6:00 PM, e.g. 10:30 AM): ");
            String time = sc.nextLine().trim();
            if (!InputValidator.isValidTime(time)) {
                System.out.println("Invalid input: Time must be in hh:mm AM/PM format (e.g. 10:30 AM, 09:05 PM). Please try again.");
                continue;
            }
            if (!InputValidator.isWithinConsultationHours(time)) {
                System.out.println("Invalid input: Doctors are available for regular consultation only between 9:00 AM and 6:00 PM. "
                        + "(Emergency patients can be attended to at any time via the Emergency Queue instead.) Please try again.");
                continue;
            }
            // Normalize so "9:05 pm" and "9:05 PM" store/display identically
            return time.toUpperCase();
        }
    }

    /** Loops until a valid emergency level (1-4) is chosen. */
    private EmergencyLevel readEmergencyLevel(Scanner sc) {
        while (true) {
            System.out.println("Select Emergency Level:");
            System.out.println("1. Critical  2. High  3. Medium  4. Low");
            System.out.print("Enter choice: ");
            String levelChoice = sc.nextLine().trim();
            switch (levelChoice) {
                case "1": return EmergencyLevel.CRITICAL;
                case "2": return EmergencyLevel.HIGH;
                case "3": return EmergencyLevel.MEDIUM;
                case "4": return EmergencyLevel.LOW;
                default:
                    System.out.println("Invalid input: please enter a number from 1 to 4. Please try again.");
            }
        }
    }
}
