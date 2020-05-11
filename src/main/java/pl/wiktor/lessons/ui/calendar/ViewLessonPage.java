package pl.wiktor.lessons.ui.calendar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;
import pl.wiktor.lessons.ui.panel.SpecificLessonsListPage;
import pl.wiktor.lessons.ui.calendar.service.buttons.ButtonCheck;
import pl.wiktor.lessons.ui.calendar.service.buttons.ButtonPressQueue;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class ViewLessonPage extends Application {
    private LocalDate date;
    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    private Lesson selectedLesson = null;

    private Button btnTakePlace = new Button();
    private Button btnPaid = new Button();
    private ButtonPressQueue buttonPressQueuePaid;
    private ButtonPressQueue buttonPressQueueTakePlace;

    private HBox hbBtnMakeTakePlace = new HBox(10);
    private HBox hbBtnPaid = new HBox(10);

    private Button btnRemoveLesson = new Button("Usuń lekcję");
    private HBox hbBtnRemoveLesson = new HBox(10);

    private Button btnLessonPage;
    private SpecificLessonsListPage specificLessonsListPage;

    public ViewLessonPage(LocalDate date, Button btnLessonPage) {
        this.date = date;
        this.btnLessonPage = btnLessonPage;
    }

    public ViewLessonPage(LocalDate date, SpecificLessonsListPage specificLessonsListPage) {
        this.date = date;
        this.specificLessonsListPage = specificLessonsListPage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TableView<Lesson> tableView = new TableView<>(observableList());

        TableColumn<Lesson, Lesson> column1 = new TableColumn<>("Uczeń");
        column1.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<Lesson, Lesson> column2 = new TableColumn<>("Od");
        column2.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));

        TableColumn<Lesson, Lesson> column3 = new TableColumn<>("Do");
        column3.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));

        TableColumn<Lesson, Lesson> column4 = new TableColumn<>("Kwota");
        column4.setCellValueFactory(new PropertyValueFactory<>("formattedTotalPrice"));

        TableColumn<Lesson, Lesson> column5 = new TableColumn<>("Lekcja\nzrealizowana");
        column5.setCellValueFactory(new PropertyValueFactory<>("formattedTakenPlace"));

        TableColumn<Lesson, Lesson> column6 = new TableColumn<>("Opłacono\nlekcje");
        column6.setCellValueFactory(new PropertyValueFactory<>("formattedPaid"));

        tableView.getColumns().setAll(Arrays.asList(column1,column2,column3, column4, column5, column6));

        initialize(tableView);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                selectedLesson = newValue;
                buttonPressQueueTakePlace.refreshButton(selectedLesson);
                buttonPressQueuePaid.refreshButton(selectedLesson);
            }
        });
        grid.add(tableView, 0, 1, 10,1);

        hbBtnMakeTakePlace.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnMakeTakePlace.setPrefWidth(200);
        grid.add(hbBtnMakeTakePlace, 0, 3, 3, 1);

        hbBtnPaid.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnPaid.setPrefWidth(200);
        grid.add(hbBtnPaid, 4, 3, 3, 1);

        if(!Objects.isNull(buttonPressQueueTakePlace) && !Objects.isNull(buttonPressQueuePaid)){
            btnTakePlace = buttonPressQueueTakePlace.getButton();
            btnPaid = buttonPressQueuePaid.getButton();
        }

        Button btnAddLesson = new Button("Dodaj lekcję");
        HBox hbBtnAddLesson = new HBox(10);
        hbBtnAddLesson.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnAddLesson.getChildren().add(btnAddLesson);
        hbBtnAddLesson.setPrefWidth(200);

        btnAddLesson.setOnAction(event -> {
            try {
                new AddLessonPage(date, tableView, this).start(new Stage());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        grid.add(btnAddLesson, 8, 3, 3, 1);

        hbBtnRemoveLesson.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnRemoveLesson.setPrefWidth(200);
        grid.add(hbBtnRemoveLesson, 8, 5, 3, 1);

        btnRemoveLesson.setOnAction(event -> {
            if(!Objects.isNull(selectedLesson)){
                lessonFacade.deleteLesson(selectedLesson);
                tableView.getItems().remove(selectedLesson);
                initialize(tableView);
            }
        });

        tableView.setMinWidth(550);
        Scene scene = new Scene(grid, 824, 625);
        primaryStage.setTitle(date.toString());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private ObservableList<Lesson> observableList(){
        ObservableList<Lesson> lessons = FXCollections.observableArrayList(lessonFacade.getLessons(date));

        if(!Objects.isNull(btnLessonPage)){
            lessons.addListener(refreshButtonLessonPageFromCalendar());
        }

        if(!Objects.isNull(specificLessonsListPage)){
            lessons.addListener(refreshSpecificLessonListPage());
        }

        return lessons;
    }

    private ListChangeListener<Lesson> refreshSpecificLessonListPage(){
        return pChange -> {
            while (pChange.next()) {
                specificLessonsListPage.refresh();
            }
        };
    }

    private ListChangeListener<Lesson> refreshButtonLessonPageFromCalendar(){
        return pChange -> {
            while (pChange.next()) {
                if (!lessonFacade.getLessons(date).isEmpty()) {
                    btnLessonPage.setStyle("-fx-background-color: #5256ff");
                } else {
                    btnLessonPage.setStyle(new Button().getStyle());
                }
            }
        };
    }

    void initialize(TableView<Lesson> tableView){
        hbBtnMakeTakePlace.getChildren().remove(btnTakePlace);
        hbBtnPaid.getChildren().remove(btnPaid);
        hbBtnRemoveLesson.getChildren().remove(btnRemoveLesson);
        if(!tableView.getItems().isEmpty()){
            this.selectedLesson = tableView.getItems().get(0);
            this.buttonPressQueueTakePlace = ButtonPressQueue.press(btnTakePlace, selectedLesson, tableView, date, ButtonCheck.TAKE_PLACE);
            this.buttonPressQueuePaid = ButtonPressQueue.press(btnPaid, selectedLesson, tableView, date, ButtonCheck.PAID);
            tableView.getSelectionModel().selectFirst();

            hbBtnMakeTakePlace.getChildren().add(btnTakePlace);
            hbBtnPaid.getChildren().add(btnPaid);
            hbBtnRemoveLesson.getChildren().add(btnRemoveLesson);
        }
    }
}
