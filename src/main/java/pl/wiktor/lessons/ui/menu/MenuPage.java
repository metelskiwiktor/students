package pl.wiktor.lessons.ui.menu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.wiktor.lessons.ui.panel.StudentPanelPage;
import pl.wiktor.lessons.ui.calendar.CalendarPage;

public class MenuPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.getStylesheets().add(getClass().getResource("scene.css").toExternalForm());
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button btnCalendar = new Button("Kalendarz");
        btnCalendar.setMinWidth(150);
        HBox hbBtnCalendar = new HBox(10);
        hbBtnCalendar.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnCalendar.getChildren().add(btnCalendar);
        grid.add(hbBtnCalendar, 0, 3);

        btnCalendar.setOnAction(event -> new CalendarPage().start(primaryStage));

        Button btnStudentPanel = new Button("Panel studentów");
        btnStudentPanel.setMinWidth(150);
        HBox hbBtnStudentPanel = new HBox(10);
        hbBtnStudentPanel.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnStudentPanel.getChildren().add(btnStudentPanel);
        grid.add(hbBtnStudentPanel, 0, 4);

        btnStudentPanel.setOnAction(event -> new StudentPanelPage().start(primaryStage));

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);

        if(Double.isNaN(primaryStage.getWidth()) || Double.isNaN(primaryStage.getHeight())){
            primaryStage.setWidth(1024);
            primaryStage.setHeight(875);
        }
        primaryStage.show();
    }
}
