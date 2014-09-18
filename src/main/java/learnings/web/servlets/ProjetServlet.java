package learnings.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.ProjetManager;
import learnings.model.Projet;

@WebServlet(urlPatterns = { "/eleve/projet" })
public class ProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 5394627956570317471L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Projet projet = ProjetManager.getInstance().getLastProjetAvecRessources();

		request.setAttribute("projet", projet);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/projet.jsp");
		view.forward(request, response);

	}
}
