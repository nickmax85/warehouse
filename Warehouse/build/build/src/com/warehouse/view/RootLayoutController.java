package com.warehouse.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.warehouse.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;

/**
 * 
 * @author Markus Thaler
 */
public class RootLayoutController implements Initializable {

	// Reference to the main application
	private Main main;
	private ResourceBundle resources;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	private void handleScannerData() {

	}

	@FXML
	private void handleEditData() {

		main.showEditDialog();

	}

	@FXML
	private void handleEinstellungen() {

		main.showEinstellungen();

	}

	@FXML
	private void handleAbout() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("about"));
		alert.setHeaderText(resources.getString("appname"));
		alert.setContentText(resources.getString("programer") + "\n" + Main.revision);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(
				getClass().getClassLoader().getResource("com/warehouse/resource/css/MyTheme.css").toExternalForm());
		alert.show();

	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

}