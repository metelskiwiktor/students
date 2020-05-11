package pl.wiktor.lessons.ui.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

import static pl.wiktor.lessons.backend.translator.MonthTranslator.getFormattedName;

public class TestCardPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        board.setVgap(10);
        board.setHgap(10);
        board.setPadding(new Insets(25));


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Random random = new Random();

        int x = 0;
        int y = 0;
        for(int i = 0; i < 42; i++){
            if(x == 7 ){
                y++;
                x = 0;
            }
            GridPane cardGridUp = new GridPane();
            cardGridUp.setAlignment(Pos.CENTER);
            Text text = new Text( i + "");

            VBox vBox = new VBox(20);
            vBox.getChildren().add(text);
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefSize(200, 15);

            GridPane cardGripDown = new GridPane();
            cardGripDown.setAlignment(Pos.CENTER);
            cardGripDown.setPrefSize(200, 90);
            cardGripDown.setStyle("-fx-background-color: blueviolet");
            cardGripDown.setPadding(new Insets(10));

            cardGripDown.setHgap(10);
            cardGripDown.setVgap(10);

            Rectangle hr = new Rectangle(200, 6);
            hr.setFill(Color.BLACK);

            //max column = 4 = {0,1,2,3,4}
            cardGridUp.add(vBox, 0,0, 4, 1);
            cardGridUp.add(hr, 0, 1, 4, 1);
            cardGridUp.add(cardGripDown, 0, 2, 4, 1);

            int a = random.nextInt(4);
            int row = 0;
            for(int e = 1; e < a; e++){
                GridPane lesson = new GridPane();
                lesson.setStyle("-fx-background-color: orange");
                lesson.setMinSize(160, 20);
//                lesson.setHgap(1);
//                lesson.setVgap(1);
//                lesson.setPadding(new Insets(25, 25, 25, 25));

                Text studentFirstNameText = new Text("Wiktor");
                Text lessonTimeText = new Text("19:30-20:30");
                Text paymentText = new Text("N-Opł");
                Text takenPlaceText = new Text("N-Odb");
                paymentText.setFill(Color.GREEN);
                takenPlaceText.setFill(Color.GREEN);
                if(!paymentText.getText().equals("Opł")) paymentText.setFill(Color.RED);
                if(!takenPlaceText.getText().equals("Odb")) takenPlaceText.setFill(Color.RED);

                lesson.add(studentFirstNameText, 0, 0);
                lesson.add(lessonTimeText, 1, 0);
                lesson.add(paymentText, 0, 1);
                lesson.add(takenPlaceText, 1, 1);

                cardGripDown.add(lesson, 0, row++);
            }

            cardGridUp.setStyle("-fx-background-color: #5256ff");
            cardGridUp.setOnMouseClicked(event -> System.out.println("Kliknięto"));
            cardGridUp.setOnMouseEntered(event -> cardGridUp.setStyle(cardGridUp.getStyle() + ";-fx-cursor: hand"));
            grid.add(cardGridUp, x, y);
            x++;
        }

        Button previousMonth = new Button("Marzec");
        previousMonth.setMinWidth(125);
        Button nextMonth = new Button("Maj");
        nextMonth.setMinWidth(125);
        Button menu = new Button("Powrót do menu");
        menu.setMinWidth(125);

        board.add(grid, 0, 0, 18, 1);
        board.add(previousMonth, 18, 2);
        board.add(nextMonth, 19, 2);
        board.add(menu, 19, 3);

        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1550);
        primaryStage.setHeight(800);

        primaryStage.setFullScreen(false);
        primaryStage.show();
    }
}
