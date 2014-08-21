
var supprimerUtilisateur = function(id) {
	$.post("utilisateur",{id:id, action:"supprimer"}).done(function() {
		$("#utilisateur"+id).hide("slow", function(){$(this).remove()});
	});
};

var donnerAdminUtilisateur = function(id) {
	$.post("utilisateur",{id:id, action:"donnerAdmin"}).done(function() {
		$("#utilisateur"+id+" .donnerAdminUtilisateurAction").hide("fast", function(){
			$("#utilisateur"+id+" .enleverAdminUtilisateurAction").show("fast");
		});
	});
};

var enleverAdminUtilisateur = function(id) {
	$.post("utilisateur",{id:id, action:"enleverAdmin"}).done(function() {
		$("#utilisateur"+id+" .enleverAdminUtilisateurAction").hide("fast", function(){
			$("#utilisateur"+id+" .donnerAdminUtilisateurAction").show("fast");
		});
	});
};

var reinitMdpUtilisateur = function(id) {
	$.post("utilisateur",{id:id, action:"reinitialiserMotDePasse"}).done(function() {
		
	});
};

$(document).ready(function(){
	$("#listeUtilisateurs .supprimerUtilisateurAction").on("click", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		supprimerUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs .donnerAdminUtilisateurAction").on("click", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		donnerAdminUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs .enleverAdminUtilisateurAction").on("click", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		enleverAdminUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs .reinitMdpUtilisateurAction").on("click", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		reinitMdpUtilisateur(idUtilisateur);
	});
});