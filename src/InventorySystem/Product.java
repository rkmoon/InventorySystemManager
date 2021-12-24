package InventorySystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author Ryan Moon
 */

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds an associated part to the product
     * @param part part to add to the product
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes the associated part from the product
     * @param selectedAssociatedPart part to delete
     * @return true if part is deleted, false if not
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        boolean isPartDeleted = false;
        int numItems = associatedParts.size();
        for (int i = 0; i < numItems; i++) {
            if (associatedParts.get(i) == selectedAssociatedPart) {
                associatedParts.remove(i);
                isPartDeleted = true;
            }
        }
        return isPartDeleted;
    }

    /**
     * @return all parts associated with the product.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


}
