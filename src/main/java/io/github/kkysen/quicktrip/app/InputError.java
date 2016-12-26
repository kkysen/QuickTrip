package io.github.kkysen.quicktrip.app;

import lombok.Getter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InputError extends Exception {
    
    private static final long serialVersionUID = 3352045631378935293L;
    
    private final @Getter Alert errorDialog;
    
    public InputError(final String error, final String msg) {
        super(error + ": " + msg);
        errorDialog = new Alert(AlertType.ERROR);
        errorDialog.setTitle(error);
        errorDialog.setHeaderText(msg);
        errorDialog.setContentText(msg);
    }
    
}