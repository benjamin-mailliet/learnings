package learnings.web.servlets;

import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eleve/bulletin")
public class BulletinServlet extends GenericLearningsServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateEngine engine = this.createTemplateEngine(request);
        WebContext context = new WebContext(request, response, getServletContext());
        context.setVariable("seancesNotees", SeanceManager.getInstance().listerSeancesNotees());
        context.setVariable("eleve", UtilisateurManager.getInstance().getEleveAvecNotes(getUtilisateurCourant(request).getId()));
        engine.process("eleve/bulletin", context, response.getWriter());
    }
}
