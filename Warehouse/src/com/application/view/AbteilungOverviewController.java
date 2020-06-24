package com.application.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.application.Main;
import com.application.db.util.Service;
import com.application.model.Abteilung;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AbteilungOverviewController implements Initializable {

	private ResourceBundle resources;

	private Main main;

	@FXML
	private TableView<Abteilung> table;
	@FXML
	private TableColumn<Abteilung, String> nameColumn;

	@FXML
	private Label nameLabel;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public AbteilungOverviewController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize the person table
		nameColumn.setCellValueFactory(new PropertyValueFactory<Abteilung, String>("name"));

		// Auto resize columns
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// clear person
		showDetails(null);

		// Listen for selection changes
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Abteilung>() {

			@Override
			public void changed(ObservableValue<? extends Abteilung> observable, Abteilung oldValue,
					Abteilung newValue) {
				showDetails(newValue);
			}
		});

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param main
	 */
	public void setMain(Main main) {
		this.main = main;

		// Add observable list data to the table
		ObservableList<Abteilung> obsList = FXCollections.observableArrayList(Service.getInstance().getAbteilungen());
		table.setItems(obsList);
	}

	private void showDetails(Abteilung abteilung) {
		if (abteilung != null) {
			nameLabel.setText(abteilung.getName());

		} else {
			nameLabel.setText("");

		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDelete() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			table.getItems().remove(selectedIndex);
		} else {
			// Nothing selected
			// Dialogs.showWarningDialog(main.getPrimaryStage(), "Please select
			// in the table.",
			// "No Selection");
		}
	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for new data.
	 */
	@FXML
	private void handleNew() {
		Abteilung tempHeimart = new Abteilung();
		boolean okClicked = main.showAbteilungEditDialog(tempHeimart);
		if (okClicked) {
			Service.getInstance().insertAbteilung(tempHeimart);
			refreshTable();
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected data.
	 */
	@FXML
	private void handleEdit() {
		Abteilung selectedHeimart = table.getSelectionModel().getSelectedItem();
		if (selectedHeimart != null) {
			boolean okClicked = main.showAbteilungEditDialog(selectedHeimart);
			if (okClicked) {
				refreshTable();
				showDetails(selectedHeimart);
			}

		} else {
			// Nothing selected
			// Dialogs.showWarningDialog(main.getPrimaryStage(), "Please select
			// a heimart in the table.",
			// "No Heimart Selected", "No Selection");
		}
	}

	private void refreshTable() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		table.setItems(null);
		table.layout();
		table.setItems(FXCollections.observableArrayList(Service.getInstance().getAbteilungen()));
		// Must set the selected index again (see
		// http://javafx-jira.kenai.com/browse/RT-26291)
		table.getSelectionModel().select(selectedIndex);
	}
}