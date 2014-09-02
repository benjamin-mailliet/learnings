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
import learnings.utils.TpAvecTravail;

@WebServlet(urlPatterns = { "/eleve/remisetp" })
@MultipartConfig
public class RemiseTPServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Utilisateur> binomes = UtilisateurManager.getInstance().listerAutresEleves(this.getUtilisateurCourant(request));
		request.setAttribute("listeBinomes", binomes);

		List<TpAvecTravail> listeTp = SeanceManager.getInstance().listerTPRenduAccessible(this.getUtilisateurCourant(request).getId());
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
				// Si pas de binôme, utilisateur2Id = 0
				if (utilisateur2Id == 0L) {
					utilisateur2Id = null;
				}
				Part fichier = request.getPart("fichiertp");
				if (fichier.getSize() == 0L) {
					this.ajouterMessageErreur(request, "Veuillez ajouter un fichier.");
				} else {
					String nomFichier = this.getNomDuFichier(fichier);
					SeanceManager.getInstance().rendreTP(tpId, utilisateur1Id, utilisateur2Id, nomFichier, fichier.getInputStream(), fichier.getSize());
					this.ajouterMessageSucces(request, "Le fichier a bien été enregistré.");
				}
			} else {
				this.ajouterMessageErreur(request, "Veuillez sélectionner un binôme.");
			}
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		} catch (LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
		}

		response.sendRedirect("remisetp");
	}

	private String getNomDuFichier(Part fichier) {
		String contentDisposition = fichier.getHeader("content-disposition");
		for (String headerPropertie : contentDisposition.split(";")) {
			if (headerPropertie.trim().startsWith("filename=")) {
				return headerPropertie.substring(headerPropertie.indexOf("\"") + 1, headerPropertie.lastIndexOf("\""));
			}
		}
		return null;
	}

}
