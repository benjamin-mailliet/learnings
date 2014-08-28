package learnings.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Seance;
import learnings.model.Utilisateur;

@WebServlet(urlPatterns = { "/eleve/remisetp" })
@MultipartConfig
public class RemiseTPServlet extends GenericServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Utilisateur> binomes = UtilisateurManager.getInstance().listerAutresEleves(this.getUtilisateurCourant(request));
		request.setAttribute("listeBinomes", binomes);

		List<Seance> listeTp = SeanceManager.getInstance().listerTPRenduAccessible();
		request.setAttribute("listeTp", listeTp);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/remisetp.jsp");
		view.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long tpId = Long.parseLong(request.getParameter("idtp"));
			Long utilisateur1Id = this.getUtilisateurCourant(request).getId();
			Long utilisateur2Id = Long.parseLong(request.getParameter("eleve2"));
			Part fichier = request.getPart("fichiertp");
			SeanceManager.getInstance().rendreTP(tpId, utilisateur1Id, utilisateur2Id, fichier.getInputStream());
			this.ajouterMessageSucces(request, "Le fichier a bien été enregistré.");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remisetp");
	}

}
