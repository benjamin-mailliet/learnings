package learnings.web.webservices;

import com.google.gson.Gson;
import learnings.managers.NoteManager;
import learnings.managers.RenduTpManager;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/note")
public class NoteWebservice{

    @Path("/tp/{idRendu}")
    @Produces("application/json;charset=UTF-8")
    @GET
    public String getRenduTpForNote(@PathParam("idRendu") Long idRendu)
    {
        Gson gsonParser = new Gson();
        return gsonParser.toJson(RenduTpManager.getInstance().getRenduTp(idRendu));
    }

    @Path("/seance")
    @Produces("application/json;charset=UTF-8")
    @GET
    public String getNoteBySeanceAndEleve(@QueryParam("seance") Long idSeance, @QueryParam("eleve") Long idEleve){
        Gson gsonParser = new Gson();
        return gsonParser.toJson(NoteManager.getInstance().getNoteByEnseignementAndEleve(idSeance, idEleve));
    }

    @Path("/tp/")
    @POST
    public Response enregistrerNoteTp(@FormParam("idSeance") Long idSeance, @FormParam("idEleve") Long idEleve, @FormParam("note") BigDecimal note, @FormParam("commentaireNote") String commentaire)
    {
        RenduTpManager.getInstance().enregistrerNoteTp(idSeance, idEleve, note, commentaire);
        return Response.status(200).entity("OK").build();
    }
}
