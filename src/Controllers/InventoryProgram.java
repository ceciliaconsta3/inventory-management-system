package Controllers;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryProgram extends Application {
    /**
     */

    @Override
    public void init(){
        System.out.println("Starting...");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        // https://stackoverflow.com/questions/35282724/exception-in-application-start-method-java-lang-reflect-invocationtargetexceptio
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Inventory Management Program");
        primaryStage.setScene(new Scene(root, 886, 413));
        primaryStage.setResizable(false);
        primaryStage.show();

        // https://www.ign.com/wikis/final-fantasy-vii/Materia
        Inventory.addPart(new InHouse(Inventory.getPartIdCount(), 78,1,99,"Fire",600.00, 1));
        Inventory.addPart(new InHouse(Inventory.getPartIdCount(), 90,1,99,"Ice",600.00, 2));
        Inventory.addPart(new InHouse(Inventory.getPartIdCount(), 55,1,99,"Lightning",600.00, 3));
        Inventory.addPart(new Outsourced(Inventory.getPartIdCount(), 12,1,99,"Cure",750.00,  "Shinra"));
        Inventory.addPart(new Outsourced(Inventory.getPartIdCount(), 12,1,99,"Heal",1500.00, "Shinra"));

        // http://www.uffsite.net/ff7/weapons.php
        Inventory.addProduct(new Product(Inventory.getProductIdCount(),15,1,99,"Buster Sword",0.00));
        Inventory.addProduct(new Product(Inventory.getProductIdCount(),18,1,99,"Hardedge",1500.00));
        Inventory.addProduct(new Product(Inventory.getProductIdCount(),25,1,99,"Rocket Punch",3200.00));
        Inventory.addProduct(new Product(Inventory.getProductIdCount(),55,1,99,"Platinum Fist",2700.00));
        Inventory.addProduct(new Product(Inventory.getProductIdCount(),95,1,99,"Aurora Rod",5800.00));
    }

    public static void main(String[] args) {

//        InHouse part1 = new InHouse(1, 1000,1,99,"Hard Drive",123.99, 01);
//        Outsourced part2 = new Outsourced(2, 1000, 1, 99, "Graphics Card", 500.00, "Razer");
//
//            Part part1 = new InHouse(1, 1000,1,99,"Hard Drive",123.99, 01);
//            Part part2 = new InHouse(1, 1000,1,99,"Hard Drive",123.99, 01);
//            Inventory.addPart(part1);
//            Inventory.addPart(part2);
//
//            Product product1 = new Product(2, 1, 1, 99, "Desktop", 1000.00);
//            product1.addAssociatedPart(part1);
//            product1.addAssociatedPart(part2);
//            Inventory.addProduct(product1);
//
//        Inventory inv = new Inventory(2,1);
//        addTestData(inv);
        // Single selection Table Views
        // Objects maintained in an observable list
        launch(args);
    }






}
