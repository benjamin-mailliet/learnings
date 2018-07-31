package learnings.web.servlets;

import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/connexion")
public class ConnexionServlet extends GenericLearningsServlet {
	private static final long serialVersionUID = 3038302649713866775L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("utilisateur") == null) {
			TemplateEngine engine = this.createTemplateEngine(request);
			engine.process("public/connexion", new WebContext(request, response, getServletContext()), response.getWriter());
		} else {
			response.sendRedirect("eleve/");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String identifiant = request.getParameter("identifiant");
		String motDePasse = request.getParameter("motDePasse");
		try {
			if (UtilisateurManager.getInstance().validerMotDePasse(identifiant, motDePasse)) {
				request.getSession().setAttribute("utilisateur", UtilisateurManager.getInstance().getUtilisateur(identifiant));
			} else {
				this.ajouterMessageErreur(request, "Le mot de passe renseigné est faux.");
			}
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("connexion");
	}

}
