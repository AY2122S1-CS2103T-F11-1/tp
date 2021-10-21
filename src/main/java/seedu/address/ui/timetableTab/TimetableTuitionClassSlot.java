package seedu.address.ui.timetableTab;

import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seedu.address.model.person.ClassTiming;

/**
 * Ui for the timetable tuition class slot.
 */
public class TimetableTuitionClassSlot extends TimetableRegion {

    private static final String FXML = "timetableTab/TimetableTuitionClassSlot.fxml";

    @FXML
    private Label time;

    @FXML
    private Label className;

    /**
     * Creates a timetable tuition class slot on the timetable ui.
     *
     * @param tuitionClassName Tuition class name to be displayed on the slot.
     * @param classTiming ClassTiming to be displayed on the slot.
     */
    public TimetableTuitionClassSlot(String tuitionClassName, ClassTiming classTiming) {
        super(FXML, classTiming.getStartTime().until(classTiming.getEndTime(), ChronoUnit.MINUTES));
        className.setText(tuitionClassName);
        time.setText(classTiming.getClassTiming());
    }
}
