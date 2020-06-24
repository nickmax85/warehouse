package com.application.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.application.Main;
import com.application.db.util.Service;
import com.application.model.Article;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * 
 * @author Markus Thaler
 */
public class EditController implements Initializable {

	// Reference to the main application
	private Main main;

	private ResourceBundle resources;
	private Stage dialogStage;

	private List<Article> articles = new ArrayList<Article>();
	private ObservableList<Article> articlesObs;

	@FXML
	private TableView<Article> articleFilterDBTable;
	@FXML
	private TableColumn<Article, String> nrFilterDBColumn;
	@FXML
	private TableColumn<Article, String> timestampFilterDBColumn;

	public EditController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

		// Initialize the table with the two columns.
		nrFilterDBColumn.setCellValueFactory(cellData -> cellData.getValue().nrProperty());
		timestampFilterDBColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());

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

	@FXML
	private void handleGetArticles() {

		ObservableList<Article> articles;

		articles = FXCollections.observableArrayList();
		articles.addAll(Service.getInstance().getArticles());
		articleFilterDBTable.setItems(articles);

	}

	@FXML
	private void handleExport() {

		try {
			exportToCSV();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDelete() {
		int selectedIndex = articleFilterDBTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Article art;
			art = articleFilterDBTable.getSelectionModel().getSelectedItem();

			Service.getInstance().deleteArticle(art);
			if (!Service.getInstance().isErrorStatus()) {
				articleFilterDBTable.getItems().remove(selectedIndex);
			}

		} else {

			showNothingSelectedDialog();
		}
	}

	private void showNothingSelectedDialog() {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Keine Daten ausgewählt!");
		alert.setContentText("Bitte Daten aus der Tabelle auswählen!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(
				getClass().getClassLoader().getResource("com/warehouse/resource/css/MyTheme.css").toExternalForm());
		alert.showAndWait();
	}

	@FXML
	public void exportToCSV() throws Exception {
		Writer writer = null;
		File file = null;
		try {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle(resources.getString("save_as"));

			int returnVal = fileChooser.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String path = fileChooser.getSelectedFile().getPath();

				if (!path.toLowerCase().endsWith(".csv"))
					path = path + ".csv";

				file = new File(path);

			}

			if (file == null)
				return;

			writer = new BufferedWriter(new FileWriter(file));

			writer.write(nrFilterDBColumn.getText() + ";" + timestampFilterDBColumn.getText() + "\n");

			for (Article article : Service.getInstance().getArticlesDistinct()) {

				String text = article.getNr() + ";" + article.getCount() + "\n";

				writer.write(text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			writer.flush();
			writer.close();
		}
	}

}