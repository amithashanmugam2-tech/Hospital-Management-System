# Hospital Management System (Java Console App)

A menu-driven, console-based Hospital Management System built with plain
Java (`java.util.*` only) — no Spring Boot, no Maven/Gradle, no database,
no external libraries. Compiles and runs with nothing but the JDK.

## How to Compile & Run

```bash
javac *.java
java Main
```

To run the end-to-end test script instead of typing manually:
```bash
java Main < sample_input.txt
```

## Login

- **Password:** `admin123` (hardcoded in `Authentication.java`)
- **3 attempts** allowed before the program terminates.

## Input Validation Rules (every field, every time)

All of these use a **retry loop** — invalid input never exits the
program or aborts the whole operation, it just re-asks the same field:

| Field | Rule |
|---|---|
| Patient / Doctor ID | Must be a positive whole number, and not already used |
| Name / Gender / Specialization | Letters and spaces only (no digits/symbols) |
| Gender | Must be exactly `Male`, `Female`, or `Transgender` |
| Age (Patient) | Positive whole number, no upper limit |
| Age (Doctor) | Must be at least 21 |
| Doctor Experience | Cannot exceed `(age - 21)` — e.g. a 19-year-old cannot have 75 years of experience |
| Phone Number | Exactly 10 digits, **must start with 6, 7, 8, or 9** (valid Indian mobile number) |
| Blood Group | Must be one of A+, A-, B+, B-, AB+, AB-, O+, O- (checked via array + linear search) |
| Appointment Date | Strict `dd-mm-yyyy` format |
| Appointment Time | Strict `hh:mm AM/PM` format |
| Charges / Fees | Non-negative numbers |

For ID lookups (searching/booking/billing by an existing Patient or
Doctor), typing **`0`** cancels that operation and returns to the menu.

## Booking an Appointment

Appointment booking asks for the **Doctor's name**, not ID:
- No match → re-asks for a name
- One match → auto-selected
- Multiple matches → shows a numbered list to pick from

## Main Menu

```
1.  Add Patient              11. Cancel Appointment
2.  View Patients            12. Add Emergency Patient
3.  Search Patient           13. View Emergency Queue
4.  Update Patient           14. Add Patient to Waiting Queue
5.  Delete Patient           15. View Waiting Queue
6.  Add Doctor               16. Serve Next Patient
7.  View Doctors             17. Generate Bill
8.  Search Doctor            18. Sort Patients by Name
9.  Book Appointment         19. Sort Patients by Age
10. View Appointments        20. Exit
```

## Design Highlights

| Requirement | Where |
|---|---|
| Singleton | `HospitalManagementSystem.getInstance()` |
| Arrays | `InputValidator.VALID_BLOOD_GROUPS` (plain `String[]`) |
| ArrayList | `PatientService` / `DoctorService` / `AppointmentService` |
| HashMap | `PatientService` / `DoctorService` (O(1) ID lookup) |
| Queue | `QueueService` → `Queue<Patient>` (FIFO waiting line) |
| PriorityQueue | `EmergencyService` → ordered by `EmergencyLevel` (Critical first) |
| Searching | HashMap lookup (fast) + manual linear search (name, blood group) |
| Sorting | `Collections.sort()` with `Comparable` (name) and `Comparator` (age) |
| OOP (4 pillars) | `Person` (abstract) → `Patient`, `Doctor`; `Displayable` interface |
| SOLID | Each `*Service` has one responsibility; controller depends on services, not raw data structures |
| Exception Handling | `HMSException` → `DuplicateIdException`, `RecordNotFoundException`, `InvalidInputException` |
| GST Billing | `Billing.GST_RATE` — single constant (18%), subtotal/GST/total computed automatically |

## Notes

- All data is in-memory only — nothing persists after the program exits.
- Change the GST rate: edit `GST_RATE` in `Billing.java`.
- Change the login password: edit `PASSWORD` in `Authentication.java`.
- Change the minimum doctor age: edit `MIN_DOCTOR_AGE` in `InputValidator.java`.
