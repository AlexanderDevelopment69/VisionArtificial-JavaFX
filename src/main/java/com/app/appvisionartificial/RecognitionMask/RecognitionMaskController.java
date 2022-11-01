package com.app.appvisionartificial.RecognitionMask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecognitionMaskController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton btnMaximize;
    @FXML
    private JFXButton btnMinimize;

    @FXML
    private JFXDrawer drawer;
    @FXML
    public ImageView currenFrame;
    @FXML
    public Label text ;

    @FXML
    private StackPane MenuDrawerADD;


    @FXML
    private MFXToggleButton toogleButton;


    private WebCamCapture webCamCapture;





    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    public void setLabel(String labelText){
        text.setText(labelText);
    }
    public Label getLabel() {
        return text;
    }


    @FXML
    void maximize(ActionEvent event) {

        Stage stage = (Stage) btnMaximize.getScene().getWindow();
        if(stage.isMaximized()){
            stage.setMaximized(false);
        }else{
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
        if (drawer.isOpening()) {
            drawer.close();

        } else {
            drawer.setSidePane(MenuDrawerADD);
            drawer.open();
            drawer.toFront();

//                drawer.setMinWidth(300);
        }
    }

    @FXML
    void activeCamara(MouseEvent event) throws IOException {
        if (toogleButton.isSelected()){
            System.out.println("prendido");
            currenFrame.fitWidthProperty().bind(anchorPane.widthProperty());
            currenFrame.fitHeightProperty().bind(anchorPane.heightProperty());
            currenFrame.setVisible(true);
            webCamCapture = new WebCamCapture(this.currenFrame);

        }else{
            System.out.println("apagado");

            if(webCamCapture != null){
                webCamCapture.stop();
                currenFrame.setVisible(false);
            }
        }



        }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }


}
