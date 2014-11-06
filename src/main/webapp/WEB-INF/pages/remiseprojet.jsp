<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Rendre le projet</title>
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
						<h2>Projet transversal personnel
					</header>
					<section class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<h3>Dates de rendu</h3>
									<ul>
										<li>Le lot 1 du projet doit être rendu avant le <strong><time><fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot1}" pattern="dd/MM/yyyy"/></time></strong> à <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot1}" pattern="HH:mm"/>. Il vous reste <strong>${projetAvecTravail.nbJoursRestantsLot1}</strong> jours.</li>
										<li>Le lot 2 du projet doit être rendu avant le <strong><time><fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot2}" pattern="dd/MM/yyyy"/></time></strong> à <fmt:formatDate value="${projetAvecTravail.projet.dateLimiteRenduLot2}" pattern="HH:mm"/>. Il vous reste <strong>${projetAvecTravail.nbJoursRestantsLot2}</strong> jours.</li>
									</ul>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-12">
								<h3>Projet actuellement soumis :</h3>
								<c:if test="${projetAvecTravail.travail == null}">
									<p>Vous n'avez encore rien rendu pour le projet.</p>
								</c:if>
								<c:if test="${projetAvecTravail.travail != null}">
									<c:choose>
			   							<c:when test="${projetAvecTravail.travail.chemin != null}">
											
											Fichier ayant le nom ${projetAvecTravail.travail.nomFichier} rendu le <small><em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="dd/MM/yyyy" /></em> à <em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="HH:mm" /></em></small>
											
										</c:when>
										<c:when test="${projetAvecTravail.travail.urlRepository != null}">
											
											Url de repository : ${projetAvecTravail.travail.urlRepository} rendu le <small><em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="dd/MM/yyyy"/></em> à <em><fmt:formatDate value="${projetAvecTravail.travail.dateRendu}" pattern="HH:mm" /></em></small>
											
										</c:when>
		  							</c:choose>
								</c:if>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-12">
								<h3>Envoyer un fichier ou renseigner un repository :</h3>
								<p>Si vous soumettez un projet, il remplacera le projet actuel.</p>
								<form role="form" method="post" enctype="multipart/form-data">
									<div class="row">
										<div class="form-group col-md-5">
											<label for="fichierprojet">Fichier zip de votre projet :</label>
											<input type="file" id="fichierprojet" name="fichierprojet">
											<p class="help-block">Attention à ne pas dépasser les 10 Mo.</p>
										</div>
										<div class="col-md-2">
											<strong>OU</strong>
										</div>
										<div class="form-group col-md-5">
											<label for="urlRepo">URL du repository GIT de votre projet :</label>
											<input type="url" class="form-control" id="urlRepo" name="urlRepo">
										</div>
									</div>
									<div class="row">
										<div class="form-group col-md-12">
											<label for="commentaire">Commentaire éventuel :</label>
											<textarea id="commentaire" name="commentaire" rows="5" class="form-control"></textarea>
										</div>
									</div>
									<div class="row">
										<div class="form-group col-md-12">
											<input type="hidden" name="idprojet" value="${projetAvecTravail.projet.id}" />
											<button type="submit" class="btn btn-default">Envoyer</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</section>
				</article>
			</section>
			
		</div>
	</body>
</html>