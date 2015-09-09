<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Projet personnel</title>
		<c:import url="../includes/htmlheader.jsp" />
		<link href="../css/projet.css" rel="stylesheet">
		<!-- script src="../js/projet.js"></script-->
	</head>
	<body>
		<c:import url="../includes/menuprincipal.jsp">
			<c:param name="pageSelectionnee" value="projet"/>
		</c:import>		
		<div class="container">
			<c:import url="../includes/messages.jsp" />
			<header class="page-header"><h1>${projet.titre}</h1></header>
			
			<section class="row-fluid description">
				<article class="col-md-9">
						<div class="text-justify">${projet.description}</div>
				</article>
				<aside class="col-md-3">
					<div class="panel panel-default">
						<div class="panel-heading"><h4>Ressources à télécharger</h4></div>
						<div class="panel-body">
							<ul>
								<c:forEach var="ressource" items="${projet.ressources}">
									<li><a href="telechargerRessource?id=${ressource.id}">${ressource.titre}</a></li>	
								</c:forEach>
							</ul>
						</div>
					</div>
				</aside>
			</section>
			<section class="row-fluid">
				<article class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-heading"><h4>Travaux à rendre</h4></div>
						<div class="panel-body">
							<p>Pour nous rendre le projet, vous pouvez utiliser la page spécifique de rendu de projet. Il y a deux étapes et donc deux travaux à rendre pour ce projet.</p>
						
							<table class="table table-bordered table-striped">
								<thead>
									<tr>
										<th></th>
										<th>Date limite de rendu</th>
										<th>Contenu du lot</th>
									</tr>
								</thead>
								<tr>
									<td><h4>Etape 1</h4></td>
									<td><time><abbr title="Ne soyez pas en retard !"><fmt:formatDate value="${projet.dateLimiteRenduLot1}" pattern="dd/MM/yyyy à HH:mm" /></abbr></time></td>
									<td>
										<ul>
											<li>Pages HTML</li>
											<li>Styles CSS (sans utilisation de framework)</li>
											<li>Schéma de la BDD</li>
										</ul>
									</td>
								</tr>
								<tr>
									<td><h4>Etape 2</h4></td>
									<td><time><abbr title="Ne soyez pas en retard !"><fmt:formatDate value="${projet.dateLimiteRenduLot2}" pattern="dd/MM/yyyy à HH:mm" /></abbr></time></td>
									<td>
										<ul>
											<li>Lot 1 finalisé</li>
											<li>Dynamisation avec Javascript</li>
											<li>Gestion du stockage des données</li>
											<li>Code métier J2E</li>
										</ul>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</article>
				
			</section>
		</div>
	</body>
</html>