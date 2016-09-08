<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Import des utilisateurs</title>
		<c:import url="../../includes/htmlheader.jsp" />
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="utilisateur"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>
					Import des utilisateurs
					<small><a href="utilisateur" class="label label-default">Retour à la liste</a></small>
				</h1>
			</header>
			<form class="form-horizontal" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="fichier" class="col-sm-2 control-label">Fichier :</label>
					<div class="col-sm-6">
						<input type="file" class="form-control" id="fichier" name="fichier">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-primary">Importer</button>
					</div>
				</div>
			</form>
		</div>
	</body>
</html>