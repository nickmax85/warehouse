package com.application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.application.model.Abteilung;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AbteilungEditDialogController implements Initializable {

	private ResourceBundle resources;

	@FXML
	private TextField artField;

	private Stage dialogStage;
	private Abteilung abteilung;

	private boolean okClicked;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Abteilung abteilung) {
		this.abteilung = abteilung;

		artField.setText(abteilung.getName());

	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {

		System.out.println("OK");
		if (isInputValid()) {
			abteilung.setName(artField.getText());

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (artField.getText() == null || artField.getText().length() == 0) {
			errorMessage += "Keine gültige Art!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message
			// Dialogs.showErrorDialog(dialogStage, errorMessage, "Please
			// correct invalid fields", "Invalid Fields");
			return false;
		}
	}

}
