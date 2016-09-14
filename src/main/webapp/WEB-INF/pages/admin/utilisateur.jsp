<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des utilisateurs</title>
		<c:import url="../../includes/htmlheader.jsp" />
		<script src="../js/utilisateur.js"></script>
		<link href="../css/utilisateur.css" rel="stylesheet">
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="utilisateur"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>
					Gestion des utilisateurs
					<a href="importutilisateurs" class="btn btn-success"><i class="fa fa-cloud-upload"></i> Importer utilisateurs</a>
					<button id="boutonEnvoiMails" class="btn btn-default pull-right"><i class="fa fa-envelope" ></i> Envoyer Email</button>
				</h1>
			</header>
			<table id="listeUtilisateurs" class="table table-striped">
				<tr>
					<th>#</th>
					<th>Nom</th>
					<th>Prenom</th>
					<th>Email</th>
					<th>Groupe</th>
					<th>Admin</th>
					<th style="width:20%">Actions</th>
				</tr>
				<c:forEach var="utilisateur" items="${utilisateurs}">
					<tr id="utilisateur${utilisateur.id}">
						<td>${utilisateur.id}</td>
						<td>${utilisateur.nom}</td>
						<td>${utilisateur.prenom}</td>
						<td>${utilisateur.email}</td>
						<td>${utilisateur.groupe.libelle}</td>
						<td>${utilisateur.admin ? 'Oui' : 'Non'}</td>
						<td>
							<c:if test="${utilisateur.id != sessionScope.utilisateur.id}">
								<button type="button" class="btn btn-primary btn-xs reinitMdpUtilisateurAction" title="Réinitialiser le mot de passe"><span class="glyphicon glyphicon-repeat"></span></button>
								<button type="button" class="btn btn-danger btn-xs supprimerUtilisateurAction" title="Supprimer l'utilisateur"><span class="glyphicon glyphicon-trash"></span></button>
								<button style="${utilisateur.admin ? 'display:none;' : ''}" type="button" class="btn btn-success btn-xs donnerAdminUtilisateurAction" title="Donner l'admin"><span class="glyphicon glyphicon-chevron-up"></span></button>
								<button style="${!utilisateur.admin ? 'display:none;' : ''}" type="button" class="btn btn-warning btn-xs enleverAdminUtilisateurAction" title="Enlever l'admin"><span class="glyphicon glyphicon-chevron-down"></span></button>
							</c:if>
						</td>
					</tr>
				</c:forEach>
				<tr id="nouvelUtilisateurRow">
					<td></td>
					<td><input type="text" class="form-control input-sm" id="nomNouvelUtilisateur" name="nomNouvelUtilisateur" placeholder="Nom"></td>
					<td><input type="text" class="form-control input-sm" id="prenomNouvelUtilisateur" name="prenomNouvelUtilisateur" placeholder="Prénom"></td>
					<td>
						<div class="input-group">
			   	 			<div class="input-group-addon">@</div>
							<input type="email" class="form-control input-sm" id="emailNouvelUtilisateur" name="emailNouvelUtilisateur" placeholder="Email">
						</div>
					</td>
					<td>
						<select class="form-control input-sm" id="groupeNouvelUtilisateur" name="groupeNouvelUtilisateur">
							<option value=""></option>
							<c:forEach var="groupe" items="${groupes}">
								<option value="${groupe}">${groupe.libelle}</option>
							</c:forEach>
						</select>
					</td>
					<td><input type="checkbox" id="adminNouvelUtilisateur" name="adminNouvelUtilisateur" ></td>
					<td><button type="button" class="btn btn-info btn-xs" id="nouvelUtilisateurAction"><span class="glyphicon glyphicon-plus"></span></button></td>				
				</tr>
			</table>
		</div>

		<div id="listeEmailsContainer" style="display: none">
			<div class="container">
				<div class="row">
					<div class="col-md-6 col-md-offset-3">
						<div id="listeEmailsModal" class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title">Voici la liste des adresses email des élèves :</h3>
							</div>
							<div class="panel-body">
								<p id="listeEmailsData"><textarea class="form-control"></textarea></p>
							</div>
							<div class="panel-footer">
								<button class="btn btn-primary" id="boutonOuvrirClientMail">Ouvrir un clien mail</button>
								<button class="btn btn-default" id="boutonFermerModal">Fermer</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>