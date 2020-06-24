package com.application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.application.Main;

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

	private ResourceBundle resources;

	public final static String STYLESHEET = "/com/application/resource/css/MyTheme.css";
	public final static String APP_ICON = "com/application/resource/icons/data48.png";

	private Main main;

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
	private void handleDataOverview() {

		main.showDataOverview();

	}

	@FXML
	private void handleHomeScreen() {

		main.showHomeScreen();

	}

	@FXML
	private void handleSettings() {

		main.showSettingsDialog();

	}

	@FXML
	private void handleAbout() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("about"));
		alert.setHeaderText(resources.getString("appname"));
		alert.setContentText(resources.getString("programer"));

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(getClass().getResource(RootLayoutController.STYLESHEET).toExternalForm());
		alert.show();

	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

}