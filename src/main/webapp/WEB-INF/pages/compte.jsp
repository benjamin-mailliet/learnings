<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Mon compte</title>
		<c:import url="../includes/htmlheader.jsp" />
		<link href="../css/fichiers-utiles.css" rel="stylesheet">
		<script src="../js/fichiers-utiles.js"></script>
	</head>
	<body>
		<c:import url="../includes/menuprincipal.jsp">
			<c:param name="pageSelectionnee" value=""/>
		</c:import>		
		<div class="container">
			<header class="page-header"><h1>Mon compte</h1></header>
			<section class="panel-group">
				<h2>Changer le mot de passe</h2>
				<c:if test="${errorMessage != null && errorMessage != ''}">
					<div class="alert alert-danger" role="alert">${errorMessage}</div>
				</c:if>
				<form class="form-horizontal" role="form" method="post" action="compte">
  					<div class="form-group">
    					<label for="motDePasse" class="col-sm-4 control-label">Nouveau mot de passe</label>
    					<div class="col-sm-4">
      						<input type="password" class="form-control" id="motDePasse" name="motDePasse">
    					</div>
  					</div>
  					<div class="form-group">
    					<label for="motDePasseConfirm" class="col-sm-4 control-label">Nouveau mot de passe (Confirmation)</label>
    					<div class="col-sm-4">
      						<input type="password" class="form-control" id="motDePasseConfirm" name="motDePasseConfirm">
    					</div>
  					</div>
  					<div class="form-group">
   						<div class="col-sm-offset-4 col-sm-4">
     				 		<button type="submit" class="btn btn-default">Changer le mot de passe</button>
    					</div>
  					</div>
				</form>
			</section>
		</div>
	</body>
</html>