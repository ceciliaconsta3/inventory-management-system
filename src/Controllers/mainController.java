package Controllers;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;


public class mainController implements Initializable {
    /**
     * @param args the command line arguments®
     * @author Cecilia Constantine
     */

    public TableView mainParts;
    public TableColumn partID;
    public TableColumn partName;
    public TableColumn partInventoryLevel;
    public TableColumn partPricePerUnit;
    public TableView mainProducts;
    public TableColumn productID;
    public TableColumn productName;
    public TableColumn productInventoryLevel;
    public TableColumn productPricePerUnit;
    public Button addParts;
    public Button modifyParts;
    public Button deleteParts;
    public Button addProducts;
    public Button modifyProducts;
    public Button deleteProducts;
    public TextField mainPartsTextField;
    public TextField mainProductsTextField;
    public Label mainPartsLabel;
    public Label mainProductsLabel;


    @FXML
    private AnchorPane main;

    @FXML
    public void showAddPartScreen(MouseEvent event) throws IOException {

        // https://www.youtube.com/watch?v=LDVztNtJWOo
            Parent root = FXMLLoader.load(getClass().getResource("/Views/addPart.fxml"));
            Scene addPartStage = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(addPartStage);
            primaryStage.setResizable(false);
            primaryStage.show();
//            primaryStage.setOnHiding(event1 -> {
//                System.out.println("testing");
//                mainParts.setItems(Inventory.getAllParts());
//                mainParts.getSortOrder().add(partID);
//            });
    }
    @FXML
    public void showAddProductScreen(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/addProduct.fxml"));
        Scene addProductStage = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(addProductStage);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    public void deleteParts(MouseEvent event) {
        if (mainParts.getSelectionModel().getSelectedItem().getClass().getName().contains("Model.InHouse")){
            InHouse selectedRow = (InHouse) mainParts.getSelectionModel().getSelectedItem();

            if( selectedRow == null){ return; }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Deletion Check");
            confirmAlert.setContentText("Are you sure you want to delete this?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.get() == ButtonType.OK){
                Inventory.deletePart(selectedRow);
            } else {
                return;
            }

        } else {
            Outsourced selectedRow2 = (Outsourced) mainParts.getSelectionModel().getSelectedItem();

            if( selectedRow2 == null){ return; }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Deletion Check");
            confirmAlert.setContentText("Are you sure you want to delete this?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.get() == ButtonType.OK){
                Inventory.deletePart(selectedRow2);
            } else {
                return;
            }
        }
    }
    @FXML
    public void deleteProducts(MouseEvent event){
        Product selectedRow = (Product)mainProducts.getSelectionModel().getSelectedItem();

        if(selectedRow == null){
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Deletion Check");
        confirmAlert.setContentText("Are you sure you want to delete this?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.OK){
            Inventory.deleteProduct(selectedRow);
        } else {
            return;
        }

    }
    @FXML
    public void showModifyPartScreen(MouseEvent event) throws IOException {
        Part p = (Part)mainParts.getSelectionModel().getSelectedItem();
        if (p == null){
            return;
        }
        ModifyPartController.partData = p;

        Parent root = FXMLLoader.load(getClass().getResource("/Views/modifyPart.fxml"));
        Scene modifyPartScene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(modifyPartScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    public void showModifyProductScreen(MouseEvent event) throws IOException {
        Product pD = (Product)mainProducts.getSelectionModel().getSelectedItem();
        if (pD == null){
            return;
        }
        ModifyProductController.productData = pD;


        Parent root = FXMLLoader.load(getClass().getResource("/Views/modifyProduct.fxml"));
        Scene modifyProductScene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(modifyProductScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    public void exitInventorySystem(MouseEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Exit Check");
        confirmAlert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.OK){
            exit();
        } else {
            return;
        }
    }
    @FXML
    public void searchParts(MouseEvent event) {
        String userInput = mainPartsTextField.getText();
        ObservableList<Part> allParts = searchByPartName(userInput);

        if (allParts.size() == 0){
            try {
                int partID = Integer.parseInt(userInput);
                Part item = searchByPartID(partID);
                if(item != null) {
                    allParts.add(item);
                }
            } catch (NumberFormatException e) {
            }

        }
        mainParts.setItems(allParts);
        mainPartsLabel.setText(Integer.toString(allParts.size()) + " part(s) returned");
    }
    @FXML
    public void searchProducts(MouseEvent event) {
        String userInput = mainProductsTextField.getText();
        ObservableList<Product> allProducts = searchByProductName(userInput);

        if (allProducts.size() == 0){
            try {
                int productID = Integer.parseInt(userInput);
                Product item = searchByProductID(productID);
                if(item != null) {
                    allProducts.add(item);
                }
            } catch (NumberFormatException e) {
            }

        }
        mainProducts.setItems(allProducts);
        mainProductsLabel.setText(Integer.toString(allProducts.size()) + " product(s) returned");
    }

    private ObservableList<Part> searchByPartName(String partialName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        ObservableList<Part> parts = Inventory.getAllParts();

        for (Part item : parts){
            if(item.getName().contains(partialName)){
                namedParts.add(item);
            }
        }
        return namedParts;
    }
    private Part searchByPartID(int partID) {
        ObservableList<Part> matchedID = FXCollections.observableArrayList();
        ObservableList<Part> parts = Inventory.getAllParts();

        // Regular indexed based search
        for(int i = 0; i < parts.size(); i++){
            Part item = parts.get(i);

            if (item.getId() == partID){
                return item;
            }
        }
        return null;
    }
    private ObservableList<Product> searchByProductName(String partialName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();
        ObservableList<Product> products = Inventory.getAllProducts();

        for (Product item : products){
            if(item.getName().contains(partialName)){
                namedProducts.add(item);
            }
        }
        return namedProducts;
    }
    private Product searchByProductID(int productID) {
        ObservableList<Product> matchedID = FXCollections.observableArrayList();
        ObservableList<Product> products = Inventory.getAllProducts();

        // Regular indexed based search
        for(int i = 0; i < products.size(); i++){
            Product item = products.get(i);

            if (item.getId() == productID){
                return item;
            }
        }
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){

        // associate list with the table
        mainParts.setItems(Inventory.getAllParts());
        mainProducts.setItems(Inventory.getAllProducts());

        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}

