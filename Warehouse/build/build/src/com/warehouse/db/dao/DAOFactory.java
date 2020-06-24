package com.warehouse.db.dao;

public class DAOFactory {

	private ArticleDAO articleDAO;

	public DAOFactory(EDAOType eDAOType) {

		if (eDAOType == EDAOType.JDBC) {

			articleDAO = new ArticleJDBCDAO();

		}

		if (eDAOType == EDAOType.HIBERNATE) {

		}

		if (eDAOType == EDAOType.MEMORY) {

		}

	}

	public ArticleDAO getArticleDAO() {

		return articleDAO;
	}

}
