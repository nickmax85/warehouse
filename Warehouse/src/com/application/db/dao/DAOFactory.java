package com.application.db.dao;

public class DAOFactory {

	private ArticleDAO articleDAO;

	public DAOFactory(EDAOType eDAOType) {

		if (eDAOType == EDAOType.JDBC) {

			articleDAO = new ArticleJDBCDAO();

		}

		if (eDAOType == EDAOType.MEMORY) {
			articleDAO = new ArticleMemoryDAO();
		}

	}

	public ArticleDAO getArticleDAO() {
		return articleDAO;
	}

}
