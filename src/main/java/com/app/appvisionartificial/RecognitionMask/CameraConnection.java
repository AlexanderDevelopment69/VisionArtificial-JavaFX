package com.app.appvisionartificial.RecognitionMask;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class CameraConnection {
    private VideoCapture capture;
    private Mat frame;
    private boolean streaming;


    private FaceDetectionUtil faceDetectionUtil;

//    public CameraConnection() {
//        capture = new VideoCapture();
//        frame = new Mat();
//        streaming = false;
//

//    }

    public CameraConnection(String xmlModelPath) {
        capture = new VideoCapture();
        frame = new Mat();
        streaming = false;
        faceDetectionUtil = new FaceDetectionUtil(xmlModelPath);
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

//    public void startCameraStream(ImageView imageView) {
//        if (!capture.isOpened()) {
//            return; // Cámara no conectada
//        }
//
//        streaming = true;
//        new Thread(() -> {
//            while (streaming) {
//                capture.read(frame);
//
//                if (!frame.empty()) {
//                    Image image = matToImage(frame);
//                    Platform.runLater(() -> imageView.setImage(image));
//                }
//            }
//
//            // Detener el streaming y liberar recursos al apagar la cámara
//            capture.release();
//            frame.release();
//        }).start();
//    }


    //DE OPENCV
//    public void startCameraStream(ImageView imageView) {
//        if (!capture.isOpened()) {
//            return; // Cámara no conectada
//        }
//
//        streaming = true;
//        new Thread(() -> {
//            while (streaming) {
//                capture.read(frame);
//
//                if (!frame.empty()) {
//                    // Realizar la detección de rostros
//                    MatOfRect faces = new MatOfRect();
//                    faceDetector.detectMultiScale(frame, faces, 1.1, 2, 0, new Size(30, 30));
//
//                    // Dibujar rectángulos alrededor de los rostros detectados
//                    for (Rect rect : faces.toArray()) {
//                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
//                    }
//
//                    Image image = matToImage(frame);
//                    Platform.runLater(() -> imageView.setImage(image));
//                }
//            }
//
//            // Detener el streaming y liberar recursos al apagar la cámara
//            capture.release();
//            frame.release();
//        }).start();
//    }


    //Sin retornar NINGUN TEXTO, solo decta el rostro en un cuadro
//    public void startCameraStream(ImageView imageView) {
//        if (!capture.isOpened()) {
//            return; // Cámara no conectada
//        }
//
//        streaming = true;
//        new Thread(() -> {
//            while (streaming) {
//                capture.read(frame);
//
//                if (!frame.empty()) {
//                    // Realizar la detección de rostros en tiempo real
//                    Mat frameWithFaces = faceDetectionUtil.detectFaces(frame);
//
//                    // Convertir el frame con rostros a una imagen para mostrar en el ImageView
//                    Image image = matToImage(frameWithFaces);
//                    Platform.runLater(() -> imageView.setImage(image));
//                }
//            }
//
//            // Detener el streaming y liberar recursos al apagar la cámara
//            capture.release();
//            frame.release();
//        }).start();
//    }


    public void startCameraStream(ImageView imageView, Label statusLabel) {
        if (!capture.isOpened()) {
            return; // Cámara no conectada
        }

        streaming = true;
        new Thread(() -> {
            while (streaming) {
                capture.read(frame);

                if (!frame.empty()) {
////                    OBTIENE EL CUADRO EN EL CONTORNO DE LA CARA
//                    Mat frameWithFaces = faceDetectionUtil.detectFaces(frame);
//                    Image image = matToImage(frameWithFaces);
//                    Platform.runLater(() -> imageView.setImage(image));
//
//                    //OBTIENE EL RESULTADO SIN CUADRO EN EL CONTORNO
//                    String detectionResult = faceDetectionUtil.detectFacesAndReturnResult(frame);
//                    Platform.runLater(() -> statusLabel.setText(detectionResult));

                    //--------------------------------------------------------------------------------

                    // Detectamos los rostros y dibujamos los rectángulos
                    Mat imageWithRectangles = faceDetectionUtil.detectFacesAndDrawRectangle(frame);
                    Image image = matToImage(imageWithRectangles);

                    // Mostramos el resultado en el Label
                    if (statusLabel != null) {
                        Platform.runLater(() -> statusLabel.setText(faceDetectionUtil.getLastDetectionResult()));
                    }

                    // Mostramos el frame con los rectángulos en el ImageView
                    if (imageView != null) {

//                        Platform.runLater(() -> imageView.setImage(matToImage(imageWithRectangles)));
                        Platform.runLater(() -> imageView.setImage(image));
                    }



                }
            }

            // Detener el streaming y liberar recursos al apagar la cámara
            capture.release();
            frame.release();

            // Borramos el texto del Label al apagar la cámara
            if (statusLabel != null) {
                Platform.runLater(() -> statusLabel.setText(null));
            }


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
