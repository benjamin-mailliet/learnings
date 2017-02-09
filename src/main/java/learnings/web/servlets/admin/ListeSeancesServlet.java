package learnings.web.servlets.admin;

import learnings.managers.SeanceManager;
import learnings.model.Seance;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/listeseances" })
public class ListeSeancesServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -5832886661094788806L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Seance> seances = SeanceManager.getInstance().listerSeances();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("seances", seances);
		engine.process("admin/listeSeances", context, response.getWriter());
	}
}
