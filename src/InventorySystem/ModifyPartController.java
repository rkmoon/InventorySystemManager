package InventorySystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
/**
 *
 * @author Ryan Moon
 */



public class ModifyPartController {

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private Label machineCompanyLabel;

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
    private TextField machineIdText;

    @FXML
    private TextField minText;

    @FXML
    private Button cancelButton;

    private Part origPart;
    private Part newPart;
    private int index;

    private boolean isInHouse;


    /**
     * Detects changes in the radio button and changes the name of the field appropriately.
     */
    @FXML
    void changeSource() {
        if (outsourcedRadio.isSelected()) {
            machineCompanyLabel.setText("Company Name");
            isInHouse = false;
        } else {
            machineCompanyLabel.setText("Machine ID");
            isInHouse = true;
        }
    }

    /**
     * Activated when the Save button is pressed. It goes through all of the fields
     * and verifies if each has the valid inputs. It then creates a new part and modifies the one in the Inventory.
     * @throws Exception Failed to open error window.
     */
    @FXML
    void saveInfo() throws Exception {
        String name = "";
        int inv = 0;
        double priceCost = 0;
        int max = 0;
        int min = 0;
        int machineID = 0;
        String compName = "";
        boolean isInfoValid = true;


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
                e.printStackTrace();
            }
            System.out.print(machineID);
            newPart = new InHouse(origPart.getId(), name, priceCost, inv, min, max, machineID);
        } else {
            compName = machineIdText.getText();
            newPart = new Outsourced(origPart.getId(), name, priceCost, inv, min, max, compName);
        }
        if (verifyInfo(newPart) && isInfoValid) {
            Inventory.updatePart(index, newPart);
            windowClose();
        }
    }

    /**
     * Used to close the window after a successful save or if cancelled.
     */
    @FXML
    void windowClose() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
     * Sets that the part being modified is In-House.
     * @param part The part to change.
     */
    public void setInHousePartValues(InHouse part) {
        isInHouse = true;
        inHouseRadio.setSelected(true);
        machineCompanyLabel.setText("Machine Id");
        setInitialPartValues(part);
        machineIdText.setText(String.valueOf(part.getMachineId()));
    }

    /**
     * Sets that the part being modified is Outsourced
     * @param part The part to change.
     */
    public void setOutsourcedPartValues(Outsourced part) {
        isInHouse = false;
        outsourcedRadio.setSelected(true);
        machineCompanyLabel.setText("Company Name");
        setInitialPartValues(part);
        machineIdText.setText(part.getCompanyName());
    }

    /**
     * Fills the fields with the part being modified's info.
     * @param part The part being modified
     */
    void setInitialPartValues(Part part) {
        origPart = part;
        index = Inventory.getAllParts().indexOf(origPart);
        idText.setText(String.valueOf(part.getId()));
        nameText.setText(part.getName());
        invText.setText(String.valueOf(part.getStock()));
        priceCostText.setText(String.valueOf(part.getPrice()));
        maxText.setText(String.valueOf(part.getMax()));
        minText.setText(String.valueOf(part.getMin()));
    }




}
