package learnings.web.servlets;

import learnings.exceptions.LearningsException;
import learnings.managers.RenduTpManager;
import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Utilisateur;
import learnings.pojos.TpAvecTravaux;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/eleve/remisetpbinome" })
@MultipartConfig
public class RemiseTPBinomeServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("remisetp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long tpId = Long.parseLong(request.getParameter("idtp"));
			Long utilisateur1Id = this.getUtilisateurCourant(request).getId();

			Long utilisateur2Id = null;
			if (request.getParameter("eleve2") != null && !"".equals(request.getParameter("eleve2"))) {
				utilisateur2Id = Long.parseLong(request.getParameter("eleve2"));
				// Si pas de binôme, utilisateur2Id = 0
				if (utilisateur2Id == 0L) {
					utilisateur2Id = null;
				}

				RenduTpManager.getInstance().ajouterBinome(tpId, utilisateur1Id, utilisateur2Id);
				this.ajouterMessageSucces(request, "Le binôme a bien été enregistré.");
			} else {
				this.ajouterMessageErreur(request, "Veuillez sélectionner un binôme.");
			}


		} catch (IllegalArgumentException | LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remisetp");
	}
}
