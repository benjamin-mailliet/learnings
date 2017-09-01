$(document).ready(function(){
    var showError = function(erreurTexte) {
        $("erreurNote").text(erreurTexte);
        $("#erreurNote").show();

    };

    $('#activerNoteParEleve').click(function(){
        if ($(this).is(':checked')) {
            $('#noteParEleve').show();
            $('#noteBinome').hide();
            $('.modal-dialog').css({width: '900px'});
        } else {
            $('#noteParEleve').hide();
            $('#noteBinome').show();
            $('.modal-dialog').css({width: '600px'});
        }
    });

    $('#popupNote').on('show.bs.modal', function (event) {
        $("#erreurNote").hide();
        var button = $(event.relatedTarget); // Button that triggered the modal
        var idRendu = button.data('travail');
        $("#formNote").hide();
        $("#ajaxLoading").show();
        $.ajax({
            method: "GET",
            url: "ws/note/tp/" + idRendu
        }).done(function (data) {
            $("#noteValue").val(data.note);
            $("#noteComment").val(data.commentaireNote);
            $("#idSeanceNote").val(data.binome.seance.id);
            $("#idEleve1").val(data.binome.eleve1.id);
            $("#idBinome").val(data.binome.id);

            $('#activerNoteParEleve').attr('checked',false);
            $('#contentActiveNoteParEleve').hide();
            if(data.binome.eleve2){
                $('#contentActiveNoteParEleve').show();
                $("#idEleve2").val(data.binome.eleve2.id);
            }
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

    var enregistrerNote = function(idSeance, idEleve, valeur, commentaire){
        $("#erreurNote").hide();
        $.ajax({
            method: "POST",
            url: "ws/note/tp",
            data: {idSeance: idSeance, idEleve:idEleve, note: valeur, commentaire: commentaire}
        })
            .done(function () {
                console.log("Enregistrement de la note OK");
                actualiserNote(idBinome, valeur);
                actualiserLigneTableau(idBinome);
                $('#popupNote').modal('hide');
            })
            .fail(function () {
                console.error("Erreur d'enregistrement de la note");
                showError("Erreur lors de l'enregistrement de la note");
            })
    };




    $("#validerNote").click(function(){
        var idSeance = $("#idSeanceNote").val();
        if($('#activerNoteParEleve').is(':checked')){
            var noteCommune = $("#note").val();
            var commentCommun = $("#noteComment").val();
            enregistrerNote(idSeance, $("#idEleve1").val(), noteCommune,commentCommun);
            enregistrerNote(idSeance, $("#idEleve2").val(), noteCommune,commentCommun);
        }else{
            enregistrerNote(idSeance, $("#idEleve1").val(), $("#noteEleve1").val(),$("#noteCommentEleve1").val());
            enregistrerNote(idSeance, $("#idEleve2").val(), $("#noteEleve2").val(),$("#noteCommentEleve2").val());
        }
    });

    /*var getBodyMail = function(){
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
    });*/


});