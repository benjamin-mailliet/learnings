package learnings.model;

import java.io.Serializable;
import java.util.Date;

public class Cours extends Enseignement implements Serializable  {
	private static final long serialVersionUID = 4961204598561923877L;
	
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
