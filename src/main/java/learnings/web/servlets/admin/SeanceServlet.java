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

import learnings.enums.TypeSeance;
import learnings.managers.SeanceManager;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/seance" })
public class SeanceServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;
	private static DateFormat formatJour = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat formatJourHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long idSeance = null;
		try {
			idSeance = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// Ne rien faire
		}
		if (idSeance == null) {
			request.setAttribute("mode", "creation");
		} else {
			request.setAttribute("mode", "modification");
			Seance seance = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);
			request.setAttribute("seance", seance);
		}

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/seance.jsp");
		view.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Long idSeance = null;
		try {
			idSeance = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
		Date date = null;
		try {
			date = formatJour.parse(request.getParameter("date"));
		} catch (ParseException e) {
		}
		Date dateLimiteRendu = null;
		try {
			dateLimiteRendu = formatJourHeure.parse(request.getParameter("dateLimiteRendu"));
		} catch (ParseException e) {
		}
		TypeSeance type = null;
		try {
			type = TypeSeance.valueOf(request.getParameter("type"));
		} catch (IllegalArgumentException e) {
		} catch (NullPointerException e) {
		}
		Seance seance = new Seance(idSeance, request.getParameter("titre"), request.getParameter("description"), date, Boolean.parseBoolean(request
				.getParameter("isNote")), dateLimiteRendu, type);
		try {
			if (idSeance == null) {
				SeanceManager.getInstance().ajouterSeance(seance);
				this.ajouterMessageSucces(request, "La nouvelle séance a été créée.");
			} else {
				SeanceManager.getInstance().modifierSeance(seance);
				this.ajouterMessageSucces(request, String.format("La séance n°%d a été modifiée.", idSeance));
			}
			response.sendRedirect("listeseances");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
			response.sendRedirect("seance");
		}

	}
}
