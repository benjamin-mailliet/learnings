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
			<c:param name="pageSelectionnee" value="remisetp"/>
		</c:import>		
		<div class="container">
			<c:import url="../includes/messages.jsp" />
			<header class="page-header"><h1>Rendre son TP</h1></header>
			<c:forEach var="tpAvecTravaux" items="${listeTp}">
				<article class="panel panel-default">
					<header class="panel-heading">
						<h3>${tpAvecTravaux.tp.titre} <small>Avant <fmt:formatDate value="${tpAvecTravaux.tp.dateLimiteRendu}" pattern="HH:mm"/> le <fmt:formatDate value="${tpAvecTravaux.tp.dateLimiteRendu}" pattern="dd/MM/yyyy"/></small></h3>
					</header>
					<section class="panel-body">
						<h4>Déjà rendu :</h4>
						<c:if test="${fn:length(tpAvecTravaux.travaux) == 0}">
							<p>Vous n'avez encore rien rendu pour ce TP.</p>
						</c:if>
						<c:if test="${fn:length(tpAvecTravaux.travaux) > 0}">
							<ul>
								<c:forEach var="travail" items="${tpAvecTravaux.travaux}">
									<li>${travail.nomFichier} <small><em><fmt:formatDate value="${travail.dateRendu}" pattern="dd/MM/yyyy HH:mm"/></em></small></li>
								</c:forEach>
							</ul>
						</c:if>
						<h4>Envoyer un fichier :</h4>
						<form role="form" method="post" enctype="multipart/form-data">
							<div class="form-group">
								<label for="nomEleve1">Binômes :</label>
								<div class="row">
									<div class="col-xs-4">
										<select class="form-control small-input" id="eleve1" name="eleve1" readonly>
											<option>${sessionScope.utilisateur.email}</option>
										</select>
									</div>
									<div class="col-xs-4">
										<select class="form-control small-input" id="eleve2" name="eleve2">
											<option value="" disabled selected>Choisir son binôme</option>
											<c:forEach var="binome" items="${listeBinomes}">
												<option value="${binome.id}">${binome.email}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="fichiertp">Fichier zip de votre TP :</label>
								<input type="file" id="fichiertp" name="fichiertp">
								<p class="help-block">Attention à ne pas dépasser les 10 Mo.</p>
							</div>
							<div class="form-group">
								<input type="hidden" name="idtp" value="${tpAvecTravaux.tp.id}" />
								<button type="submit" class="btn btn-default">Envoyer</button>
							</div>
						</form>
					</section>
				</article>
			</c:forEach>
		</div>
	</body>
</html>