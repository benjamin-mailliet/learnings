package learnings.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.managers.EnseignementManager;
import learnings.managers.UtilisateurManager;
import learnings.model.TP;
import learnings.model.Utilisateur;

@WebServlet(urlPatterns = { "/eleve/remisetp" })
public class RemiseTPServlet extends GenericServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Utilisateur> binomes = UtilisateurManager.getInstance().listerAutresEleves(this.getUtilisateurCourant(request));
		request.setAttribute("listeBinomes", binomes);

		List<TP> listeTp = EnseignementManager.getInstance().listerTPRenduAccessible();
		request.setAttribute("listeTp", listeTp);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/remisetp.jsp");
		view.forward(request, response);

	}
}
