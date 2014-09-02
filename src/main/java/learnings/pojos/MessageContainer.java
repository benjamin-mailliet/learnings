package learnings.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageContainer implements Serializable {

	private static final long serialVersionUID = -1020206634150261668L;

	public enum Niveau {
		ERROR, WARN, INFO, SUCCESS
	}

	private Map<Niveau, List<String>> messages;

	public MessageContainer() {
		super();
		messages = new HashMap<MessageContainer.Niveau, List<String>>();
	}

	public void ajouterMessage(Niveau niveau, String message) {
		if (messages.get(niveau) == null) {
			messages.put(niveau, new ArrayList<String>());
		}
		messages.get(niveau).add(message);
	}

	public List<String> getMessages(Niveau niveau) {
		if (messages.get(niveau) == null) {
			messages.put(niveau, new ArrayList<String>());
		}
		return messages.get(niveau);
	}

	public void purgerMessages() {
		messages = new HashMap<MessageContainer.Niveau, List<String>>();
	}

}
