package learnings.web.servlets.admin;

import learnings.managers.ProjetManager;
import learnings.model.Projet;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		if (idProjet == null) {
			context.setVariable("mode", "creation");
		} else {
			context.setVariable("mode", "modification");
			Projet projet = ProjetManager.getInstance().getProjetAvecRessources(idProjet);
			context.setVariable("projet", projet);
		}
		engine.process("admin/projet", context, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long idProjet = null;
		try {
			idProjet = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// Ne rien faire
		}
		Date dateLimiteRenduLot1 = null;
		Date dateLimiteRenduLot2 = null;
		try {
			dateLimiteRenduLot1 = formatJourHeure.parse(request.getParameter("dateLimiteRenduLot1"));
			dateLimiteRenduLot2 = formatJourHeure.parse(request.getParameter("dateLimiteRenduLot2"));
		} catch (ParseException e) {
			// Ne rien faire
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
