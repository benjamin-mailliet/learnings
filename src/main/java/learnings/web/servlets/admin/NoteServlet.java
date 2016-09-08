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
import java.util.List;

@WebServlet(urlPatterns = { "/admin/note" })
public class NoteServlet extends GenericLearningsServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Seance> seancesNotees = SeanceManager.getInstance().listerSeancesNoteesWithTravaux();
        request.setAttribute("seancesNotees", seancesNotees);

        List<EleveAvecTravauxEtProjet> eleves = UtilisateurManager.getInstance().listerElevesAvecTravauxEtProjet();
        request.setAttribute("eleves", eleves);


        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/note.jsp");
        view.forward(request, response);
    }
}
