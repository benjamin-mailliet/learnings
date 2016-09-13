package learnings.web.webservices;

import com.google.gson.Gson;
import learnings.managers.TravailManager;
import learnings.managers.UtilisateurManager;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/utilisateur")
public class UtilisateurWebservice {

    @Path("/emails")
    @Produces("text/plain;charset=UTF-8")
    @GET
    public String getListeEmails()    {
       return UtilisateurManager.getInstance().listerEmailsElevesPourEnvoi();
    }

}
