package model;

public class Projektmitarbeit {
	private String aufgabe;
	private Projekt projekt;
	
	public String getAufgabe() {
		return aufgabe;
	}
	public void setAufgabe(String aufgabe) {
		this.aufgabe = aufgabe;
	}
	public Projekt getProjekt() {
		return projekt;
	}
	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}
	
	public Projektmitarbeit(Projekt projekt, String aufgabe) {
		this.aufgabe = aufgabe;
		this.projekt = projekt;
	}
	
	
	
}
