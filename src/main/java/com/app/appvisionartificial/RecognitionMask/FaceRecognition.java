package com.app.appvisionartificial.RecognitionMask;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceRecognition {

    private Net faceNet;

    public FaceRecognition(String modelPath) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceNet = Dnn.readNetFromTensorflow(modelPath);
    }

    public Mat detectFaces(Mat image) {
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(300, 300), new Scalar(104, 177, 123), false, false);
        faceNet.setInput(blob);
        Mat detections = faceNet.forward();
        detections = detections.reshape(1, (int) detections.total() / 7);

        MatOfRect faces = new MatOfRect();
        for (int i = 0; i < detections.rows(); i++) {
            double confidence = detections.get(i, 2)[0];
            if (confidence > 0.5) {
                int x1 = (int) (detections.get(i, 3)[0] * image.cols());
                int y1 = (int) (detections.get(i, 4)[0] * image.rows());
                int x2 = (int) (detections.get(i, 5)[0] * image.cols());
                int y2 = (int) (detections.get(i, 6)[0] * image.rows());
                faces.fromArray(new Rect(x1, y1, x2 - x1, y2 - y1));
            }
        }

        return faces;
    }
}

