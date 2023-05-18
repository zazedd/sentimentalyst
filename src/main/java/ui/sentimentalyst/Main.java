package ui.sentimentalyst;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Sentimentalyst");
        stage.getIcons().add(new Image("file:assets/icon.png"));
        Sentiment.initializeSentPipeline();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Sentiment.initializeSentPipeline();
        POS.initializePOSPipeline();
        launch();
    }
}