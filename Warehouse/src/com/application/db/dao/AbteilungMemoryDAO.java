package com.application.db.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.application.db.util.DAOException;
import com.application.model.Abteilung;

public class AbteilungMemoryDAO implements AbteilungDAO {

	private static final Logger logger = Logger.getLogger(AbteilungMemoryDAO.class);

	private List<Abteilung> abteilungen = new ArrayList<Abteilung>();

	public AbteilungMemoryDAO() {

		generateTestdata();

	}

	private void generateTestdata() {

		Abteilung abteilung;

		abteilung = new Abteilung();
		abteilung.setId(1);
		abteilung.setName("Instandhaltung");
		abteilung.setTimestamp(Calendar.getInstance().getTime());
		abteilungen.add(abteilung);

		abteilung = new Abteilung();
		abteilung.setId(2);
		abteilung.setName("Personalabteilung");
		abteilung.setTimestamp(Calendar.getInstance().getTime());
		abteilungen.add(abteilung);

		abteilung = new Abteilung();
		abteilung.setId(3);
		abteilung.setName("Qualität");
		abteilung.setTimestamp(Calendar.getInstance().getTime());
		abteilungen.add(abteilung);

	}

	@Override
	public Abteilung getAbteilung(int id) throws DAOException {

		for (Abteilung abteilung : abteilungen) {

			if (abteilung.getId() == id)
				return abteilung;

		}

		return null;
	}

	@Override
	public List<Abteilung> getAbteilungen() throws DAOException {

		return abteilungen;
	}

	@Override
	public void insertAbteilung(Abteilung abteilung) throws DAOException {

		abteilungen.add(abteilung);

	}

	@Override
	public void updateAbteilung(Abteilung abteilung) throws DAOException {

	}

	@Override
	public void deleteAbteilung(Abteilung abteilung) throws DAOException {

		abteilungen.remove(abteilung);

	}

}
