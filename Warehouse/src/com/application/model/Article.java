package com.application.model;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Article {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty nr = new SimpleStringProperty();
	private StringProperty timestamp = new SimpleStringProperty();
	private Date timestampDate;

	private StringProperty count = new SimpleStringProperty();

	public IntegerProperty idProperty() {
		return id;
	}

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public StringProperty nrProperty() {
		return nr;
	}

	public String getNr() {
		return nr.get();
	}

	public void setNr(String nr) {
		this.nr.set(nr);
	}

	public Date getTimestamp() {
		return timestampDate;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp.set(timestamp.toString());
		this.timestampDate = timestamp;
	}

	public StringProperty timestampProperty() {
		return this.timestamp;
	}

	public StringProperty countProperty() {
		return count;
	}

	public int getCount() {

		return Integer.parseInt(count.get());

	}

	public void setCount(int count) {

		this.count.set(String.valueOf(count));
	}

	@Override
	public String toString() {
		return "Nummer: " + nr.get() + ", Anzahl: " + count.get();
	}

}
