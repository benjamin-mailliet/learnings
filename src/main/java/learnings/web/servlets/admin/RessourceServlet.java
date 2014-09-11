package learnings.web.servlets.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import learnings.exceptions.LearningsException;
import learnings.managers.RessourceManager;
import learnings.managers.SeanceManager;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/ressource" })
@MultipartConfig
public class RessourceServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -2275312450786800848L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long idSeance = null;
		try {
			idSeance = Long.parseLong(request.getParameter("idSeance"));
		} catch (NumberFormatException e) {
		}
		if (idSeance == null) {
			this.ajouterMessageErreur(request, "Une séance doit être sélectionnée.");
			response.sendRedirect("listeseances");
		} else {
			Seance seance = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);
			request.setAttribute("seance", seance);

			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/ressource.jsp");
			view.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			Long idSeance = Long.parseLong(request.getParameter("seance"));
			Part fichier = request.getPart("fichier");
			RessourceManager.getInstance().ajouterRessource(idSeance, request.getParameter("titre"), this.getNomDuFichier(fichier), fichier.getInputStream());
		} catch (IllegalArgumentException | LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
			e.printStackTrace();
		}
		response.sendRedirect("ressource?idSeance=" + request.getParameter("idSeance"));
	}

}
