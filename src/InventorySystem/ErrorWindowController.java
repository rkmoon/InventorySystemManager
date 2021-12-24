package InventorySystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 *
 * @author Ryan Moon
 */

public class ErrorWindowController {

    @FXML
    private Label errMessage;

    /**
     * The setErrMessage method sets the message that will be used on the Error window.
     * @param message The message to display.
     */
    public void setErrMessage(String message){
        errMessage.setText(message);
    }

    /**
     * The windowClose method is activated when the user presses the Okay button and closes the window.
     */
    public void windowClose(){
        Stage stage = (Stage) errMessage.getScene().getWindow();
        stage.close();
    }
}
