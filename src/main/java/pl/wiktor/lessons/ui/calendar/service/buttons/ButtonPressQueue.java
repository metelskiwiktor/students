package pl.wiktor.lessons.ui.calendar.service.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import pl.wiktor.lessons.backend.model.Lesson;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;

import java.time.LocalDate;
import java.util.*;

public class ButtonPressQueue {
    public static final String MAKE_TAKEN_PLACE = "Zrealizuj spotkanie";
    public static final String UNMAKE_TAKEN_PLACE = "Odrealizuj spotkanie";
    public static final String MAKE_PAID = "Oznacz jako zapłacone";
    public static final String UNMAKE_PAID = "Odznacz jako zapłacone";

    private Map<String, EventHandler<ActionEvent>> actions = new HashMap<>();
    private Queue<String> buttonNames = new LinkedList<>();
    LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();
    Lesson selectedLesson;
    private LocalDate date;
    private Button button;
    private ButtonCheck buttonCheck;
    private TableView<Lesson> tableView;

    public ButtonPressQueue(LocalDate date, Button button, Lesson selectedLesson, ButtonCheck buttonCheck, TableView<Lesson> tableView) {
        this.date = date;
        this.button = button;
        this.selectedLesson = selectedLesson;
        this.buttonCheck = buttonCheck;
        this.tableView = tableView;
    }

    public static ButtonPressQueue press(Button button, Lesson selectedLesson, TableView<Lesson> tableView, LocalDate date, ButtonCheck buttonCheck){
        ButtonPressQueue buttonPressQueue = new ButtonPressQueue(date, button, selectedLesson, buttonCheck, tableView);

        buttonPressQueue.actions = buttonCheck.putMap(buttonPressQueue);
        buttonPressQueue.setKeyName();
        return buttonPressQueue;
    }

    private void setKeyName(){
        buttonNames.addAll(buttonCheck.keyNames(selectedLesson));
    }

    public void refreshButton(Lesson lesson){
        this.selectedLesson = lesson;

        buttonNames.clear();
        setKeyName();
        next();
    }

    private void next(){
        String nameKey = buttonNames.poll();
        button.setText(nameKey);
        button.setOnAction(actions.get(nameKey));

        buttonNames.add(nameKey);
    }

    public Button getButton(){
        next();
        return button;
    }

    void refresh(){
        tableView.getItems().clear();
        tableView.getItems().addAll(lessonFacade.getLessons(date));
        next();
        tableView.getSelectionModel().select(selectedLesson);
    }
}


