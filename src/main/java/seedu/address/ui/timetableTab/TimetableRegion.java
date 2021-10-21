package seedu.address.ui.timetableTab;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.ui.UiPart;

/**
 * A UI for the timetable region.
 */
public class TimetableRegion extends UiPart<Region> {

    @FXML
    private HBox slotPane;

    /**
     * Creates the timetable region UI.
     *
     * @param fxml Specified file path of the fxml to construct the timetable region.
     * @param scale Amount to scale the Ui by.
     */
    public TimetableRegion(String fxml, long scale) {
        super(fxml);
        int scaleFactor = 2;
        slotPane.setPrefWidth(scale * scaleFactor);
        slotPane.setMinWidth(scale * scaleFactor);
    }

}
