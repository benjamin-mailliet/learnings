package learnings.web.servlets;

import learnings.exceptions.LearningAccessException;
import learnings.exceptions.LearningsException;
import learnings.managers.RessourceManager;
import learnings.managers.SeanceManager;
import learnings.model.Ressource;
import learnings.model.Seance;
import learnings.pojos.RessourceHtml;
import learnings.utils.MarkdownUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eleve/sujet")
public class RessourceMarkdownServlet extends GenericLearningsServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long idRessource = Long.parseLong(request.getParameter("id"));
            RessourceHtml ressource = RessourceManager.getInstance().getRessourceMarkdownHtmlEleve(idRessource);

            TemplateEngine engine = this.createTemplateEngine(request);
            WebContext context = new WebContext(request, response, getServletContext());
            context.setVariable("ressource", ressource);
            engine.process("eleve/ressourcemd", context, response.getWriter());
        } catch (IllegalArgumentException e) {
            this.ajouterMessageErreur(request, "L'identifiant de la ressource est incorrect.");
            e.printStackTrace();
            response.sendRedirect("seances");
        } catch (LearningsException e) {
            this.ajouterMessageErreur(request, "Une erreur est apparue à la récupération du fichier.");
            e.printStackTrace();
            response.sendRedirect("seances");
        } catch (LearningAccessException e) {
            response.sendError(403);
        }
    }
}
