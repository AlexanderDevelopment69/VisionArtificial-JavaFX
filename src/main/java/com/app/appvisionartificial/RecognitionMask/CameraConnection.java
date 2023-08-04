package com.app.appvisionartificial.RecognitionMask;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import org.bytedeco.javacv.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class CameraConnection {
    private VideoCapture capture;
    private Mat frame;
    private boolean streaming;

    public CameraConnection() {
        capture = new VideoCapture();
        frame = new Mat();
        streaming = false;


    }

    public boolean isConnected() {
        return capture.isOpened();
    }

    public boolean connect(int cameraIndex) {
        if (capture.isOpened()) {
            return false; // Ya está conectado
        }

        return capture.open(cameraIndex);
    }

    public boolean disconnect() {
        if (capture.isOpened()) {
            capture.release();
            return true;
        }

        return false; // No estaba conectado
    }

    public void startCameraStream(ImageView imageView) {
        if (!capture.isOpened()) {
            return; // Cámara no conectada
        }

        streaming = true;
        new Thread(() -> {
            while (streaming) {
                capture.read(frame);

                if (!frame.empty()) {
                    Image image = matToImage(frame);
                    Platform.runLater(() -> imageView.setImage(image));
                }
            }

            // Detener el streaming y liberar recursos al apagar la cámara
            capture.release();
            frame.release();
        }).start();
    }


    public void stopCameraStream() {
        streaming = false;
    }

    private Image matToImage(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }


    public boolean connectToWifiCamera(String cameraURL) {
        if (capture.isOpened()) {
            return false; // Ya está conectado
        }

        return capture.open(cameraURL);
    }


}

//    Explicación de los métodos:
//
//        isConnected(): Verifica si la cámara está conectada.
//
//        connect(int cameraIndex): Intenta conectar con una cámara especificada por el índice cameraIndex. Retorna true si se conectó correctamente o false si ya estaba conectada o no pudo conectarse.
//
//        disconnect(): Desconecta la cámara si estaba conectada. Retorna true si la cámara estaba conectada y se desconectó correctamente, o false si la cámara no estaba conectada.
//
//        startCameraStream(ImageView imageView): Inicia el streaming del video de la cámara en un hilo separado. Cada frame capturado se convierte en una Image de JavaFX y se muestra en el ImageView proporcionado. Este método se ejecuta en segundo plano para evitar bloquear la interfaz de usuario.
//
//        stopCameraStream(): Detiene el streaming del video de la cámara.
//
//        matToImage(Mat frame): Convierte un Mat de OpenCV en una Image de JavaFX.
