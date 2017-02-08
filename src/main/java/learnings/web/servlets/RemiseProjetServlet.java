package learnings.web.servlets;

import learnings.exceptions.LearningsException;
import learnings.managers.ProjetManager;
import learnings.managers.TravailManager;
import learnings.pojos.ProjetAvecTravail;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet(urlPatterns = { "/eleve/remiseprojet" })
@MultipartConfig
public class RemiseProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -3575126338268294035L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProjetAvecTravail projetAvecTravail = ProjetManager.getInstance().getProjetAvecTravail(this.getUtilisateurCourant(request).getId());

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		if(projetAvecTravail!=null) {
			context.setVariable("projetAvecTravail", projetAvecTravail);
		}
		engine.process("eleve/remiseprojet", context, response.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long projetId = Long.parseLong(request.getParameter("idprojet"));
			Long utilisateur1Id = this.getUtilisateurCourant(request).getId();
			
			String commentaire = request.getParameter("commentaire");
			String urlRepository = request.getParameter("urlRepo");
			Part fichier = request.getPart("fichierprojet");
			if(urlRepository!= null && (!"".equals(urlRepository)) && fichier.getSize() > 0L){
				this.ajouterMessageErreur(request, "Veuillez ne renseigner qu'une méthode de rendu de projet : l'URL ou le fichier.");
			}else if(urlRepository!= null && !"".equals(urlRepository)){
				TravailManager.getInstance().rendreProjetWithRepo(projetId, utilisateur1Id, commentaire, urlRepository);
				this.ajouterMessageSucces(request, "Le projet a bien été enregistré.");
			}else if(fichier.getSize() > 0L){
				TravailManager.getInstance().rendreProjetWithFichier(projetId, utilisateur1Id, commentaire, fichier.getSubmittedFileName(), fichier.getInputStream(),
						fichier.getSize());
				this.ajouterMessageSucces(request, "Le projet a bien été enregistré.");
			}else{
				this.ajouterMessageErreur(request, "Veuillez soit saisir une URL de repository, soit ajouter un fichier.");
			}
			
		} catch (IllegalArgumentException | LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remiseprojet");
	}
}
