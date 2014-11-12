package learnings.web.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.ProjetManager;
import learnings.managers.SeanceManager;
import learnings.model.Projet;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet("/admin/travailprojet")
public class TravailProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -30384521654511151L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Projet> projets = ProjetManager.getInstance().listerProjets();
		request.setAttribute("projets", projets);

		if (request.getParameter("idProjet") != null && !"".equals(request.getParameter("idProjet"))) {
			Projet projetSelectionne = ProjetManager.getInstance().getProjetAvecTravaux(Long.parseLong(request.getParameter("idProjet")));
			request.setAttribute("projetSelectionne", projetSelectionne);
		}

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/travailprojet.jsp");
		view.forward(request, response);
	}

}
