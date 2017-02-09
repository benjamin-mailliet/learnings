package learnings.web.webservices;

import learnings.managers.UtilisateurManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/utilisateur")
public class UtilisateurWebservice {

    @Path("/emails")
    @Produces("text/plain;charset=UTF-8")
    @GET
    public String getListeEmails()    {
       return UtilisateurManager.getInstance().listerEmailsElevesPourEnvoi();
    }

}
