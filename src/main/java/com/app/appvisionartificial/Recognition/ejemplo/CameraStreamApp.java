package com.app.appvisionartificial.Recognition.ejemplo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

public class CameraStreamApp extends Application {

    private JavaFXFrameConverter frameConverter;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        String streamUrl = "http://192.168.88.15:4747/video"; // Reemplaza con la dirección RTSP de tu cámara WiFi

        frameConverter = new JavaFXFrameConverter();
        imageView = new ImageView();

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Streaming de Cámara WiFi");
        primaryStage.show();

        // Iniciar el procesamiento del video en un hilo separado
        new Thread(() -> startProcessing(streamUrl)).start();
    }

    private void startProcessing(String streamUrl) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamUrl)) {
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);
            grabber.setOption("pix_fmt", "rgb24");
            grabber.setOption("rtsp_transport", "tcp"); // Opcional: Cambiar el transporte a TCP si tienes problemas
            grabber.start();

            while (!Thread.interrupted()) {
                Frame frame = grabber.grab();
                if (frame != null) {
                    Image image = frameConverter.convert(frame);
                    if (image != null) {
                        Platform.runLater(() -> imageView.setImage(image));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Detener el procesamiento de la cámara al cerrar la aplicación
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
