package gestionnairecours.model;

import java.io.Serializable;

public class Utilisateur implements Serializable {
	private static final long serialVersionUID = -1167387777176439635L;

	private String identifiant;
	private boolean admin;

	public Utilisateur() {
		super();
	}

	public Utilisateur(String identifiant, boolean admin) {
		super();
		this.identifiant = identifiant;
		this.admin = admin;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
