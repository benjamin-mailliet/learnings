package learnings.web.servlets.admin;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learnings.exceptions.LearningsException;
import learnings.managers.SeanceManager;
import learnings.pojos.FichierComplet;
import learnings.web.servlets.GenericLearningsServlet;

@WebServlet("/admin/telechargerTravail")
public class TelechargerTravail extends GenericLearningsServlet {

	private static final long serialVersionUID = -7057569089405233362L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("id") == null || "".equals(request.getParameter("id"))) {
			this.ajouterMessageErreur(request, "L'identifiant du travail est incorrect.");
			response.sendRedirect("travailtp");
		} else {
			try {
				FichierComplet fichier = SeanceManager.getInstance().getTravail(Long.parseLong(request.getParameter("id")));

				response.setHeader("Content-disposition", "attachment; filename=" + fichier.getNom());

				OutputStream out = response.getOutputStream();
				byte[] buffer = new byte[4096];
				int length;
				while ((length = fichier.getDonnees().read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				fichier.getDonnees().close();
				out.flush();
			} catch (IllegalArgumentException e) {
				this.ajouterMessageErreur(request, "L'identifiant du travail est incorrect.");
				e.printStackTrace();
				response.sendRedirect("travailtp");
			} catch (LearningsException e) {
				this.ajouterMessageErreur(request, "Une erreur est apparue à la récupération du fichier.");
				e.printStackTrace();
				response.sendRedirect("travailtp");
			}
		}
	}
}
