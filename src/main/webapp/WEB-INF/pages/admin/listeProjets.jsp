<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des Projets</title>
		<c:import url="../../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="projet"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>Projet <a href="projet" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Ajouter</a></h1>
			</header>
			<table id="listeProjets" class="table table-striped">
				<tr>
					<th>#</th>
					<th>Titre</th>
					<th>Date limite de rendu du lot n°1</th>
					<th>Date limite de rendu du lot n°2</th>
					<th>Actions</th>
				</tr>
				<c:forEach var="projet" items="${projets}">
					<tr id="projet${projet.id}">
						<td>${projet.id}</td>
						<td>${projet.titre}</td>
						<td><fmt:formatDate value="${projet.dateLimiteRenduLot1}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td><fmt:formatDate value="${projet.dateLimiteRenduLot2}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td>
							<a class="btn btn-xs btn-info" href="projet?id=${projet.id}" title="Modifier"><span class="glyphicon glyphicon-edit"></span></a>
							<a class="btn btn-xs btn-warning" href="ressource?idProjet=${projet.id}" title="Gérer les ressources"><span class="glyphicon glyphicon-list-alt"></span></a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>