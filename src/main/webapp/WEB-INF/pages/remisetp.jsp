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
			<c:forEach var="tp" items="${listeTp}">
				<section class="well">
					<h3>${tp.titre}</h3>
					<p>Date limite de rendu : <fmt:formatDate value="${tp.dateLimiteRendu}" pattern="dd/MM/yyyy HH:mm"/></p>
					<form role="form" method="post" enctype="multipart/form-data">
						<div class="form-group">
							<label for="nomEleve1">Binômes :</label>
							<div class="row">
								<div class="col-xs-4">
									<select class="form-control small-input" id="eleve1" name="eleve1" readonly>
										<option>${sessionScope.utilisateur.email}</option>
									</select>
								</div>
								<div class="col-xs-4">
									<select class="form-control small-input" id="eleve2" name="eleve2">
										<c:forEach var="binome" items="${listeBinomes}">
											<option value="${binome.id}">${binome.email}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="fichiertp">Fichier zip de votre TP :</label>
							<input type="file" id="fichiertp" name="fichiertp">
							<p class="help-block">Attention à ne pas dépasser les 10 Mo.</p>
						</div>
						<div class="form-group">
							<input type="hidden" name="idtp" value="${tp.id}" />
							<button type="submit" class="btn btn-default">Envoyer</button>
						</div>
					</form>
				</section>
			</c:forEach>
		</div>
	</body>
</html>