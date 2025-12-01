<%@ page import="database.*,java.util.*,model.*,helper.*" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Projektverwaltung</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">
  </head>
  <body>
    
  <%
      Database db = EclipseStore.db();
        	
           // Parameter auslesen
        	 Integer delperson = Helper.convertToInt(request.getParameter("delperson"));
           Integer delprojekt = Helper.convertToInt(request.getParameter("delprojekt"));
        	 String persort = request.getParameter("persort");
        	 String projsort = request.getParameter("projsort");
        	 String filter = request.getParameter("filter");
        	 
        	 if (filter == null)
        		 filter = "";
        	 
        	 // Aktionen
        	 if (delperson != null)
        	 	db.deletePerson(delperson);
        	 if (delprojekt != null)
        	 	db.deleteProjekt(delprojekt);
        	 
        	 // neue Person?
           String pernr = request.getParameter("pernr");
        	 if (pernr != null) {
        		 Integer nr = Helper.convertToInt(pernr);
        		 if (nr != null) {
        			 Person p = db.getPerson(nr);
        			 String vorname = request.getParameter("vorname");
        			 String nachname = request.getParameter("nachname");
        			 String position = request.getParameter("position");
        			 String[] projekte = request.getParameterValues("projekt");
        			 
        			 List<Projektmitarbeit> pm = new ArrayList<>();
        			 if (projekte != null) {
        				 for (String projekt: projekte) {
        					 Integer pnr = Helper.convertToInt(projekt);
        					 if (pnr != null) {
        						String aufgabe = request.getParameter("aufgabe"+pnr);
        					 	pm.add(new Projektmitarbeit(db.getProjekt(pnr), aufgabe));
        					 }
        				 }
        			 }
        			 
        			 if (p != null)	{ // gefunden
        				 p.setVorname(vorname);
        			 	 p.setNachname(nachname);
        			 	 p.setPosition(position);
        			 	 p.setProjekte(pm);
        			 	 db.updatePerson(p);
        			 } else { // neu
        				 p = new Person(nr, vorname, nachname, position);
        				 p.setProjekte(pm);
        			 	 db.insertPerson(p);
        			 }

        		 }
        	 }
        	 
       	 // neues Projekt?
           String projnr = request.getParameter("projnr");
        	 if (projnr != null) {
        		 Integer nr = Helper.convertToInt(projnr);
        		 if (nr != null) {
        			 Projekt p = db.getProjekt(nr);
        			 String name = request.getParameter("name");
        			 String kunde = request.getParameter("kunde");
        			 if (p != null)	{ // gefunden
        				 p.setName(name);
        			 	 p.setKunde(kunde);
        			 	 db.updateProjekt(p);
        			 } else { // neu
        				 p = new Projekt(nr, name, kunde);
        			 	 db.insertProjekt(p);
        			 }

        		 }
        	 }
      %>
  <section class="section">
    <div class="container">
      <h1 class="title">
        Personal <a class="button is-primary" href="editperson.jsp"><span class="icon"><i class="fa fa-plus-square"></i></span></a>
      </h1>
      <form>
      	<div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" placeholder="Nachname" name="filter" value="<%= filter%>"/>
      			
      		</div>
      		<div class="column is-3">
      			<button class="button is-primary" type="submit">
     	 
    			<span class="icon">
      			<i class="fa fa-search"></i>
    			</span>
        		</button>
      		</div>
      	</div>
      </form>
      <table class="table is-striped">
        <thead>
          <tr>
            <th><div>Nr. <a class="button is-small" href="?persort=nr&projsort=<%= projsort %>&filter=<%= filter %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></div></th>
            <th>Vorname <a class="button is-small" href="?persort=vorname&projsort=<%= projsort %>&filter=<%= filter %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Nachname <a class="button is-small" href="?persort=nachname&projsort=<%= projsort %>&filter=<%= filter %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Position <a class="button is-small" href="?persort=position&projsort=<%= projsort %>&filter=<%= filter %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Projektmitarbeit (Aufgabe)</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
        
        <% 
           List<Person> personal = db.getPersonal(persort, filter);
           for (Person p: personal) {
        %>
          <tr>
            <td><%= p.getNr() %></td>
            <td><%= p.getVorname() %></td>
            <td><%= p.getNachname() %></td>
            <td><%= p.getPosition() %></td>
            <td>
            
            <% List<Projektmitarbeit> projekte = p.getProjekte();
               for (int i=0; i<projekte.size(); i++) {
            	   Projektmitarbeit pm = projekte.get(i);
            %>
            	<%= pm.getProjekt().getName() %> (<%= pm.getAufgabe() %>)<%
            	   if ( i <projekte.size()-1 ) out.println(",");
            	%>
            <% } %>
            
            </td>
            <td>
              <a class="button" href="editperson.jsp?nr=<%= p.getNr() %>"><span class="icon"><i class="fa fa-edit"></i></span></a>
                <a class="button" href="?delperson=<%= p.getNr() %>&persort=<%= persort %>&projsort=<%= projsort %>&filter=<%= filter %>"><span class="icon"><i class="fa fa-trash"></i></span></a></td>
          </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  
    <div class="container">
      <h1 class="title">
        Projekte <a class="button is-primary" href="editprojekt.jsp"><span class="icon"><i class="fa fa-plus-square"></i></span></a>
      </h1>
      <table class="table">
        <thead>
          <tr>
            <th>Nr. <a class="button is-small" href="?projsort=nr&persort=<%= persort %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Name <a class="button is-small" href="?projsort=name&persort=<%= persort %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Kunde <a class="button is-small" href="?projsort=kunde&persort=<%= persort %>"><span class="icon"><i class="fa fa-sort-down"></i></span></a></th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
        
         <% List<Projekt> projekte = db.getProjekte(projsort);
           for (Projekt p: projekte) {
         %>
      
          <tr>
            <td><%= p.getNr() %></td>
            <td><%= p.getName() %></td>
            <td><%= p.getKunde() %></td>
            <td><a class="button" href="editprojekt.jsp?nr=<%= p.getNr() %>"><span class="icon"><i class="fa fa-edit"></i></span></a>
                <a class="button" href="?delprojekt=<%= p.getNr() %>&persort=<%= persort %>&projsort=<%= projsort %>"><span class="icon"><i class="fa fa-trash"></i></span></a></td>
          </tr>
          
          <% } %>
          
        </tbody>
      </table>
    </div>
  </section> 
  
  </body>
</html>
