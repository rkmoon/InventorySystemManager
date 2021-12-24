package InventorySystem;

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

public class MainWindowController {

    @FXML
    private TextField searchPartText;

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
    private TextField searchProductText;

    @FXML
    private TableView<Product> prodTable;

    @FXML
    private TableColumn<Product, Integer> prodIdCol;

    @FXML
    private TableColumn<Product, String> prodNameCol;

    @FXML
    private TableColumn<Product, Integer> prodInvCol;

    @FXML
    private TableColumn<Product, Double> prodPriceCol;

    @FXML
    private Button exitButton;

    /**
     * <p>Initializes the Part and Product tables and populates them.
     * It also sets up the filtered lists to allow for searches of either name or ID
     * using the search field. A message will show in either table if there are no search results.</p>
     *
     * <p>Setting up the table to show the data caused me many logical and runtime issues. It began with giving
     * me a null reference every time I tried to start the program because I didn't initialize the parts and
     * products lists in the Inventory class as ObservableArrays. After I did that I couldn't get the columns to display
     * any information until I set each one up as a Cellvalue Factory.</p>
     *
     * <p>An extension that could be added to this program is decrementing the part number each time one is added to the
     * product. Further extending on that, parts would not be allowed to be added to products if the number of parts in
     * the inventory would go below the minimum number of parts allowed.</p>
     */
    @FXML
    public void initialize() {

        //set up part table
        FilteredList<Part> filteredParts = new FilteredList<>((Inventory.getAllParts()));

        partIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory("price"));

        searchPartText.textProperty().addListener((observable, oldValue, newValue) -> {
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

        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedParts);


        //set up product table
        FilteredList<Product> filteredProducts = new FilteredList<>(Inventory.getAllProducts(), b -> true);

        searchProductText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(product.getId()).contains(lowerCaseFilter)) {
                    return true;
                } else {
                    prodTable.setPlaceholder(new Label("No Results Found"));
                    return false;
                }
            });
        });
        prodIdCol.setCellValueFactory(new PropertyValueFactory("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory("price"));

        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(prodTable.comparatorProperty());
        prodTable.setItems(sortedProducts);

    }

    /**
     * Activated by the Add button under the part table. It opens the Add Part Window.
     *
     * @throws Exception Failed to open window.
     */
    public void openAddPartMenu() throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Activated by the Modify button under the part table.
     * It opens the Modify Part Window based on the part chosen
     * in the table. It will open an error window if no part is chosen. Based on if
     * the part is In House or Outsources, it will open the appropriate modify part
     * window.
     *
     * @throws Exception Failed to open window
     */
    public void openModifyPartMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
        Parent root = loader.load();

        ModifyPartController modifyPartController = loader.<ModifyPartController>getController();
        Part partToModify = partTable.getSelectionModel().getSelectedItem();
        if (partToModify != null) {
            if (partToModify instanceof InHouse) {
                modifyPartController.setInHousePartValues((InHouse) partToModify);
            } else if (partToModify instanceof Outsourced) {
                modifyPartController.setOutsourcedPartValues((Outsourced) partToModify);
            }

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        } else {
            openErrorWin(ErrorMsg.getNoProductSelected());
        }
    }
    /**
     * Activated by the Delete button under the part table.
     * It deletes the part currently selected. If there is no part selected an error
     * popup will display. It also checks to see if any parts
     * remain in the inventory. If there are no parts remaining, the table will show a message of "no content"
     * to avoid confusion with not seeing any search results.
     *
     * @throws Exception Failed to open error window.
     */
    public void deletePart() throws Exception {
        Part partToDelete = partTable.getSelectionModel().getSelectedItem();
        if (partToDelete != null) {
            openPartConfirmWin(ErrorMsg.getConfirmDelete() + partToDelete.getName(), partToDelete);
            if (Inventory.getAllParts().size() == 0) {
                prodTable.setPlaceholder(new Label("No Content in Table"));
            }
        } else {
            openErrorWin(ErrorMsg.getNoPartSelected());
        }
    }

    /**
     * Activated by the Add button under the product table. It opens the App Product Window.
     *
     * @throws Exception Failed to open window.
     */
    public void openAddProductMenu() throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Activated by the Modify button under the product table.
     * It opens the Modify Product Window based on the product selected in the table. An
     * error popup will occur if no product is selected.
     *
     * @throws Exception Failed to open window.
     */
    public void openModifyProductMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
        Parent root = loader.load();

        ModifyProductController modifyProductController = loader.<ModifyProductController>getController();
        Product prodToModify = prodTable.getSelectionModel().getSelectedItem();
        if (prodToModify != null) {
            modifyProductController.setInitProdValues(prodToModify);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        } else {
            openErrorWin(ErrorMsg.getNoProductSelected());
        }

    }

    /**
     * Activated by the delete button under the product table.
     * It deletes the currently selected product. If there is no product selected, an
     * error popup will display informing the user. If the product has more than 0 parts assigns, an error
     * message will display informing the user. If there are no products left in the inventory, a message
     * of no content will display in the table in order to prevent confusion of the user thinking there
     * are no search results.
     * @throws Exception Error window failed to open.
     */
    public void deleteProduct() throws Exception {
        Product productToDelete = prodTable.getSelectionModel().getSelectedItem();
        if(productToDelete != null) {
            if (productToDelete.getAllAssociatedParts().size() > 0) {
                openErrorWin(ErrorMsg.getProductHasParts());
            } else {
                openProductConfirmWin(ErrorMsg.getConfirmDelete() + productToDelete.getName(), productToDelete);
            }
            if (Inventory.getAllProducts().size() == 0) {
                prodTable.setPlaceholder(new Label("No Content in Table"));
            }
        }
        else{
            openErrorWin(ErrorMsg.getNoProductSelected());
        }
    }

    /**
     * Activated by the exit button. It closes the application.
     */
    public void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Used to open and display any error messages that may occur.
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
     * Opens a confirmation window to confirm deletion of a part.
     * @param message The message to ask the user if they want to confirm deletion of that part.
     * @param partToDelete The part that will be deleted from the inventory.
     * @throws Exception Failure to open the window.
     */
    public void openPartConfirmWin(String message, Part partToDelete) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmWindow.fxml"));
        Parent root = loader.load();

        ConfirmWindowController confirmWindowController = loader.<ConfirmWindowController>getController();
        confirmWindowController.partDelete(message, partToDelete);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Confirm Delete");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Opens a confirmation window to confirm deletion of a product.
     * @param message The message to display to the user confirming deletion of that product.
     * @param productToDelete The product that will be deleted.
     * @throws Exception Failure to open window.
     */
    public void openProductConfirmWin(String message, Product productToDelete) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmWindow.fxml"));
        Parent root = loader.load();

        ConfirmWindowController confirmWindowController = loader.<ConfirmWindowController>getController();
        confirmWindowController.prodDelete(message, productToDelete);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Confirm Delete");
        stage.setScene(scene);
        stage.showAndWait();
    }

}
