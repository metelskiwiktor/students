package pl.wiktor.lessons.ui.calendar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;
import pl.wiktor.lessons.ui.menu.MenuPage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static pl.wiktor.lessons.backend.translator.MonthTranslator.*;

public class CalendarPage extends Application {
    private static final int MAX_DAYS_FOR_CALENDAR_PAGE = 42;

    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    private Month month;
    private int year;
    private GridPane grid;

    public CalendarPage() {
        this.month = LocalDate.now().getMonth();
        this.year = LocalDate.now().getYear();
    }

    public CalendarPage(Month month, int year) {
        this.month = month;
        this.year = year;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
//        grid.getStylesheets().add(getClass().getResource("scene.css").toExternalForm());

        Text sceneTitle = new Text(getFormattedName(month));
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label monday = new Label("Poniedziałek");
        grid.add(monday, 1, 1);

        Label tuesday = new Label("Wtorek");
        grid.add(tuesday, 2, 1);

        Label wednesday = new Label("Środa");
        grid.add(wednesday, 3, 1);

        Label thursday = new Label("Czwartek");
        grid.add(thursday, 4, 1);

        Label friday = new Label("Piątek");
        grid.add(friday, 5, 1);

        Label saturday = new Label("Sobota");
        grid.add(saturday, 6, 1);

        Label sunday = new Label("Niedziela");
        grid.add(sunday, 7, 1);

        int x;
        int y = 2;

        dateBeforeActual();
        for (int i = 1; i <= daysInMonth(month); i++) {

            LocalDate date = LocalDate.of(year, month, i);
            x = date.getDayOfWeek().getValue();
            Button btnLessonPage = new Button(date.toString());
            HBox hbBtnLessonPage = new HBox(10);
            hbBtnLessonPage.getChildren().add(btnLessonPage);
            hbBtnLessonPage.setPrefWidth(120);
            hbBtnLessonPage.setPrefHeight(10);
            grid.add(hbBtnLessonPage, x, y);
            btnLessonPage.setOnAction(event -> new ViewLessonPage(date, btnLessonPage).start(new Stage()));

            if (nextDayOfWeek(date) == DayOfWeek.MONDAY) {
                y++;
            }

            if (!lessonFacade.getLessons(date).isEmpty()) {
                final String CONTAINS_LESSON_BUTTON_STYLE= ";-fx-background-color: #5256ff";
                final String CONTAINS_LESSON_BUTTON_STYLE_HOVERED = ";-fx-background-color: indigo";

                btnLessonPage.setStyle(CONTAINS_LESSON_BUTTON_STYLE);
                btnLessonPage.setOnMouseEntered(e -> btnLessonPage.setStyle(CONTAINS_LESSON_BUTTON_STYLE_HOVERED));
                btnLessonPage.setOnMouseExited(e -> btnLessonPage.setStyle(CONTAINS_LESSON_BUTTON_STYLE));
            }

            if (isToday(date)) {
                String value = btnLessonPage.getStyle();
                btnLessonPage.setStyle(value + "; -fx-border-color: darkmagenta");
                btnLessonPage.setDefaultButton(true);
            }
        }
        dateAfterActual();

        Button btnPreviousMonth = new Button(getFormattedName(previousMonth()));
        HBox hbBtnPreviousMonth = new HBox(10);
        hbBtnPreviousMonth.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnPreviousMonth.getChildren().add(btnPreviousMonth);
        btnPreviousMonth.setPrefWidth(100);
        grid.add(hbBtnPreviousMonth, 6, 10);

        btnPreviousMonth.setOnAction(event -> {
            int year = this.year;
            if (previousMonth() == Month.DECEMBER) {
                year = previousYear();
            }
            new CalendarPage(previousMonth(), year).start(primaryStage);
        });

        Button btnNextMonth = new Button(getFormattedName(nextMonth()));
        HBox hbBtnNextMonth = new HBox(10);
        hbBtnNextMonth.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnNextMonth.getChildren().add(btnNextMonth);
        btnNextMonth.setPrefWidth(100);
        grid.add(hbBtnNextMonth, 7, 10);

        btnNextMonth.setOnAction(event -> {
            int year = this.year;
            if (nextMonth() == Month.JANUARY) {
                year = nextYear();
            }
            new CalendarPage(nextMonth(), year).start(primaryStage);
        });

        Button btnBack = new Button("Powrót do menu");
        HBox hbBtnBack = new HBox(10);
        hbBtnBack.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnBack.getChildren().add(btnBack);
        grid.add(btnBack, 7, 11);

        btnBack.setOnAction(event -> {
            new MenuPage().start(primaryStage);
        });

        Button btnBackToActualMonth = new Button("" + getFormattedName(LocalDate.now().getMonth()));
        HBox hbBtnBackToActualMonth = new HBox(10);
        hbBtnBackToActualMonth.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnBackToActualMonth.getChildren().add(btnBackToActualMonth);
        hbBtnBackToActualMonth.setPrefWidth(100);

        btnBackToActualMonth.setOnAction(event -> {
            new CalendarPage().start(primaryStage);
        });

        if (LocalDate.now().getMonth() != month || LocalDate.now().getYear() != year) {
            grid.add(btnBackToActualMonth, 1, 10);
        }

        Scene scene = new Scene(grid);
        primaryStage.setTitle(year + " - " + getFormattedName(month));
        primaryStage.setScene(scene);
        primaryStage.show();
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

    private DayOfWeek nextDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().plus(1);
    }

