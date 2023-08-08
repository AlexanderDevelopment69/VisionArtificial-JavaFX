package com.app.appvisionartificial.Recognition;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class CameraConnection {
    private final VideoCapture capture;
    private final Mat frame;
    private boolean streaming;
    private final FaceDetectionUtil faceDetectionUtil;

    private ImprovedFaceDetection improvedFaceDetection;


    private FFmpegFrameGrabber grabber;
    private JavaFXFrameConverter frameConverter;



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
        improvedFaceDetection= new ImprovedFaceDetection(xmlModelPath);

        frameConverter = new JavaFXFrameConverter(); // Inicializar el JavaFXFrameConverter
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
//                    Mat imageWithRectangles = faceDetectionUtil.detectFacesAndDrawRectangle(frame);
                    Mat imageWithRectangles = improvedFaceDetection.detectFacesAndDrawRectangle(frame);
                    Image image = matToImage(imageWithRectangles);

                    // Mostramos el resultado en el Label
                    if (statusLabel != null) {
//                        Platform.runLater(() -> statusLabel.setText(faceDetectionUtil.getLastDetectionResult()));
                        Platform.runLater(() -> statusLabel.setText(improvedFaceDetection.getLastDetectionResult()));
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
        try {
            grabber = new FFmpegFrameGrabber(cameraURL);
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);
            grabber.setOption("pix_fmt", "rgb24");
            grabber.setOption("rtsp_transport", "tcp"); // Opcional: Cambiar el transporte a TCP si tienes problemas
            grabber.start();
            return true; // Si no hay errores al iniciar el grabber, la conexión se considera exitosa
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Si ocurre algún error, la conexión falla y se devuelve false
        }
    }


    public void startProcessing(ImageView imageView, Label statusLabel) {
        // Verificar si ya se está transmitiendo

        streaming = true;
        new Thread(() -> {

            while (streaming) {
                try {
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        if (imageView != null) {
                            // Convertir el frame a una imagen de JavaFX
                            Image image = frameConverter.convert(frame);
                            Mat matFrame = ImageUtils.convertImageToMat(image);
                            Mat detectionResult = faceDetectionUtil.detectFacesAndReturnResultHTTP(matFrame);
                            Image resultImage = ImageUtils.matToImage(detectionResult);
                            // Actualizar la imagen en el ImageView en el hilo de JavaF
                            Platform.runLater(() -> imageView.setImage(resultImage));
                        }

                        // Detectamos los rostros y mostramos el resultado en el Label
                        if (statusLabel != null) {
                            Platform.runLater(() -> statusLabel.setText(faceDetectionUtil.getLastDetectionResult()));
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        }).start();
    }


    public void stopCameraProcessing() {
        streaming = false;
        try {
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









//    public static Mat convertImageToMat(Image image) {
//        int width = (int) image.getWidth();
//        int height = (int) image.getHeight();
//
//        byte[] imageData = new byte[width * height * 4];
//        PixelReader pixelReader = image.getPixelReader();
//        WritablePixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraInstance();
//
//        pixelReader.getPixels(0, 0, width, height, pixelFormat, imageData, 0, width * 4);
//
//        Mat mat = new Mat(height, width, CvType.CV_8UC4);
//        mat.put(0, 0, imageData);
//
//        return mat;
//    }
//
//    public static Image matToImage(Mat mat) {
//        MatOfByte matOfByte = new MatOfByte();
//        Imgcodecs.imencode(".png", mat, matOfByte);
//
//        byte[] byteArray = matOfByte.toArray();
//        int width = mat.cols();
//        int height = mat.rows();
//        mat.channels();
//
//        WritablePixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
//        Image image = new Image(new ByteArrayInputStream(byteArray), width, height, true, true);
//
//        return image;
//    }


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
