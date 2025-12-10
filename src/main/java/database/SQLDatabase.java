package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Person;
import model.PersonComparator;
import model.Projekt;
import model.ProjektComparator;
// Projektmitarbeit isn't referenced in this SQL-backed class (kept in Database)
import model.Projektmitarbeit;

import java.sql.*;

public class SQLDatabase extends Database {

    // Listen erstellen 
    private List<Person> personal = new ArrayList<>();
    private List<Projekt> projekte = new ArrayList<>();
    private List<Projektmitarbeit> projektmitarbeitList = new ArrayList<>();

        
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
        String sql = "SELECT * FROM person";
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

    public List<Projektmitarbeit> loadProjectPartic(){
        Connection con = establishConnection();
        projektmitarbeitList.clear();
        String sql = "SELECT * FROM projektmitarbeit";
        try(Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Person p = getPerson(rs.getInt("personennummer"));
                Projekt pr = getProjekt(rs.getInt("projektnummer"));
                String aufgabe = rs.getString("aufgabe");
                if (p != null && pr != null){
                    Projektmitarbeit pm = new Projektmitarbeit(pr, aufgabe);
                    projektmitarbeitList.add(pm);
                }
            }
            con.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return projektmitarbeitList;
    }


    @Override
	public List<Person> getPersonal(String sort, String filter) {
		
    	List<Person> newList;
		
		if (filter != null && !filter.isEmpty()) {

			newList = new ArrayList<>();
		
			for (Person p: loadPersonal()) {
				if (p.getNachname().contains(filter))
					newList.add(p);
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
    	
    	List<Projekt> newList = new ArrayList<>(loadProject());
		
		if (sort != null) {
			
			newList.sort(new ProjektComparator(sort));
		}
		
		return newList;
		
	}

	
    @Override
    public void deletePerson(int nr) {
    	Connection con = establishConnection();
    	
        String sql1 = "DELETE FROM projektmitarbeit WHERE personennummer = ?"; // if such table/column exists
    	String sql2 = "DELETE FROM person WHERE personennummer = ?";

    	try (PreparedStatement ps1 = con.prepareStatement(sql1)) {
    		ps1.setInt(1, nr);
            ps1.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
            ps2.setInt(1, nr);
            ps2.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
    @Override
    public void deleteProjekt(int nr) {
        Connection con = establishConnection();

        String sql1 = "DELETE FROM projektmitarbeit WHERE projektnummer = ?"; // if such table/column exists
        String sql2 = "DELETE FROM projekt WHERE projektnummer = ?";

        try (PreparedStatement p1 = con.prepareStatement(sql1)) {
            p1.setInt(1, nr);
            p1.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        try (PreparedStatement p2 = con.prepareStatement(sql2)) {
            p2.setInt(1, nr);
            p2.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    @Override
    public Person getPerson(int nr) {
    	Person p = null;
        Connection con = establishConnection();
        String sql = "SELECT personennummer, vorname, nachname, position FROM person WHERE personennummer = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Person(rs.getInt("personennummer"), rs.getString("vorname"), rs.getString("nachname"), rs.getString("position"));
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
	
    @Override
    public Projekt getProjekt(int nr) {
    	Projekt p = null;
        Connection con = establishConnection();
        String sql = "SELECT projektnummer, name, kunde FROM projekt WHERE projektnummer = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Projekt(rs.getInt("projektnummer"), rs.getString("name"), rs.getString("kunde"));
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
        
    @Override
    public int getMaxNrPerson() {
    	int pMax = 0;
        Connection con = establishConnection();
        String sql = "SELECT MAX(personennummer) as maxnr FROM person";
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) {
                pMax = rs.getInt("maxnr");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pMax;
    }
	
        
    @Override
    public int getMaxNrProjekt() {
    	int pMax = 0;
        Connection con = establishConnection();
        String sql = "SELECT MAX(projektnummer) as maxnr FROM projekt";
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) {
                pMax = rs.getInt("maxnr");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pMax;
    }
	
    @Override
    public void insertPerson(Person p) {
        Connection con = establishConnection();
        String sql1 = "INSERT INTO person (personennummer, vorname, nachname, position) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql1)) {
            ps.setInt(1, p.getNr());
            ps.setString(2, p.getVorname());
            ps.setString(3, p.getNachname());
            ps.setString(4, p.getPosition());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadPersonal();
    }
	
    @Override
    public void insertProjekt(Projekt p) {
        Connection con = establishConnection();
        String sql = "INSERT INTO projekt (projektnummer, name, kunde) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getNr());
            ps.setString(2, p.getName());
            ps.setString(3, p.getKunde());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    @Override
    public void updatePerson(Person p) {
        Connection con = establishConnection();
        String sql = "UPDATE person SET vorname = ?, nachname = ?, position = ? WHERE personennummer = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getVorname());
            ps.setString(2, p.getNachname());
            ps.setString(3, p.getPosition());
            ps.setInt(4, p.getNr());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // refresh local cache
        loadPersonal();
    }
	
    @Override
    public void updateProjekt(Projekt p) {
        Connection con = establishConnection();
        String sql = "UPDATE projekt SET name = ?, kunde = ? WHERE projektnummer = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getKunde());
            ps.setInt(3, p.getNr());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
