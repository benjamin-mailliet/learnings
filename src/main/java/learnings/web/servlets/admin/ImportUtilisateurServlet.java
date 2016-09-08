package learnings.web.servlets.admin;

import learnings.enums.Groupe;
import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.web.servlets.GenericLearningsServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/importutilisateurs" })
@MultipartConfig
public class ImportUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/importutilisateurs.jsp");
		view.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");


		response.sendRedirect("utilisateur");

	}

}
