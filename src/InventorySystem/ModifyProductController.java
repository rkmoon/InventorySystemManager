package InventorySystem;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
/**
 *
 * @author Ryan Moon
 */

public class ModifyProductController {
    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField invText;

    @FXML
    private TextField priceCostText;

    @FXML
    private TextField maxText;

    @FXML
    private TextField minText;

    @FXML
    private TextField searchText;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> assPartTable;

    @FXML
    private TableColumn<Part, Integer> assPartIdCol;

    @FXML
    private TableColumn<Part, String> assPartNameCol;

    @FXML
    private TableColumn<Part, Integer> assPartInvCol;

    @FXML
    private TableColumn<Part, Double> assPartPriceCol;

    @FXML
    private Button cancelButton;

    private Product origProduct;
    private int index;

    /**
     * Initializes the part tables and the associated part tables. It also sets up the filtered lists
     * to allow for searches of either name or ID using the search field. A message will show in the table if
     * there are no search results.
     */
    @FXML
    public void initialize() {
        //set up part table and columns
        partIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory("price"));

        //Set up table for filtering by search
        FilteredList<Part> filteredParts = new FilteredList<>((Inventory.getAllParts()), b -> true);
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(part -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(part.getId()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    partTable.setPlaceholder(new Label("No Results Found"));
                    return false;
                }
            });
        });

        //Finish populating table with sorted list
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedParts);

        //Populate associated parts table
        assPartIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        assPartNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        assPartInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        assPartPriceCol.setCellValueFactory(new PropertyValueFactory("price"));

    }

    /**
     * Activated when the Save button is pressed. It goes through all of the fields
     * and verifies if each has the valid inputs. It then creates a new product with the selected parts
     * and updates the original part in the inventory.
     * @throws Exception Failed to open error window.
     */
    public void saveInfo() throws Exception {
        Product newProduct;
        String name = "";
        int inv = 0;
        double priceCost = 0;
        int max = 0;
        int min = 0;
        boolean isInfoValid = true;

        name = nameText.getText();

        // All these try/catches are to validate that the info is valid for each field
        try {
            inv = Integer.parseInt(invText.getText());
        } catch (NumberFormatException e) {
            openErrorWin(ErrorMsg.getInvInvalid());
            isInfoValid = false;
        }
        try {
            priceCost = Double.parseDouble(priceCostText.getText());
        } catch (NumberFormatException e) {
            openErrorWin(ErrorMsg.getPriceInvalid());
            isInfoValid = false;
        }
        try {
            max = Integer.parseInt(maxText.getText());
        } catch (NumberFormatException e) {
            openErrorWin(ErrorMsg.getMaxInvalid());
            isInfoValid = false;
        }
        try {
            min = Integer.parseInt(minText.getText());
        } catch (NumberFormatException e) {
            openErrorWin(ErrorMsg.getMinInvalid());
            isInfoValid = false;
        }
        ObservableList<Part> assParts = assPartTable.getItems();
        newProduct = new Product(origProduct.getId(), name, priceCost, inv, min, max);

        // Run through each part on the associated parts table and add it to the product
        if (assParts != null) {
            for (Part part : assParts) {
                newProduct.addAssociatedPart(part);
            }
        }
        // Just validating and verifying info before updating the product
        // Verify info will give info if info is not valid
        if (verifyInfo(newProduct) && isInfoValid) {
            Inventory.updateProduct(index, newProduct);
            windowClose();
        }
    }

    /**
     * Sets the values of the product to be modified to populate the fields.
     * @param prod Product to be modified.
     */
    public void setInitProdValues(Product prod) {
        origProduct = prod;
        index = Inventory.getAllProducts().indexOf(origProduct);
        assPartTable.setItems(origProduct.getAllAssociatedParts());
        idText.setText(String.valueOf(origProduct.getId()));
        nameText.setText(origProduct.getName());
        invText.setText(String.valueOf(origProduct.getStock()));
        priceCostText.setText(String.valueOf(origProduct.getPrice()));
        maxText.setText(String.valueOf(origProduct.getMax()));
        minText.setText(String.valueOf(origProduct.getMin()));

    }
    /**
     * Checks if the min is less than the max, and if the inventory value is inbetween min and max.
     * @param prodToCheck The part to validate.
     * @return True if all checks are valid, false if any are not valid.
     * @throws Exception Failure to open error window.
     */
    public boolean verifyInfo(Product prodToCheck) throws Exception {
        boolean isInfoValid = true;

        if (prodToCheck.getMin() > prodToCheck.getMax()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getMinMax());
        }
        if (prodToCheck.getStock() < prodToCheck.getMin()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getInvLow());
        }
        if (prodToCheck.getStock() > prodToCheck.getMax()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getInvHigh());
        }
        return isInfoValid;
    }

    /**
     * Adds a part to the associated part table to be added to the product. It checks to make sure a part
     * is selected.
     * @throws Exception Failure to open error window.
     */
    public void addPart() throws Exception {
        if (partTable.getSelectionModel().getSelectedItem() != null) {
            origProduct.addAssociatedPart(partTable.getSelectionModel().getSelectedItem());
            System.out.print(origProduct.getAllAssociatedParts().size());
        } else {
            openErrorWin(ErrorMsg.getNoPartSelected());
        }
    }

    /**
     * Removes part from the associated parts table so it will not be added to the product on save. A confirmation
     * window will popup to confirm the user wants to remove that part.
     * @throws Exception Failure to open confirmation window.
     */
    public void removePart() throws Exception {
        if (assPartTable.getSelectionModel().getSelectedItem() != null) {
            Part itemToRemove = assPartTable.getSelectionModel().getSelectedItem();
            openPartConfirmWin(ErrorMsg.getConfirmDelete() + itemToRemove.getName(), itemToRemove,origProduct);
        } else {
            openErrorWin(ErrorMsg.getNoPartSelected());
        }
    }
    /**
     * The openErrorWin method is used to open and display any error messages that may occur.
     * @param message The message to display on the error window.
     * @throws Exception Failed to open window.
     */
    public void openErrorWin(String message) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorWindow.fxml"));
        Parent root = loader.load();

        ErrorWindowController errorWindowController = loader.<ErrorWindowController>getController();
        errorWindowController.setErrMessage(message);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Error");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Opens popup window to confirm that the user wants to remove the part. Upon confirmation, the part will be removed
     * from the product.
     * @param message Message displayed in popup.
     * @param partToDelete The part that will be removed from the associated parts table.
     * @param product The product to remove the part from.
     * @throws Exception Failure to open window.
     */
    public void openPartConfirmWin(String message, Part partToDelete, Product product) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmWindow.fxml"));
        Parent root = loader.load();

        ConfirmWindowController confirmWindowController = loader.<ConfirmWindowController>getController();
        confirmWindowController.modifyAssPartDelete(message, partToDelete, product);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Confirm Delete");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Closes the window on a successful save or cancellation.
     */
    public void windowClose() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
