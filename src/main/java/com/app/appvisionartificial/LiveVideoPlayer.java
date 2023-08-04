package com.app.appvisionartificial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class LiveVideoPlayer extends Application {

    private static final String VIDEO_URL = "http://192.168.88.15:4747/video"; // Cambiar por la URL de tu cámara IP o transmisión de video en vivo

    @Override
    public void start(Stage primaryStage) {
        Media media = new Media(VIDEO_URL);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Ajustar tamaño del MediaView según tus necesidades
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);

        StackPane root = new StackPane(mediaView);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DroidCam Viewer");
        primaryStage.show();

        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
