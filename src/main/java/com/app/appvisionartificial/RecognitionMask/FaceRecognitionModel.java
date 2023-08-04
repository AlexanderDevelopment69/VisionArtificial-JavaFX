package com.app.appvisionartificial.RecognitionMask;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceRecognitionModel {

    private CascadeClassifier faceCascade;

    public FaceRecognitionModel(String cascadeFilePath) {
        faceCascade = new CascadeClassifier(cascadeFilePath);
    }

    public Mat detectAndRecognizeFaces(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayImage, faces);

        for (Rect rect : faces.toArray()) {
            // Aquí puedes realizar el reconocimiento facial en cada rostro detectado
            // y dibujar un rectángulo alrededor de la cara en la imagen original
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }

        return image;
    }
}
