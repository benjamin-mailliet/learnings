package learnings.web.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import learnings.exceptions.LearningsException;
import learnings.managers.ProjetManager;
import learnings.managers.TravailManager;
import learnings.pojos.ProjetAvecTravail;

@WebServlet(urlPatterns = { "/eleve/remiseprojet" })
@MultipartConfig
public class RemiseProjetServlet extends GenericLearningsServlet {

	
	private static final long serialVersionUID = -3575126338268294035L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProjetAvecTravail projetAvecTravail = ProjetManager.getInstance().getProjetAvecTravail(this.getUtilisateurCourant(request).getId());
		request.setAttribute("projetAvecTravail", projetAvecTravail);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/remiseprojet.jsp");
		view.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			Long projetId = Long.parseLong(request.getParameter("idprojet"));
			Long utilisateur1Id = this.getUtilisateurCourant(request).getId();
			
			String commentaire = request.getParameter("commentaire");
			String urlRepository = request.getParameter("urlRepo");
			Part fichier = request.getPart("fichierprojet");
			if(urlRepository!= null && (!"".equals(urlRepository)) && fichier.getSize() > 0L){
				this.ajouterMessageErreur(request, "Veuillez ne remplir qu'un champ de rendu, l'URL ou le fichier.");
			}else if(urlRepository!= null && !"".equals(urlRepository)){
				TravailManager.getInstance().rendreProjetWithRepo(projetId, utilisateur1Id, commentaire, urlRepository);
				this.ajouterMessageSucces(request, "Le projet a bien été enregistré.");
			}else if(fichier.getSize() > 0L){
				String nomFichier = this.getNomDuFichier(fichier);
				TravailManager.getInstance().rendreProjetWithFichier(projetId, utilisateur1Id, commentaire, nomFichier, fichier.getInputStream(),
						fichier.getSize());
				this.ajouterMessageSucces(request, "Le projet a bien été enregistré.");
			}else{
				this.ajouterMessageErreur(request, "Veuillez soit saisir une URL de repository, soit ajouter un fichier.");
			}
			
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		} catch (LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remiseprojet");
	}
}
