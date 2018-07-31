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

@WebServlet("/admin/ajouterutilisateur")
public class AjouterUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8988657856940495904L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String email = request.getParameter("email");
		Groupe groupe = null;
		try {
			groupe = Groupe.valueOf(request.getParameter("groupe"));
		} catch (IllegalArgumentException e) {
			// Ne rien faire
		}
		boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
		try {
			Utilisateur nouvelUtilisateur = new Utilisateur(null, nom, prenom, email, groupe, admin);
			TemplateEngine engine = this.createTemplateEngine(request);
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("utilisateur", UtilisateurManager.getInstance().ajouterUtilisateur(nouvelUtilisateur));
			engine.process("admin/nouvelUtilisateur", context, response.getWriter());
		} catch (IllegalArgumentException e) {
			response.sendError(400);
		}
	}

}
