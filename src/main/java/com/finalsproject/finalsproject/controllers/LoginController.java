package com.finalsproject.finalsproject.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class LoginController{

    @FXML
    private Button createAcc;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private Button signUp;

    @FXML
    private PasswordField su_password;

    @FXML
    private AnchorPane registerForm;

    @FXML
    private AnchorPane left_form;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private TextField su_username;

    @FXML
    private Label employeeLabel;

    @FXML
    private Label newLabel;

    @FXML
    private Button leftFormAlready;

    public void switchForm(ActionEvent event){
        TranslateTransition slider = new TranslateTransition();
        if(event.getSource() == createAcc){
            slider.setNode(left_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) ->{
                leftFormAlready.setVisible(true);
                createAcc.setVisible(false);
                newLabel.setVisible(false);
            });
            slider.play();
        }else if(event.getSource() == leftFormAlready){
            slider.setNode(left_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) ->{
                leftFormAlready.setVisible(false);
                createAcc.setVisible(true);
                newLabel.setVisible(true);

            });
            slider.play();
        }
    }

}
