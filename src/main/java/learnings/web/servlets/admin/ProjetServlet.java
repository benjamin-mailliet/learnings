package learnings.web.servlets.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.ProjetManager;
import learnings.model.Projet;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/projet" })
public class ProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;
	private static DateFormat formatJourHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long idProjet = null;
		try {
			idProjet = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// Ne rien faire
		}
		if (idProjet == null) {
			request.setAttribute("mode", "creation");
		} else {
			request.setAttribute("mode", "modification");
			Projet projet = ProjetManager.getInstance().getProjetAvecRessources(idProjet);
			request.setAttribute("projet", projet);
		}

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/projet.jsp");
		view.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Long idProjet = null;
		try {
			idProjet = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
		Date dateLimiteRenduLot1 = null;
		Date dateLimiteRenduLot2 = null;
		try {
			dateLimiteRenduLot1 = formatJourHeure.parse(request.getParameter("dateLimiteRenduLot1"));
			dateLimiteRenduLot2 = formatJourHeure.parse(request.getParameter("dateLimiteRenduLot2"));
		} catch (ParseException e) {
		}

		Projet projet = new Projet(idProjet, request.getParameter("titre"), request.getParameter("description"), dateLimiteRenduLot1, dateLimiteRenduLot2);
		try {
			if (idProjet == null) {
				ProjetManager.getInstance().ajouterProjet(projet);
				this.ajouterMessageSucces(request, "Le nouveau projet a été créé.");
			} else {
				ProjetManager.getInstance().modifierProjet(projet);
				this.ajouterMessageSucces(request, String.format("Le projet n°%d a été modifié.", idProjet));
			}
			response.sendRedirect("listeprojets");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
			response.sendRedirect("projet");
		}

	}
}
