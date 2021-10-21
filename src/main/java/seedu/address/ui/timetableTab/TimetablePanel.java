package seedu.address.ui.timetableTab;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.ClassTiming;
import seedu.address.model.person.Student;
import seedu.address.ui.UiPart;

/**
 * A UI for the Timetable Panel Tab.
 */
public class TimetablePanel extends UiPart<Region> {
    private static final String FXML = "timetableTab/TimetablePanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TimetablePanel.class);

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane timetable;

    /**
     * Creates a {@code TimetablePanel} with the given {@code ObservableList}.
     */
    public TimetablePanel(ObservableList<Student> studentList) {
        super(FXML);
        build(studentList);
        timetable.maxWidth(1539);
        if (studentList != null) {
            studentList.addListener(new ListChangeListener<Student>() {
                @Override
                public void onChanged(Change<? extends Student> change) {
                    while (change.next()) {
                        build(studentList);
                    }
                }
            });
        }
    }

    /**
     * Builds the UI of the timetable panel.
     *
     * @param studentList List of students to retrieve the tuition class timings from for the timetable.
     */
    public void build(ObservableList<Student> studentList) {
        clearAll();
        if (studentList == null || studentList.isEmpty()) {
            Label label = new Label("You have no classes.");
            timetable.add(label, 0, 0);
        } else {
            buildHeader(studentList);
            buildDays();
            buildClasses(studentList);
        }
    }

    /**
     * Builds the header panel for the timetable panel ui.
     *
     * @param studentList List of students to retrieve the earliest tuition class start timing and latest end timing.
     */
    public void buildHeader(ObservableList<Student> studentList) {
        assert studentList != null;

        LocalTime earliestHour = getEarliestHour(studentList);
        LocalTime latestHour = getLatestHour(studentList);

        timetable.add(new TimetableHeader("Time Slots", 50).getRoot(), 0, 0, 50, 1);

        int columnIndex = 50;

        while (earliestHour.isBefore(latestHour)) {
            if (earliestHour.equals(LocalTime.parse("23:30", DateTimeFormatter.ofPattern("HH:mm")))) {
                timetable.add(new TimetableHeader(earliestHour, earliestHour.plusMinutes(29)).getRoot(),
                        columnIndex, 0, 15, 1);
                break;
            } else {
                timetable.add(new TimetableHeader(earliestHour, earliestHour.plusMinutes(30)).getRoot(),
                        columnIndex, 0, 15, 1);
            }
            columnIndex += 30;

            earliestHour = earliestHour.plusMinutes(30);
        }

    }

    /**
     * Builds the tuition class slot UI for the timetable.
     *
     * @param studentList List of students to retrieve the tuition class timings.
     */
    public void buildClasses(ObservableList<Student> studentList) {
        assert studentList != null;

        LocalTime earliestHour = getEarliestHour(studentList);

        //earliest time is after the date
        ArrayList<Student> sortedList = new ArrayList<Student>(studentList);
        sortedList.sort((student1, student2) -> student1.getClassTiming().compareTo(student2.getClassTiming()));
        List<ClassTiming> sortedClassTimings = sortedList
                .stream()
                .map(Student::getClassTiming)
                .collect(Collectors.toList());
        ArrayList<ClassTiming> uniqueClassTiming = removeDuplicateClassTimings(sortedClassTimings);

        // build the timetable
        LocalTime previousTime = earliestHour;
        for (int i = 0; i < uniqueClassTiming.size(); i++) {
            ClassTiming currentClassTiming = uniqueClassTiming.get(i);
            if (currentClassTiming.isAfter(previousTime)) {
                addEmptySlot(previousTime, currentClassTiming.getStartTime(),
                        currentClassTiming.getDayToInt(),
                        getColumnIndex(earliestHour, previousTime));
            }

            addTuitionClassSlot(currentClassTiming, earliestHour);

            if (i != uniqueClassTiming.size() - 1
                    && currentClassTiming.getDayToInt() < uniqueClassTiming.get(i + 1).getDayToInt()) {
                previousTime = earliestHour;
            } else {
                previousTime = currentClassTiming.getEndTime();
            }
        }
    }

    /**
     * Creates a tuition class slot to be displayed on the timetable.
     *
     * @param classTiming ClassTiming of tuition class slot.
     * @param earliestTime Earliest class start time of the timetable.
     */
    private void addTuitionClassSlot(ClassTiming classTiming, LocalTime earliestTime) {
        assert !classTiming.getStartTime().isBefore(earliestTime);

        TimetableTuitionClassSlot timetableTuitionClassSlot =
                new TimetableTuitionClassSlot("test", classTiming);
        int duration = getTimeDifference(classTiming.getStartTime(), classTiming.getEndTime());
        int columnIndex = getColumnIndex(earliestTime, classTiming.getStartTime());
        timetable.add(timetableTuitionClassSlot.getRoot(), columnIndex, classTiming.getDayToInt(),
                duration, 1);
    }

    /**
     * Gets the column index which the tuition class timing starts from on the timetable.
     *
     * @param earliestTime Earliest class start time of the timetable.
     * @param timeToIndex Time which we want to find the column index of.
     * @return Column index of the timeToIndex.
     */
    private int getColumnIndex(LocalTime earliestTime, LocalTime timeToIndex) {
        return TimetableDay.getWidth() + getTimeDifference(earliestTime, timeToIndex);
    }

