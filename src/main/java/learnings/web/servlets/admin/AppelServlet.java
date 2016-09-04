package learnings.web.servlets.admin;

import learnings.managers.AppelManager;
import learnings.managers.SeanceManager;
import learnings.model.Appel;
import learnings.model.Seance;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/appel")
public class AppelServlet extends HttpServlet {

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
}
