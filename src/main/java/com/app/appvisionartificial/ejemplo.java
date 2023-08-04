package com.app.appvisionartificial;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class ejemplo {

    public static void main(String[] args) {
        // Cargar el modelo entrenado desde el archivo .xml
        CascadeClassifier faceDetector = new CascadeClassifier("src/main/java/com/app/appvisionartificial/Model/modeloEigenFace.xml");

        // Cargar una imagen para la detección de rostros
        Mat image = Imgcodecs.imread("ruta_de_la_imagen.jpg");

        // Convertir la imagen a escala de grises (el modelo requiere una imagen en escala de grises)
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Detectar rostros en la imagen
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(grayImage, faces);

        // Dibujar un rectángulo alrededor de cada rostro detectado
        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }

        // Mostrar la imagen con los rostros detectados
        HighGui.imshow("Rostros Detectados", image);
        HighGui.waitKey();
    }
}
