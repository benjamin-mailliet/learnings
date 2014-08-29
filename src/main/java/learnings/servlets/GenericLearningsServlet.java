package learnings.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import learnings.context.MessageContainer;
import learnings.context.MessageContainer.Niveau;
import learnings.model.Utilisateur;

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
}
