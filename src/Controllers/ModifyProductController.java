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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/*
 * Course: Software 1
 * Topic: 
 */

public class ModifyProductController implements Initializable {


    /**
     * @param args the command line arguments
     * @author Cecilia Constantine
     */

    @FXML
    private AnchorPane modifyProduct;

    public TableView mainProducts;

    public TextField addProductID;
    public TextField addProductName;
    public TextField addProductInv;
    public TextField addProductPrice;
    public TextField addProductMax;
    public TextField addProductMin;

    public TableView allProductOwnedParts;
    public Button mainPartsSearch;
    public TextField mainPartsTextField;
    public Label modifiedAllProductOwnedPartsLabel;
    public TableColumn productPartID;
    public TableColumn productPartName;
    public TableColumn productPartInv;
    public TableColumn productPartPrice;

    public TableView modifiedProductOwnedParts;
    public TableColumn modProductPartID;
    public TableColumn modProductPartName;
    public TableColumn modProductPartInv;
    public TableColumn modProductPartPrice;



    private int thisProductID;
    public static Product productData = null;
    public static Part thisProductPartData = null;
    public static ObservableList<Part> productPartData = FXCollections.observableArrayList();
    public static ObservableList<Part> modifiedProductPartData = FXCollections.observableArrayList();
    @FXML
    void searchModifiedProduct(MouseEvent event) {
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
        allProductOwnedParts.setItems(allParts);
        modifiedAllProductOwnedPartsLabel.setText(Integer.toString(allParts.size()) + " part(s) returned");
    }

    @FXML
    public void deleteAddedProduct(MouseEvent mouseEvent) {
        Part selectedRow = (Part) modifiedProductOwnedParts.getSelectionModel().getSelectedItem();

        if (selectedRow == null){
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Deletion Check");
        confirmAlert.setContentText("Are you sure you want to delete this?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.OK){
            productPartData.remove(selectedRow);
        } else {
        return;
        }


    }

    @FXML
    public void addProductParts(MouseEvent mouseEvent) {
        try {
            Part selectedRow = (Part)allProductOwnedParts.getSelectionModel().getSelectedItem();
            if (selectedRow == null){
                return;
            }
            if (!productPartData.contains(selectedRow)){
                productPartData.add(selectedRow);
            }
            modifiedProductOwnedParts.setItems(productPartData);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Product must have at least one Part added");
                alert.setContentText("Double check your answers");
                e.printStackTrace();
                alert.showAndWait();
                // catch (NullPointerException ex) with dialog
            }
    }

    @FXML
    public void saveModifiedProduct(MouseEvent mouseEvent) throws IOException {
        try {
            String userName = addProductName.getText();
            int userStock = Integer.parseInt(addProductInv.getText());
            int userMin = Integer.parseInt(addProductMin.getText());
            int userMax = Integer.parseInt(addProductMax.getText());
            double userPrice = Double.parseDouble(addProductPrice.getText());
            double totalUserPrice = 0;

            for (Part p: productPartData){
                totalUserPrice += p.getPrice();
            }

            if (userMin > userMax){
                Alert minMaxWarning = new Alert(Alert.AlertType.WARNING);
                minMaxWarning.setTitle("Min/Max Check");
                minMaxWarning.setHeaderText("Please check your values");
                minMaxWarning.setContentText("Minimum must have a value less than maximum and Maximum must have a value greater than minimum");
                minMaxWarning.showAndWait();
            } else if (totalUserPrice > userPrice){
                Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
                userPartsAlert.setTitle("Value Check");
                userPartsAlert.setHeaderText("Please check your values");
                userPartsAlert.setContentText("Ensure that the price of a product cannot be less than the cost of the parts");
                userPartsAlert.showAndWait();
            } else if (userStock > userMax || userStock < userMin){
                Alert userInv = new Alert(Alert.AlertType.WARNING);
                userInv.setTitle("Inventory Check");
                userInv.setHeaderText("Please check your values");
                userInv.setContentText("Inventory must be between the minimum or maximum value for that Part or Product");
                userInv.showAndWait();
            } else if (modifiedProductOwnedParts.getItems().isEmpty()) {
                Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
                userPartsAlert.setTitle("Part Check");
                userPartsAlert.setHeaderText("Your Product is missing Parts");
                userPartsAlert.setContentText("Products must have at least one part");
                userPartsAlert.showAndWait();
            } else {
                thisProductID = productData.getId();
                addProductID.setText("" + thisProductID);

                Product pD = new Product(thisProductID, userStock, userMin, userMax, userName, userPrice);
                Inventory.updateProduct(thisProductID, pD);

                for (Part part : productPartData) {
                    pD.addAssociatedPart(part);
                }

                Parent root = FXMLLoader.load(getClass().getResource("/Views/main.fxml"));
                Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(new Scene(root, 886, 413));
                primaryStage.setResizable(false);
                primaryStage.show();
            }
        } catch (NumberFormatException e) {
            Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
            userPartsAlert.setTitle("Value Check");
            userPartsAlert.setHeaderText("Please check your values");
            userPartsAlert.setContentText("Ensure that your product has a price and inventory");
            userPartsAlert.showAndWait();
        }
    }

    @FXML
    public void cancelNewProduct(MouseEvent mouseEvent) throws IOException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Check");
        confirmAlert.setContentText("Are you sure you want to leave this screen?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/main.fxml"));
            Scene mainScene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } else {
            return;
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        thisProductID = productData.getId();
        addProductID.setText("" + thisProductID);
        addProductInv.setText("" + productData.getStock());
        addProductMin.setText("" + productData.getMin());
        addProductMax.setText("" + productData.getMax());
        addProductName.setText("" + productData.getName());
        addProductPrice.setText("" + productData.getPrice());

        allProductOwnedParts.setItems(Inventory.getAllParts());
        productPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPartData = productData.getAssociatedParts();
        modifiedProductOwnedParts.setItems(productPartData);
        modProductPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
