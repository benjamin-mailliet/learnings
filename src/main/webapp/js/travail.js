$(document).ready(function(){
    $('#popupNote').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var idTravail = button.data('travail');
        $("#popupNote").children("#idTravail").val(idTravail);
    });
});