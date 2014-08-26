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
			<form role="form" class="form-horizontal">
				<div class="form-group">
					<label for="nomEleve1">Binômes :</label>
					<div class="row">
						<div class="col-xs-4">
							<select class="form-control small-input" id="eleve1" readonly>
								<option>Johnny Bigood</option>
							</select>
						</div>
						<div class="col-xs-4">
							<select class="form-control small-input" id="eleve2">
								<option>Sophie Lapixe</option>
								<option>Jonathan Davis</option>
								<option>Robert Chipoune</option>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="zipTP">Fichier zip de votre TP :</label>
					<input type="file" id="zipTP">
					<p class="help-block">Attention à ne pas dépasser les 10 Mo.</p>
				</div>
				<div class="form-group">
					<div class="checkbox">
						<label>
							<input type="checkbox"> Je confirme avoir travailler dur et avoir rendu le meilleur travail possible.
						</label>
					</div>
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-default">Envoyer</button>
				</div>
			</form>
		</div>
	</body>
</html>