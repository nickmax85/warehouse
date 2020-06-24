package com.application.model;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Abteilung {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty timestamp = new SimpleStringProperty();
	private Date timestampDate;

	public IntegerProperty idProperty() {
		return id;
	}

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public StringProperty artProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String art) {
		this.name.set(art);
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

}
