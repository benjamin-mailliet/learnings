package gestionnairecours.servlets;

import gestionnairecours.model.Utilisateur;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
	private static final long serialVersionUID = 3038302649713866775L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur utilisateur = new Utilisateur("UTILISATEUR", false);
		request.getSession().setAttribute("utilisateur", utilisateur);
		response.sendRedirect("eleve/fichiers");
	}

}
