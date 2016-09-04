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
					Appel de la séance "${seance.titre}"
					<small><a href="listeseances" class="label label-default">Retour à la liste</a></small>
				</h1>
			</header>
			<form class="form-horizontal" method="post" action="appel">
				<table class="table table-bordered">
					<tr>
						<th>Email</th>
						<th colspan="5">Statut</th>
					</tr>
					<c:forEach var="appel" items="${appels}">
					<tr>
						<td>${appel.eleve.email}</td>
						<td class="success text-success">
							<label>
								<input type="radio" name="appel[${appel.eleve.id}]" value="PRESENT" ${appel.statut == 'PRESENT' ? 'checked' : ''}/>
								Présent
							</label>
						</td>
						<td class="warning text-warning">
							<label>
								<input type="radio" name="appel[${appel.eleve.id}]" value="RETARD" ${appel.statut == 'RETARD' ? 'checked' : ''}/>
								Retard
							</label>
						</td>
						<td class="info text-info">
							<label>
								<input type="radio" name="appel[${appel.eleve.id}]" value="EXCUSE" ${appel.statut == 'EXCUSE' ? 'checked' : ''}/>
								Excusé
							</label>
						</td>
						<td class="danger text-danger">
							<label>
								<input type="radio" name="appel[${appel.eleve.id}]" value="ABSENT" ${appel.statut == 'ABSENT' ? 'checked' : ''}/>
								Absent
							</label>
						</td>
						<td>
							<label>
								<input type="radio" name="appel[${appel.eleve.id}]" value="NON_SAISI" ${appel.statut == null ? 'checked' : ''}/>
								Non saisi
							</label>
						</td>
					</tr>
					</c:forEach>
				</table>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-primary">Enregistrer</button>
				    </div>
			  </div>
			</form>
		</div>
	</body>
</html>