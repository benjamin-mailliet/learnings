package learnings.web.servlets.admin;

import learnings.exceptions.LearningsException;
import learnings.managers.ProjetManager;
import learnings.managers.RessourceManager;
import learnings.managers.SeanceManager;
import learnings.model.Enseignement;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/ressource"})
@MultipartConfig
public class RessourceServlet extends GenericLearningsServlet {

    private static final long serialVersionUID = -2275312450786800848L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
        Long idProjet = null;
        String type = null;
        try {
            idProjet = Long.parseLong(request.getParameter("idProjet"));
            type = "projet";
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
            type = "seance";
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        if (idSeance == null && idProjet == null) {
            this.ajouterMessageErreur(request, "Une séance ou un projet doit être sélectionné.");
            response.sendRedirect("listeseances");
        } else {
            Enseignement enseignement;
            if (idSeance != null) {
                enseignement = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);
            } else {
                enseignement = ProjetManager.getInstance().getProjetAvecRessources(idProjet);
            }

            TemplateEngine engine = this.createTemplateEngine(request);
            WebContext context = new WebContext(request, response, getServletContext());
            context.setVariable("enseignement", enseignement);
            context.setVariable("type", type);
            engine.process("admin/ressource", context, response.getWriter());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
        Long idProjet = null;
        try {
            idProjet = Long.parseLong(request.getParameter("idProjet"));
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        try {
            Part fichier = request.getPart("fichier");
            RessourceManager.getInstance().ajouterRessource(idSeance, idProjet, request.getParameter("titre"), fichier.getSubmittedFileName(), fichier.getInputStream());
        } catch (IllegalArgumentException | LearningsException e) {
            this.ajouterMessageErreur(request, e.getMessage());
            e.printStackTrace();
        }
        if (idSeance != null) {
            response.sendRedirect("ressource?idSeance=" + idSeance);
        } else if (idProjet != null) {
            response.sendRedirect("ressource?idProjet=" + idProjet);
        } else {
            response.sendRedirect("ressource");
        }
    }
}
