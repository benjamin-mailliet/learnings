package learnings.web.servlets.admin;

import learnings.enums.RessourceCategorie;
import learnings.exceptions.LearningsException;
import learnings.managers.RessourceManager;
import learnings.managers.SeanceManager;
import learnings.model.Seance;
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

        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        if (idSeance == null ) {
            this.ajouterMessageErreur(request, "Une séance doit être sélectionnée.");
            response.sendRedirect("listeseances");
        } else {
            Seance seance = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);

            TemplateEngine engine = this.createTemplateEngine(request);
            WebContext context = new WebContext(request, response, getServletContext());
            context.setVariable("seance", seance);
            engine.process("admin/ressource", context, response.getWriter());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
            // Ne rien faire
        }
        RessourceCategorie categorie = null;
        try {
            categorie = RessourceCategorie.valueOf(request.getParameter("categorie"));
        } catch (IllegalArgumentException ignored) {}

        try {
            Part fichier = request.getPart("fichier");
            RessourceManager.getInstance().ajouterRessource(idSeance, request.getParameter("titre"), categorie, request.getParameter("lien"),
                    fichier.getSubmittedFileName(), fichier.getInputStream());
        } catch (IllegalArgumentException | LearningsException e) {
            this.ajouterMessageErreur(request, e.getMessage());
            e.printStackTrace();
        }
        if (idSeance != null) {
            response.sendRedirect("ressource?idSeance=" + idSeance);
        } else {
            response.sendRedirect("ressource");
        }
    }
}
