package outerhaven;

import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class App  {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("test");
        FlowPane root = new FlowPane();
        Scene test = new Scene(root,1258,750, Color.color(0,0,0));
        primaryStage.setScene(test);
        primaryStage.show();
    }
}