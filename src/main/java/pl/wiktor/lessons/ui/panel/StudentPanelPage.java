package pl.wiktor.lessons.ui.panel;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.wiktor.lessons.backend.model.info.MonthSummaryInfo;
import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.model.info.TableStudentInfo;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;
import pl.wiktor.lessons.ui.menu.MenuPage;
import pl.wiktor.lessons.ui.panel.service.SpecificListPick;
import pl.wiktor.lessons.ui.panel.service.StudentActionPick;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static pl.wiktor.lessons.backend.translator.MonthTranslator.getFormattedName;

public class StudentPanelPage extends Application {
    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    private Month month;
    private int year;
    private Student pickedStudent = null;
    private TableView<Map.Entry<Student , TableStudentInfo>> tableView;

    private MonthSummaryInfo monthSummaryInfo;
    private Label countTakenPlaceLessonsValue;
    private Label countExpectedLessonsValue;
    private Label earnedTextValue;
    private Label unpaidValue;
    private Label expectedEarnValue;

    private Label studentEarned;

    public StudentPanelPage() {
        this.month = LocalDate.now().getMonth();
        this.year = LocalDate.now().getYear();
    }

    public StudentPanelPage(Month month, int year) {
        this.month = month;
        this.year = year;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StudentPanelPage studentPanelPage = this;
        primaryStage.setTitle("Panel studentów");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label dateLabel = new Label(year + " - " + getFormattedName(month));
        grid.add(dateLabel, 0, 0);

        Map<Student, TableStudentInfo> students = lessonFacade.studentsInfo(LocalDate.of(year, month, 1));

        ObservableList<Map.Entry<Student, TableStudentInfo>> items = FXCollections.observableArrayList(students.entrySet());
        tableView = new TableView<>(items);

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column1 = new TableColumn<>("Student");
        column1.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().getName()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column2 = new TableColumn<>("Cena");
        column2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().getFormattedCostPerHouse()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column3 = new TableColumn<>("Czas\nzrealizowany");
        column3.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getTakenLessonDuration()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column4 = new TableColumn<>("Ilość\nzrealizowanych\nlekcji");
        column4.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getCountTakenLessons()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column5 = new TableColumn<>("Zapłacone");
        column5.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getPaid()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column6 = new TableColumn<>("Nieopłacone");
        column6.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getUnpaid()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column7 = new TableColumn<>("Lekcje\nw kolejce");
        column7.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getLessonPlanned()));

        TableColumn<Map.Entry<Student, TableStudentInfo>, String> column8 = new TableColumn<>("Przewidywany\nłączny\nzarobek");
        column8.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getExpectedEarn()));

        tableView.getColumns().setAll(column1, column2, column3, column4, column5, column6, column7, column8);

        grid.add(tableView, 0, 2, 7, 10);

        Label studentName = new Label("Nazwa ucznia");
        grid.add(studentName, 7, 2, 2, 1);
        studentEarned = new Label("Zarobiono");
        studentEarned.setMinWidth(200);
        grid.add(studentEarned, 7, 3, 2, 1);

        Button buttonUnpaidStudentLessons = new Button("Wyświetl nieopłacone lekcje");

//        button.managedProperty().bind(button.visibleProperty());

        grid.add(buttonUnpaidStudentLessons, 7, 4, 2, 1);

        buttonUnpaidStudentLessons.setOnAction(event -> {
            new SpecificLessonsListPage(pickedStudent, SpecificListPick.UNPAID_STUDENT, studentPanelPage).start(new Stage());
        });

        Button buttonAllStudentsLessons = new Button("Wyświetl wszystkie lekcje");

//        button.managedProperty().bind(button.visibleProperty());

        grid.add(buttonAllStudentsLessons, 7, 5, 2, 1);

        buttonAllStudentsLessons.setOnAction(event -> {
            new SpecificLessonsListPage(pickedStudent, SpecificListPick.ALL_LESSONS_STUDENT, studentPanelPage).start(new Stage());
        });

        Button btnEditStudent = new Button("Zedytuj");

        btnEditStudent.setOnAction(event -> {
            if(pickedStudent == null) return;;
            new StudentAddPage(studentPanelPage, pickedStudent, StudentActionPick.EDIT).start(new Stage());
        });

        grid.add(btnEditStudent, 7, 6, 2, 1);

        tableView.getSelectionModel().selectFirst();
        if(!Objects.isNull(tableView.getItems()) && !Objects.isNull(tableView.getSelectionModel().getSelectedItem())){
            fillStudentInfoIfSelected(studentName, btnEditStudent, tableView.getSelectionModel().getSelectedItem().getKey());
        }

//        tableView.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.75));
//        studentEarned.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.25));
        tableView.setMinWidth(750);
