package seedu.address.ui.timetableTab;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * A UI for the empty slot with no tuition class in the timetable.
 */
public class TimetableEmptySlot extends TimetableRegion {

    private static final String FXML = "timetableTab/TimetableEmptySlot.fxml";

    /**
     * Builds the empty slot with no tuition class.
     *
     * @param startTime Start Timing of empty slot.
     * @param endTime End Timing of empty slot.
     */
    public TimetableEmptySlot(LocalTime startTime, LocalTime endTime) {
        super(FXML, startTime.until(endTime, ChronoUnit.MINUTES));
    }
}
