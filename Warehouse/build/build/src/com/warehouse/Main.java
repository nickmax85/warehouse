
package com.warehouse;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.warehouse.util.ApplicationProperties;
import com.warehouse.view.DataOverviewController;
import com.warehouse.view.EditController;
import com.warehouse.view.RootLayoutController;
import com.warehouse.view.SettingsController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//REVISION HISTORY
//$LastChangedDate: $
//$Revision: 21 $
//$LastChangedBy: $
//$Id: $

public class Main extends Application {

	public static String revision = "$Revision: 21 $";

	private static final Logger logger = Logger.getLogger(Main.class);
	private ResourceBundle resources = ResourceBundle.getBundle("language");

	private Stage primaryStage;
	private BorderPane rootLayout;

	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(resources.getString("appname"));

		PropertyConfigurator.configure(getClass().getClassLoader().getResource("log4j.properties"));
		ApplicationProperties.configure("application.properties", "c:/Warehouse", "application.properties");
		ApplicationProperties.getInstance().setup();

		this.primaryStage.getIcons().add(new Image(
				getClass().getClassLoader().getResourceAsStream("com/warehouse/resource/icon/barcode48.png")));

		initRootLayout();

	}

	/*
	 * @Override public void start(Stage primaryStage) {
	 * 
	 * PropertyConfigurator.configure(getClass().getClassLoader().getResource(
	 * "log4j.properties"));
	 * 
	 * try { BorderPane root = new BorderPane(); Scene scene = new Scene(root,
	 * 400, 400);
	 * scene.getStylesheets().add(getClass().getResource("application.css").
	 * toExternalForm()); primaryStage.setScene(scene); primaryStage.show(); }
	 * catch (Exception e) { e.printStackTrace(); } }
	 */
	/**
	 * Initializes the root layout and tries to load the last opened person
	 * file.
	 */
	public void initRootLayout() {
		try {

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets()
					.add(getClass().getResource("/com/warehouse/resource/css/MyTheme.css").toExternalForm());

			// primaryStage.setMaximized(true);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			showDataOverview();

			primaryStage.show();
		} catch (IOException e) {

			if (logger.isInfoEnabled()) {
				logger.error(e);
			}

			e.printStackTrace();
		}

	}

	public void showDataOverview() {

		try {

			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DataOverview.fxml"));
			loader.setResources(resources);
			AnchorPane homeScreen = (AnchorPane) loader.load();

			// Set overview into the center of root layout.
			ScrollPane sp = new ScrollPane();
			sp.setContent(homeScreen);
			rootLayout.setCenter(sp);

			// Give the controller access to the main app.
			DataOverviewController controller = loader.getController();
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean showEinstellungen() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Settings.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(resources.getString("edit"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets()
					.add(getClass().getResource("/com/warehouse/resource/css/MyTheme.css").toExternalForm());
			dialogStage.setScene(scene);

			// Set the person into the controller.
			SettingsController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void showEditDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/EditView.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setTitle(resources.getString("edit"));
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets()
					.add(getClass().getResource("/com/warehouse/resource/css/MyTheme.css").toExternalForm());
			dialogStage.setScene(scene);

			// Set the person into the controller.
			EditController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
