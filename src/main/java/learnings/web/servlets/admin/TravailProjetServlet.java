package learnings.web.servlets.admin;

import learnings.managers.NoteManager;
import learnings.managers.ProjetManager;
import learnings.model.Note;
import learnings.model.Projet;
import learnings.pojos.ProjetAvecRendus;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/travailprojet")
public class TravailProjetServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = -30384521654511151L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Projet> projets = ProjetManager.getInstance().listerProjets();

		TemplateEngine engine = this.createTemplateEngine(request);
		WebContext context = new WebContext(request, response, getServletContext());
		context.setVariable("projets", projets);
		final String idProjet = request.getParameter("idProjet");
		if (idProjet != null && !"".equals(idProjet)) {
			ProjetAvecRendus projetSelectionne = ProjetManager.getInstance().getProjetAvecRendus(Long.parseLong(idProjet));
			context.setVariable("projetSelectionne", projetSelectionne);

			Map<Long, Note> mapNoteEleveNotesSeance = NoteManager.getInstance().getMapNoteEleve(Long.parseLong(idProjet), Projet.class);
			context.setVariable("mapNoteEleve", mapNoteEleveNotesSeance);
		}
		engine.process("admin/travailprojet", context, response.getWriter());
	}

}
