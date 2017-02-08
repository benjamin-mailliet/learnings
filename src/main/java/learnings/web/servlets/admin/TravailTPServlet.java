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

@WebServlet("/admin/travailtp")
public class TravailTPServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 9181054821006337181L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Seance> seances = SeanceManager.getInstance().listerSeancesNotees();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("seances", seances);
		if (request.getParameter("idSeance") != null && !"".equals(request.getParameter("idSeance"))) {
			Seance seanceSelectionnee = SeanceManager.getInstance().getSeanceAvecTravaux(Long.parseLong(request.getParameter("idSeance")));
			context.setVariable("seanceSelectionnee", seanceSelectionnee);
		}
		engine.process("admin/travailtp", context, response.getWriter());
	}

}
