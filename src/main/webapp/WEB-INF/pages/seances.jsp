<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Séances (Cours et TPs)</title>
		<c:import url="../includes/htmlheader.jsp" />
		<link href="../css/seances.css" rel="stylesheet">
		<script src="../js/seances.js"></script>
	</head>
	<body>
		<c:import url="../includes/menuprincipal.jsp">
			<c:param name="pageSelectionnee" value="seances"/>
		</c:import>		
		<div class="container">
			<c:import url="../includes/messages.jsp" />
			<header class="page-header"><h1>Cours et TPs</h1></header>
			
			<section class="panel-group">
				<c:forEach var="seance" items="${seances}">
					<c:if test="${seance.type=='COURS'}">
						<article class="cours-article panel panel-default">
							<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#cours${seance.id}">
								<h4><span  class="glyphicon glyphicon-folder-close"></span> ${seance.titre}<small>(cliquez pour ouvrir)</small></h4>
								<time>${seance.date}</time>
							</header>
							<section class="details panel-body collapse" id="cours${seance.id}">
								<p>${seance.description}</p>
								Ressources :
								<ul>
								<c:forEach var="ressource" items="${seance.ressources}">
									<li><a href="${ressource.chemin}">${ressource.titre}</a></li>	
								</c:forEach>
								</ul>
							</section>
						</article>
					</c:if> 
					<c:if test="${seance.type=='TP'}">
						<article class="tp-article panel panel-default">
							<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#tp${seance.id}">
								<h4><span  class="glyphicon glyphicon-cog"></span> ${seance.titre}<small>(cliquez pour ouvrir)</small></h4>
								<time>${seance.date}</time>
							</header>
							<section class="details panel-body collapse" id="tp${seance.id}">
								<p>${seance.description}</p>
								Ressources :
								<ul>
								<c:forEach var="ressource" items="${seance.ressources}">
									<li><a href="${ressource.chemin}">${ressource.titre}</a></li>	
								</c:forEach>
								</ul>
							</section>
						</article>
					</c:if>
				</c:forEach>			
			</section>
		</div>
	</body>
</html>