    private boolean isToday(LocalDate date) {
        return date.equals(LocalDate.now());
    }

    private void dateBeforeActual() {

        int x = dayOfWeekOfFirstDayInMonth().getValue();
        if (x == 1) {
            return;
        }

        for (int i = 1; i < x; i++) {
            LocalDate date = LocalDate.of(year, previousMonth(), daysInMonth(previousMonth()) - x + i + 1);
            Button btnLessonPage = new Button(date.toString());
            HBox hbBtnLessonPage = new HBox(10);
            hbBtnLessonPage.getChildren().add(btnLessonPage);
            hbBtnLessonPage.setPrefWidth(120);
            hbBtnLessonPage.setPrefHeight(10);
            grid.add(hbBtnLessonPage, i, 2);
            btnLessonPage.setOnAction(event -> new ViewLessonPage(date, btnLessonPage).start(new Stage()));

            btnLessonPage.setStyle("-fx-opacity: 0.4");

            if (!lessonFacade.getLessons(date).isEmpty()) {
                String value = btnLessonPage.getStyle();
                btnLessonPage.setStyle(value + "; -fx-background-color: #5256ff");
            }
        }
    }

    private void dateAfterActual() {
        if (daysInMonth(month) == MAX_DAYS_FOR_CALENDAR_PAGE) return;
        int year = nextMonth() == Month.JANUARY ? nextYear() : this.year;
        int days = (daysInMonth(month) + daysPreviousMonthBeforeFirstDay());
        int y = days / 7 == 4 ? 6 : 7;
        for (int i = 1; i <= MAX_DAYS_FOR_CALENDAR_PAGE - daysInMonth(month) - daysPreviousMonthBeforeFirstDay(); i++) {
            LocalDate date = LocalDate.of(year, nextMonth(), i);

            Button btnLessonPage = new Button(date.toString());
            HBox hbBtnLessonPage = new HBox(10);
            hbBtnLessonPage.getChildren().add(btnLessonPage);
            hbBtnLessonPage.setPrefWidth(120);
            hbBtnLessonPage.setPrefHeight(10);
            grid.add(hbBtnLessonPage, date.getDayOfWeek().getValue(), y);
            btnLessonPage.setOnAction(event -> new ViewLessonPage(date, btnLessonPage).start(new Stage()));

            btnLessonPage.setStyle("-fx-opacity: 0.4");

            if (nextDayOfWeek(date) == DayOfWeek.MONDAY) {
                y++;
            }

            if (!lessonFacade.getLessons(date).isEmpty()) {
                String value = btnLessonPage.getStyle();
                btnLessonPage.setStyle(value + "; -fx-background-color: #5256ff");
            }
        }
    }

    private DayOfWeek dayOfWeekOfFirstDayInMonth() {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.getDayOfWeek();
    }

    private DayOfWeek dayOfWeekOfLastDayInMonth() {
        LocalDate date = LocalDate.of(year, month, daysInMonth(month));
        return date.getDayOfWeek();
    }

    private int daysInMonth(Month month) {
        return LocalDate.of(year, month, 1).isLeapYear() ? month.maxLength() : month.minLength();
    }

    private int daysPreviousMonthBeforeFirstDay() {
        return dayOfWeekOfFirstDayInMonth().getValue() - 1;
    }

}