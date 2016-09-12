$(document).ready(function(){
    var showError = function(erreurTexte) {
        $("erreurNote").text(erreurTexte);
        $("#erreurNote").show();
    };

    $('#popupNote').on('show.bs.modal', function (event) {
        $("#erreurNote").hide();
        var button = $(event.relatedTarget); // Button that triggered the modal
        var idTravail = button.data('travail');
        $("#formNote").hide();
        $("#ajaxLoading").show();
        $.ajax({
            method: "GET",
            url: "ws/note/" + idTravail
        }).done(function (data) {
            $("#noteValue").val(data.note);
            $("#noteComment").val(data.commentaireNote);
            $("#idTravail").val(idTravail);
            $("#ajaxLoading").hide();
            $("#formNote").show();
        })
        .fail(function () {
            console.error("Erreur de chargement de la note");
            $("#loadingAjax").hide();
                showError("Erreur lors du chargement de la note");
            });
    });

    var  actualiserNote = function(travailId, valeur) {
        var noteActuelle = $("#noteActuelle" + travailId);
        noteActuelle.text(valeur);
        };

    var actualiserLigneTableau = function(travailId) {
        var ligneTravail = $("#ligneTravail" + travailId);
        if (!ligneTravail.hasClass("success")) {
            ligneTravail.addClass("success");
        }
    };

    var enregistrerNote = function(travailId, valeur, commentaire){
        $("#erreurNote").hide();
        $.ajax({
            method: "POST",
            url: "ws/note",
            data: {idTravail: travailId, note: valeur, commentaireNote: commentaire}
        })
            .done(function (data) {
                console.log("Enregistrement de la note OK");
                actualiserNote(travailId, valeur);
                actualiserLigneTableau(travailId);
                $('#popupNote').modal('hide');
            })
            .fail(function () {
                console.error("Erreur d'enregistrement de la note");
                showError("Erreur lors de l'enregistrement de la note");
            })
    };


    $("#validerNote").click(function(){
        enregistrerNote($("#idTravail").val(), $("#noteValue").val(),$("#noteComment").val());
    })
});