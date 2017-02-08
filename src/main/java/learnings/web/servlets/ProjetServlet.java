package learnings.web.servlets;

import learnings.managers.ProjetManager;
import learnings.model.Projet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = { "/eleve/projet" })
public class ProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 5394627956570317471L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Projet projet = ProjetManager.getInstance().getLastProjetAvecRessources();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("projet", projet);
		engine.process("eleve/projet", context, response.getWriter());

	}
}
