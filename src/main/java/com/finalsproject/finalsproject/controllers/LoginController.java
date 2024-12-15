package com.finalsproject.finalsproject.controllers;

import com.finalsproject.finalsproject.Data;
import com.finalsproject.finalsproject.DatabaseConnection;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    @FXML
    private ImageView logo;

    private Alert alert;

    private Connection connect;
    private PreparedStatement pstmt;
    private ResultSet result;

    public void loginBtn() {

        if (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        } else {

            String selectData = "SELECT username, password FROM employees WHERE username = ? and password = ?";

            connect = DatabaseConnection.connectDB();

            try {

                pstmt = connect.prepareStatement(selectData);
                pstmt.setString(1, loginUsername.getText());
                pstmt.setString(2, loginPassword.getText());

                result = pstmt.executeQuery();
                if (result.next()) {
                    Data.username = loginUsername.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/formDesign.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setTitle("Mcdonald's POS System");
                    stage.setMinWidth(1100);
                    stage.setMinHeight(600);

                    stage.setScene(scene);
                    stage.show();

                    loginBtn.getScene().getWindow().hide();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void regBtn(){

        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all blank fields");
            alert.showAndWait();
        }else{
            String regData = "INSERT into employees (username, password,date)" + "VALUES(?,?,?)";
            connect = DatabaseConnection.connectDB();

            try{
                pstmt = connect.prepareStatement(regData);
                pstmt.setString(1,su_username.getText());
                pstmt.setString(2,su_password.getText());

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                pstmt.setString(3,String.valueOf(sqlDate));

                pstmt.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Registered Successfully!!\n WELCOME TO MCDONALD'S!!");
                alert.showAndWait();

                su_username.setText("");
                su_password.setText("");

                TranslateTransition slider = new TranslateTransition();
                slider.setNode(left_form);
                slider.setToX(0);
                slider.setDuration(Duration.seconds(.5));

                slider.setOnFinished((ActionEvent e) ->{
                    leftFormAlready.setVisible(false);
                    createAcc.setVisible(true);
                    newLabel.setVisible(true);

                });
                slider.play();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

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
