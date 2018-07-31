function afficherFormNote(isNoteParEleve) {
    if(isNoteParEleve) {
        $('#noteParEleve').show();
        $('#noteBinome').hide();
        $('.modal-dialog').css({width: '900px'});
    }else{
        $('#noteParEleve').hide();
        $('#noteBinome').show();
        $('.modal-dialog').css({width: '600px'});
    }
}
$(document).ready(function(){
    var showError = function(erreurTexte) {
        $("erreurNote").text(erreurTexte);
        $("#erreurNote").show();

    };

    $('#activerNoteParEleve').click(function(){
        afficherFormNote($(this).is(':checked'));
    });

    $('#popupNote').on('show.bs.modal', function (event) {
        $("#erreurNote").hide();
        var button = $(event.relatedTarget); // Button that triggered the modal
        var idSeance = button.data('seance');
        var idEleve1 = button.data('eleve1');
        var idEleve2 = button.data('eleve2');
        var prenomNomEleve1 = button.data('eleve1prenomnom');
        var prenomNomEleve2 = button.data('eleve2prenomnom');
        var idBinome = button.data('binome');

        $("#formNote").hide();
        $("#formNote")[0].reset();
        $("#ajaxLoading").show();
        var requests;
        if(idEleve2){
            requests = $.when(
                $.ajax({
                    method: "GET",
                    url: "ws/note/seance?seance=" + idSeance+"&eleve=" + idEleve1
                }),
                $.ajax({
                    method: "GET",
                    url: "ws/note/seance?seance=" + idSeance+"&eleve=" + idEleve2
                })
            )
        }else{
            requests =$.when($.ajax({
                method: "GET",
                url: "ws/note/seance?seance=" + idSeance+"&eleve=" + idEleve1
            }));
        }

        $("#idBinome").val(idBinome);
        $("#idSeanceNote").val(idSeance);
        $("#idEleve1").val(idEleve1);
        $('#contentActiveNoteParEleve').hide();
        if (idEleve2) {
            $('#contentActiveNoteParEleve').show();
            $("#idEleve2").val(idEleve2);
            $("#nomPrenomEleve1").text(prenomNomEleve1);
            $("#nomPrenomEleve2").text(prenomNomEleve2);
        }

        requests.then(function (dataNoteEleve1, dataNoteEleve2) {
            var noteEleve1 = Array.isArray(dataNoteEleve1) ? dataNoteEleve1[0] : dataNoteEleve1;
            if(noteEleve1) {
                var noteEleve2 = Array.isArray(dataNoteEleve2) ? dataNoteEleve2[0] : undefined;
                if (!noteEleve2 || noteEleve1.valeur == noteEleve2.valeur && noteEleve1.commentaire == noteEleve2.commentaire) {
                    $('#activerNoteParEleve').attr('checked', false);
                    $("#noteValue").val(noteEleve1.valeur);
                    $("#noteComment").val(noteEleve1.commentaire);
                    afficherFormNote(false);
                } else {
                    $('#activerNoteParEleve').attr('checked', true);
                    $("#noteValueEleve1").val(noteEleve1.valeur);
                    $("#noteCommentEleve1").val(noteEleve1.commentaire);
                    $("#noteValueEleve2").val(noteEleve2.valeur);
                    $("#noteCommentEleve2").val(noteEleve2.commentaire);
                    afficherFormNote(true);
                }
            }
            $("#ajaxLoading").hide();
            $("#formNote").show();
            setTimeout(function () {
                $("#noteValue").focus();
            }, 500);
        }).fail(function () {
            console.error("Erreur de chargement de la note de l'eleve 1");
            $("#loadingAjax").hide();
            showError("Erreur lors du chargement de la note de l'eleve 1");
        });
    });

    var  actualiserNote = function(eleveId, valeur) {
        var noteActuelle = $("#noteEleve" + eleveId);
        noteActuelle.html("<span>"+valeur+"</span>");
    };

    var actualiserLigneTableau = function(idBinome) {
        var ligneBinome = $("#ligneRendu" + idBinome);
        if (!ligneBinome.hasClass("success")) {
            ligneBinome.addClass("success");
        }
    };

    var enregistrerNote = function(idSeance, idEleve, valeur, commentaire){
        $("#erreurNote").hide();
        $.ajax({
            method: "POST",
            url: "ws/note/tp",
            data: {idSeance: idSeance, idEleve:idEleve, note: valeur, commentaireNote: commentaire}
        })
            .done(function () {
                console.log("Enregistrement de la note OK");
                var idBinome = $('#idBinome').val();
                actualiserNote(idEleve, valeur);
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
            enregistrerNote(idSeance, $("#idEleve1").val(), $("#noteValueEleve1").val(),$("#noteCommentEleve1").val());
            enregistrerNote(idSeance, $("#idEleve2").val(), $("#noteValueEleve2").val(),$("#noteCommentEleve2").val());
        }else{
            var noteCommune = $("#noteValue").val();
            var commentCommun = $("#noteComment").val();
            enregistrerNote(idSeance, $("#idEleve1").val(), noteCommune,commentCommun);
            var idEleve2 = $("#idEleve2").val();
            if(idEleve2) {
                enregistrerNote(idSeance, idEleve2, noteCommune, commentCommun);
            }
        }
    });
});