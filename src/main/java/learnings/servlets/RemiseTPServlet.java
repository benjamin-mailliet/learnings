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

import learnings.exceptions.LearningsException;
import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.utils.TpAvecTravaux;

@WebServlet(urlPatterns = { "/eleve/remisetp" })
@MultipartConfig
public class RemiseTPServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Utilisateur> binomes = UtilisateurManager.getInstance().listerAutresEleves(this.getUtilisateurCourant(request));
		request.setAttribute("listeBinomes", binomes);

		List<TpAvecTravaux> listeTp = SeanceManager.getInstance().listerTPRenduAccessible(this.getUtilisateurCourant(request).getId());
		request.setAttribute("listeTp", listeTp);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/remisetp.jsp");
		view.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long tpId = Long.parseLong(request.getParameter("idtp"));
			Long utilisateur1Id = this.getUtilisateurCourant(request).getId();
			Long utilisateur2Id = null;
			if (request.getParameter("eleve2") != null && !"".equals(request.getParameter("eleve2"))) {
				utilisateur2Id = Long.parseLong(request.getParameter("eleve2"));
			}
			Part fichier = request.getPart("fichiertp");
			SeanceManager.getInstance().rendreTP(tpId, utilisateur1Id, utilisateur2Id, fichier.getSubmittedFileName(), fichier.getInputStream(),
					fichier.getSize());
			this.ajouterMessageSucces(request, "Le fichier a bien été enregistré.");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		} catch (LearningsException e) {
			this.ajouterMessageErreur(request, "Problème technique à l'enregistrement du fichier.");
		}

		response.sendRedirect("remisetp");
	}

}
