package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Person;
import model.PersonComparator;
import model.Projekt;
import model.ProjektComparator;
// Projektmitarbeit isn't referenced in this SQL-backed class (kept in Database)

import java.sql.*;

public class SQLDatabase extends Database {

    // personal liste erstellen 
    private List<Person> personal = new ArrayList<>();
    private List<Projekt> projekte = new ArrayList<>();
        
    //verbindung aufbauen
    public Connection establishConnection() {
        Connection con = null;
    	try {
            //Datenbanktreiber laden
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Projekt", "postgres", "geheim");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    // auffüllen der personal liste aus der datenbank
    public List<Person> loadPersonal() {
    	Connection con = establishConnection();
    	personal.clear();
        String sql = "SELECT * FROM personen";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Person p = new Person(rs.getInt("Personennummer"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                personal.add(p);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personal;
    }
    
    
    public List<Projekt> loadProject(){
    	Connection con = establishConnection();
    	projekte.clear();
    	String sql = "SELECT * FROM projekt";
    	try (Statement stmt = con.createStatement();
    			ResultSet rs = stmt.executeQuery(sql)){
    		while (rs.next()) {
    			Projekt pr = new Projekt(rs.getInt("projektnummer"), rs.getString("name"), rs.getString("kunde"));
    			projekte.add(pr);
    		}
    		con.close();
    	}catch (SQLException e){
    		e.printStackTrace();
    	}
    	return projekte;
    }
    

    @Override
	public List<Person> getPersonal(String sort, String filter) {
		//neue liste wird angelegt zum speichern der gefilterten personen
		List<Person> newList;
		// überprüfung ob ein filter gesetzt ist
		if (filter != null && !filter.isEmpty()) {
            Connection con = establishConnection();
			newList = new ArrayList<>();
            // filterung der personen nach nachnamen
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM personen WHERE nachname LIKE ?")) {
                ps.setString(1, "%" + filter + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Person p = new Person(rs.getInt("nr"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                        newList.add(p);
                    }
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {   
			newList = new ArrayList<>(loadPersonal());
		}

		if (sort != null) {
			newList.sort(new PersonComparator(sort));
		}
        
		return newList;
	}

	
    @Override
	public List<Projekt> getProjekte(String sort) {
        List<Projekt> newList = new ArrayList<>();
        String sql = "SELECT * FROM projekte";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                Projekt p = new Projekt(rs.getInt("nr"), rs.getString("name"), rs.getString("kunde"));
                newList.add(p);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        if (sort != null){
            newList.sort(new ProjektComparator(sort));
        }
        return newList;
	}

	
    @Override
    public void deletePerson(int nr) {
        // remove person from database and from local list
        String sql1 = "DELETE FROM projektmitarbeit WHERE person_nr = ?"; // if such table/column exists
        String sql2 = "DELETE FROM personen WHERE nr = ?";
        try (PreparedStatement p1 = con.prepareStatement(sql1)) {
            p1.setInt(1, nr);
            p1.executeUpdate();
        } catch (SQLException e) {
            // ignore if table/column doesn't exist or other errors
            // fall back to deleting person row only
        }

        try (PreparedStatement p2 = con.prepareStatement(sql2)) {
            p2.setInt(1, nr);
            p2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // keep local cache in sync
        Iterator<Person> it = personal.iterator();
        while (it.hasNext()) {
            if (it.next().getNr() == nr)
                it.remove();
        }
    }
	
    @Override
    public void deleteProjekt(int nr) {
        // Remove project assignments, then project
        String sql1 = "DELETE FROM projektmitarbeit WHERE projekt_nr = ?"; // if such table/column exists
        String sql2 = "DELETE FROM projekte WHERE nr = ?";

        try (PreparedStatement p1 = con.prepareStatement(sql1)) {
            p1.setInt(1, nr);
            p1.executeUpdate();
        } catch (SQLException e) {
            // ignore if no mapping table exists
        }

        try (PreparedStatement p2 = con.prepareStatement(sql2)) {
            p2.setInt(1, nr);
            p2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
        
    @Override
    public Person getPerson(int nr) {
        String sql = "SELECT nr, vorname, nachname, position FROM personen WHERE nr = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Person(rs.getInt("nr"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
    @Override
    public Projekt getProjekt(int nr) {
        String sql = "SELECT nr, name, kunde FROM projekte WHERE nr = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Projekt(rs.getInt("nr"), rs.getString("name"), rs.getString("kunde"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
        
    @Override
    public int getMaxNrPerson() {
        String sql = "SELECT MAX(nr) as maxnr FROM personen";
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("maxnr");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
        
    @Override
    public int getMaxNrProjekt() {
        String sql = "SELECT MAX(nr) as maxnr FROM projekte";
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("maxnr");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
    @Override
    public void insertPerson(Person p) {
        String sql = "INSERT INTO personen (nr, vorname, nachname, position) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getNr());
            ps.setString(2, p.getVorname());
            ps.setString(3, p.getNachname());
            ps.setString(4, p.getPosition());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // keep local cache in sync
        loadPersonal();
    }
	
    @Override
    public void insertProjekt(Projekt p) {
        String sql = "INSERT INTO projekte (nr, name, kunde) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getNr());
            ps.setString(2, p.getName());
            ps.setString(3, p.getKunde());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    @Override
    public void updatePerson(Person p) {
        String sql = "UPDATE personen SET vorname = ?, nachname = ?, position = ? WHERE nr = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getVorname());
            ps.setString(2, p.getNachname());
            ps.setString(3, p.getPosition());
            ps.setInt(4, p.getNr());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // refresh local cache
        loadPersonal();
    }
	
    @Override
    public void updateProjekt(Projekt p) {
        String sql = "UPDATE projekte SET name = ?, kunde = ? WHERE nr = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getKunde());
            ps.setInt(3, p.getNr());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
