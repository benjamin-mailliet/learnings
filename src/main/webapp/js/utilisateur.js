
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

var ajouterUtilisateur = function() {
	var email = $("#emailNouvelUtilisateur").val();
	if(email == null || email == undefined || email == "") {
		$("#emailNouvelUtilisateur").parent().addClass("has-error");
	} else {
		var admin = $("#adminNouvelUtilisateur").is(":checked");
		$.post("ajouterutilisateur",{email:email, admin:admin}).done(function(data) {
			$("#emailNouvelUtilisateur").parent().removeClass("has-error");
			$("#emailNouvelUtilisateur").val("");
			$("#adminNouvelUtilisateur").attr('checked', false);
			$("#nouvelUtilisateurRow").before(data);
			$("#nouvelUtilisateurRow").prev().show("slow");
		}).fail(function() {
			$("#emailNouvelUtilisateur").parent().addClass("has-error");
		});
	}
}

$(document).ready(function(){
	$("#listeUtilisateurs").on("click", ".supprimerUtilisateurAction", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		supprimerUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs").on("click", ".donnerAdminUtilisateurAction", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		donnerAdminUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs").on("click", ".enleverAdminUtilisateurAction", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		enleverAdminUtilisateur(idUtilisateur);
	});

	$("#listeUtilisateurs").on("click", ".reinitMdpUtilisateurAction", function() {
		var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
		reinitMdpUtilisateur(idUtilisateur);
	});
	
	$("#nouvelUtilisateurAction").on("click", function() {
		ajouterUtilisateur();
	})
});