//        tableView.maxWidth(800);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(!Objects.isNull(newSelection))
                fillStudentInfoIfSelected(studentName, btnEditStudent, newSelection.getKey());
        });

        monthSummaryInfo = lessonFacade.monthSummaryInfo(LocalDate.of(year, month, 1));

        Label countTakenPlaceLessonsTitle = new Label("Ilość zrealizowanych lekcji: ");
        countTakenPlaceLessonsValue = new Label(monthSummaryInfo.getCountTakenPlaceLessons());
        grid.add(countTakenPlaceLessonsTitle, 0, 14);
        grid.add(countTakenPlaceLessonsValue, 1, 14);

        Label countExpectedLessonsTitle = new Label("Ilość zarezerwowanych lekcji:");
        countExpectedLessonsValue = new Label(monthSummaryInfo.getCountExpectedLessons());
        grid.add(countExpectedLessonsTitle, 0, 15);
        grid.add(countExpectedLessonsValue, 1, 15);

        Label earnedTitle = new Label("Zarobiona kwota: ");
        earnedTextValue = new Label(monthSummaryInfo.getEarned());
        grid.add(earnedTitle, 0, 16);
        grid.add(earnedTextValue, 1, 16);

        Label unpaidTitle = new Label("Niezapłacona kwota: ");
        unpaidValue = new Label(monthSummaryInfo.getUnpaid());
        grid.add(unpaidTitle, 0, 17);
        grid.add(unpaidValue, 1, 17);

        Label expectedEarnTitle = new Label("Spodziewany zarobek: ");
        expectedEarnValue = new Label(monthSummaryInfo.getExpectedEarn());
        grid.add(expectedEarnTitle, 0, 18);
        grid.add(expectedEarnValue, 1, 18);

        Button btnPreviousMonth = new Button(getFormattedName(previousMonth()));
        HBox hbBtnPreviousMonth = new HBox(10);
        hbBtnPreviousMonth.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnPreviousMonth.getChildren().add(btnPreviousMonth);
        btnPreviousMonth.setPrefWidth(100);
        grid.add(hbBtnPreviousMonth, 6, 19);

        btnPreviousMonth.setOnAction(event -> {
            int year = this.year;
            if (previousMonth() == Month.DECEMBER) {
                year = previousYear();
            }
            new StudentPanelPage(previousMonth(), year).start(primaryStage);
        });

        Button btnNextMonth = new Button(getFormattedName(nextMonth()));
        HBox hbBtnNextMonth = new HBox(10);
        hbBtnNextMonth.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnNextMonth.getChildren().add(btnNextMonth);
        btnNextMonth.setPrefWidth(100);
        grid.add(hbBtnNextMonth, 7, 19);

        btnNextMonth.setOnAction(event -> {
            int year = this.year;
            if (nextMonth() == Month.JANUARY) {
                year = nextYear();
            }
            new StudentPanelPage(nextMonth(), year).start(primaryStage);
        });

        Button btnAddStudent = new Button("Dodaj ucznia");
        HBox hbAddStudent = new HBox(10);
        hbAddStudent.getChildren().add(btnAddStudent);
        hbAddStudent.setAlignment(Pos.BOTTOM_RIGHT);

        btnAddStudent.setOnAction(event -> {
            new StudentAddPage(studentPanelPage, StudentActionPick.ADD).start(new Stage());
        });

        grid.add(hbAddStudent, 7, 20);

        Button btnBack = new Button("Powrót do menu");
        HBox hbBtnBack = new HBox(10);
        hbBtnBack.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnBack.getChildren().add(btnBack);
        grid.add(btnBack, 7, 21);

        btnBack.setOnAction(event -> {
            new MenuPage().start(primaryStage);
        });

        Button btnBackToActualMonth = new Button("" + getFormattedName(LocalDate.now().getMonth()));
        HBox hbBtnBackToActualMonth = new HBox(10);
        hbBtnBackToActualMonth.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnBackToActualMonth.getChildren().add(btnBackToActualMonth);
        hbBtnBackToActualMonth.setPrefWidth(100);

        btnBackToActualMonth.setOnAction(event -> {
            new StudentPanelPage().start(primaryStage);
        });

        if(LocalDate.now().getMonth() != month || LocalDate.now().getYear() != year){
            grid.add(btnBackToActualMonth, 0, 19);
        }

        Scene scene = new Scene(grid, 1024, 875);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fillStudentInfoIfSelected(Label studentName, Button btnEditStudent, Student student) {
        if (student != null) {
            pickedStudent = student;
            studentName.setText(pickedStudent.getName());
            btnEditStudent.setText("Zedytuj " + pickedStudent.getName());
            studentEarned.setText("Zarobiono: " + lessonFacade.earnedStudent(pickedStudent));
        }
    }

    private Month nextMonth() {
        return month.plus(1);
    }

    private Month previousMonth() {
        return month.minus(1);
    }

    private int nextYear() {
        return year + 1;
    }

    private int previousYear() {
        return year - 1;
    }

    void refresh(){
        tableView.getItems().clear();
        tableView.getItems().addAll(lessonFacade.studentsInfo(LocalDate.of(year, month, 1)).entrySet());
        refreshMonthSummaryInfo();
        tableView.getSelectionModel().selectFirst();
    }

    private void refreshMonthSummaryInfo(){
        monthSummaryInfo = lessonFacade.monthSummaryInfo(LocalDate.of(year, month, 1));
        countTakenPlaceLessonsValue.setText(monthSummaryInfo.getCountTakenPlaceLessons());
        countExpectedLessonsValue.setText(monthSummaryInfo.getCountExpectedLessons());
        earnedTextValue.setText(monthSummaryInfo.getEarned());
        unpaidValue.setText(monthSummaryInfo.getUnpaid());
        expectedEarnValue.setText(monthSummaryInfo.getExpectedEarn());

        studentEarned.setText("Zarobiono: \n" + lessonFacade.earnedStudent(pickedStudent));
    }
}
