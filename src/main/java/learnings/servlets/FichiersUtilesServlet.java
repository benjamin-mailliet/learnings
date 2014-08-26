package learnings.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.model.Enseignement;

@WebServlet(urlPatterns = { "/eleve/", "/eleve/fichiers" })
public class FichiersUtilesServlet extends GenericServlet {

	private static final long serialVersionUID = -5862878402579733845L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Enseignement> listeFichiersUtiles = new ArrayList<>();

		/*
		 * List<Seance> listeCours = SeanceManager.getInstance().listerCours();
		 * listeFichiersUtiles.addAll(listeCours);
		 * 
		 * Collections.sort(listeFichiersUtiles, new Comparator<Enseignement>()
		 * { public int compare(Enseignement a, Enseignement b) { return
		 * a.get.compareTo(b.get(1)); } }); listeFichiersUtiles.
		 */
		request.setAttribute("fichiersUtiles", listeFichiersUtiles);
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/pages/fichiersutiles.jsp");
		view.forward(request, response);

	}
}
