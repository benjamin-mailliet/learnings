package learnings.web.servlets.admin;

import learnings.managers.ProjetManager;
import learnings.model.Projet;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/listeprojets" })
public class ListeProjetsServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Projet> projets = ProjetManager.getInstance().listerProjets();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("projets", projets);
		engine.process("admin/listeProjets", context, response.getWriter());
	}
}
