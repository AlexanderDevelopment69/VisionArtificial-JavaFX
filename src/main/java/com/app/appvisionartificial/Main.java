package com.app.appvisionartificial;

import com.app.appvisionartificial.Componets.FXResizeHelper;
import com.app.appvisionartificial.Componets.Resize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/app/appvisionartificial/RecognitionMask/RecognitionMaskView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 620);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        // Mover scena arrastrando la scena ----------------------
        scene.setOnMousePressed(pressEvent -> {
            scene.setOnMouseDragged(dragEvent -> {
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });
        // Mover scena arrastrando la scena ----------------------
    }


    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }



    public static void main(String[] args) {
        launch();
    }
}