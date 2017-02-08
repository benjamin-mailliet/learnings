package learnings.web.servlets.admin;

import learnings.exceptions.LearningsException;
import learnings.exceptions.LearningsSecuriteException;
import learnings.managers.UtilisateurManager;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet(urlPatterns = { "/admin/importutilisateurs" })
@MultipartConfig
public class ImportUtilisateurServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = this.createTemplateEngine(request);
		engine.process("admin/importutilisateurs", new WebContext(request, response, getServletContext()), response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part fichier = request.getPart("fichier");

		try {
			UtilisateurManager.getInstance().importerUtilisateurs(fichier.getInputStream());
			this.ajouterMessageSucces(request, "L'import des utilisateurs est terminé.");
		} catch (LearningsException | LearningsSecuriteException e) {
			this.ajouterMessageErreur(request, String.format("L'import des utilisateurs a levé une erreur : %s.", e.getMessage()));
			e.printStackTrace();
		}

		response.sendRedirect("utilisateur");

	}

}
