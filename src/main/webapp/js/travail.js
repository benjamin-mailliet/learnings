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
            setTimeout(function () {  $("#noteValue").focus(); }, 500);
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
            .done(function () {
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
    });

    var getBodyMail = function(){
        return encodeURI($("#noteComment").val().replace("[[note]]",$("#noteValue").val()));
    };

    var getEmailEleves = function(travailId){
        var emails = "";
        $("#mailTravail"+travailId).find("li").each(function(index, element){
           emails = emails + ";"+element.textContent;
        });
        return emails;
    };

    var getObjectMail = function(){
        var titreTravail = "";
        var seance = $("#idSeance");
        if(seance.length>0) {
            titreTravail = seance.find("option[selected]")[0].textContent;
        }else{
            titreTravail = $("#idProjet").find("option[selected]")[0].textContent;
        }
        return "Note :  ".concat(titreTravail);
    };

    $("#mailNote").click(function(){
        window.location = "mailto:"+getEmailEleves($("#idTravail").val())+"?subject="+getObjectMail()+"&body="+getBodyMail();
    });


});