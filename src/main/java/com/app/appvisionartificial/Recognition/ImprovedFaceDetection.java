package com.app.appvisionartificial.Recognition;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.LinkedList;
import java.util.Queue;

public class ImprovedFaceDetection {

    private static final int MAX_PREVIOUS_DETECTIONS = 5;
    private static final double CONFIDENCE_THRESHOLD = 2.0; // Ajusta según sea necesario

    private static Queue<Rect[]> previousDetections = new LinkedList<>();


    private  CascadeClassifier faceCascade;

    private String lastDetectionResult;

    public ImprovedFaceDetection(String xmlModelPath) {
        faceCascade = new CascadeClassifier(xmlModelPath);

    }


    public  Mat detectFacesAndDrawRectangle(Mat image) {


        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(image, faces, 1.1, 3, 0, new Size(), new Size());

        // Convertir las detecciones en un array y agregarlo a la cola
        Rect[] detectionsArray = faces.toArray();
        previousDetections.offer(detectionsArray);

        // Si la cola es más grande que el máximo, eliminar la detección más antigua
        if (previousDetections.size() > MAX_PREVIOUS_DETECTIONS) {
            previousDetections.poll();
        }

        // Calcular el promedio de las detecciones anteriores
        Rect[] averagedDetections = averageDetections();

        for (Rect rect : averagedDetections) {
            // Aplicar umbral de confianza para dibujar solo detecciones confiables
            if (rect.width > 0 && rect.height > 0) {
                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
            }
        }

        if (!faces.empty()) {
            lastDetectionResult = "Rostro detectado";
        } else {
            lastDetectionResult = "Ningún rostro detectado";
        }

        return image;
    }

    private static Rect[] averageDetections() {
        int totalDetections = previousDetections.size();
        Rect[] averagedDetections = new Rect[totalDetections > 0 ? previousDetections.peek().length : 0];

        for (int i = 0; i < averagedDetections.length; i++) {
            int totalX = 0;
            int totalY = 0;
            int totalWidth = 0;
            int totalHeight = 0;
            int validDetections = 0;

            for (Rect[] detectionsArray : previousDetections) {
                if (i < detectionsArray.length) {
                    Rect detection = detectionsArray[i];
                    if (detection.width > 0 && detection.height > 0) {
                        totalX += detection.x;
                        totalY += detection.y;
                        totalWidth += detection.width;
                        totalHeight += detection.height;
                        validDetections++;
                    }
                }
            }

            if (validDetections > 0) {
                averagedDetections[i] = new Rect(
                        totalX / validDetections,
                        totalY / validDetections,
                        totalWidth / validDetections,
                        totalHeight / validDetections
                );
            }
        }

        return averagedDetections;
    }


    public String getLastDetectionResult() {
        return lastDetectionResult;
    }

}
