package com.app.appvisionartificial.RecognitionMask;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private MFXToggleButton toogleButton;






    @FXML
    void activeCamara(MouseEvent event) throws IOException {
//
//        FXMLLoader loader = new FXMLLoader();
//        RecognitionMaskController nsc = new RecognitionMaskController();
//        loader.setController(nsc);
//
//        nsc.ImageView().setVisible(true);

//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("/com/app/appvisionartificial/RecognitionMask/RecognitionMaskView.fxml"));
//        AnchorPane frame = fxmlLoader.load();
//
//        RecognitionMaskController c = fxmlLoader.getController();
//        c.ImageView().setVisible(true);

        if (toogleButton.isSelected()){
            System.out.println("prendido");



//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/com/app/appvisionartificial/RecognitionMask/RecognitionMaskView.fxml"));
//            Parent frame = fxmlLoader.load();
//            RecognitionMaskController c = fxmlLoader.getController();
            new RecognitionMaskController().getLabel().setText("hola");
//            c.getImageView().setVisible(true);
//            c.getLabel().setText("Pasamontañas");
//
        }else{
            System.out.println("apagado");
//            currentFrame.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
