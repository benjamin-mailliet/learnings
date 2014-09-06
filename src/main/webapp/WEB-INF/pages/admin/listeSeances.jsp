<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des séances</title>
		<c:import url="../../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="seance"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>Séances <a href="seance" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Ajouter</a></h1>
			</header>
			<table id="listeSeances" class="table table-striped">
				<tr>
					<th>#</th>
					<th>Titre</th>
					<th>Date</th>
					<th>Noté</th>
					<th>Date limite de rendu</th>
					<th>Actions</th>
				</tr>
				<c:forEach var="seance" items="${seances}">
					<tr id="seance${seance.id}">
						<td>${seance.id}</td>
						<td>${seance.titre}</td>
						<td><fmt:formatDate value="${seance.date}" pattern="dd/MM/yyyy"/></td>
						<td>
							<c:if test="${seance.isNote}">
								<span class="label label-danger">Oui</span>
							</c:if>
							<c:if test="${!seance.isNote}">
								<span class="label label-success">Non</span>
							</c:if>
						</td>
						<td><fmt:formatDate value="${seance.dateLimiteRendu}" pattern="dd/MM/yyyy HH:mm"/></td>
						<td>
							<a class="btn btn-xs btn-info" href="seance?id=${seance.id}"><span class="glyphicon glyphicon-search"></span></a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>