package model;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private int nr;
	private String vorname;
	private String nachname;
	private String position;
	private List<Projektmitarbeit> projekte= new ArrayList<>();
	
	public Person(int nr) {
		this.nr = nr;
		vorname = "";
		nachname = "";
		position = "";
	}
	
	public Person(int nr, String vorname, String nachname, String position) {
		this.nr = nr;
		this.vorname = vorname;
		this.nachname = nachname;
		this.position = position;
	}
	
	public int getNr() {
		return nr;
	}
	public void setNr(int nr) {
		this.nr = nr;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public List<Projektmitarbeit> getProjekte() {
		return projekte;
	}

	public void setProjekte(List<Projektmitarbeit> projekte) {
		this.projekte = projekte;
	}
	
}
