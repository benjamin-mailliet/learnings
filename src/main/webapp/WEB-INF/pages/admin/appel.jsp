<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Gestion des séances</title>
		<c:import url="../../includes/htmlheader.jsp" />
		<script src="../js/appel.js"></script>
	</head>
	<body>
		<c:import url="../../includes/menuadmin.jsp">
			<c:param name="pageSelectionnee" value="seance"/>
		</c:import>
	
		<div class="container">
			<c:import url="../../includes/messages.jsp" />
			<header class="page-header">
				<h1>
					Appel de la séance "${seance.titre}"
					<small><a href="listeseances" class="label label-default">Retour à la liste</a></small>
				</h1>
			</header>
			<form class="form-horizontal" id="form-appel" method="post">
				<table id="tableau-appel" class="table table-bordered">
					<tr>
						<th>Eleve</th>
						<th>Groupe</th>
						<th colspan="5">Statut</th>
					</tr>
					<c:forEach var="appel" items="${appels}">
					<tr>
						<c:choose>
							<c:when test="${appel.statut == 'PRESENT'}"><c:set var="cssAppelCourant" value="success" /></c:when>
							<c:when test="${appel.statut == 'RETARD'}"><c:set var="cssAppelCourant" value="warning" /></c:when>
							<c:when test="${appel.statut == 'EXCUSE'}"><c:set var="cssAppelCourant" value="info" /></c:when>
							<c:when test="${appel.statut == 'ABSENT'}"><c:set var="cssAppelCourant" value="danger" /></c:when>
							<c:otherwise><c:set var="cssAppelCourant" value="active" /></c:otherwise>
						</c:choose>

						<td class="cellule-generale ${cssAppelCourant}">${appel.eleve.prenom} ${appel.eleve.nom}</td>
						<td class="cellule-generale ${cssAppelCourant}">${appel.eleve.groupe.libelle}</td>
						<td class="text-success ${appel.statut == 'PRESENT' ? 'success' : ''}">
							<label>
								<input type="radio" name="appel.${appel.eleve.id}" value="PRESENT" ${appel.statut == 'PRESENT' ? 'checked' : ''} class="radio-appel radio-present" />
								Présent
							</label>
						</td>
						<td class="text-warning ${appel.statut == 'RETARD' ? 'warning' : ''}">
							<label>
								<input type="radio" name="appel.${appel.eleve.id}" value="RETARD" ${appel.statut == 'RETARD' ? 'checked' : ''} class="radio-appel radio-retard" />
								Retard
							</label>
						</td>
						<td class="text-info ${appel.statut == 'EXCUSE' ? 'info' : ''}">
							<label>
								<input type="radio" name="appel.${appel.eleve.id}" value="EXCUSE" ${appel.statut == 'EXCUSE' ? 'checked' : ''} class="radio-appel radio-excuse" />
								Excusé
							</label>
						</td>
						<td class="text-danger ${appel.statut == 'ABSENT' ? 'danger' : ''}">
							<label>
								<input type="radio" name="appel.${appel.eleve.id}" value="ABSENT" ${appel.statut == 'ABSENT' ? 'checked' : ''} class="radio-appel radio-absent" />
								Absent
							</label>
						</td>
						<td class="text-active ${appel.statut == 'NON_SAISI' || appel.statut == null ? 'active' : ''}">
							<label>
								<input type="radio" name="appel.${appel.eleve.id}" value="NON_SAISI" ${appel.statut == 'NON_SAISI' || appel.statut == null ? 'checked' : ''} class="radio-appel radio-non-saisi" />
								Non saisi
							</label>
						</td>
					</tr>
					</c:forEach>
				</table>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="button" class="btn btn-default" id="boutonChargementSauvegardeLocale">Charger sauvegarde locale</button>
						<button type="button" class="btn btn-default" id="boutonSauvegardeLocale">Enregistrer en local</button>
						<button type="submit" class="btn btn-primary">Enregistrer</button>
				    </div>
			  </div>
			</form>
		</div>
	</body>
</html>