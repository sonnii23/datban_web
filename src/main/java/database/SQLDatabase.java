package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Person;
import model.PersonComparator;
import model.Projekt;
import model.ProjektComparator;
import model.Projektmitarbeit;

import java.sql.*;

public class SQLDatabase extends Database {

    // personal liste erstellen 
    private List<Person> personal = new ArrayList<>();
    
    Connection con = null;
    
    //verbindung aufbauen
    public void establishConnection() {
        try {
            //Datenbanktreiber laden
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Projekt", "postgres", "geheim");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //auffüllen der personal liste aus der datenbank
    public void loadPersonal() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM personen");
            while (rs.next()) {
                Person p = new Person(rs.getInt("nr"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                personal.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
	public List<Person> getPersonal(String sort, String filter) {
		//neue liste wird angelegt zum speichern der gefilterten personen
		List<Person> newList;
		//überprüfung ob ein filter gesetzt ist
		if (filter != null && !filter.isEmpty()) {
			newList = new ArrayList<>();
            // filterung der personen nach nachnamen
            try {
    			PreparedStatement ps = con.prepareStatement("SELECT * FROM personen WHERE nachname LIKE ?");
                ps.setString(1, "%" + filter + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Person p = new Person(rs.getInt("nr"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                    newList.add(p);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }		
        } else {
			newList = new ArrayList<>(personal);
		}
		if (sort != null) {
			newList.sort(new PersonComparator(sort));
		}
		return newList;
	}
	
    @Override
	public List<Projekt> getProjekte(String sort) {
            return super.getProjekte(sort);
	}
	
    @Override
	public void deletePerson(int nr) {
            super.deletePerson(nr);
	}
	
    @Override
	public void deleteProjekt(int nr) {
            super.deleteProjekt(nr);
	}
	
        
    @Override
	public Person getPerson(int nr) {
            return super.getPerson(nr);
	}
	
    @Override
	public Projekt getProjekt(int nr) {
            return super.getProjekt(nr);
	}
        
    @Override
	public int getMaxNrPerson() {
            return super.getMaxNrPerson();
	}
	
        
    @Override
	public int getMaxNrProjekt() {
            return super.getMaxNrProjekt();
	}
	
    @Override
	public void insertPerson(Person p) {
            super.insertPerson(p);
	}
	
    @Override
	public void insertProjekt(Projekt p) {
            super.insertProjekt(p);
	}
        
    @Override
	public void updatePerson(Person p) {
            super.updatePerson(p);
        }
	
    @Override
	public void updateProjekt(Projekt p) {
            super.updateProjekt(p);
    }
    
}
