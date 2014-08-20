<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<c:import url="../../includes/htmlheader.jsp">
		<c:param name="titrePage" value="Gestion des utilisateurs"/>
	</c:import>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="utilisateur"/>
		</c:import>
	
		<div class="container">
			<h2>Gestion des utilisateurs</h2>
			<table class="table table-striped">
				<tr>
					<th>#</th>
					<th>Email</th>
					<th>Admin</th>
					<th>Actions</th>
				</tr>
				<c:forEach var="utilisateur" items="${utilisateurs}">
				<tr>
					<td>${utilisateur.id}</td>
					<td>${utilisateur.email}</td>
					<td>${utilisateur.admin ? 'Oui' : 'Non'}</td>
					<td>
						<button type="button" class="btn btn-primary btn-xs" title="Réinitialiser le mot de passe"><span class="glyphicon glyphicon-repeat"></span></button>
						<c:if test="${!utilisateur.admin}">
							<button type="button" class="btn btn-success btn-xs" title="Donner l'admin"><span class="glyphicon glyphicon-chevron-up"></span></button>
						</c:if>
						<c:if test="${utilisateur.admin}">
							<button type="button" class="btn btn-warning btn-xs" title="Enlever l'admin"><span class="glyphicon glyphicon-chevron-down"></span></button>
						</c:if>
						<button type="button" class="btn btn-danger btn-xs" title="Supprimer l'utilisateur"><span class="glyphicon glyphicon-trash"></span></button>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>