package learnings.web.webservices;

import com.google.gson.Gson;
import learnings.managers.TravailManager;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/note")
public class NoteWebservice{

    @Path("/{idTravail}")
    @Produces("application/json;charset=UTF-8")
    @GET
    public String getTravailForNote(@PathParam("idTravail") Long idTravail)
    {
        Gson gsonParser = new Gson();
        return gsonParser.toJson(TravailManager.getInstance().getTravail(idTravail));
    }

    @Path("/")
    @POST
    public Response enregistrerNote(@FormParam("idTravail") Long idTravail, @FormParam("note") BigDecimal note, @FormParam("commentaireNote") String commentaire)
    {
        TravailManager.getInstance().enregistrerNoteTravail(idTravail, note, commentaire);
        return Response.status(200).entity("OK").build();
    }
}
