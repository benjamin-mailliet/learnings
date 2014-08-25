package learnings.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.servlets.GenericServlet;

@WebServlet(urlPatterns = { "/admin/", "/admin/utilisateur" })
public class GestionUtilisateurServlet extends GenericServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Utilisateur> utilisateurs = UtilisateurManager.getInstance().listerUtilisateurs();
		request.setAttribute("utilisateurs", utilisateurs);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/utilisateur.jsp");
		view.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long idUtilisateur = Long.parseLong(request.getParameter("id"));
			String action = request.getParameter("action");
			switch (action) {
			case "supprimer":
				UtilisateurManager.getInstance().supprimerUtilisateur(idUtilisateur);
				break;
			case "donnerAdmin":
				UtilisateurManager.getInstance().donnerDroitsAdmin(idUtilisateur);
				break;
			case "enleverAdmin":
				UtilisateurManager.getInstance().enleverDroitsAdmin(idUtilisateur);
				break;
			case "reinitialiserMotDePasse":
				UtilisateurManager.getInstance().reinitialiserMotDePasse(idUtilisateur);
				break;
			default:
				response.sendError(400);
				break;
			}
		} catch (IllegalArgumentException e) {
			response.sendError(400);
			e.printStackTrace();
		}

	}

}
