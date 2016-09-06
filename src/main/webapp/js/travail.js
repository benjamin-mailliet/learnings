$(document).ready(function(){
    $('#popupNote').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var idTravail = button.data('travail');
        $("#idTravail").val(idTravail);
    });

    var enregistrerNote = function(travailId, valeur, commentaire){
        $.ajax({
            method: "POST",
            url: "ws/note",
            data: {idTravail: travailId, note: valeur, commentaireNote: commentaire}
        })
            .done(function (data) {
                console.log("Enregistrement de la note OK");
            })
            .fail(function () {
                console.error("Erreur d'enregistrement de la note");
            })
    };


    $("#validerNote").click(function(){
        enregistrerNote($("#idTravail").val(), $("#noteValue").val(),$("#noteComment").val());
    })
});