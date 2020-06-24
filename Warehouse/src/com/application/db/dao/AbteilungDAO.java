package com.application.db.dao;

import java.util.List;

import com.application.db.util.DAOException;
import com.application.model.Abteilung;

public interface AbteilungDAO {

	public Abteilung getAbteilung(int id) throws DAOException;

	public List<Abteilung> getAbteilungen() throws DAOException;

	public void insertAbteilung(Abteilung abteilung) throws DAOException;

	public void updateAbteilung(Abteilung abteilung) throws DAOException;

	public void deleteAbteilung(Abteilung abteilung) throws DAOException;

}