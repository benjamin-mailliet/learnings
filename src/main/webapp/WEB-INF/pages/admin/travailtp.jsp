<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Travaux rendus</title>
<c:import url="../../includes/htmlheader.jsp" />
	<script src="../js/utilisateur.js"></script>
	<script src="../js/travail.js"></script>

</head>
<body>
	<c:import url="../../includes/menuadmin.jsp">
		<c:param name="pageSelectionnee" value="travailtp" />
	</c:import>

	<c:import url="../../includes/popupnote.jsp">
	</c:import>

	<div class="container">
		<c:import url="../../includes/messages.jsp" />
		<header class="page-header">
			<h1>Travaux rendus</h1>
		</header>
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<form method="get" class="form-inline text-center well">
					<div class="form-group">
						<label class="sr-only" for="idSeance">Sélectionner une séance :</label>
						<select class="form-control" id="idSeance" name="idSeance">
							<c:forEach var="seance" items="${seances}">
								<option value="${seance.id}" ${seance.id == seanceSelectionnee.id ? 'selected' : ''}>${seance.titre}</option>
							</c:forEach>
						</select>
					</div>
					<button type="submit" class="btn btn-primary">Valider</button>
				</form>
			</div>
		</div>
		<c:if test="${seanceSelectionnee != null}">
			<c:if test="${fn:length(seanceSelectionnee.travauxRendus) > 0}">
				<p>Travaux rendu pour la séance « ${seanceSelectionnee.titre} » :</p>
				<table class="table table-bordered">
					<tr>
						<th>Fichier</th>
						<th>Élèves</th>
						<th>Commentaire</th>
						<th>Note</th>
						<th>Actions</th>
					</tr>
					<c:forEach var="travail" items="${seanceSelectionnee.travauxRendus}">
						<tr class="${(travail.note!='' &&  travail.note!=null) ? 'success' : ''}" id="ligneTravail${travail.id}">
							<td>
								<p>
									<a href="telechargerTravail?id=${travail.id}">${travail.nomFichier}</a><br>
									<small>Rendu à <fmt:formatDate value="${travail.dateRendu}" pattern="HH:mm"/> le <fmt:formatDate value="${travail.dateRendu}" pattern="dd/MM/yyyy"/></small>
								</p>
							</td>
							<td>
								<ul id="mailTravail${travail.id}">
									<c:forEach var="utilisateur" items="${travail.utilisateurs}">
										<li>${utilisateur.email}</li>
									</c:forEach>
								</ul>
							</td>
							<td>${travail.commentaire}</td>
							<td><h4 id="noteActuelle${travail.id}">${travail.note}</h4></td>
							<td>
								<button id="noterTravail${travail.id}" type="button" class="btn btn-info btn-note" data-toggle="modal" data-target="#popupNote" data-travail="${travail.id}" title="Gérer l'évaluation"><i class="fa fa-tags" aria-hidden="true"></i></button>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<c:if test="${fn:length(seanceSelectionnee.travauxRendus) == 0}">
				<p>Aucun travail n'a encore été rendu pour la séance « ${seanceSelectionnee.titre} ».
			</c:if>
		</c:if>
	</div>
</body>
</html>