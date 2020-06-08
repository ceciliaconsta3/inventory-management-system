package Controllers;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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


public class AddProductController implements Initializable {



    /**
     * @param args the command line arguments
     * @author Cecilia Constantine
     */


        @FXML
        private AnchorPane addProduct;

        public Pane addProductPane;

        public TableView mainParts;
        public TableColumn partID;
        public TableColumn partName;
        public TableColumn partInventoryLevel;
        public TableColumn partPricePerUnit;
        public TextField mainPartsTextField;
        public Button mainPartsSearch;
        public Label mainPartsLabel;
        public Label allProductOwnedPartsLabel;

        public TableView allProductOwnedParts;
        public TableColumn productPartID;
        public TableColumn productPartName;
        public TableColumn productPartInv;
        public TableColumn productPartPrice;
        public TextField addProductID;
        public TextField addProductName;
        public TextField addProductInv;
        public TextField addProductPrice;
        public TextField addProductMax;
        public TextField addProductMin;
        public Button addProductPart;
        public Button addProductSave;
        public Button addProductCancel;


        private int thisProductID;
        private int thisPartID;
        public Product productData = null;
        private ObservableList<Part> productPartData = FXCollections.observableArrayList();

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
            allProductOwnedPartsLabel.setText(Integer.toString(allParts.size()) + " part(s) returned");
        }

        @FXML
        void addProductParts(ActionEvent event) {
            try {
                Part selectedRow = (Part)mainParts.getSelectionModel().getSelectedItem();
                if (selectedRow == null){
                    return;
                }
                if (!productPartData.contains(selectedRow)){
                    productPartData.add(selectedRow);
                }
                allProductOwnedParts.setItems(productPartData);


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
        public void saveNewProduct(MouseEvent mouseEvent) throws IOException {
            try {
//              2. Create Product
                int userStock = Integer.parseInt(addProductInv.getText());
                int userMin = Integer.parseInt(addProductMin.getText());
                int userMax = Integer.parseInt(addProductMax.getText());
                String userName = addProductName.getText();
                double userPrice = Double.parseDouble(addProductPrice.getText());
                double totalUserPrice = 0;

                for (Part p : productPartData) {
                    totalUserPrice += p.getPrice();
                }

                if (userMin > userMax) {
                    Alert userInvAlert = new Alert(Alert.AlertType.WARNING);
                    userInvAlert.setTitle("Min/Max Check");
                    userInvAlert.setHeaderText("Please check your values");
                    userInvAlert.setContentText("Minimum must have a value less than maximum and Maximum must have a value greater than minimum");
                    userInvAlert.showAndWait();
                } else if (totalUserPrice > userPrice) {
                    Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
                    userPartsAlert.setTitle("Value Check");
                    userPartsAlert.setHeaderText("Please check your values");
                    userPartsAlert.setContentText("Ensure that the price of a product cannot be less than the cost of the parts");
                    userPartsAlert.showAndWait();
                } else if (userStock > userMax || userStock < userMin) {
                    Alert userInvAlert = new Alert(Alert.AlertType.WARNING);
                    userInvAlert.setTitle("Inventory Check");
                    userInvAlert.setHeaderText("Please check your values");
                    userInvAlert.setContentText("Inventory must be between the minimum or maximum value for that Part or Product");
                    userInvAlert.showAndWait();
                } else if (userName.isEmpty()) {
                    Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
                    userPartsAlert.setTitle("Value Check");
                    userPartsAlert.setHeaderText("Please check your values");
                    userPartsAlert.setContentText("Ensure that a product must have a name");
                    userPartsAlert.showAndWait();
                } else if (allProductOwnedParts.getItems().isEmpty()) {
                    Alert userPartsAlert = new Alert(Alert.AlertType.WARNING);
                    userPartsAlert.setTitle("Part Check");
                    userPartsAlert.setHeaderText("Your Product is missing Parts");
                    userPartsAlert.setContentText("Products must have at least one part");
                    userPartsAlert.showAndWait();
                } else {

                    thisProductID = Inventory.getProductIdCount();
                    addProductID.setText("" + thisProductID);

                    Product pD = new Product(thisProductID, userStock, userMin, userMax, userName, userPrice);

                    // 3. Associate part to product
                    for (Part part : productPartData) {
                        pD.addAssociatedPart(part);
                    }
                    pD.setAssociatedParts(productPartData);
                    Inventory.addProduct(pD);


                    //  4. Back to Main
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

        @FXML
        void deleteAddedProduct(MouseEvent event) {
            Part selectedRow = (Part) allProductOwnedParts.getSelectionModel().getSelectedItem();
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

        public void initialize(URL location, ResourceBundle resources) {

            mainParts.setItems(Inventory.getAllParts());
            allProductOwnedParts.setItems(productPartData);

            partID.setCellValueFactory(new PropertyValueFactory<>("id"));
            partName.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

            productPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
            productPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            productPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
            productPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

}


