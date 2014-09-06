<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des séances</title>
		<c:import url="../../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="seance"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>
					<c:if test="${mode == 'creation'}">Ajouter une séance</c:if>
					<c:if test="${mode == 'modification'}">Modifier la séance n°${seance.id}</c:if>
					<small><a href="listeseances" class="label label-default">Retour à la liste</a></small>
				</h1>
			</header>
			<form class="form-horizontal" method="post" action="seance">
				<div class="form-group">
			    	<label for="titre" class="col-sm-2 control-label">Titre</label>
			    	<div class="col-sm-6">
			      		<input type="text" class="form-control" id="titre" name="titre" value="${seance.titre}">
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="description" class="col-sm-2 control-label">Description</label>
			    	<div class="col-sm-6">
			      		<textarea rows="3" class="form-control" id="description" name="description">${seance.description}</textarea>
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="date" class="col-sm-2 control-label">Date</label>
			    	<div class="col-sm-2">
			      		<input type="date" class="form-control" id="date" name="date" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${seance.date}"/>" />
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label class="col-sm-2 control-label">Noté</label>
			    	<div class="col-sm-10">
			      		<label class="checkbox-inline"><input type="radio" name="isNote" value="true" ${seance.isNote ? 'checked' : ''} /> Oui</label>
			      		<label class="checkbox-inline"><input type="radio" name="isNote" value="false" ${seance.isNote ? '' : 'checked'} /> Non</label>
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="dateLimiteRendu" class="col-sm-2 control-label">Date limite de rendu</label>
			    	<div class="col-sm-2">
			      		<input type="datetime" class="form-control" id="dateLimiteRendu" name="dateLimiteRendu" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${seance.dateLimiteRendu}" />" />
		    		</div>
			  	</div>
			  	<div class="form-group">
			    	<label for="type" class="col-sm-2 control-label">Type</label>
			    	<div class="col-sm-2">
			      		<select class="form-control" name="type" id="type">
			      			<option value="COURS" ${seance.type == 'COURS' ? 'selected' : ''}>Cours</option>
			      			<option value="TP" ${seance.type == 'TP' ? 'selected' : ''}>TP</option>
			      		</select>
		    		</div>
			  	</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<c:if test="${mode == 'modification'}"><input type="hidden" name="id" value="${seance.id}" /></c:if>
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