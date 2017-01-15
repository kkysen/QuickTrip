package io.github.kkysen.quicktrip.app.input;

import lombok.Getter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@Getter
public class InputError extends Exception {
    
    private static final long serialVersionUID = 3352045631378935293L;
    
    private final String error;
    protected String msg;
    
    private final Alert errorDialog;
    
    public InputError(final String error, final String msg) {
        super(error + ": " + msg);
        this.error = error;
        this.msg = msg;
        errorDialog = new Alert(AlertType.ERROR);
        errorDialog.setTitle(error);
        errorDialog.setHeaderText(msg);
        errorDialog.setContentText(msg);
    }
    
}