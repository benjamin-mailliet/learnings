<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
					<tr id="utilisateur${utilisateur.id}" style="display:none">
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