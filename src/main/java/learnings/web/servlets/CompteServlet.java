package learnings.web.servlets;

import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eleve/compte")
public class CompteServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8760587785170356120L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = this.createTemplateEngine(request);
		engine.process("eleve/compte", new WebContext(request, response, getServletContext()), response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String motDePasse = request.getParameter("motDePasse");
		String motDePasseConfirmation = request.getParameter("motDePasseConfirm");
		try {
			Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateur");
			UtilisateurManager.getInstance().modifierMotDePasse(utilisateur.getId(), motDePasse, motDePasseConfirmation);
			this.ajouterMessageSucces(request, "Le mot de passe a été modifié avec succès.");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}
		response.sendRedirect("compte");
	}
}
