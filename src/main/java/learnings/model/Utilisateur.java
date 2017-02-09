package learnings.model;

import learnings.enums.Groupe;

import java.io.Serializable;

public class Utilisateur implements Serializable {
	private static final long serialVersionUID = -1167387777176439635L;

	private Long id;
	private String nom;
	private String prenom;
	private String email;
	private Groupe groupe;
	private boolean admin;

	public Utilisateur() {
		super();
	}

	public Utilisateur(Long id, String nom, String prenom, String email, Groupe groupe, boolean admin) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.groupe = groupe;
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
}
