<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Rendre son TP</title>
		<c:import url="../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../includes/menuprincipal.jsp">
			<c:param name="pageSelectionnee" value="remiseprojet"/>
		</c:import>		
		<div class="container">
			<section id="sectionLot1">
				<c:import url="../includes/messages.jsp" />
				<header class="page-header"><h1>Remise du projet</h1></header>
				<article class="panel panel-default">
					<header class="panel-heading">
						<h2>Le lot 1 du projet doit être rendu avant le <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot1}" pattern="dd/MM/yyyy"/> à <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot1}" pattern="HH:mm"/></h2><br />
						<h2>Le lot 2 du projet doit être rendu avant le <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot2}" pattern="dd/MM/yyyy"/> à <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot2}" pattern="HH:mm"/></h2>
					</header>
					<section class="panel-body">
						<h4>Projet déjà soumis :</h4>
						<c:if test="${projetAvecTravail.travail == null}">
							<p>Vous n'avez encore rien rendu pour le projet.</p>
						</c:if>
						<c:if test="${projetAvecTravail.travail != null}">
							<c:choose>
	   							<c:when test="${projetAvecTravail.travail.chemin != null}">
									<ul>
										<li>${projetAvecTravail.travail.nomFichier} <small><em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="dd/MM/yyyy HH:mm"/></em></small></li>
									</ul>
								</c:when>
								<c:when test="${projetAvecTravail.travail.urlRepository != null}">
									<ul>
										<li>${projetAvecTravail.travail.urlRepository} <small><em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="dd/MM/yyyy HH:mm"/></em></small></li>
									</ul>
								</c:when>
  							</c:choose>
						</c:if>
						<h4>Envoyer un fichier :</h4>
						<form role="form" method="post" enctype="multipart/form-data">
							<div class="form-group">
								<label for="nomEleve1">Elève :</label>
								<div class="row">
									<div class="col-xs-4">
										<select class="form-control small-input" id="eleve1" name="eleve1" readonly>
											<option>${sessionScope.utilisateur.email}</option>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="fichierprojet">Fichier zip de votre projet :</label>
								<input type="file" id="fichierprojet" name="fichierprojet">
								<p class="help-block">Attention à ne pas dépasser les 10 Mo.</p>
							</div>
							<div class="form-group">
								<label for="urlRepo">URL du repository votre projet :</label>
								<input type="url" id="urlRepo" name="urlRepo">
							</div>
							<div class="form-group">
								<label for="commentaire">Commentaire éventuel :</label>
								<textarea id="commentaire" name="commentaire" rows="5" class="form-control"></textarea>
							</div>
							<div class="form-group">
								<input type="hidden" name="idprojet" value="${projetAvecTravail.projet.id}" />
								<button type="submit" class="btn btn-default">Envoyer</button>
							</div>
						</form>
					</section>
				</article>
			</section>
			
		</div>
	</body>
</html>