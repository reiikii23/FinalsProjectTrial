package com.finalsproject.finalsproject.controllers;

import com.finalsproject.finalsproject.Data;
import com.finalsproject.finalsproject.DatabaseConnection;
import com.finalsproject.finalsproject.ProductMod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.Date;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FormDesignController implements Initializable {

    @FXML
    private Button customersBtn;

    @FXML
    private Button dashBoardBtn;

    @FXML
    private AnchorPane formDesign;

    @FXML
    private Button importBtn;

    @FXML
    private Button inventoryAddBtn;

    @FXML
    private Button inventoryBtn;

    @FXML
    private TableColumn<ProductMod, String> inventoryCategory;

    @FXML
    private Button inventoryClrBtn;

    @FXML
    private TableColumn<ProductMod, String> inventoryDate;

    @FXML
    private Button inventoryDelBtn;

    @FXML
    private AnchorPane inventoryForm;

    @FXML
    private AnchorPane dashboardForm;

    @FXML
    private ImageView inventoryImageView;

    @FXML
    private TableColumn<ProductMod, String> inventoryPrice;

    @FXML
    private TableColumn<ProductMod, String> inventoryProductID;

    @FXML
    private TableColumn<ProductMod, String> inventoryProductName;

    @FXML
    private TableColumn<ProductMod, String> inventoryStatus;

    @FXML
    private TableColumn<ProductMod, String> inventoryStock;

    @FXML
    private Button inventoryUpdBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button menuBtn;

    @FXML
    private ComboBox<?> txtCategory;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtProductID;

    @FXML
    private TextField txtProductName;

    @FXML
    private ComboBox<?> txtStatus;

    @FXML
    private TableView<ProductMod> inventoryTableView;

    @FXML
    private Label username;

    @FXML
    private Button menuCancelBtn;

    @FXML
    private Label menuChange;

    @FXML
    private AnchorPane menuForm;

    @FXML
    private GridPane menuGridPane;

    @FXML
    private Button menuOrderBtn;

    @FXML
    private TableColumn<?, ?> menuPrice;

    @FXML
    private TableColumn<?, ?> menuProductName;

    @FXML
    private TableColumn<?, ?> menuQuantity;

    @FXML
    private ScrollPane menuScrollPane;

    @FXML
    private TableView<?> menuTableView;

    @FXML
    private TextField menuToPay;

    @FXML
    private Label menuTotal;

    private Alert alert;

    private Connection connect;
    private PreparedStatement pstmt;
    private Statement statement;
    private ResultSet result;
    private Image image;

    private ObservableList<ProductMod> productBoxData = FXCollections.observableArrayList();

    public void inventoryAddBtn(){

        if (txtProductID.getText().isEmpty()
                || txtProductName.getText().isEmpty()
                || txtCategory.getSelectionModel().getSelectedItem() == null
                || txtStock.getText().isEmpty()
                || txtPrice.getText().isEmpty()
                || txtStatus.getSelectionModel().getSelectedItem() == null
                || Data.path == null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        }else {

            String checkProductID = "SELECT productID FROM products WHERE productID = '"
                    + txtProductID.getText() + "'";

            connect = DatabaseConnection.connectDB();

            try {

                statement = connect.createStatement();
                result = statement.executeQuery(checkProductID);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(txtProductID.getText() + " has already been taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO products "
                            + "(productID, productName,category, stock, price, status, image, date) "
                            + "VALUES(?,?,?,?,?,?,?,?)";

                    pstmt = connect.prepareStatement(insertData);
                    pstmt.setString(1, txtProductID.getText());
                    pstmt.setString(2, txtProductName.getText());
                    pstmt.setString(3, (String) txtCategory.getSelectionModel().getSelectedItem());
                    pstmt.setString(4, txtStock.getText());
                    pstmt.setString(5, txtPrice.getText());
                    pstmt.setString(6, (String) txtStatus.getSelectionModel().getSelectedItem());

                    String path = Data.path;
                    path = path.replace("\\", "\\\\");
                    pstmt.setString(7, path);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    pstmt.setString(8, String.valueOf(sqlDate));

                    pstmt.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inventoryUpdateBtn() {

        if (txtProductID.getText().isEmpty()
                || txtProductName.getText().isEmpty()
                || txtCategory.getSelectionModel().getSelectedItem() == null
                || txtStock.getText().isEmpty()
                || txtPrice.getText().isEmpty()
                || txtStatus.getSelectionModel().getSelectedItem() == null
                || Data.path == null || Data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String path = Data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE products SET "
                    + "productID = '" + txtProductID.getText() + "', productName = '"
                    + txtProductName.getText() + "', category = '"
                    + txtCategory.getSelectionModel().getSelectedItem() + "', stock = '"
                    + txtStock.getText() + "', price = '"
                    + txtPrice.getText() + "', status = '"
                    + txtStatus.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "', date = '"
                    + Data.date + "' WHERE id = " + Data.id;

            connect = DatabaseConnection.connectDB();

            try {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE the Product ID: " + txtProductID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    pstmt = connect.prepareStatement(updateData);
                    pstmt.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!!");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inventoryDeleteBtn() {
        if (Data.id == 0) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE the Product ID: " + txtProductID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM products WHERE id = " + Data.id;
                try {
                    pstmt = connect.prepareStatement(deleteData);
                    pstmt.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }

    public void inventoryClearBtn() {

        txtProductID.setText("");
        txtProductName.setText("");
        txtCategory.getSelectionModel().clearSelection();
        txtStock.setText("");
        txtPrice.setText("");
        txtStatus.getSelectionModel().clearSelection();
        Data.path = "";
        Data.id = 0;
        inventoryImageView.setImage(null);

    }

    public void inventoryImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(formDesign.getScene().getWindow());

        if (file != null) {

            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 200, 201, false, true);

            inventoryImageView.setImage(image);
        }
    }

    public void inventorySelectData() {

        ProductMod productMod = inventoryTableView.getSelectionModel().getSelectedItem();
        int num = inventoryTableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        txtProductID.setText(productMod.getProductId());
        txtProductName.setText(productMod.getProductName());
        txtStock.setText(String.valueOf(productMod.getStock()));
        txtPrice.setText(String.valueOf(productMod.getPrice()));

        Data.path = productMod.getImage();

        String path = "File:" + productMod.getImage();
        Data.date = String.valueOf(productMod.getDate());
        Data.id = productMod.getId();

        image = new Image(path, 200, 201, false, true);
        inventoryImageView.setImage(image);
    }

    private String[] typeList = {"Meals", "Drinks"};
    public void inventoryTypeList(){

        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
    }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        txtCategory.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {

        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(statusL);
        txtStatus.setItems(listData);

    }
        public ObservableList<ProductMod> inventoryDataList() {

        ObservableList<ProductMod> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM products";

        connect = DatabaseConnection.connectDB();

        try {

            pstmt = connect.prepareStatement(sql);
            result = pstmt.executeQuery();

            ProductMod productData;

            while (result.next()) {

                productData = new ProductMod(result.getInt("id"),
                        result.getString("productID"),
                        result.getString("productName"),
                        result.getString("stock"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(productData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<ProductMod> inventoryListData;
    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventoryProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventoryProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventoryCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        inventoryStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventoryPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventoryStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventoryTableView.setItems(inventoryListData);

    }

    private double totalP;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customers WHERE customerID = " + cID;

        connect = DatabaseConnection.connectDB();

        try {

            pstmt = connect.prepareStatement(total);
            result = pstmt.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<ProductMod> menuGetData(){

        String sql = "SELECT * FROM products";

        ObservableList<ProductMod> prodListData = FXCollections.observableArrayList();
        connect = DatabaseConnection.connectDB();

        try {
            pstmt = connect.prepareStatement(sql);
            result = pstmt.executeQuery();

            ProductMod product;

            while (result.next()) {
                product = new ProductMod(result.getInt("id"),
                        result.getString("productID"),
                        result.getString("productName"),
                        result.getString("category"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));

                prodListData.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prodListData;
    }

    public void menuDisplayCard() {

        productBoxData.clear();
        productBoxData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menuGridPane.getChildren().clear();
        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        for (int q = 0; q < productBoxData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/fxml/productBox.fxml"));
                AnchorPane pane = load.load();
                ProductBoxController cardC = load.getController();
                cardC.setData(productBoxData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int cID;
    public void customerID() {

        String sql = "SELECT MAX(customerID) FROM customers";
        connect = DatabaseConnection.connectDB();

        try {
            pstmt = connect.prepareStatement(sql);
            result = pstmt.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customerID)");
            }

            String checkCID = "SELECT MAX(customerID) FROM receipt";
            pstmt = connect.prepareStatement(checkCID);
            result = pstmt.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customerID)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            Data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event){

        if(event.getSource() == dashBoardBtn) {
            dashboardForm.setVisible(true);
            inventoryForm.setVisible(false);
            menuForm.setVisible(false);

        }else if (event.getSource() == inventoryBtn){
                dashboardForm.setVisible(false);
                inventoryForm.setVisible(true);
                menuForm.setVisible(false);

                inventoryTypeList();
                inventoryStatusList();
                inventoryShowData();

        }else if (event.getSource() == menuBtn){
            dashboardForm.setVisible(false);
            inventoryForm.setVisible(false);
            menuForm.setVisible(true);

            menuDisplayCard();

        }

    }
    public void logout() {

        try {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logoutBtn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Mcdonald's POS System");

                stage.setScene(scene);
                stage.show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayUsername() {

        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        username.setText(user);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
        menuDisplayCard();

    }
}
