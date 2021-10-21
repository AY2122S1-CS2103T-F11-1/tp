package seedu.address.ui.classTab;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Student;
import seedu.address.ui.StudentCard;
import seedu.address.ui.UiPart;

import java.util.logging.Logger;

/**
 * A UI for the Class Panel Tab.
 */
public class ClassPanel extends UiPart<Region> {
    private static final String FXML = "timetableTab/TimetablePanel.fxml";
    private final Logger logger = LogsCenter.getLogger(seedu.address.ui.timetableTab.TimetablePanel.class);

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ListView<Student> studentListView;

    @FXML
    private ListView<Student> classListView;


    /**
     * Creates a {@code StudentListPanel} with the given {@code ObservableList}.
     */
    public ClassPanel(ObservableList<Student> studentList) {
        super(FXML);
        studentListView.setItems(studentList);
        classListView.setItems(studentList);
        studentListView.setCellFactory(listView -> new StudentListViewCell());
        classListView.setCellFactory(listView -> new ClassListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Student} using a {@code StudentCard}.
     */
    class StudentListViewCell extends ListCell<Student> {
        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);

            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StudentCard(student, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Student} using a {@code StudentCard}.
     */
    // NEED TO IMPORT STUFF DED
    class ClassListViewCell extends ListCell<TuitionClass> {
        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);

            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StudentCard(student, getIndex() + 1).getRoot());
            }
        }
    }

}