package pl.wiktor.lessons.ui.calendar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DateTimeStringConverter;
import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddLessonPage extends Application {
    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    private LocalDate date;
    private TableView<Lesson> tableView;
    private ViewLessonPage viewLessonPage;

    public AddLessonPage() {
        this.date = LocalDate.now();
    }

    public AddLessonPage(LocalDate date, TableView<Lesson> lessonTableView, ViewLessonPage viewLessonPage) {
        this.date = date;
        this.tableView = lessonTableView;
        this.viewLessonPage = viewLessonPage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ParseException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        primaryStage.setTitle(date.toString());

        Label studentLabel = new Label("Uczeń:");

        grid.add(studentLabel, 0, 0);

        ComboBox<Student> cbxStudent = new ComboBox<>();
        cbxStudent.getItems().setAll(lessonFacade.getStudents());
        cbxStudent.getSelectionModel().selectFirst();

        Callback<ListView<Student>, ListCell<Student>> cellFactory = new Callback<ListView<Student>, ListCell<Student>>() {

            @Override
            public ListCell<Student> call(ListView<Student> l) {
                return new ListCell<Student>() {

                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                } ;
            }
        };

        cbxStudent.setButtonCell(cellFactory.call(null));
        cbxStudent.setCellFactory(cellFactory);

        grid.add(cbxStudent, 1, 0, 4, 1);

        Label timeLabel = new Label("Godzina: ");
        grid.add(timeLabel, 0, 2);

        Label fromLabel = new Label("od: ");
        grid.add(fromLabel, 1, 2);

        TextField fromTextField = new TextField();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        fromTextField.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00")));
        fromTextField.setMaxWidth(70);

        grid.add(fromTextField, 2, 2, 2, 1);

        Label toLabel = new Label("do: ");
        grid.add(toLabel, 4, 2);

        TextField toTextField = new TextField();
        toTextField.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00")));
        toTextField.setMaxWidth(70);

        grid.add(toTextField, 5, 2, 1, 1);

        Button btnSave = new Button("Zapisz lekcję");
        HBox hbBtnSave = new HBox(10);
        hbBtnSave.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnSave.getChildren().add(btnSave);
        hbBtnSave.setPrefWidth(200);

        btnSave.setOnAction(event -> {
            Student student = cbxStudent.getSelectionModel().getSelectedItem();

            int startHours = Integer.parseInt(fromTextField.getText().substring(0, 2));
            int startMinutes = Integer.parseInt(fromTextField.getText().substring(3, 5));

            int endHours = Integer.parseInt(toTextField.getText().substring(0, 2));
            int endMinutes = Integer.parseInt(toTextField.getText().substring(3, 5));

            LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), startHours, startMinutes);
            LocalDateTime end = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), endHours, endMinutes);
            Lesson lesson = new Lesson(student, start, end);
            lessonFacade.addLesson(lesson);
            tableView.getItems().clear();
            tableView.getItems().addAll(lessonFacade.getLessons(date));

            viewLessonPage.initialize(tableView);
            tableView.refresh();
            primaryStage.hide();
        });

        grid.add(hbBtnSave, 5, 4, 1, 1);

        Scene scene = new Scene(grid, 500, 375);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
