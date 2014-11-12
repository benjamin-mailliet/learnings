<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Projets rendus</title>
<c:import url="../../includes/htmlheader.jsp" />
<script src="../js/utilisateur.js"></script>
</head>
<body>
	<c:import url="../../includes/menuadmin.jsp">
		<c:param name="pageSelectionnee" value="travailprojet" />
	</c:import>

	<div class="container">
		<c:import url="../../includes/messages.jsp" />
		<header class="page-header">
			<h1>Projets rendus</h1>
		</header>
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<form method="get" class="form-inline text-center well">
					<div class="form-group">
						<label class="sr-only" for="idProjet">Sélectionner un projet :</label>
						<select class="form-control" id="idProjet" name="idProjet">
							<c:forEach var="projet" items="${projets}">
								<option value="${projet.id}" ${projet.id == projetSelectionne.id ? 'selected' : ''}>${projet.titre}</option>
							</c:forEach>
						</select>
					</div>
					<button type="submit" class="btn btn-primary">Valider</button>
				</form>
			</div>
		</div>
		<c:if test="${projetSelectionne != null}">
			<c:if test="${fn:length(projetSelectionne.travauxRendus) > 0}">
				<p>Travaux rendus pour le projet « ${projetSelectionne.titre} » :</p>
				<table class="table table-bordered">
					<tr>
						<th>Fichier ou Repository</th>	
						<th>Élève</th>
						<th>Commentaire</th>
					</tr>
					<c:forEach var="travail" items="${projetSelectionne.travauxRendus}">
						<tr>
							<td>
								<p>
									<c:choose>
										<c:when test="${travail.chemin!=null}">
											<a href="telechargerTravail?id=${travail.id}">${travail.nomFichier}</a>
										</c:when>									
										<c:otherwise>
									      	${travail.urlRepository}
									    </c:otherwise>
									</c:choose>
									<br />
									<small>Rendu à <fmt:formatDate value="${travail.dateRendu}" pattern="HH:mm"/> le <fmt:formatDate value="${travail.dateRendu}" pattern="dd/MM/yyyy"/></small>
								</p>
							</td>
							<td>
								<ul>
									<c:forEach var="utilisateur" items="${travail.utilisateurs}">
										<li>${utilisateur.email}</li>
									</c:forEach>
								</ul>
							</td>
							<td>${travail.commentaire}</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<c:if test="${fn:length(projetSelectionne.travauxRendus) == 0}">
				<p>Aucun travail n'a encore été rendu pour le projet « ${projetSelectionne.titre} ».
			</c:if>
		</c:if>
	</div>
</body>
</html>