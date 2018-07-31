package learnings.web.servlets.admin;

import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Seance;
import learnings.pojos.EleveAvecNotes;
import learnings.utils.CsvUtils;
import learnings.web.servlets.GenericLearningsServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;

@WebServlet(urlPatterns = { "/admin/note" })
public class NoteServlet extends GenericLearningsServlet {

    List<EleveAvecNotes> eleves;
    List<Seance> seancesNotees;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        seancesNotees = SeanceManager.getInstance().listerSeancesNoteesWithTravaux();
        eleves = UtilisateurManager.getInstance().listerElevesAvecNotes();


        TemplateEngine engine = this.createTemplateEngine(request);
        WebContext context = new WebContext(request, response, getServletContext());
        context.setVariable("eleves", eleves);

        if(eleves.size()>0) {
            SeanceManager.getInstance().calculerMoyenneSeance(seancesNotees, eleves);
            context.setVariable("seancesNotees", seancesNotees);

            OptionalDouble moyenneOptionnel = eleves.stream().filter(e -> e.getMoyenne() != null).mapToDouble(e -> e.getMoyenne().doubleValue()).average();
            if(moyenneOptionnel.isPresent()){
                Double moyenneClasse = moyenneOptionnel.getAsDouble();
                context.setVariable("moyenneClasse", new DecimalFormat("####0.00").format(moyenneClasse));
            }
        }

        engine.process("admin/note", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("export-csv") != null) {
            CsvUtils.creerCSVElevesNotes(response.getWriter(), eleves, seancesNotees);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"notes.csv\"");
        }
    }
}
