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
			<c:param name="pageSelectionnee" value="${type}"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header"> 
				<h1>
					<c:if test="${type=='seance'}">
						Ressources de la séance n°${enseignement.id}
						<small><a href="listeseances" class="label label-default">Retour à la liste</a></small>
					</c:if>
					<c:if test="${type=='projet'}">
						Ressources du projet n°${enseignement.id}
						<small><a href="listeprojets" class="label label-default">Retour à la liste</a></small>
					</c:if>
					
				</h1>
			</header>
			<form class="form" method="post" enctype="multipart/form-data">
				<table class="table table-striped">
					<tr>
						<th>#</th>
						<th>Titre</th>
						<th>Chemin</th>
						<th></th>
					</tr>
					<c:forEach var="ressource" items="${enseignement.ressources}">
						<tr>
							<td>${ressource.id}</td>
							<td>${ressource.titre}</td>
							<td><a href="../admin/telechargerRessource?id=${ressource.id}">${ressource.chemin}</a></td>
							<td>
								<c:if test="${type=='seance'}">
									<c:set var="urlSuppression" value="supprimerressource?idSeance=${enseignement.id}&amp;idRessource=${ressource.id}"></c:set>
								</c:if>
								<c:if test="${type=='projet'}">
									<c:set var="urlSuppression" value="supprimerressource?idProjet=${enseignement.id}&amp;idRessource=${ressource.id}"></c:set>
								</c:if>
								<a href="${urlSuppression}" class="btn btn-danger btn-xs" title="Supprimer la ressource"><span class="glyphicon glyphicon-trash"></span></a>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td>*</td>
						<td><input class="form-control" type="text" name="titre" /></td>
						<td><input type="file" name="fichier" /></td>
						<td>
							<c:if test="${type=='seance'}">
								<input type="hidden" name="seance" value="${enseignement.id}" />
							</c:if>
							<c:if test="${type=='projet'}">
								<input type="hidden" name="projet" value="${enseignement.id}" />
							</c:if>
							<input class="btn btn-primary" type="submit" value="Ajouter" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>