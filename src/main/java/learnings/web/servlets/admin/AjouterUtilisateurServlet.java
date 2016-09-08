package learnings.web.servlets.admin;

import java.io.IOException;

import javax.rmi.CORBA.Util;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet("/admin/ajouterutilisateur")
public class AjouterUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8988657856940495904L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
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
			request.setAttribute("utilisateur", UtilisateurManager.getInstance().ajouterUtilisateur(nouvelUtilisateur));
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/nouvelUtilisateur.jsp");
			view.forward(request, response);
		} catch (IllegalArgumentException e) {
			response.sendError(400);
		} catch (LearningsSecuriteException e) {
			response.sendError(500);
		}
	}

}
