package com.myown.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IFISController {
    @FXML
    private Label warning;

    @FXML
    private void handle_import(){
        warning.setText("*Please enter a valid file path!!");
    }

}