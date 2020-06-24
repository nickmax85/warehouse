package com.warehouse.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.warehouse.db.util.ConnectionManager;
import com.warehouse.db.util.DAOException;
import com.warehouse.model.Article;

public class ArticleJDBCDAO implements ArticleDAO {

	private static final Logger logger = Logger.getLogger(ArticleJDBCDAO.class);

	private final static String GET_ARTICLES = "SELECT * FROM article ORDER BY timestamp DESC";
	private final static String GET_ARTICLE = "SELECT * FROM article where id = ?";
	private final static String INSERT_ARTICLE = "INSERT INTO article(nr) VALUES (?)";
	private final static String UPDATE_ARTICLE = "UPDATE article SET nr = ?, WHERE id = ?";
	private final static String DELETE_ARTICLE = "DELETE FROM article WHERE id= ?";
	private final static String SELECT_ARTICLES_DISTINCT = "SELECT *, count(*) as count FROM article GROUP BY nr";
	private final static String SEARCH_ARTICLE_FROM_TO_DATE = "SELECT *, count(*)  as count FROM article WHERE (timestamp BETWEEN ? AND ?) GROUP BY nr";

	@Override
	public void deleteArticle(Article article) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_ARTICLE);
			ps.setInt(1, article.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(article);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

	}

	@Override
	public Article getArticle(int wartungId) throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		Article article;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_ARTICLE);
			ps.setInt(1, wartungId);
			rs = ps.executeQuery();
			rs.next();

			article = new Article();
			article.setId(new Integer(rs.getInt("id")));
			article.setNr(rs.getString("nr"));
			article.setTimestamp(rs.getTimestamp("timestamp"));

			if (logger.isInfoEnabled()) {
				logger.info(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

		return article;
	}

	@Override
	public List<Article> getArticles() throws DAOException {
		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<Article> articleList = new ArrayList<Article>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ARTICLES);

			while (rs.next()) {
				Article article = new Article();
				article.setId(new Integer(rs.getInt("id")));
				article.setNr(rs.getString("nr"));
				article.setTimestamp(rs.getTimestamp("timestamp"));
				articleList.add(article);
			}

			if (logger.isInfoEnabled()) {
				logger.info(articleList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

		return articleList;
	}

	@Override
	public void insertArticle(Article article) throws DAOException {

		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_ARTICLE);

			ps.setString(1, article.getNr());
			ps.executeUpdate();

			article.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(article);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

	}

	public Integer selectLastID() throws DAOException {

		Integer lastId = null;

		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection()
					.prepareStatement("select last_insert_id()");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lastId = rs.getInt(1);
			}

			if (logger.isInfoEnabled()) {
				logger.info(lastId);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

		return lastId;
	}

	@Override
	public void updateArticle(Article article) throws DAOException {

		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_ARTICLE);

			ps.setString(1, article.getNr());
			ps.setInt(2, article.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(article);
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

	}

	@Override
	public List<Article> getArticles(Date from, Date to) throws DAOException {

		// From DB
		// Timestamp timestamp = new Timestamp(date.getTime());
		// preparedStatement = connection.prepareStatement("SELECT * FROM tbl
		// WHERE ts > ?");
		// preparedStatement.setTimestamp(1, timestamp);

		// To DB
		// Timestamp timestamp = resultSet.getTimestamp("ts");
		// java.util.Date date = timestamp; // You can just upcast.

		ResultSet rs = null;
		PreparedStatement ps = null;

		List<Article> articles = new ArrayList<Article>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(SEARCH_ARTICLE_FROM_TO_DATE);

			// java.util.Date date = new Date(System.currentTimeMillis());

			Timestamp startTime = new Timestamp(from.getTime());
			ps.setTimestamp(1, startTime);

			Timestamp endTime = new Timestamp(to.getTime());
			ps.setTimestamp(2, endTime);

			rs = ps.executeQuery();

			while (rs.next()) {

				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setNr(rs.getString("nr"));
				article.setTimestamp(rs.getTimestamp("timestamp"));
				article.setCount(rs.getInt("count"));

				if (logger.isInfoEnabled()) {
					logger.info(article);
				}

				articles.add(article);

			}
			if (logger.isInfoEnabled()) {
				logger.info(articles);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

		return articles;

	}

	@Override
	public List<Article> getArticlesDistinct() throws DAOException {

		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<Article> articles = new ArrayList<Article>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(SELECT_ARTICLES_DISTINCT);

			while (rs.next()) {
				Article article = new Article();
				article.setId(new Integer(rs.getInt("id")));
				article.setNr(rs.getString("nr"));
				article.setTimestamp(rs.getTimestamp("timestamp"));
				article.setCount(rs.getInt("count"));

				if (logger.isInfoEnabled()) {
					logger.info(article);
				}

				articles.add(article);
			}

			if (logger.isInfoEnabled()) {
				logger.info(articles);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		finally {
			try {
				ConnectionManager.getInstance().getConnection().close();
			} catch (SQLException e) {

				e.printStackTrace();
				throw new DAOException(e);
			}
		}

		return articles;

	}

}
