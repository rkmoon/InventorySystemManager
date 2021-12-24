package InventorySystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author Ryan Moon
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart part to add to inventory
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * @param newProduct product to add to inventory
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * @param partId id to search for
     * @return part with the id
     */
    public static Part lookupPart(int partId){
        Part partToFind = null;
        int numItems = allParts.size();
        for(int i = 0; i < numItems; i++){
            if((allParts.get(i).getId() == partId)){
                partToFind = allParts.get(i);
            }
        }

        return partToFind;
    }

    /**
     * @param productId id to search for
     * @return product with the id
     */
    public static Product lookupProduct(int productId){
        Product productToFind = null;
        int numItems = allProducts.size();
        for(int i = 0; i < numItems; i++){
            if(allProducts.get(i).getId() == productId){
                productToFind = allProducts.get(i);
            }
        }
        return productToFind;
    }

    /**
     * @param partName name to search for
     * @return part with the name
     */
    public static ObservableList<Part> lookUpPart(String partName){
        ObservableList<Part> partsFound = null;
        int numItems = allParts.size();
        for(int i = 0; i < numItems; i++){
            if(allParts.get(i).getName().contains(partName)){
                partsFound.add(allParts.get(i));
            }
        }
        return partsFound;
    }

    /**
     * @param productName name of product to search for
     * @return product with the name
     */
    public static ObservableList<Product> lookUpProduct(String productName){
        ObservableList<Product> productsFound = null;
        int numItems = allProducts.size();
        for(int i = 0; i<numItems; i++){
            if(allProducts.get(i).getName().contains(productName)) {
                productsFound.add(allProducts.get(i));
            }
        }
        return productsFound;
    }

    /**
     * Updates a part using the index
     * @param index index to use
     * @param selectedPart part to update
     */
    public static void updatePart(int index, Part selectedPart){ allParts.set(index, selectedPart); }

    /**
     * Updates a product using the index
     * @param index index to use
     * @param newProduct product to update
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart part to delete
     * @return true if item is deleted, false if item not found
     */
    public static boolean deletePart(Part selectedPart){
        boolean isItemDeleted = false;
        int numItems = allParts.size();
        for(int i = 0; i < numItems; i++){
            if (allParts.get(i) == selectedPart) {
                allParts.remove(i);
                isItemDeleted = true;
            }
        }
        return isItemDeleted;
    }

    /**
     * @param selectedProduct product to delete
     * @return true if item is deleted, false if item not found
     */
    public static boolean deleteProduct(Product selectedProduct){
        boolean isItemDeleted = false;
        int numItems = allProducts.size();
        for(int i = 0; i < numItems; i++){
            if (allProducts.get(i) == selectedProduct) {
                allProducts.remove(i);
                isItemDeleted = true;
            }
        }
        return isItemDeleted;
    }

    /**
     * @return all parts in inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return all products in inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}


