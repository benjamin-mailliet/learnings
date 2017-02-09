package learnings.web.servlets.admin;

import learnings.enums.StatutAppel;
import learnings.managers.AppelManager;
import learnings.managers.SeanceManager;
import learnings.model.Appel;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/appel")
public class AppelServlet extends GenericLearningsServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
            // Ne rien faire
        }

        Seance seance = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);
        List<Appel> appels = AppelManager.getInstance().listerAppels(idSeance);

        TemplateEngine engine = this.createTemplateEngine(request);
        WebContext context = new WebContext(request, response, getServletContext());
        context.setVariable("seance", seance);
        context.setVariable("appels", appels);
        context.setVariable("cssClasses", getCssClasses());
        engine.process("admin/appel", context, response.getWriter());
    }

    private Map<StatutAppel, String> getCssClasses() {
        Map<StatutAppel, String> cssClasses = new HashMap<>();
        cssClasses.put(StatutAppel.PRESENT, "success");
        cssClasses.put(StatutAppel.RETARD, "warning");
        cssClasses.put(StatutAppel.EXCUSE, "info");
        cssClasses.put(StatutAppel.ABSENT, "danger");

        return cssClasses;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance;
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
            super.ajouterMessageErreur(request, "L'identifiant de séance est incorrect.");
            response.sendRedirect("listeseances");
            return;
        }
        Map<Long, StatutAppel> appels = this.parseAppels(request);
        AppelManager.getInstance().enregistrerAppels(idSeance, appels);

        super.ajouterMessageSucces(request, "L'appel a été enregistré.");
        response.sendRedirect("listeseances");
    }

    private Map<Long, StatutAppel> parseAppels(HttpServletRequest request) {
        Map<Long, StatutAppel> appels = new HashMap<>();

        for (String parameter : request.getParameterMap().keySet()) {
            if (parameter.startsWith("appel.")) {
                Long idEleve = Long.parseLong(parameter.substring(parameter.indexOf(".")+1));
                StatutAppel statut = StatutAppel.valueOf(request.getParameter(parameter));

                appels.put(idEleve, statut);
            }
        }

        return appels;
    }
}
