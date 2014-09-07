package learnings.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import learnings.model.Utilisateur;
import learnings.pojos.MessageContainer;
import learnings.pojos.MessageContainer.Niveau;

public abstract class GenericLearningsServlet extends HttpServlet {

	private static final long serialVersionUID = 8250982756065318974L;

	protected void ajouterMessageErreur(HttpServletRequest request, String message) {
		this.ajouterMessage(request, Niveau.ERROR, message);
	}

	protected void ajouterMessageWarn(HttpServletRequest request, String message) {
		this.ajouterMessage(request, Niveau.WARN, message);
	}

	protected void ajouterMessageInfo(HttpServletRequest request, String message) {
		this.ajouterMessage(request, Niveau.INFO, message);
	}

	protected void ajouterMessageSucces(HttpServletRequest request, String message) {
		this.ajouterMessage(request, Niveau.SUCCESS, message);
	}

	private void ajouterMessage(HttpServletRequest request, Niveau niveau, String message) {
		MessageContainer container = (MessageContainer) request.getSession().getAttribute("messages");
		container.ajouterMessage(niveau, message);
	}

	protected Utilisateur getUtilisateurCourant(HttpServletRequest request) {
		return (Utilisateur) request.getSession().getAttribute("utilisateur");
	}

	protected String getNomDuFichier(Part fichier) {
		String contentDisposition = fichier.getHeader("content-disposition");
		for (String headerPropertie : contentDisposition.split(";")) {
			if (headerPropertie.trim().startsWith("filename=")) {
				return headerPropertie.substring(headerPropertie.indexOf("\"") + 1, headerPropertie.lastIndexOf("\""));
			}
		}
		return null;
	}
}
