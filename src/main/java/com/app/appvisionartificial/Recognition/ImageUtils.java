package com.app.appvisionartificial.Recognition;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static Mat convertImageToMat(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            return Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Image matToImage(Mat mat) {
        Mat convertedMat = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
        Imgproc.cvtColor(mat, convertedMat, Imgproc.COLOR_BGR2RGB);

        int width = convertedMat.width();
        int height = convertedMat.height();
        int byteSize = width * height * 3;
        byte[] byteArray = new byte[byteSize];
        convertedMat.get(0, 0, byteArray);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.getRaster().setDataElements(0, 0, width, height, byteArray);

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }







}
