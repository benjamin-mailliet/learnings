package learnings.servlets.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.UtilisateurManager;
import learnings.servlets.GenericLearningsServlet;

@WebServlet("/admin/ajouterutilisateur")
public class AjouterUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8988657856940495904L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
		try {
			request.setAttribute("utilisateur", UtilisateurManager.getInstance().ajouterUtilisateur(email, admin));
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/nouvelUtilisateur.jsp");
			view.forward(request, response);
		} catch (IllegalArgumentException e) {
			response.sendError(400);
		} catch (RuntimeException e) {
			response.sendError(500);
		}
	}

}
