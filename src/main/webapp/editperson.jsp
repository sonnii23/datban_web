<%@ page import="database.*,java.util.*,model.*,helper.*" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Personal</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.6.1/css/bulma.min.css">
  </head>
  <body>
  
  <%
    Database db = EclipseStore.db();
      	Integer nr = Helper.convertToInt(request.getParameter("nr"));
      	Person p = null;
      	
      	if (nr != null)
      		p = db.getPerson(nr);
      	
      	if (p == null)
    		p = new Person(db.getMaxNrPerson()+1);
    %>
  
  
  <section class="section">
    <form action="index.jsp" method="post">
      	<input type="hidden" name="pernr" value="<%= p.getNr() %>"/>
    <div class="container">
      <h1 class="title">
        Personal
      </h1>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="pernr" placeholder="Nr" value="<%= p.getNr() %>" disabled/>
      		</div>
      </div>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="vorname" placeholder="Vorname" value="<%= p.getVorname() %>"/>
      		</div>
      </div>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="nachname" placeholder="Nachname" value="<%= p.getNachname() %>"/>
      		</div>
      </div>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="position" placeholder="Position" value="<%= p.getPosition() %>"/>
      		</div>
      </div>
      
      <div class="columns">
     		<div class="column is-12">
     		
    <label class="label">Projekte: </label></div></div>
  
  	  <% 
  	  
  	  Map<Integer, String> projekte = new HashMap<>();
  	  for (Projektmitarbeit pm: p.getProjekte())
  		  projekte.put(pm.getProjekt().getNr(), pm.getAufgabe());
  	  
  	  for (Projekt pro: db.getProjekte(null)) {
  		  String aufgabe="";
  		  if (projekte.containsKey(pro.getNr()))
  				  aufgabe = projekte.get(pro.getNr());
  		  %>
  	  <div class="columns">
     <div class="column is-2">
  	  
      <label class="checkbox">
      <input type="checkbox" name="projekt" value="<%= pro.getNr() %>"
      <% if (projekte.keySet().contains(pro.getNr())) { %>
        checked
      <% } %>
      >
      <%= pro.getName() %>
      </label></div>
      <div class="column is-3">
      	<input class="input" type="text" name="aufgabe<%= pro.getNr() %>" placeholder="Aufgabe" value="<%= aufgabe %>"/>
      </div>
  </div>
      <% } %>
  
    

<div class="columns">
     		<div class="column is-3">
      
      <button class="button is-success" type="submit">
     	 
    	<span class="icon">
      	<i class="fa fa-check"></i>
    	</span>
        </button>
    
  <a class="button is-danger" href="index.jsp">
    
    <span class="icon">
      <i class="fa fa-times"></i>
    </span>
  </a>
   
   </div></div>   
   </div>
    </form>
  </section>
  </body>
</html>