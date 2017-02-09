package learnings.web.servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eleve/liensutiles")
public class LiensUtilesServlet extends GenericLearningsServlet {

	private static final long serialVersionUID = 8073998393328784221L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = this.createTemplateEngine(request);
		engine.process("eleve/liensutiles",  new WebContext(request, response, getServletContext()), response.getWriter());
	}

}
