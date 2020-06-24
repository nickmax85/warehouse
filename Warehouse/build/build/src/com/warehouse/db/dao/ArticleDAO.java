package com.warehouse.db.dao;

import java.util.Date;
import java.util.List;

import com.warehouse.db.util.DAOException;
import com.warehouse.model.Article;

public interface ArticleDAO {

	public void deleteArticle(Article Article) throws DAOException;

	public Article getArticle(int id) throws DAOException;

	public List<Article> getArticles() throws DAOException;

	public void insertArticle(Article Article) throws DAOException;

	public void updateArticle(Article Article) throws DAOException;

	public List<Article> getArticlesDistinct() throws DAOException;

	public List<Article> getArticles(Date start, Date end) throws DAOException;

}