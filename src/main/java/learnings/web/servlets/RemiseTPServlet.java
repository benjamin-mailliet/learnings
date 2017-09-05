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

@WebServlet(urlPatterns = { "/eleve/remisetp" })
@MultipartConfig
public class RemiseTPServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Utilisateur> binomes = UtilisateurManager.getInstance().listerAutresEleves(this.getUtilisateurCourant(request).getId());
		List<TpAvecTravaux> listeTp = SeanceManager.getInstance().listerTPRenduAccessible(this.getUtilisateurCourant(request).getId());

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("listeBinomes", binomes);
		context.setVariable("listeTp", listeTp);
		engine.process("eleve/remisetp", context, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long tpId = Long.parseLong(request.getParameter("idtp"));
			Long eleveId = this.getUtilisateurCourant(request).getId();
			String commentaire = request.getParameter("commentaire");

			Part fichier = request.getPart("fichiertp");
			if (fichier.getSize() == 0L) {
				this.ajouterMessageErreur(request, "Veuillez ajouter un fichier.");
			} else {
				RenduTpManager.getInstance().rendreTP(tpId, eleveId, commentaire, fichier.getSubmittedFileName(), fichier.getInputStream(),
						fichier.getSize());
				this.ajouterMessageSucces(request, "Le fichier a bien été enregistré.");
			}

		} catch (IllegalArgumentException | LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remisetp");
	}
}
