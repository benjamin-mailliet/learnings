package learnings.dao;

import learnings.model.Binome;

public interface BinomeDao {

    Binome ajouterBinome(Binome binome);

    Binome getBinome(Long idSeance, Long idEleve);
    Binome getBinome(Long idBinome);
}
