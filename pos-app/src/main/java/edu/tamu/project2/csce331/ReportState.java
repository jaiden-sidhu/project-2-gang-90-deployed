package edu.tamu.project2.csce331;

import java.time.LocalDate;

/**
 * Global, in-memory state for report calculations.
 * <p>
 * This utility holds a single logical {@link LocalDate} used by reporting
 * screens (e.g., X and Z reports). It initializes lazily to the current
 * system date and offers simple getters/setters with basic thread safety via
 * {@code synchronized} methods.
 * </p>
 *
 * <h3>Thread-safety</h3>
 * <ul>
 *   <li>All public methods are {@code synchronized}, making access safe across
 *   JavaFX and background threads.</li>
 *   <li>State is process-local only; it is not persisted.</li>
 * </ul>
 *
 * <h3>Usage</h3>
 * <ul>
 *   <li>Call {@link #getCurrentDate()} to retrieve the current logical date.</li>
 *   <li>Use {@link #setCurrentDate(LocalDate)} to override for testing or to
 *   align reports to a specific day.</li>
 *   <li>{@link #incrementDay()} advances the logical date by five days to
 *   facilitate time-skipping during demos/tests.</li>
 * </ul>
 *
 * @author Kevin Chen
 * @version 1.0
 */
public final class ReportState {
    /** The current logical date for reports; lazily initialized. */
    private static LocalDate currentDate;

    private ReportState() {}

    /**
     * Lazily initializes {@link #currentDate} if it hasn't been set.
     * <p>Default value is {@link LocalDate#now()}.</p>
     */
    public static synchronized void initIfNeeded() {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }
    }

    /**
     * Returns the current logical report date, initializing if necessary.
     *
     * @return non-null logical date used by reports
     */
    public static synchronized LocalDate getCurrentDate() {
        initIfNeeded();
        return currentDate;
    }

    /**
     * Sets the current logical report date.
     * <p>
     * Passing {@code null} clears the value; subsequent reads will reinitialize
     * to {@link LocalDate#now()} via {@link #getCurrentDate()}.
     * </p>
     *
     * @param date the new logical date, or {@code null} to clear
     */
    public static synchronized void setCurrentDate(LocalDate date) {
        currentDate = date;
    }

    /**
     * Advances the logical date by five days.
     * <p>Useful for demos/tests to fast-forward reporting windows.</p>
     */
    public static synchronized void incrementDay() {
        initIfNeeded();
        currentDate = currentDate.plusDays(5);
    }
}
