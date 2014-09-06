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
						<c:set var="typeSeance" value="cours" scope="request"/>
					</c:if>
					<c:if test="${seance.type=='TP'}">
						<c:set var="typeSeance" value="tp" scope="request"/>
					</c:if>
					<article class="${typeSeance}-article panel ${seance.datePassee ? 'panel-info' : 'panel-default'}">
						<header class="panel-heading" data-toggle="collapse" data-toggle="collapse" data-target="#${typeSeance}${seance.id}">
							<h4>
								<span  class="glyphicon ${seance.type=='TP' ? 'glyphicon-cog' : 'glyphicon-folder-close'}"></span>
								${seance.titre}
								<c:if test="${seance.datePassee}"><small>(cliquez pour ouvrir)</small></c:if>
							</h4>
							<time><fmt:formatDate value="${seance.date}" pattern="dd/MM/yyyy" /></time>
						</header>
						<c:if test="${seance.datePassee}">
							<section class="details panel-body collapse" id="${typeSeance}${seance.id}">
								<p>${seance.description}</p>
								Ressources :
								<ul>
								<c:forEach var="ressource" items="${seance.ressources}">
									<li><a href="${ressource.chemin}">${ressource.titre}</a></li>	
								</c:forEach>
								</ul>
							</section>
						</c:if>
					</article>
				</c:forEach>			
			</section>
		</div>
	</body>
</html>