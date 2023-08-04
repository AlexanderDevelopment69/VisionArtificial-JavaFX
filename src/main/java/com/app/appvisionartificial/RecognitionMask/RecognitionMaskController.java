package com.app.appvisionartificial.RecognitionMask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXToggleButton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecognitionMaskController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MFXButton btnMaximize;
    @FXML
    private MFXButton btnMinimize;

    @FXML
    private JFXDrawer drawer;
    @FXML
    public ImageView imageView;
    @FXML
    public Label text;

    @FXML
    private StackPane MenuDrawerADD;
    @FXML
    private JFXButton buttonSetting;

    @FXML
    private MFXToggleButton toggleButton;

    private CameraConnection cameraConnection;


    private Task<Boolean> connectTask;


    private FaceRecognitionModel faceRecognitionModel;

    public RecognitionMaskController() {
        String cascadeFilePath = "ruta_al_archivo_cascade.xml"; // Ruta al archivo XML del modelo Haar Cascade
        faceRecognitionModel = new FaceRecognitionModel(cascadeFilePath);
    }




    @FXML
    void exit(ActionEvent event) {
        cameraConnection.stopCameraStream();
        Platform.exit();
    }

    public void setLabel(String labelText) {
        text.setText(labelText);
    }

    public Label getLabel() {
        return text;
    }


    @FXML
    void maximize(ActionEvent event) {

        Stage stage = (Stage) btnMaximize.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);

        }


    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    void showSettings(ActionEvent event) throws IOException {
//        MenuDrawerADD = FXMLLoader.load(getClass().getResource("/com/app/appvisionartificial/RecognitionMask/Settings.fxml"));
//        drawer.setOnDrawerClosed(e -> drawer.setSidePane());
//        if (drawer.isOpening()) {
//            drawer.close();
//        } else {
//            drawer.setSidePane(MenuDrawerADD);
//            drawer.open();
//            drawer.toFront();
//
////                drawer.setMinWidth(300);
//        }
        drawer.setSidePane(MenuDrawerADD);
        if (!drawer.isOpened()) {

//            drawer.toFront(); // Mover el JFXDrawer al frente (opcional si hay otros elementos que se superponen)
            drawer.open(); // Abre el panel lateral
        } else {
            drawer.close(); // Cierra el panel lateral
        }


        // Cerrar el JFXDrawer cuando se hace clic fuera de él
        anchorPane.setOnMouseClicked(e -> {
            if (drawer.isOpened()) {
                drawer.close();
            }
        });

    }

//    @FXML
//    void toggleCamera() throws IOException {
//
//        if (toggleButton.isSelected()) {
//            if (!cameraConnection.isConnected()) {
//                // Inicializar la cámara en un hilo separado
//                Task<Boolean> connectTask = new Task<>() {
//                    @Override
//                    protected Boolean call() {
//                        return cameraConnection.connect(0); // Puedes cambiar el índice de la cámara si tienes varias
//                    }
//                };
//
//                connectTask.setOnSucceeded(event -> {
//                    if (connectTask.getValue()) {
//                        cameraConnection.startCameraStream(imageView);
//                    } else {
//                        toggleButton.setSelected(false);
//                    }
//                });
//
//                new Thread(connectTask).start();
//            }
//        } else {
//            cameraConnection.stopCameraStream();
//        }
//    }


    @FXML
    private void toggleCamera() {
        // Deshabilitar el ToggleButton para evitar activaciones rápidas
        toggleButton.setDisable(true);

        if (toggleButton.isSelected()) {
            if (!cameraConnection.isConnected()) {

                Task<Boolean> connectTask = new Task<>() {
                    @Override
                    protected Boolean call() {
                        return cameraConnection.connect(0);
//                        String cameraURL = "http://192.168.88.15:4747/video"; // Cambiar por la URL de la cámara de red WiFi
//                        return cameraConnection.connectToWifiCamera(cameraURL);

                    }
                };

                connectTask.setOnSucceeded(connectEvent -> {
                    imageView.setVisible(true);
                    if (connectTask.getValue()) {
                        // Si la conexión es exitosa, iniciar el streaming de la cámara
                        cameraConnection.startCameraStream(imageView);

                    } else {
                        // Si la conexión falla, deseleccionar el ToggleButton
                        toggleButton.setSelected(false);
                    }

                    // Habilitar nuevamente el ToggleButton para permitir interacción con el usuario
                    toggleButton.setDisable(false);
                });

                connectTask.setOnFailed(connectEvent -> {
                    // En caso de fallo, deseleccionar el ToggleButton y habilitarlo nuevamente
                    toggleButton.setSelected(false);
                    toggleButton.setDisable(false);
                });


                // Iniciar la conexión de la cámara en otro hilo
                new Thread(connectTask).start();

            }

        } else {
            // Detener el streaming
            cameraConnection.stopCameraStream();
            // Habilitar nuevamente el ToggleButton
            toggleButton.setDisable(false);

            imageView.setImage(null);

            imageView.setVisible(false);


        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cameraConnection = new CameraConnection();
        // Vincular el tamaño de la imagen al tamaño del AnchorPane
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());

        drawer.setSidePane(MenuDrawerADD);

        // Controlamos los eventos del ratón sobre el botón para abrir y cerrar el Drawer
        buttonSetting.setOnMouseEntered(event -> {
            if (drawer.isClosed()) {
                drawer.open();
            }
        });


        anchorPane.setOnMouseEntered(event -> {
            if (drawer.isOpened()) {

                drawer.close();
            }

        });


    }


}
