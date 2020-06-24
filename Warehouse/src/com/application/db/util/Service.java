package com.application.db.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import com.application.db.dao.AbteilungDAO;
import com.application.db.dao.ArticleDAO;
import com.application.db.dao.DAOFactory;
import com.application.db.dao.EDAOType;
import com.application.model.Abteilung;
import com.application.model.Article;
import com.application.util.ApplicationProperties;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Service {

	private static Service instance;

	private DAOFactory daoFactory;

	private boolean errorStatus = false;

	private AbteilungDAO abteilungDAO;
	private ArticleDAO articleDAO;

	private Service() {

		if (ApplicationProperties.getInstance().getProperty("demo").equalsIgnoreCase("true")) {
			daoFactory = new DAOFactory(EDAOType.MEMORY);
		} else
			daoFactory = new DAOFactory(EDAOType.JDBC);

		articleDAO = daoFactory.getArticleDAO();
	}

	public synchronized static Service getInstance() {

		if (instance == null) {
			instance = new Service();
		}

		return instance;

	}

	public void insertAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.insertAbteilung(abteilung);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public void updateAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.updateAbteilung(abteilung);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public void deleteAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.deleteAbteilung(abteilung);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public void getAbteilung(int id) {

		try {
			abteilungDAO.getAbteilung(id);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

	public List<Abteilung> getAbteilungen() {

		List<Abteilung> abteilungen = null;

		try {
			abteilungen = abteilungDAO.getAbteilungen();
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();
			showExceptionMessage(e);
		}
		return abteilungen;

	}

	public List<Article> getArticles() {

		List<Article> articles = null;
		try {
			articles = articleDAO.getArticles();
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionMessage(e);
			e.printStackTrace();
		}

		return articles;

	}

	public List<Article> getArticlesDistinct() {

		List<Article> articles = null;
		try {
			articles = articleDAO.getArticlesDistinct();
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionMessage(e);
			e.printStackTrace();
		}

		return articles;

	}

	public List<Article> getArticlesFromToDate(Date start, Date end) {

		List<Article> articles = null;
		try {
			articles = articleDAO.getArticles(start, end);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionMessage(e);
			e.printStackTrace();
		}

		return articles;

	}

	public void deleteArticle(Article anlage) {

		try {
			articleDAO.deleteArticle(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionMessage(e);
			e.printStackTrace();
		}

	}

	public void insertArticle(Article anlage) {

		try {
			articleDAO.insertArticle(anlage);

			errorStatus = false;
		} catch (DAOException e) {
			showExceptionMessage(e);
			e.printStackTrace();

		}

	}

	public boolean isErrorStatus() {
		return errorStatus;
	}

	private void showExceptionMessage(DAOException e) {
		errorStatus = true;
		showExceptionAlertDialog(e);
	}

	private void showExceptionAlertDialog(DAOException e) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setContentText(e.getClass().getName());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

	}

}
