package seedu.address.ui.timetableTab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * A Ui to display the Days at the left side of the timetable.
 */
public class TimetableDay extends TimetableRegion {

    private static final int WIDTH = 50;

    private static final String FXML = "timetableTab/TimetableDay.fxml";

    @FXML
    private Label day;

    /**
     * Creates a new Timetable Day.
     *
     * @param dayStr Day to be created.
     */
    public TimetableDay(String dayStr) {
        super(FXML, WIDTH);
        day.setText(dayStr);
    }

    /**
     * Gets the width of the TimetableDay UI.
     *
     * @return Width of the Timetable Day UI.
     */
    public static int getWidth() {
        return WIDTH;
    }
}
