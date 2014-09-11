package learnings.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import learnings.pojos.MessageContainer;
import learnings.pojos.MessageContainer.Niveau;

public class MessageFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.traiterMessages((HttpServletRequest) request);

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	private void traiterMessages(HttpServletRequest request) {
		MessageContainer container = (MessageContainer) request.getSession().getAttribute("messages");
		if (container == null) {
			container = new MessageContainer();
			request.getSession().setAttribute("messages", container);
		}

		request.setAttribute("messagesErreur", container.getMessages(Niveau.ERROR));
		request.setAttribute("messagesWarn", container.getMessages(Niveau.WARN));
		request.setAttribute("messagesInfo", container.getMessages(Niveau.INFO));
		request.setAttribute("messagesSucces", container.getMessages(Niveau.SUCCESS));

		container.purgerMessages();
	}

}