    /**
     * Creates an empty slot to be displayed on the timetable.
     *
     * @param startTime Start Time of the empty slot.
     * @param endTime End Time of the empty slot.
     * @param row Row to add the empty slot to.
     * @param column Column to add the empty slot to.
     */
    private void addEmptySlot(LocalTime startTime, LocalTime endTime, int row, int column) {
        assert startTime.isBefore(endTime);

        TimetableEmptySlot emptySlot = new TimetableEmptySlot(startTime, endTime);
        int duration = getTimeDifference(startTime, endTime);
        timetable.add(emptySlot.getRoot(), column , row, duration, 1);
    }

    /**
     * Gets the time difference from two timings.
     *
     * @param startTime Start time of the two timings.
     * @param endTime End time of the two timings.
     * @return Time difference between the start time and the end time.
     */
    public int getTimeDifference(LocalTime startTime, LocalTime endTime) {
        assert startTime.isBefore(endTime) || startTime.equals(endTime);

        return (int) startTime.until(endTime, ChronoUnit.MINUTES);
    }

    /**
     * Removes duplicate class timings from a list of class timings.
     *
     * @param classTimings List of class timings which duplicates are to be removed from.
     * @return ArrayList of classTimings which have duplicates removed.
     */
    public ArrayList<ClassTiming> removeDuplicateClassTimings(List<ClassTiming> classTimings) {
        assert classTimings != null;

        ArrayList<ClassTiming> uniqueClassTimings = new ArrayList<>();

        for (int i = 0; i < classTimings.size(); i++) {
            ClassTiming currentClassTiming = classTimings.get(i);
            if (uniqueClassTimings.contains(currentClassTiming)) {
                continue;
            }
            uniqueClassTimings.add(currentClassTiming);
        }

        return uniqueClassTimings;
    }

    /**
     * Gets the earliest hour of the earliest class timing from the list of students.
     *
     * @param studentList List of students to get the earliest hour from.
     * @return Earliest hour of the earliest class timing.
     */
    public LocalTime getEarliestHour(ObservableList<Student> studentList) {
        assert studentList != null && studentList.size() > 0;

        LocalTime earliestTime = getEarliestTime(studentList);
        String earliestTimeStr = earliestTime.toString();
        String earliestHourWithoutMinutes = earliestTimeStr.split(":")[0];
        String earliestHourStr = earliestHourWithoutMinutes + ":00";
        return LocalTime.parse(earliestHourStr, DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Gets the earliest class timing of the list of students.
     *
     * @param studentList List of students to get the earliest class timing from.
     * @return Earliest class timing from studentList.
     */
    public LocalTime getEarliestTime(ObservableList<Student> studentList) {
        assert studentList != null && studentList.size() > 0;

        return studentList.stream()
                .map(student -> student.getClassTiming().getStartTime())
                .reduce((classTime1, classTime2) -> classTime1.isBefore(classTime2) ? classTime1 : classTime2)
                .get();
    }

    /**
     * Gets the latest hour which all classes has ended.
     *
     * @param studentList List of students to get the latest hour from.
     * @return Latest hour which all the classes has ended.
     */
    public LocalTime getLatestHour(ObservableList<Student> studentList) {
        assert studentList != null && studentList.size() > 0;

        LocalTime latestTime = getLatestTime(studentList);
        String latestTimeStr = latestTime.toString();
        String latestHourMinutes = latestTimeStr.split(":")[1];
        if (latestHourMinutes.equals("00")) {
            return latestTime;
        } else {
            String latestHourWithoutMinutes = latestTimeStr.split(":")[0];
            String latestHourStr = latestHourWithoutMinutes + ":00";
            LocalTime latestHour = LocalTime.parse(latestHourStr, DateTimeFormatter.ofPattern("HH:mm"));
            return latestHour.equals(LocalTime.parse("23:00", DateTimeFormatter.ofPattern("HH:mm")))
                    ? latestHour.plusMinutes(59)
                    : latestHour.plusHours(1);
        }
    }

    /**
     * Gets the latest class end time from the list of students.
     *
     * @param studentList List of students to get the latest class end time from.
     * @return Latest class end time.
     */
    public LocalTime getLatestTime(ObservableList<Student> studentList) {
        assert studentList != null && studentList.size() > 0;

        return studentList.stream()
                .map(student -> student.getClassTiming().getEndTime())
                .reduce((classTime1, classTime2) -> classTime1.isAfter(classTime2) ? classTime1 : classTime2)
                .get();
    }

    /**
     * Builds the days side panel of the timetable panel ui.
     */
    public void buildDays() {
        timetable.add(new TimetableDay("Mon").getRoot(), 0, 1, 50, 1);
        timetable.add(new TimetableDay("Tue").getRoot(), 0, 2, 50, 1);
        timetable.add(new TimetableDay("Wed").getRoot(), 0, 3, 50, 1);
        timetable.add(new TimetableDay("Thu").getRoot(), 0, 4, 50, 1);
        timetable.add(new TimetableDay("Fri").getRoot(), 0, 5, 50, 1);
        timetable.add(new TimetableDay("Sat").getRoot(), 0, 6, 50, 1);
        timetable.add(new TimetableDay("Sun").getRoot(), 0, 7, 50, 1);
    }

    /**
     * Clears the previously constructed timetable.
     */
    private void clearAll() {
        timetable.getChildren().clear();
        timetable.getRowConstraints().clear();
        timetable.getColumnConstraints().clear();
    }
}
