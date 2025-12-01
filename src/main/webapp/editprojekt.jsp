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
      	Projekt p = null;
      	
      	if (nr != null)
      		p = db.getProjekt(nr);
      	
      	if (p == null)
    		p = new Projekt(db.getMaxNrProjekt()+1);
    %>
  
  
  <section class="section">
    <form action="index.jsp" method="post">
      	<input type="hidden" name="projnr" value="<%= p.getNr() %>"/>
    <div class="container">
      <h1 class="title">
        Projekt
      </h1>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="projnr" placeholder="Nr" value="<%= p.getNr() %>" disabled/>
      		</div>
      </div>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="name" placeholder="Name" value="<%= p.getName() %>"/>
      		</div>
      </div>
      <div class="columns">
     		<div class="column is-3">
      			<input class="input" type="text" name="kunde" placeholder="Kunde" value="<%= p.getKunde() %>"/>
      		</div>
      </div>
      
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
      
    </div>
    </form>
  </section>
  </body>
</html>