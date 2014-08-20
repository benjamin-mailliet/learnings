package learnings.servlets.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/", "/admin/utilisateur" })
public class GestionUtilisateurServlet extends HttpServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/utilisateur.jsp");
		view.forward(request, response);
	}

}
