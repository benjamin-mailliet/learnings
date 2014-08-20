package learnings.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.UtilisateurManager;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
	private static final long serialVersionUID = 3038302649713866775L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("utilisateur") == null) {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/pages/accueil.jsp");
			view.forward(request, response);
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
				request.setAttribute("errorMessage", "Le mot de passe renseigné est faux.");
			}
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}

		this.doGet(request, response);
	}

}
