var supprimerUtilisateur = function (id) {
    $.post("utilisateur", {id: id, action: "supprimer"}).done(function () {
        $("#utilisateur" + id).hide("slow", function () {
            $(this).remove()
        });
    });
};

var donnerAdminUtilisateur = function (id) {
    $.post("utilisateur", {id: id, action: "donnerAdmin"}).done(function () {
        $("#utilisateur" + id + " .donnerAdminUtilisateurAction").hide("fast", function () {
            $("#utilisateur" + id + " .enleverAdminUtilisateurAction").show("fast");
        });
    });
};

var enleverAdminUtilisateur = function (id) {
    $.post("utilisateur", {id: id, action: "enleverAdmin"}).done(function () {
        $("#utilisateur" + id + " .enleverAdminUtilisateurAction").hide("fast", function () {
            $("#utilisateur" + id + " .donnerAdminUtilisateurAction").show("fast");
        });
    });
};

var reinitMdpUtilisateur = function (id) {
    $.post("utilisateur", {id: id, action: "reinitialiserMotDePasse"}).done(function () {

    });
};

var estVide = function (valeur) {
    return valeur == null || valeur == undefined || valeur == "";
};

var validerFormulaireNouvelUtilisateur = function () {
    var formOk = true;
    if (estVide($("#nomNouvelUtilisateur").val())) {
        $("#nomNouvelUtilisateur").parent().addClass("has-error");
        formOk = false;
    }
    if (estVide($("#prenomNouvelUtilisateur").val())) {
        $("#prenomNouvelUtilisateur").parent().addClass("has-error");
        formOk = false;
    }
    if (estVide($("#emailNouvelUtilisateur").val())) {
        $("#emailNouvelUtilisateur").parent().addClass("has-error");
        formOk = false;
    }
    return formOk;
};

var reinitFormulaireNouvelUtilisateur = function () {
    $("#nomNouvelUtilisateur").parent().removeClass("has-error");
    $("#nomNouvelUtilisateur").val("");
    $("#prenomNouvelUtilisateur").parent().removeClass("has-error");
    $("#prenomNouvelUtilisateur").val("");
    $("#emailNouvelUtilisateur").parent().removeClass("has-error");
    $("#emailNouvelUtilisateur").val("");
};

var getUtilisateurJson = function () {
    return {
        nom: $("#nomNouvelUtilisateur").val(),
        prenom: $("#prenomNouvelUtilisateur").val(),
        email: $("#emailNouvelUtilisateur").val(),
        groupe: $("#groupeNouvelUtilisateur").val(),
        admin: $("#adminNouvelUtilisateur").is(":checked")
    };
};

var ajouterUtilisateur = function () {
    if (validerFormulaireNouvelUtilisateur()) {
        $.post("ajouterutilisateur", getUtilisateurJson()).done(function (data) {
            reinitFormulaireNouvelUtilisateur();
            $("#adminNouvelUtilisateur").attr('checked', false);
            $("#nouvelUtilisateurRow").before(data);
            $("#nouvelUtilisateurRow").prev().show("slow");
        }).fail(function (data) {
            $("#nomNouvelUtilisateur").parent().addClass("has-error");
            $("#prenomNouvelUtilisateur").parent().addClass("has-error");
            $("#emailNouvelUtilisateur").parent().addClass("has-error");
        });
    }
};

$(document).ready(function () {
    $("#listeUtilisateurs").on("click", ".supprimerUtilisateurAction", function () {
        var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
        supprimerUtilisateur(idUtilisateur);
    });

    $("#listeUtilisateurs").on("click", ".donnerAdminUtilisateurAction", function () {
        var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
        donnerAdminUtilisateur(idUtilisateur);
    });

    $("#listeUtilisateurs").on("click", ".enleverAdminUtilisateurAction", function () {
        var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
        enleverAdminUtilisateur(idUtilisateur);
    });

    $("#listeUtilisateurs").on("click", ".reinitMdpUtilisateurAction", function () {
        var idUtilisateur = $(this).parents("tr").attr("id").substr("utilisateur".length);
        reinitMdpUtilisateur(idUtilisateur);
    });

    $("#nouvelUtilisateurAction").on("click", function () {
        ajouterUtilisateur();
    });

    $("#boutonEnvoiMails").on("click", function () {
        $.get("ws/utilisateur/emails", function (data) {
            $("#listeEmailsContainer").show();
            $("#listeEmailsData").find("textarea").val(data);
        });
    });

    $("#boutonOuvrirClientMail").on("click", function () {
        window.location.href = "mailto:" + $("#listeEmailsData").find("textarea").val();
    });

    $("#listeEmailsContainer").on("click", function () {
        $("#listeEmailsContainer").hide();
    });

    $("#boutonFermerModal").on("click", function () {
        $("#listeEmailsContainer").hide();
    });

    $("#listeEmailsModal").on("click", function (event) {
        event.stopPropagation();
    });
});