package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final Character DOLLAR_SIGN = '$';
    private static final String PER_HOUR = "/hr";
    private static final String CLASS_TIMING_FIELD = "Class Timing: ";
    private static final String LOCATION_FIELD = "Location: ";
    private static final String ADDRESS_FIELD = "Address: ";
    private static final String MOBILE_FIELD = "Mobile: ";
    private static final String EMAIL_FIELD = "Email: ";
    private static final String RATE_FIELD = "Rate: ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;

    // Student
    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label rate;
    @FXML
    private Label classTiming;
    @FXML
    private Label classLocation;
    @FXML
    private FlowPane tags;

    // Nok
    @FXML
    private Label nokName;
    @FXML
    private Label nokPhone;
    @FXML
    private Label nokAddress;
    @FXML
    private Label nokEmail;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;

        // Student
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(MOBILE_FIELD + student.getPhone().value);
        address.setText(ADDRESS_FIELD + student.getAddress().value);
        email.setText(EMAIL_FIELD + student.getEmail().value);
        // TODO: Class timing and location is commented out for now because they are no longer coupled with the student.
        //        rate.setText(RATE_FIELD + DOLLAR_SIGN + student.getRate().value + PER_HOUR);
        //        classTiming.setText(CLASS_TIMING_FIELD + student.getClassTiming().value);
        //        classLocation.setText(LOCATION_FIELD + student.getLocation().value);

        // Nok
        if (student.getNok() != null) {
            nokName.setText(student.getNok().getName().fullName);
            nokPhone.setText(student.getNok().getPhone().value);
            nokAddress.setText(student.getNok().getAddress().value);
            nokEmail.setText(student.getNok().getEmail().value);
        } else {
            nokName.setText("");
            nokPhone.setText("");
            nokAddress.setText("");
            nokEmail.setText("");
        }

        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }
}
