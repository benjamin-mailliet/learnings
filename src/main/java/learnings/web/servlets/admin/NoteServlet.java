package learnings.web.servlets.admin;

import learnings.managers.SeanceManager;
import learnings.managers.UtilisateurManager;
import learnings.model.Seance;
import learnings.pojos.EleveAvecTravauxEtProjet;
import learnings.web.servlets.GenericLearningsServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/note" })
public class NoteServlet extends GenericLearningsServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Seance> seancesNotees = SeanceManager.getInstance().listerSeancesNoteesWithTravaux();


        List<EleveAvecTravauxEtProjet> eleves = UtilisateurManager.getInstance().listerElevesAvecTravauxEtProjet();
        request.setAttribute("eleves", eleves);

        if(eleves.size()>0) {
            SeanceManager.getInstance().calculerMoyenneSeance(seancesNotees, eleves);
            request.setAttribute("seancesNotees", seancesNotees);

            Double moyenneProjet = eleves.stream().filter(e -> e.getProjet() != null && e.getProjet().getNote() != null).mapToDouble(e -> e.getProjet().getNote().doubleValue()).average().getAsDouble();
            request.setAttribute("moyenneProjet", new DecimalFormat("####0.00").format(moyenneProjet));

            Double moyenneClasse = eleves.stream().filter(e -> e.getMoyenne() != null).mapToDouble(e -> e.getMoyenne().doubleValue()).average().getAsDouble();
            request.setAttribute("moyenneClasse", new DecimalFormat("####0.00").format(moyenneClasse));
        }

        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/note.jsp");
        view.forward(request, response);
    }
}
