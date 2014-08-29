package learnings.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.SeanceManager;
import learnings.model.Seance;
import learnings.servlets.GenericLearningsServlet;

@WebServlet("/admin/travailtp")
public class TravailTPServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 9181054821006337181L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Seance> seances = SeanceManager.getInstance().listerSeancesNotees();
		request.setAttribute("seances", seances);

		if (request.getParameter("idSeance") != null && !"".equals(request.getParameter("idSeance"))) {
			Seance seanceSelectionnee = SeanceManager.getInstance().getSeanceAvecTravaux(Long.parseLong(request.getParameter("idSeance")));
			request.setAttribute("seanceSelectionnee", seanceSelectionnee);
		}

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/travailtp.jsp");
		view.forward(request, response);
	}

}
