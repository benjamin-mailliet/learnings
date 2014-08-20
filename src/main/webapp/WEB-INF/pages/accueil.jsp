<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Accueil</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<script src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="container">
			<h1>Gestionnaire de cours</h1>
			<c:if test="${errorMessage != null && errorMessage != ''}">
				<div class="alert alert-danger" role="alert">${errorMessage}</div>
			</c:if>
	
			<form role="form" method="post" action="connexion">
				<div class="form-group">
					<label for="identifiant">Identifiant</label>
					<input type="text" class="form-control" id="identifiant" name="identifiant" placeholder="Identifiant">
				</div>
				<div class="form-group">
					<label for="motDePasse">Mot de passe</label>
					<input type="password" class="form-control" id="motDePasse" name="motDePasse" placeholder="Mot de passe">
				</div>
				<button type="submit" class="btn btn-default">Submit</button>
			</form>
	
		</div>
	</body>
</html>