package learnings.web.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.ProjetManager;
import learnings.model.Projet;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/listeprojets" })
public class ListeProjetsServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Projet> projets = ProjetManager.getInstance().listerProjets();
		request.setAttribute("projets", projets);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/listeProjets.jsp");
		view.forward(request, response);
	}
}
