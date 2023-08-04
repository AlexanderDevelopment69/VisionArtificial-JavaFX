package com.app.appvisionartificial;

import com.app.appvisionartificial.Componets.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

import java.io.IOException;

public class Main extends Application {


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/app/appvisionartificial/RecognitionMask/RecognitionMaskView.fxml"));
        AnchorPane root = fxmlLoader.load();

        stage.setMinWidth(950);
        stage.setMinHeight(620);


        // Establecer eventos para redimensionar la ventana desde las esquinas
        ResizeHelper.setResizeHandlers(stage,root);
        ResizeHelper.setMoveHandlers(stage);


        Scene scene = new Scene(root, 950, 620);



        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();


//        // Mover scena arrastrando la scena ----------------------
//        scene.setOnMousePressed(pressEvent -> {
//            scene.setOnMouseDragged(dragEvent -> {
//                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
//                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
//            });
//        });



    }

    public static void main(String[] args) {
        launch();
    }

}