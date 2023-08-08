package com.app.appvisionartificial.Recognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


import static org.opencv.imgproc.Imgproc.cvtColor;

public class FaceDetectionUtil {

    private CascadeClassifier faceDetector;

    private String lastDetectionResult;

    public FaceDetectionUtil(String xmlModelPath) {
        faceDetector = new CascadeClassifier(xmlModelPath);

    }



    public Mat detectFaces(Mat frame) {
        Mat grayFrame = new Mat();
        MatOfRect faces = new MatOfRect();

        // Convertir el frame a escala de grises (el modelo requiere una imagen en escala de grises)
        cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        // Detectar rostros en el frame
        faceDetector.detectMultiScale(grayFrame, faces);

        // Dibujar un rectángulo alrededor de cada rostro detectado
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }

//        // Mostrar el resultado en la etiqueta
//        if (faces.toArray().length > 0) {
////            Platform.runLater(() -> resultLabel.setText("¡Se ha detectado una cara!"));
//            System.out.println("Se ha detectado una cara");
//        } else {
////            Platform.runLater(() -> resultLabel.setText("No se ha detectado una cara."));
//            System.out.println("No se ha detectado nada");
//        }
        return frame;
    }

    public String detectFacesAndReturnResult(Mat image) {
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);

        // Dibujar un rectángulo alrededor de cada rostro detectado
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }

        if (!faces.empty()) {
            return "Rostro detectado";
        } else {
            return "Ningún rostro detectado";
        }
    }



    public Mat detectFacesAndDrawRectangle(Mat image) {
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();

//        faceDetector.detectMultiScale(grayImage, faces);
//        for (Rect rect : faces.toArray()) {
//            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
//        }


        // Ajustar el valor 'minSize' para permitir la detección de caras más grandes
        Size minSize = new Size(100, 100); // Puedes ajustar estos valores según tus necesidades

        faceDetector.detectMultiScale(image, faces, 1.1, 3, 0, minSize, new Size());

        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }


        if (!faces.empty()) {
            lastDetectionResult = "Rostro detectado";
        } else {
            lastDetectionResult = "Ningún rostro detectado";
        }

        return image;
    }



    public Mat detectFacesAndReturnResultHTTP(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);


        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }


        if (!faces.empty()) {
            lastDetectionResult = "Rostro detectado";
        } else {
            lastDetectionResult = "Ningún rostro detectado";
        }

        return image;
    }






    public String getLastDetectionResult() {
        return lastDetectionResult;
    }


}

