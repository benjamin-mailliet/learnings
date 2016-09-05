package learnings.dao;

import learnings.model.Appel;

import java.util.List;

public interface AppelDao {

    List<Appel> listerAppels(Long idSeance);

    void ajouterAppel(Long idSeance, Appel appel);

    void modifierAppel(Long idSeance, Appel appel);
}
