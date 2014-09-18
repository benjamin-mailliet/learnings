<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Séances (Cours et TPs)</title>
		<c:import url="../includes/htmlheader.jsp" />
		<!-- link href="../css/projet.css" rel="stylesheet"-->
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
				<article class="col-md-12">
					<div class=" well">
						<div>${projet.description}</div>
					</div> 
				</article>
			</section>
			<section class="row-fluid">
				<article class="col-md-8">
					<div class="panel panel-default">
						<div class="panel-heading"><h4>Comment rendre le projet ?</h4></div>
						<div class="panel-body">
							<p>Pour nous rendre le projet, vous pouvez utiliser la page de rendu de projet qui sera accessible en temps voulu. Il y a deux étapes pour ce projet.</p>
						
							<table class="table table-bordered">
								<thead>
									<tr>
										<th></th>
										<th>Date limite de rendu</th>
										<th>Contenu du lot</th>
									</tr>
								</thead>
								<tr>
									<td>Etape 1</td>
									<td><time><fmt:formatDate value="${projet.dateLimiteRenduLot1}" pattern="dd/MM/yyyy à HH:mm" /></time></td>
									<td>
										<ul>
											<li>Pages HTML</li>
											<li>Styles CSS (sans utilisation de framework)</li>
											<li>Dynamisation avec Javascript</li>
										</ul>
									</td>
								</tr>
								<tr>
									<td>Etape 2</td>
									<td><time><fmt:formatDate value="${projet.dateLimiteRenduLot2}" pattern="dd/MM/yyyy à HH:mm" /></time></td>
									<td>
										Ensemble du projet
									</td>
								</tr>
							</table>
						</div>
					</div>
				</article>
				<aside class="col-md-3 col-md-offset-1">
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
		</div>
	</body>
</html>