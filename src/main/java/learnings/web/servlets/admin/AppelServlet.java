package learnings.web.servlets.admin;

import learnings.enums.StatutAppel;
import learnings.managers.AppelManager;
import learnings.managers.SeanceManager;
import learnings.model.Appel;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
        request.setAttribute("seance", seance);
        List<Appel> appels = AppelManager.getInstance().listerAppels(idSeance);
        request.setAttribute("appels", appels);

        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/appel.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
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
