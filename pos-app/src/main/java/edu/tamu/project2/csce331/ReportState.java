package edu.tamu.project2.csce331;

import java.time.LocalDate;

public final class ReportState {
    private static LocalDate currentDate;

    private ReportState() {}

    public static synchronized void initIfNeeded() {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }
    }

    public static synchronized LocalDate getCurrentDate() {
        initIfNeeded();
        return currentDate;
    }

    public static synchronized void setCurrentDate(LocalDate date) {
        currentDate = date;
    }

    public static synchronized void incrementDay() {
        initIfNeeded();
        currentDate = currentDate.plusDays(1);
    }
}
