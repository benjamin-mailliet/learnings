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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

			Set<Long> idsEleves = new HashSet<>();
			idsEleves.add(this.getUtilisateurCourant(request).getId());

			if (request.getParameterValues("eleves") != null) {
				String[] idsPartenaires = request.getParameterValues("eleves");
				for (String idPartenaireAsString : idsPartenaires) {
					long idPartenaire = Long.parseLong(idPartenaireAsString);
					if(idPartenaire != 0L) {
						idsEleves.add(idPartenaire);
					}
				}
				RenduTpManager.getInstance().ajouterBinome(tpId, idsEleves);
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
