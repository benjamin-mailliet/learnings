<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="modal fade" tabindex="-1" role="dialog" id="popupNote">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Noter ce travail</h4>
            </div>
            <div class="modal-body">
                <form>
                    <input type="hidden" name="idTravail" id="idTravail" name="idTravail"/>
                    <div class="form-group">
                        <label>Note</label>
                        <input class="form-control" type="number" value="10" name="noteValue" id="noteValue"/>
                    </div>
                    <div class="form-group">
                        <label>Commentaire</label>
                        <textarea class="form-control" id="noteComment" name="noteComment"></textarea>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-success" id="validerNote">Valider</button>
                <button class="btn btn-danger" data-dismiss="modal">Annuler</button>
            </div>
        </div>
    </div>
</div>