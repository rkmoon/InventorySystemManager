package InventorySystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
/**
 *
 * @author Ryan Moon
 */
public class ConfirmWindowController {
    @FXML
    private Label confirmMessage;

    private Part partToDelete;

    private Product prodToDelete;

    private TableView<Part> partTable;

    /**
     * Enum to determine what is being deleting by the confirmation window
     */
    enum Deleting{
        PART,
        PRODUCT,
        ADDASSPART,
        MODIFYASSPART
    }
    Deleting whatToDelete;

    /**
     * @param message message to display on confirmation window
     * @param part part to delete
     */
    public void partDelete(String message, Part part) {
        whatToDelete = Deleting.PART;
        confirmMessage.setText(message);
        partToDelete = part;

    }

    /**
     * @param message message to display on confirmation window
     * @param product product to delete
     */
    public void prodDelete(String message, Product product) {
        whatToDelete = Deleting.PRODUCT;
        confirmMessage.setText(message);
        prodToDelete = product;
    }

    /**
     * @param message message to display on confirmation window
     * @param part associated part to delete from table
     * @param assPartTable associated table to delete part from
     */
    public void addAssPartDelete(String message, Part part, TableView<Part> assPartTable){
        whatToDelete = Deleting.ADDASSPART;
        partTable = assPartTable;
        confirmMessage.setText(message);
        partToDelete = part;
    }

    /**
     * @param message message to display on confirmation window
     * @param part associated part to delete from product
     * @param product product to delete associated part from
     */
    public void modifyAssPartDelete(String message, Part part, Product product){
        whatToDelete = Deleting.MODIFYASSPART;
        confirmMessage.setText(message);
        partToDelete = part;
        prodToDelete = product;
    }

    /**
     * Takes the value of enum to determine which deletion process is happening.
     */
    public void confirmDelete() {
        if (whatToDelete == Deleting.PART) {
            Inventory.deletePart(partToDelete);
        }
        else if(whatToDelete == Deleting.PRODUCT) {
            Inventory.deleteProduct(prodToDelete);
        }
        else if(whatToDelete == Deleting.ADDASSPART){
            partTable.getItems().remove(partToDelete);
        }
        else if(whatToDelete == Deleting.MODIFYASSPART){
            prodToDelete.deleteAssociatedPart(partToDelete);
        }
        windowClose();
    }

    /**
     * Activated when cancelled
     */
    public void doNotDelete() {
        windowClose();
    }


    /**
     * Closes window on deletion or cancellation.
     */
    public void windowClose() {
        Stage stage = (Stage) confirmMessage.getScene().getWindow();
        stage.close();
    }

}
