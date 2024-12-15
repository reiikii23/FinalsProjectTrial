package com.finalsproject.finalsproject.controllers;

import com.finalsproject.finalsproject.Data;
import com.finalsproject.finalsproject.DatabaseConnection;
import com.finalsproject.finalsproject.ProductMod;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

public class ProductBoxController implements Initializable {

    @FXML
    private AnchorPane boxForm;

    @FXML
    private Button productAddBtn;

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private Spinner<Integer> productSpinner;

    private ProductMod productMod;
    private Image image;

    private SpinnerValueFactory<Integer> spin;

    private Alert alert;
    private String prodID;
    private Connection connect;
    private PreparedStatement pstmt;
    private ResultSet result;

    public void setData(ProductMod productMod){
        this.productMod = productMod;

        productImage = productMod.getImage();
        productDate = String.valueOf(productMod.getDate());
        category = productMod.getCategory();
        prodID = productMod.getProductId();
        productName.setText(productMod.getProductName());
        productPrice.setText('₱' +String.valueOf(productMod.getPrice()));
        String path = "File:" + productMod.getImage();
        image = new Image(path, 166,112,false,true);
        productImageView.setImage(image);
        pr = productMod.getPrice();
    }
    private int quantity;
    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        productSpinner.setValueFactory(spin);
    }

    private String category;
    private String productImage;
    private String productDate;
    private double totalP;
    private double pr;

    public void addBtn() {

        FormDesignController dForm = new FormDesignController();
        dForm.customerID();

        quantity = productSpinner.getValue();
        String check = "";
        String checkAvailable = "SELECT status FROM products WHERE productID = '"
                + prodID + "'";

        connect = DatabaseConnection.connectDB();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM products WHERE productID = '"
                    + prodID + "'";

            pstmt = connect.prepareStatement(checkStock);
            result = pstmt.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("Stock");
            }

            if(checkStck == 0){

                String updateStock = "UPDATE products SET productName = '"
                        + productName.getText() + "', category = '"
                        + category + "', stock = 0, price = " + pr
                        + ", status = 'Unavailable', image = '"
                        + productImage + "', date = '"
                        + productDate + "' WHERE productID = '"
                        + prodID + "'";
                pstmt = connect.prepareStatement(updateStock);
                pstmt.executeUpdate();

            }

            pstmt = connect.prepareStatement(checkAvailable);
            result = pstmt.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }

            if (!check.equals("Available") || quantity == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Something is Wrong");
                alert.showAndWait();
            } else {

                if (checkStck < quantity) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Out of stock...");
                    alert.showAndWait();
                } else {

                    productImage = productImage.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customers "
                            + "(customerID, productName, quantity, price, date, employeeServer) "
                            + "VALUES(?,?,?,?,?,?)";
                    pstmt = connect.prepareStatement(insertData);
                    pstmt = connect.prepareStatement(insertData);
                    pstmt.setString(1, String.valueOf(Data.cID));
                    pstmt.setString(2, productName.getText());
                    pstmt.setString(3, String.valueOf(quantity));

                    totalP = (quantity * pr);
                    pstmt.setString(4, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    pstmt.setString(5, String.valueOf(sqlDate));
                    pstmt.setString(6, Data.username);

                    pstmt.executeUpdate();

                    int upStock = checkStck - quantity;

                    String updateStock = "UPDATE products SET productName = '"
                            + productName.getText() + "', category = '"
                            + category + "', stock = " + upStock + ", price = " + pr
                            + ", status = '"
                            + check + "', image = '"
                            + productImage + "', date = '"
                            + productDate + "' WHERE productID = '"
                            + prodID + "'";

                    pstmt = connect.prepareStatement(updateStock);
                    pstmt.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();


                    dForm.menuGetTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources){
        setQuantity();
    }
}
