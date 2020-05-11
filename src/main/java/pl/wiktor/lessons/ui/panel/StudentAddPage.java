package pl.wiktor.lessons.ui.panel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.wiktor.lessons.backend.service.LessonFacade;
import pl.wiktor.lessons.backend.model.Student;
import pl.wiktor.lessons.backend.service.singleton.LessonFacadeSingleton;
import pl.wiktor.lessons.ui.panel.service.StudentActionPick;

import java.math.BigDecimal;

public class StudentAddPage extends Application {
    private LessonFacade lessonFacade = LessonFacadeSingleton.LESSON_FACADE.getLessonFacade();

    private StudentPanelPage studentPanelPage;
    private Student student;
    private StudentActionPick studentActionPick;

    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField pricePerHourTextField;

    public StudentAddPage(StudentPanelPage studentPanelPage, StudentActionPick studentActionPick) {
        this.studentPanelPage = studentPanelPage;
        this.studentActionPick = studentActionPick;
    }

    public StudentAddPage(StudentPanelPage studentPanelPage, Student pickedStudent, StudentActionPick studentActionPick) {
        this.studentPanelPage = studentPanelPage;
        this.student = pickedStudent;
        this.studentActionPick = studentActionPick;
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

        String studentName = student == null ? "" : student.getName();

        primaryStage.setTitle(String.join(" ", studentActionPick.getDescription(), studentName));

        Label firstNameLabel = new Label("Imię");
        grid.add(firstNameLabel, 0 , 0);

        firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 0);

        Label lastNameLabel = new Label("Nazwisko");
        grid.add(lastNameLabel, 0, 1);

        lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 1);

        Label pricePerHourLabel = new Label("Koszt/h");
        grid.add(pricePerHourLabel, 0, 2);

        pricePerHourTextField = new TextField();
        grid.add(pricePerHourTextField, 1, 2);

        fillTextFieldWhenEdit();

        Button btnAddStudent = new Button(studentActionPick.getDescription());
        HBox hbAddStudent = new HBox(10);
        hbAddStudent.getChildren().add(btnAddStudent);
        hbAddStudent.setAlignment(Pos.BOTTOM_RIGHT);

        btnAddStudent.setOnAction(event -> {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            BigDecimal costPerHouse = new BigDecimal(pricePerHourTextField.getText());

            Student student = new Student(firstName, lastName, costPerHouse);

            if(studentActionPick == StudentActionPick.EDIT) student.setId(this.student.getId());

            studentActionPick.makeAction(lessonFacade, student);

            studentPanelPage.refresh();
            primaryStage.hide();
        });

        grid.add(hbAddStudent, 1, 3);

        Scene scene = new Scene(grid, 785, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fillTextFieldWhenEdit(){
        if (studentActionPick == StudentActionPick.EDIT){
            firstNameTextField.setText(student.getFirstName());
            lastNameTextField.setText(student.getLastName());
            pricePerHourTextField.setText(student.getCostPerHouse().toString());
        }
    }
}
