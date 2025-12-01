package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Person;
import model.PersonComparator;
import model.Projekt;
import model.ProjektComparator;
import model.Projektmitarbeit;

public class Database {
	
	private boolean isFirstStart = true;
	
	private List<Person> personal = new ArrayList<>();
	private List<Projekt> projekte = new ArrayList<>();

	public void prepareStorage() {
		if (isFirstStart) {
			Person p1 = new Person(1, "Max", "Meier", "Entwickler");
			personal.add(p1);

			Person p2 = new Person(2, "Peter", "Maier", "Administrator");
			personal.add(p2);

			Projekt pr1 = new Projekt(1, "Zeus", "Zeus KG");
			projekte.add(pr1);

			Projekt pr2 = new Projekt(2, "Neptun", "Neptun AG");
			projekte.add(pr2);

			Projekt pr3 = new Projekt(3, "Poseidon", "Poseidon GmbH");
			projekte.add(pr3);

			p1.getProjekte().add(new Projektmitarbeit(pr2, "Modellklassen"));
			p1.getProjekte().add(new Projektmitarbeit(pr3, "Refactoring"));

			p2.getProjekte().add(new Projektmitarbeit(pr1, "Docker"));

			isFirstStart = false;
			EclipseStore.storageManager().store(personal);
			EclipseStore.storageManager().store(projekte);

			EclipseStore.storageManager().storeRoot();
		}
	}
	
    /**
     * Liefert eine Liste aller Personen, die ggfls. gefiltert und sortiert werden.
     * @param sort Feld nach dem sortiert wird (nr, vorname, nachname oder position).
     * @param filter Es werden nur Personen angezeigt, die den Filter im Nachnamen enthalten.
     * @return Liste mit Personen.
     */
	public List<Person> getPersonal(String sort, String filter) {
		
		List<Person> newList;
		
		if (filter != null && !filter.isEmpty()) {

			newList = new ArrayList<>();
		
			for (Person p: personal) {
				if (p.getNachname().contains(filter))
					newList.add(p);
			}
		} else {
			newList = new ArrayList<>(personal);
		}
		
		if (sort != null) {
			newList.sort(new PersonComparator(sort));
		}
		
		return newList;
	}
	
    /**
     * Liefert eine Liste aller Projekte, die ggfls. sortiert werden.
     * @param sort Feld nach dem sortiert wird (nr, name oder kunde).
     * @return Liste mit Projekten.
     */
	public List<Projekt> getProjekte(String sort) {
		
		List<Projekt> newList = new ArrayList<>(projekte);
		
		if (sort != null) {
			
			newList.sort(new ProjektComparator(sort));
		}
		
		return newList;
		
	}
	
    /**
     * Eine Person wird aus dem Datenbestand entfernt.
     * @param nr Nummer der zu loeschenden Person.
     */
	public void deletePerson(int nr) {

		Iterator<Person> it = personal.iterator();
		while (it.hasNext()) {
			Person p = it.next();
			if (p.getNr() == nr)
				it.remove();
		}
		EclipseStore.storageManager().store(personal);
	}
	
    /**
     * Ein Projekt wird aus dem Datenbestand entfernt.
     * @param nr nr Nummer des zu loeschenden Projektes.
     */
	public void deleteProjekt(int nr) {
		Iterator<Projekt> it = projekte.iterator();
		while (it.hasNext()) {
			Projekt p = it.next();
			if (p.getNr() == nr)
				it.remove();
		}
		
		// auch Projektmitarbeit loeschen
		Iterator<Person> itp = personal.iterator();
		while (itp.hasNext()) {
			Person p = itp.next();
			Iterator<Projektmitarbeit> itpm = p.getProjekte().iterator();
			while (itpm.hasNext()) {
				if (itpm.next().getProjekt().getNr() == nr)
					itpm.remove();
			}
		}
		EclipseStore.storageManager().storeAll(projekte, personal);
	}
	
    /**
     * Liefert eine Person mit der angegebenen Personennummer.
     * @param nr Personennummer.
     * @return Person-Objekt mit der angegebenen Personennummer.
     */
	public Person getPerson(int nr) {
		Person p = null;
		for (int i = 0; p == null && i<personal.size(); i++) {
			Person q = personal.get(i);
			if (q.getNr() == nr)
				p = q;
		}
		
		return p;
		
	}
	
    /**
     * Liefert ein Projekt mit der angegebenen Projektnummer.
     * @param nr Projektnummer.
     * @return Projekt-Objekt mit der angegebenen Projektnummer.
     */
	public Projekt getProjekt(int nr) {
		Projekt p = null;
		for (int i = 0; p == null && i<projekte.size(); i++) {
			Projekt q = projekte.get(i);
			if (q.getNr() == nr)
				p = q;
		}
		
		return p;
		
	}
	
    /**
     * Ermittlung der groessten Personennummer im Datenbestand.
     * @return Groesste Personennummer.
     */
	public int getMaxNrPerson() {
		
		int max = 0;
		
		for (int i = 0; i<personal.size(); i++) {
			Person q = personal.get(i);
			if (q.getNr() > max)
				max = q.getNr();
		}
		
		return max;
	}
	
    /**
     * Ermittlung der groessten Projektnummer im Datenbestand.
     * @return Groesste Projektnummer.
     */
	public int getMaxNrProjekt() {
		
		int max = 0;
		
		for (int i = 0; i<projekte.size(); i++) {
			Projekt q = projekte.get(i);
			if (q.getNr() > max)
				max = q.getNr();
		}
		
		return max;
	}
	
    /**
     * Es wird die uebergebene Person dem Datenbestand hinzugefuegt.
     * @param p Person, die hinzugefuegt werden soll.
     */
	public void insertPerson(Person p) {
		personal.add(p);
		EclipseStore.storageManager().store(personal);
	}
	
    /**
     * Es wird das uebergebene Projet dem Datenbestand hinzugefuegt.
     * @param p Projekt, das hinzugefuegt werden soll.
     */
	public void insertProjekt(Projekt p) {
		projekte.add(p);
		EclipseStore.storageManager().store(projekte);

	}
	
    /**
     * Die Daten der uebergebenen Person werden im Datenbestand 
     * (anhand nr) aktualisiert.
     * @param p Person, die aktualisiert werden soll.
     */
	public void updatePerson(Person p) {
			EclipseStore.storageManager().store(p);
    }
	
    /**
     * Die Daten des uebergebenen Projektes werden im Datenbestand 
     * (anhand nr) aktualisiert.
     * @param p Projekt, das aktualisiert werden soll.
     */
	public void updateProjekt(Projekt p) {
			EclipseStore.storageManager().store(p);
    }
 
 }
