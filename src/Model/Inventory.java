package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {

    static private ObservableList <Part> allParts = FXCollections.observableArrayList();
    static private ObservableList <Product> allProducts = FXCollections.observableArrayList();
    static private int partIdCount = 0;
    static private int productIdCount = 0;

//    public Inventory(ObservableList<Part> allParts, ObservableList<Product> allProducts) {
//        this.allParts = allParts;
//        this.allProducts = allProducts;
//    }

    static public ObservableList<Part> getAllParts() {
        return allParts;
    }

    static public ObservableList<Product> getAllProducts(){
        return allProducts;
    }
//    static public void setAllParts(ObservableList<Part> allParts) {
//        this.allParts = allParts;
//    }
//
//    public void setAllProducts(ObservableList<Product> allProducts) {
//        this.allProducts = allProducts;
//    }

    static public void addPart(Part part){
        allParts.add(part);
    }
    static public void addProduct(Product product){
        allProducts.add(product);
    }
    static public int getPartIdCount(){
        partIdCount++;
        return partIdCount;
    }
    static public int getProductIdCount(){
        productIdCount++;
        return productIdCount;
    }
//    static public Part lookupPart(int partid){
//        return Part.partid;
//    }
    static public Product lookupProduct(int index) {
        for (Product p : allProducts) {
            if (p.getId() == index) {
                return p;
            }
        } throw new IllegalStateException("help");
    }
//    static public ObservableList<Part> lookupPart(String partName){return Model.partName; } // partial match from search, returns the list
//    static public ObservableList<Part> lookupProduct(String partName){ return Model.partName; }
    static public void updatePart(int index, Part p){
            for (int i = 0; i < allParts.size(); i++){
                Part thisPart = allParts.get(i);
                if(thisPart.getId() == p.getId()){
                    allParts.set(i, p);
                    return;
                }
            }
        }
    static public void updateProduct(int index, Product product){
        Product pD = lookupProduct(index);
        int thisProductID = allProducts.indexOf(pD);
            if(pD.getId() == product.getId()){
                allProducts.set(thisProductID, product);
            }
    }
    static public void deletePart(Part part){
        allParts.remove(part);
    }
    static public void deleteProduct(Product product){
        allProducts.remove(product);
    }

}