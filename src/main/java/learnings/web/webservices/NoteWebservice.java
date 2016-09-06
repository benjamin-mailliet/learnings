package learnings.web.webservices;

import learnings.managers.TravailManager;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/note")
public class NoteWebservice{

    @Path("/")
    @GET
    public Response getAllNotes(){
        return Response.status(200).entity("Toutes les notes ici").build();
    }

    @Path("/")
    @POST
    public Response enregistrerNote(@FormParam("idTravail") Long idTravail, @FormParam("note") BigDecimal note, @FormParam("commentaireNote") String commentaire)
    {
        TravailManager.getInstance().enregistrerNoteTravail(idTravail, note, commentaire);
        return Response.status(200).entity("OK").build();
    }
}
