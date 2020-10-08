package outerhaven;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("test");
        StackPane root = new StackPane();
        Scene lul = new Scene(root,1258,720);
        primaryStage.setScene(lul);
        primaryStage.show();
    }
}