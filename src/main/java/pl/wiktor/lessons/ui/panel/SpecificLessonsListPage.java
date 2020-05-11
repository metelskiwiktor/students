package pl.wiktor.lessons.ui.panel;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;
import pl.wiktor.lessons.ui.calendar.ViewLessonPage;
import pl.wiktor.lessons.ui.panel.service.SpecificListPick;

import java.util.Arrays;

public class SpecificLessonsListPage extends Application {
    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    private Student student;
    private TableView<Lesson> tableView;
    private SpecificListPick specificListPick;
    private StudentPanelPage studentPanelPage;

    public SpecificLessonsListPage(Student student, SpecificListPick specificListPick, StudentPanelPage studentPanelPage) {
        this.student = student;
        this.specificListPick = specificListPick;
        this.studentPanelPage = studentPanelPage;
    }

    public SpecificLessonsListPage() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SpecificLessonsListPage specificLessonsListPage = this;

        String studentName = student == null ? "" : student.getName();

        primaryStage.setTitle(String.join(" ", specificListPick.getDescription(), studentName));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        tableView = new TableView<>();

        TableColumn<Lesson, Lesson> column1 = new TableColumn<>("Uczeń");
        column1.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<Lesson, Lesson> column2 = new TableColumn<>("Czas");
        column2.setCellValueFactory(new PropertyValueFactory<>("formattedDuration"));

        TableColumn<Lesson, Lesson> column3 = new TableColumn<>("Cena");
        column3.setCellValueFactory(new PropertyValueFactory<>("formattedTotalPrice"));

        TableColumn<Lesson, Lesson> column4 = new TableColumn<>("Kalendarz");
        column4.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        column4.setCellFactory(param -> new TableCell<Lesson, Lesson>() {
            private final Button calendarButton = new Button();

            @Override
            protected void updateItem(Lesson lesson, boolean empty) {
                super.updateItem(lesson, empty);

                if (lesson == null) {
                    setGraphic(null);
                    return;
                }

                calendarButton.setText(lesson.getFormattedDate());

                setGraphic(calendarButton);
                calendarButton.setOnAction(event -> {
                    new ViewLessonPage(lesson.getStartLesson().toLocalDate(), specificLessonsListPage).start(new Stage());
                });
            }
        });

        tableView.getItems().addAll(FXCollections.observableList(specificListPick.pickSpecificList(student, lessonFacade)));
        tableView.getColumns().addAll(Arrays.asList(column1, column2, column3, column4));

        tableView.getItems().addListener((ListChangeListener<Lesson>) change -> {
            while (change.next()) {
                studentPanelPage.refresh();
            }
        });

        tableView.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.75));

        grid.add(tableView, 0, 0, 4, 1);

        Scene scene = new Scene(grid, 600, 755);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void refresh() {
        tableView.getItems().clear();
        tableView.getItems().addAll(FXCollections.observableList(specificListPick.pickSpecificList(student, lessonFacade)));
    }
}
