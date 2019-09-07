package learnings.pojos;

public class RessourceHtml {

    private Long id;
    private String titre;
    private String html;

    public RessourceHtml(Long id, String titre, String html) {
        this.id = id;
        this.titre = titre;
        this.html = html;
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getHtml() {
        return html;
    }
}
