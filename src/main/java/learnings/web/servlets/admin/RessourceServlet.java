package learnings.web.servlets.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import learnings.exceptions.LearningsException;
import learnings.managers.ProjetManager;
import learnings.managers.RessourceManager;
import learnings.managers.SeanceManager;
import learnings.model.Enseignement;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet(urlPatterns = {"/admin/ressource"})
@MultipartConfig
public class RessourceServlet extends GenericLearningsServlet {

    private static final long serialVersionUID = -2275312450786800848L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idSeance = null;
        Long idProjet = null;
        String type = null;
        try {
            idProjet = Long.parseLong(request.getParameter("idProjet"));
            type = "projet";
        } catch (NumberFormatException e) {
        }
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
            type = "seance";
        } catch (NumberFormatException e) {
        }
        Enseignement enseignement = null;
        if (idSeance == null && idProjet == null) {
            this.ajouterMessageErreur(request, "Une séance ou un projet doit être sélectionné.");
            response.sendRedirect("listeseances");
        } else {
            if (idSeance != null) {
                enseignement = SeanceManager.getInstance().getSeanceAvecRessources(idSeance);
            } else if (idProjet != null) {
                enseignement = ProjetManager.getInstance().getProjetAvecRessources(idProjet);
            }
            request.setAttribute("enseignement", enseignement);
            request.setAttribute("type", type);

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/admin/ressource.jsp");
            view.forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Long idSeance = null;
        Long idProjet = null;
        try {
            idProjet = Long.parseLong(request.getParameter("idProjet"));
        } catch (NumberFormatException e) {
        }
        try {
            idSeance = Long.parseLong(request.getParameter("idSeance"));
        } catch (NumberFormatException e) {
        }
        try {
            Part fichier = request.getPart("fichier");
            RessourceManager.getInstance().ajouterRessource(idSeance, idProjet, request.getParameter("titre"), fichier.getSubmittedFileName(), fichier.getInputStream());
        } catch (IllegalArgumentException | LearningsException e) {
            this.ajouterMessageErreur(request, e.getMessage());
            e.printStackTrace();
        }
        if (idSeance != null) {
            response.sendRedirect("ressource?idSeance=" + idSeance);
        } else if (idProjet != null) {
            response.sendRedirect("ressource?idProjet=" + idProjet);
        } else {
            response.sendRedirect("ressource");
        }
    }
}
