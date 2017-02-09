package learnings.web.servlets.admin;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/", "/admin/utilisateur" })
public class GestionUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Utilisateur> utilisateurs = UtilisateurManager.getInstance().listerUtilisateurs();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("utilisateurs", utilisateurs);
		context.setVariable("groupes", Groupe.values());
		engine.process("admin/utilisateur", context, response.getWriter());
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
		} catch (LearningsSecuriteException e) {
			response.sendError(500);
		}

	}

}
