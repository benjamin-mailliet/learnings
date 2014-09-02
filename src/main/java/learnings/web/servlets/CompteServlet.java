package learnings.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;

@WebServlet("/eleve/compte")
public class CompteServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8760587785170356120L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/compte.jsp");
		view.forward(request, response);
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
