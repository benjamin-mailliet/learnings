package learnings.web.servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.exceptions.LearningsException;
import learnings.managers.RessourceManager;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = { "/admin/supprimerressource" })
@MultipartConfig
public class SupprimerRessourceServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -2275312450786800848L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long idRessource = Long.parseLong(request.getParameter("idRessource"));
			Long idSeance = Long.parseLong(request.getParameter("idSeance"));
			RessourceManager.getInstance().supprimerRessource(idRessource);
			response.sendRedirect("ressource?idSeance=" + idSeance);
		} catch (NumberFormatException e) {
			this.ajouterMessageErreur(request, "L'identifiant de la ressource est manquant.");
			response.sendRedirect("listeseances");
		} catch (IllegalArgumentException e) {
			this.ajouterMessageErreur(request, e.getMessage());
			response.sendRedirect("listeseances");
		} catch (LearningsException e) {
			this.ajouterMessageErreur(request, e.getMessage());
			response.sendRedirect("listeseances");
		}
	}
}
