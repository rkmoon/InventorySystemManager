package InventorySystem;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 *
 * @author Ryan Moon
 */

public class AddPartController {

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private TextField nameText;

    @FXML
    private TextField invText;

    @FXML
    private TextField priceCostText;

    @FXML
    private TextField maxText;

    @FXML
    private TextField machineIdText;

    @FXML
    private TextField minText;

    @FXML
    private Button cancelButton;

    @FXML
    private Label machineCompanyLabel;

    private boolean isInHouse;

    /**
     * Defaults isInHouse to true to match the default on the window.
     */
    @FXML
    public void initialize() {
        isInHouse = true;
    }

    /**
     * Changes the label to the appropriate label if the radio button is switched.
     */
    public void changeSource() {
        if (outsourcedRadio.isSelected()) {
            machineCompanyLabel.setText("Company Name");
            isInHouse = false;
        } else {
            machineCompanyLabel.setText("Machine ID");
            isInHouse = true;
        }
    }

    /**
     * Used on a successful save or cancellation.
     */
    public void windowClose() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
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
     * Checks if the min is less than the max, and if the inventory value is inbetween min and max.
     * @param partToCheck The part to validate.
     * @return True if all checks are valid, false if any are not valid.
     * @throws Exception Failure to open error window.
     */
    private boolean verifyInfo(Part partToCheck) throws Exception {
        boolean isInfoValid = true;

        if (partToCheck.getMin() > partToCheck.getMax()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getMinMax());
        }
        if (partToCheck.getStock() < partToCheck.getMin()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getInvLow());
        }
        if (partToCheck.getStock() > partToCheck.getMax()) {
            isInfoValid = false;
            openErrorWin(ErrorMsg.getInvHigh());
        }
        return isInfoValid;
    }

    /**
     * Activated when the Save button is pressed. It goes through all of the fields
     * and verifies if each has the valid inputs. It then creates a new part and adds it to the Inventory.
     * @throws Exception Failed to open error window.
     */
    public void saveInfo() throws Exception {
        Part newPart;
        boolean isInfoValid = true;
        int highId = 0;
        int id;
        String name = "";
        int inv = 0;
        double priceCost = 0;
        int max = 0;
        int min = 0;
        int machineID = 0;
        String compName = "";

        if (Inventory.getAllParts() == null) {
            id = 1;
        } else {
            ObservableList<Part> allParts = Inventory.getAllParts();
            for (Part part : allParts) {
                int tempID = part.getId();
                if (tempID > highId) {
                    highId = tempID;
                }
            }
            id = highId + 1;
        }

        name = nameText.getText();

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
        if (isInHouse) {
            try {
                machineID = Integer.parseInt(machineIdText.getText());
            } catch (NumberFormatException e) {
                openErrorWin(ErrorMsg.getMachineIDInvalid());
                isInfoValid = false;
            }
            System.out.print(machineID);
            newPart = new InHouse(id, name, priceCost, inv, min, max, machineID);
        } else {
            try {
                compName = machineIdText.getText();
            } catch (Exception e) {
                openErrorWin(ErrorMsg.getCompNameInvalid());
                isInfoValid = false;
            }
            newPart = new Outsourced(id, name, priceCost, inv, min, max, compName);
        }
        if (verifyInfo(newPart) && isInfoValid) {
            Inventory.addPart(newPart);
            windowClose();
        }

    }

}

