import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * InputValidator.java
 * ---------------------------------------------------------------------
 * Utility class containing static helper methods used to validate user
 * input throughout the application (Single Responsibility Principle -
 * validation logic lives in exactly one place).
 *
 * Uses java.time (LocalDate/LocalTime) - part of the core Java Standard
 * Library (java.*), not an external library - for real calendar-aware
 * date checks (rejecting things like "31-02-2026") and for comparing
 * against today's date/the clinic's consultation hours.
 * ---------------------------------------------------------------------
 */
public final class InputValidator {

    // Private constructor - this is a static utility class, never instantiated
    private InputValidator() {
    }

    /** Returns true if the string is neither null nor blank. */
    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Valid age check - must simply be a positive whole number. There is
     * NO upper bound: any positive integer (however large) is accepted,
     * only zero/negative values are rejected.
     */
    public static boolean isValidAge(int age) {
        return age > 0;
    }

    /**
     * Validates an INDIAN mobile number: exactly 10 digits, and the
     * FIRST digit must be 6, 7, 8, or 9 - the only valid starting digits
     * for an Indian mobile number (0-5 as a starting digit is invalid,
     * e.g. landline-style or malformed numbers are rejected).
     *
     * Examples: "9876543210" -> valid, "5876543210" -> invalid (starts with 5).
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("[6-9]\\d{9}");
    }

    /** Basic positive-number check, used for fees/charges. */
    public static boolean isNonNegative(double value) {
        return value >= 0;
    }

