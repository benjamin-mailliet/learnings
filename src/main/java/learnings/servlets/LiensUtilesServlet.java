package learnings.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/eleve/liensutiles")
public class LiensUtilesServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8073998393328784221L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/liensutiles.jsp");
		view.forward(request, response);
	}

}
