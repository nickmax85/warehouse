package com.warehouse.db.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import com.warehouse.db.dao.ArticleDAO;
import com.warehouse.db.dao.DAOFactory;
import com.warehouse.db.dao.EDAOType;
import com.warehouse.model.Article;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AppService {

	private static AppService instance;

	private final static EDAOType SOURCE = EDAOType.JDBC;

	private boolean errorStatus = false;

	private DAOFactory daoFactory;

	private DAOException daoException;

	private ArticleDAO articleDAO;

	private AppService() {

		daoFactory = new DAOFactory(SOURCE);

		articleDAO = daoFactory.getArticleDAO();

	}

	public synchronized static AppService getInstance() {

		if (instance == null) {
			instance = new AppService();
		}

		return instance;

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

	public void updateArticle(Article anlage) {
		try {
			articleDAO.updateArticle(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionMessage(e);
		}

	}

}
