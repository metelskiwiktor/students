package pl.wiktor.lessons;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.wiktor.lessons.ui.menu.MenuPage;

@SpringBootApplication
public class Main extends Application {
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(Main.class);
    }

    @Override
    public void start(Stage primaryStage) {
        new MenuPage().start(primaryStage);
    }

    @Override
    public void stop() {
        springContext.stop();
        springContext.close();
    }
}
