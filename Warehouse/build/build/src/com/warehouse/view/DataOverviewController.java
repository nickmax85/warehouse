package com.warehouse.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.warehouse.Main;
import com.warehouse.db.util.AppService;
import com.warehouse.model.Article;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author Markus Thaler
 */
public class DataOverviewController implements Initializable {

	// Reference to the main application
	private Main main;
	@FXML
	private ResourceBundle resources;
	private Stage dialogStage;

	private List<Article> articles = new ArrayList<Article>();
	private ObservableList<Article> articlesObs;

	@FXML
	private BarChart<String, Integer> chart;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;

	private Date endDatumConvert;
	private Date startDatumConvert;

	@FXML
	private TextField nrTextField;

	@FXML
	private TableView<Article> articleScanTable;
	@FXML
	private TableColumn<Article, String> nrScanColumn;
	@FXML
	private TableColumn<Article, String> cntScanColumn;

	@FXML
	private TableView<Article> articleFilterDBTable;
	@FXML
	private TableColumn<Article, String> nrFilterDBColumn;
	@FXML
	private TableColumn<Article, String> cntFilterDBColumn;

	public DataOverviewController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

		// Initialize the table with the two columns.
		nrScanColumn.setCellValueFactory(cellData -> cellData.getValue().nrProperty());
		cntScanColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty());

		nrFilterDBColumn.setCellValueFactory(cellData -> cellData.getValue().nrProperty());
		cntFilterDBColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty());

		Calendar calStart = Calendar.getInstance();
		startDate.setValue(LocalDate.of(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH) + 1,
				calStart.get(Calendar.DAY_OF_MONTH)));

		Calendar calEnd = Calendar.getInstance();
		endDate.setValue(LocalDate.of(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH) + 1,
				calEnd.get(Calendar.DAY_OF_MONTH)));

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				nrTextField.requestFocus();
			}
		});

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

		LocalDate startDatumLocalDate = startDate.getValue();
		startDatumConvert = Date.from(startDatumLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		Calendar cal = Calendar.getInstance();

		LocalDate endDatumLocalDate = endDate.getValue();
		endDatumConvert = Date.from(endDatumLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		cal.setTime(endDatumConvert);
		cal.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getLeastMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, Calendar.getInstance().getLeastMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, Calendar.getInstance().getLeastMaximum(Calendar.SECOND));

		endDatumConvert = cal.getTime();

		articles = FXCollections.observableArrayList();
		articles.addAll(AppService.getInstance().getArticlesFromToDate(startDatumConvert, endDatumConvert));
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

	@FXML
	private void handleNew(KeyEvent event) {

		if (event.getEventType() == KeyEvent.KEY_PRESSED)

			if (event.getCode() == KeyCode.ENTER) {

				if (!nrTextField.getText().isEmpty()) {
					Article article = new Article();
					article.setNr(nrTextField.getText());
					article.setCount(0);

					articles.add(article);

					AppService.getInstance().insertArticle(article);
					if (!AppService.getInstance().isErrorStatus()) {
						nrTextField.setText(null);
						mapArticles(articles);
					}

				}

				else {
					showNothingSelectedDialog();
				}
			}

	}

	private void showNothingSelectedDialog() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Keine Daten eingegeben!");
		alert.setContentText("Bitte Daten eingeben!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(
				getClass().getClassLoader().getResource("com/warehouse/resource/css/MyTheme.css").toExternalForm());
		alert.showAndWait();
	}

	@FXML
	private void print() {

		try {
			// Laden des Report-FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Report.fxml"));
			AnchorPane report = (AnchorPane) loader.load();
			// Zuweisung eines sehr einfachen Stylings (für den Report)
			report.getStylesheets().addAll(getClass().getClassLoader()
					.getResource("com/seeitagain/resource/ReportStyle.css").toExternalForm());
			// Erstellung des Druck-Jobs
			PrinterJob printerJob = PrinterJob.createPrinterJob();
			if (printerJob.showPrintDialog(dialogStage.getOwner()) && printerJob.printPage(report))
				printerJob.endJob();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void mapArticles(List<Article> articles) {

		List<Article> articleList = new ArrayList<Article>();

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (Article article : articles) {
			if (!map.keySet().contains(article.getNr())) {
				map.put(article.getNr(), 1);
			} else {
				map.put(article.getNr(), map.get(article.getNr()) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : map.entrySet()) {

			Article a = new Article();
			a.setNr(entry.getKey());
			a.setCount(entry.getValue());
			articleList.add(a);

		}

		ObservableList<Article> articleMaped = FXCollections.observableArrayList();
		articleMaped.addAll(articleList);
		articleScanTable.setItems(articleMaped);

	}

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

			writer.write(nrFilterDBColumn.getText() + ";" + cntFilterDBColumn.getText() + "\n");

			for (Article article : AppService.getInstance().getArticlesFromToDate(startDatumConvert, endDatumConvert)) {

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