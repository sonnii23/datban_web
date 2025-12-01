package model;

public class Projekt {
	
	private int nr;
	private String name;
	private String kunde;
	
	public Projekt(int nr) {
		this.nr = nr;
		name = "";
		kunde = "";
	}
	
	public Projekt(int nr, String name, String kunde) {
		this.nr = nr;
		this.name = name;
		this.kunde = kunde;
	}
	
	public int getNr() {
		return nr;
	}
	public void setNr(int nr) {
		this.nr = nr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKunde() {
		return kunde;
	}
	public void setKunde(String kunde) {
		this.kunde = kunde;
	}
}
