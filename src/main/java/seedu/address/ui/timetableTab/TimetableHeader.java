package seedu.address.ui.timetableTab;

import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * A UI for the header of the timetable.
 */
public class TimetableHeader extends TimetableRegion {

    private static final String FXML = "timetableTab/TimetableHeader.fxml";

    private static final int TIMETABLE_HEADER_SLOT_LENGTH = 60;

    @FXML
    private Label headerTime;

    /**
     * Creates a new Timetable header with a start time and end time.
     *
     * @param headerStartTime Start time of the header.
     * @param headerEndTime End time of the header.
     */
    public TimetableHeader(LocalTime headerStartTime, LocalTime headerEndTime) {
        super(FXML, TIMETABLE_HEADER_SLOT_LENGTH);
        String headerTime = headerStartTime.toString() + "-" + headerEndTime.toString();
        this.headerTime.setText(headerTime);
    }

    /**
     * Creates a new Timetable header with a string as its input and its width.
     *
     * @param headerStr String to be displayed on the header.
     * @param width Width of the header.
     */
    public TimetableHeader(String headerStr, int width) {
        super(FXML, width);
        this.headerTime.setText(headerStr);
    }

}
