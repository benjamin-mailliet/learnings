<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
  <title>Tableau des notes</title>
  <c:import url="../../includes/htmlheader.jsp" />

</head>
<body>
<c:import url="../../includes/menuadmin.jsp">
  <c:param name="pageSelectionnee" value="note"/>
</c:import>

<div class="container">
  <header class="page-header">
    <h1>Notes des élèves</h1>
  </header>
  <table id="listeNotes" class="table table-striped">
    <tr>
      <th>Eleve</th>
      <c:forEach var="seance" items="${seancesNotees}">
        <th><div>${seance.titre}</div><div><small>(${seance.date})</small></div></th>
      </c:forEach>
      <th>Projet</th>
      <th>Moyenne</th>
    </tr>
    <c:forEach var="eleve" items="${eleves}">
      <tr id="eleve${eleve.id}">
        <td>${eleve.email}</td>
        <c:forEach var="seance" items="${seancesNotees}">
          <td>${eleve.mapSeanceIdTravail[seance.id].note}</td>
        </c:forEach>
        <td>${eleve.projet.note}</td>
        <td>${eleve.moyenne}</td>
      </tr>
    </c:forEach>
    <tr>
      <td></td>
      <c:forEach var="seance" items="${seancesNotees}">
        <td>${seance.moyenne}</td>
      </c:forEach>
      <td>${moyenneProjet}</td>
      <td>${moyenneClasse}</td>
    </tr>
  </table>
</div>
</body>
</html>