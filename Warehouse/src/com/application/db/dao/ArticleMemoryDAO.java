package com.application.db.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.application.db.util.DAOException;
import com.application.model.Article;

public class ArticleMemoryDAO implements ArticleDAO {

	private static final Logger logger = Logger.getLogger(ArticleMemoryDAO.class);

	private List<Article> articleList = new ArrayList<Article>();

	public ArticleMemoryDAO() {
		generateTestData();
	}

	private void generateTestData() {

		Article article;

		article = new Article();
		article.setId(1);
		article.setNr("12345678");
		article.setTimestamp(Calendar.getInstance().getTime());

		articleList.add(article);

		article = new Article();
		article.setId(2);
		article.setNr("Testnummer");
		article.setTimestamp(Calendar.getInstance().getTime());

		articleList.add(article);

	}

	@Override
	public void deleteArticle(Article Article) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Article getArticle(int id) throws DAOException {

		for (Article art : articleList) {

			if (art.getId() == id)
				return art;

		}

		return null;
	}

	@Override
	public List<Article> getArticles() throws DAOException {

		return articleList;
	}

	@Override
	public void insertArticle(Article article) throws DAOException {
		article.setTimestamp(Calendar.getInstance().getTime());
		articleList.add(article);

	}

	@Override
	public void updateArticle(Article article) throws DAOException {

	}

	@Override
	public List<Article> getArticlesDistinct() throws DAOException {

		return null;
	}

	@Override
	public List<Article> getArticles(Date start, Date end) throws DAOException {

		List<Article> articleList = new ArrayList<Article>();

		for (Article art : this.articleList) {

			if (art.getTimestamp().after(start) && (art.getTimestamp().before(end)))
				articleList.add(art);

		}

		return articleList;
	}

}
