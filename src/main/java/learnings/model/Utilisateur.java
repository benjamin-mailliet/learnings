package learnings.model;

import java.io.Serializable;

public class Utilisateur implements Serializable {
	private static final long serialVersionUID = -1167387777176439635L;

	private Long id;
	private String email;
	private boolean admin;

	public Utilisateur() {
		super();
	}

	public Utilisateur(Long id, String email, boolean admin) {
		super();
		this.id = id;
		this.email = email;
		this.admin = admin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
