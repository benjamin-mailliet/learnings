package learnings.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.SeanceManager;
import learnings.model.Seance;

@WebServlet(urlPatterns = { "/eleve/", "/eleve/seances" })
public class SeancesServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Seance> listeCours = SeanceManager.getInstance().listerSeancesRenduesAccessibles();

		request.setAttribute("seances", listeCours);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/seances.jsp");
		view.forward(request, response);

	}
}
