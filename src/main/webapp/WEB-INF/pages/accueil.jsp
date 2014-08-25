<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<div class="col-md-4 col-md-offset-4">
				<h1>Learnings</h1>
				
				<c:import url="../includes/messages.jsp" />
		
				<form role="form" method="post" action="connexion" class="well">
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
		</div>
	</body>
</html>