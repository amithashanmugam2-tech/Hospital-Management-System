/**
 * EmergencyLevel.java
 * ---------------------------------------------------------------------
 * Enum representing the urgency level of an emergency patient.
 * Declaration order (CRITICAL -> LOW) matches the natural ordinal
 * ordering used by EmergencyPatient's Comparable implementation, so
 * that a min-heap PriorityQueue automatically serves CRITICAL first.
 * ---------------------------------------------------------------------
 */
public enum EmergencyLevel {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}
