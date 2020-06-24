package com.application.view;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.application.Main;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 
 * @author Markus Thaler
 */
public class DataOverviewController implements Initializable {

	private static final Logger logger = Logger.getLogger(DataOverviewController.class);

	// Reference to the main application
	private Main main;

	private ResourceBundle resources;

	@FXML
	private Label labelLeftStatus;
	@FXML
	private Label labelRightStatus;
	@FXML
	private Pane paneCenterStatus;

	private Stage dialogStage;

	@FXML
	private BorderPane dataPane;
	@FXML
	private AnchorPane treePane;

	@FXML
	private TreeView<String> treeMenu;

	public DataOverviewController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

		createTree();

		Thread th = new Thread(new DateThread());
		th.setDaemon(true);
		th.start();

		showHomeScreen();

	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;

	}

	public void showAbteilungenOverview() {

		try {

			// Load overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AbteilungOverview.fxml"));
			loader.setResources(resources);
			SplitPane pane = (SplitPane) loader.load();

			dataPane.getChildren().clear();
			dataPane.setCenter(pane);

			// Give the controller access to the main app.
			AbteilungOverviewController controller = loader.getController();
			controller.setMain(main);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showScanOverview() {

		try {

			// Load overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ScanOverview.fxml"));
			loader.setResources(resources);
			SplitPane pane = (SplitPane) loader.load();

			dataPane.getChildren().clear();
			dataPane.setCenter(pane);

			// Give the controller access to the main app.
			ScanOverviewController controller = loader.getController();
			controller.setMain(main);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showEditOverview() {

		try {

			// Load overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/EditView.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			dataPane.getChildren().clear();
			dataPane.setCenter(pane);

			// Give the controller access to the main app.
			EditController controller = loader.getController();
			controller.setMain(main);

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
			BorderPane pane = (BorderPane) loader.load();

			dataPane.getChildren().clear();
			dataPane.setCenter(pane);

			// Give the controller access to the main app.
			HomeScreenController controller = loader.getController();
			controller.setMain(main);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createTree(String... rootItems) {

		// Root
		TreeItem<String> root = new TreeItem<>("Warehouse");
		root.setGraphic(new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream("com/application/resource/icons/folder16.png"))));
		root.setExpanded(true);

		// Scannen
		TreeItem<String> itemScan = new TreeItem<>("Scannen");
		itemScan.setGraphic(new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream("com/application/resource/icons/barcode16.png"))));
		itemScan.setExpanded(true);

		// Bearbeiten
		TreeItem<String> itemBearbeiten = new TreeItem<>("Datenbank");
		itemBearbeiten.setGraphic(new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream("com/application/resource/icons/database16.png"))));
		itemBearbeiten.setExpanded(true);

		// root is the parent of itemChild
		root.getChildren().add(itemScan);
		root.getChildren().add(itemBearbeiten);

		treeMenu.setRoot(root);

		treeMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val,
					TreeItem<String> new_val) {
				TreeItem<String> selectedItem = new_val;
				if (logger.isInfoEnabled()) {
					logger.info("Selected Text: " + selectedItem.getValue());
				}

				labelLeftStatus.setText(selectedItem.getValue());

				if (selectedItem.getValue().equalsIgnoreCase("Warehouse")) {
					showHomeScreen();

				}

				if (selectedItem.getValue().equalsIgnoreCase("Scannen")) {
					showScanOverview();

				}

				if (selectedItem.getValue().equalsIgnoreCase("Auswertung")) {

				}

				if (selectedItem.getValue().equalsIgnoreCase("Datenbank")) {

					showEditOverview();

				}

			}

		});
	}

	class DateThread implements Runnable {

		DateFormat df = new SimpleDateFormat("dd. MMMM yyyy HH:mm:ss");

		@Override
		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				Date date = Calendar.getInstance().getTime();

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						String formatDate = df.format(date);
						labelRightStatus.setText(formatDate);

					}
				});

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

					e.printStackTrace();
					Thread.currentThread().interrupt();
				}

			}

		}

	}

}