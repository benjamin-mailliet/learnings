<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des projets</title>
		<c:import url="../../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="projet"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>
					<c:if test="${mode == 'creation'}">Ajouter un projet</c:if>
					<c:if test="${mode == 'modification'}">Modifier le projet n°${projet.id}</c:if>
					<small><a href="listeprojets" class="label label-default">Retour à la liste</a></small>
				</h1>
			</header>
			<form class="form-horizontal" method="post" action="projet">
				<div class="form-group">
			    	<label for="titre" class="col-sm-2 control-label">Titre</label>
			    	<div class="col-sm-6">
			      		<input type="text" class="form-control" id="titre" name="titre" value="${projet.titre}">
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="description" class="col-sm-2 control-label">Description</label>
			    	<div class="col-sm-6">
			      		<textarea rows="3" class="form-control editeur-riche" id="description" name="description">${projet.description}</textarea>
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="dateLimiteRenduLot1" class="col-sm-2 control-label">Date limite de rendu du lot 1</label>
			    	<div class="col-sm-2">
			      		<input type="text" class="form-control" id="dateLimiteRenduLot1" name="dateLimiteRenduLot1" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${projet.dateLimiteRenduLot1}" />" />
			      		(dd/MM/yyyy HH:mm) 
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="dateLimiteRenduLot2" class="col-sm-2 control-label">Date limite de rendu du lot 2</label>
			    	<div class="col-sm-2">
			      		<input type="text" class="form-control" id="dateLimiteRenduLot2" name="dateLimiteRenduLot2" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${projet.dateLimiteRenduLot2}" />" />
			      		(dd/MM/yyyy HH:mm) 
		    		</div>
			  	</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<c:if test="${mode == 'modification'}"><input type="hidden" name="id" value="${projet.id}" /></c:if>
						<button type="submit" class="btn btn-primary">
							<c:if test="${mode == 'creation'}">Ajouter</c:if>
							<c:if test="${mode == 'modification'}">Modifier</c:if>
						</button>
				    </div>
			  </div>
			</form>
		</div>
	</body>
</html>