    /**
     * Returns true only if the given string represents a valid positive
     * whole number (e.g. "101", "2045"). Used to enforce that IDs such
     * as Patient ID must be numeric, not free-form text like "P1a".
     */
    public static boolean isNumericId(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            int id = Integer.parseInt(value.trim());
            return id > 0;   // IDs must be positive integers
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true only if the string contains letters and spaces ONLY
     * (no digits, no symbols). Used for name/specialization type fields,
     * where typing a number should be flagged as invalid.
     */
    public static boolean isAlpha(String value) {
        return value != null && !value.trim().isEmpty() && value.trim().matches("[a-zA-Z ]+");
    }

    /**
     * The standard medical blood groups, held in a plain Java array
     * (not a List/ArrayList) - this is a deliberate demonstration of the
     * Arrays data structure required for this project. Includes the 8
     * main ABO/Rh groups, the A1 subtype (A1+, A1-), and the rare
     * Bombay blood group (hh phenotype - has no +/- Rh notation).
     *
     * IMPORTANT: every entry here MUST be in UPPERCASE, because
     * isValidBloodGroup() below converts whatever the user types to
     * uppercase before comparing (String.equals() is case-sensitive -
     * "Bombay" and "BOMBAY" are NOT equal to Java).
     */
    private static final String[] VALID_BLOOD_GROUPS = {
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "A1+", "A1-", "BOMBAY"
    };

    /**
     * Validates blood group by running a manual LINEAR SEARCH over the
     * VALID_BLOOD_GROUPS array (case-insensitive) - demonstrates both
     * the Arrays data structure and a Searching algorithm together.
     */
    public static boolean isValidBloodGroup(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String normalized = value.trim().toUpperCase();

        // Manual linear search through the array - no shortcuts (no .contains())
        for (int i = 0; i < VALID_BLOOD_GROUPS.length; i++) {
            if (VALID_BLOOD_GROUPS[i].equals(normalized)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates Gender against exactly three accepted values: Male,
     * Female, or Transgender (case-insensitive - "male", "MALE", "Male"
     * all pass).
     */
    public static boolean isValidGender(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String normalized = value.trim().toUpperCase();
        return normalized.equals("MALE") || normalized.equals("FEMALE") || normalized.equals("TRANSGENDER");
    }

    /**
     * Minimum realistic age for a practicing doctor. A person needs
     * roughly this many years just to complete schooling + a medical
     * degree before gaining any professional experience at all.
     */
    public static final int MIN_DOCTOR_AGE = 21;

    /** A doctor must be at least MIN_DOCTOR_AGE years old (no upper bound). */
    public static boolean isValidDoctorAge(int age) {
        return age >= MIN_DOCTOR_AGE;
    }

    /**
     * Cross-field check: a doctor's years of experience cannot exceed
     * what is actually possible given their age. Someone cannot have
     * been practicing before roughly MIN_DOCTOR_AGE - so the maximum
     * possible experience is (age - MIN_DOCTOR_AGE). This is exactly
     * the rule that catches an impossible case like "age 19, experience
     * 75 years".
     */
    public static boolean isValidExperience(int age, int experience) {
        if (experience < 0) {
            return false;
        }
        int maxPossibleExperience = age - MIN_DOCTOR_AGE;
        return experience <= maxPossibleExperience;
    }

    /**
     * Strict dd-MM-uuuu parser - STRICT mode rejects impossible dates like
     * "31-02-2026". NOTE: 'uuuu' (year) must be used instead of 'yyyy'
     * (year-of-era) here - in STRICT mode, 'yyyy' without an era marker
     * cannot be resolved, so EVERY date (even valid ones) would fail.
     */
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu", Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT);

    /**
     * Validates a date in dd-mm-yyyy format AND checks it is a real,
     * existing calendar date (day/month/year combination actually
     * exists - e.g. "31-02-2026" is rejected because February never
     * has 31 days). Does NOT check past/future here - see isNotPastDate().
     */
    public static boolean isValidDate(String value) {
        if (value == null) {
            return false;
        }
        try {
            LocalDate.parse(value.trim(), DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true only if the given dd-mm-yyyy date is TODAY or a
     * FUTURE date. An appointment dated in the past is never allowed -
     * this is what prevents someone from booking, say, "01-01-2020".
     * Assumes isValidDate(value) has already passed (format is correct).
     */
    public static boolean isNotPastDate(String value) {
        try {
            LocalDate date = LocalDate.parse(value.trim(), DATE_FORMAT);
            return !date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** Parser for "h:mm a" (e.g. "10:30 AM") - used for both format AND hour-range checks. */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    /**
     * Normalizes a typed time string ("10:30AM", "10:30 am", "10:30 AM")
     * into the single spaced-and-uppercased form ("10:30 AM") that
     * TIME_FORMAT expects, so minor spacing differences from the user
     * don't cause a parse failure.
     */
    private static String normalizeTime(String value) {
        String v = value.trim().toUpperCase();
        if (v.endsWith("AM") || v.endsWith("PM")) {
            String marker = v.substring(v.length() - 2);
            String clock = v.substring(0, v.length() - 2).trim();
            return clock + " " + marker;
        }
        return v;
    }

    /**
     * Validates a time in "hh:mm AM/PM" format (e.g. "10:30 AM", "9:05 pm").
     * Hour: 1-12, Minute: 00-59, followed by AM or PM (case-insensitive).
     */
    public static boolean isValidTime(String value) {
        if (value == null) {
            return false;
        }
        try {
            LocalTime.parse(normalizeTime(value), TIME_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Regular OPD consultation hours: 9:00 AM to 6:00 PM. Only REGULAR
     * (non-emergency) appointments are restricted to this window - an
     * Emergency Patient is added straight to the PriorityQueue with no
     * date/time booking at all, so this rule naturally never applies
     * to them (an emergency can be reported/treated any time).
     */
    public static final LocalTime CONSULTATION_START_TIME = LocalTime.of(9, 0);
    public static final LocalTime CONSULTATION_END_TIME = LocalTime.of(18, 0);

    /**
     * Returns true only if the given time falls within consultation
     * hours (9:00 AM - 6:00 PM inclusive). Assumes isValidTime(value)
     * has already passed (format is correct).
     */
    public static boolean isWithinConsultationHours(String value) {
        try {
            LocalTime time = LocalTime.parse(normalizeTime(value), TIME_FORMAT);
            return !time.isBefore(CONSULTATION_START_TIME) && !time.isAfter(CONSULTATION_END_TIME);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
