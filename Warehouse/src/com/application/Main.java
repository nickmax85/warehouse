package com.application;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.application.model.Abteilung;
import com.application.util.ApplicationProperties;
import com.application.view.AbteilungEditDialogController;
import com.application.view.DataOverviewController;
import com.application.view.HomeScreenController;
import com.application.view.RootLayoutController;
import com.application.view.SettingsController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Logger logger = Logger.getLogger(Main.class);
	private ResourceBundle resources = ResourceBundle.getBundle("language");
	private final String installPath = "c:" + File.separator + resources.getString("appname");
 
	private final String version = "V1";
	private String build = "$Revision: 49 $";
	private String temp = build.replace("$Revision:", "").replace("$", "").trim();

	private Stage primaryStage;
	private BorderPane rootLayout;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(resources.getString("appname") + " " + version + "." + temp);

		PropertyConfigurator.configure(getClass().getClassLoader().getResource("log4j.properties"));
		ApplicationProperties.configure("application.properties", installPath, "application.properties");
			
		ApplicationProperties.getInstance().setup();
		ApplicationProperties.getInstance().edit("db_host", "localhost");

		this.primaryStage
				.setMaximized(ApplicationProperties.getInstance().getProperty("start_maximized").contains("true"));
		this.primaryStage.getIcons()
				.add(new Image(getClass().getClassLoader().getResourceAsStream(RootLayoutController.APP_ICON)));

		initRootLayout();

	}

	public void initRootLayout() {
		try {

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource(RootLayoutController.STYLESHEET).toExternalForm());

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

			// Load overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DataOverview.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			// Set overview into the center of root layout.
			// ScrollPane sp = new ScrollPane();
			// sp.setContent(pane);
			rootLayout.setCenter(pane);

			// Give the controller access to the main app.
			DataOverviewController controller = loader.getController();
			controller.setDialogStage(primaryStage);
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showHomeScreen() {

		try {

			// Load overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/HomeScreen.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			// Set overview into the center of root layout.
			ScrollPane sp = new ScrollPane();
			sp.setContent(pane);
			rootLayout.setCenter(sp);

			// Give the controller access to the main app.
			HomeScreenController controller = loader.getController();
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean showAbteilungEditDialog(Abteilung abteilung) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AbteilungEditView.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setTitle(resources.getString("edit"));
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(pane);

			scene.getStylesheets().add(getClass().getResource(RootLayoutController.STYLESHEET).toExternalForm());
			dialogStage.setScene(scene);

			// Set the person into the controller.
			AbteilungEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(abteilung);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean showSettingsDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Settings.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setTitle(resources.getString("settings"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource(RootLayoutController.STYLESHEET).toExternalForm());
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

}
