package learnings.web.servlets.admin;

import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Seance;
import learnings.pojos.EleveAvecTravauxEtProjet;
import learnings.utils.CsvUtils;
import learnings.web.servlets.GenericLearningsServlet;

import javax.servlet.RequestDispatcher;
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

    List<EleveAvecTravauxEtProjet> eleves;
    List<Seance> seancesNotees;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        seancesNotees = SeanceManager.getInstance().listerSeancesNoteesWithTravaux();
        eleves = UtilisateurManager.getInstance().listerElevesAvecTravauxEtProjet();
        request.setAttribute("eleves", eleves);

        if(eleves.size()>0) {
            SeanceManager.getInstance().calculerMoyenneSeance(seancesNotees, eleves);
            request.setAttribute("seancesNotees", seancesNotees);
            OptionalDouble moyenneOptionnel = eleves.stream()
                    .filter(e -> e.getProjet() != null && e.getProjet().getNote() != null)
                    .mapToDouble(e -> e.getProjet().getNote().doubleValue()).average();
            if(moyenneOptionnel.isPresent()){
                Double moyenneProjet = moyenneOptionnel.getAsDouble();
                request.setAttribute("moyenneProjet", new DecimalFormat("####0.00").format(moyenneProjet));
            }

            moyenneOptionnel = eleves.stream().filter(e -> e.getMoyenne() != null).mapToDouble(e -> e.getMoyenne().doubleValue()).average();
            if(moyenneOptionnel.isPresent()){
                Double moyenneClasse = moyenneOptionnel.getAsDouble();
                request.setAttribute("moyenneClasse", new DecimalFormat("####0.00").format(moyenneClasse));
            }
        }

        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/note.jsp");
        view.forward(request, response);
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
