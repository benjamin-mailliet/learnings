package learnings.web.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.SeanceManager;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/listeseances" })
public class ListeSeancesServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Seance> seances = SeanceManager.getInstance().listerSeances();
		request.setAttribute("seances", seances);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/listeSeances.jsp");
		view.forward(request, response);
	}
}
