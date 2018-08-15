package learnings.managers;

import learnings.dao.BinomeDao;
import learnings.dao.RenduTpDao;
import learnings.dao.RessourceDao;
import learnings.dao.SeanceDao;
import learnings.dao.UtilisateurDao;
import learnings.dao.impl.BinomeDaoImpl;
import learnings.dao.impl.RenduTpDaoImpl;
import learnings.dao.impl.RessourceDaoImpl;
import learnings.dao.impl.SeanceDaoImpl;
import learnings.dao.impl.UtilisateurDaoImpl;
import learnings.model.Binome;
import learnings.model.Note;
import learnings.model.RenduTp;
import learnings.model.Seance;
import learnings.model.Utilisateur;
import learnings.pojos.EleveAvecNotes;
import learnings.pojos.SeanceAvecRendus;
import learnings.pojos.TpAvecTravaux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SeanceManager {


    private static class SeanceManagerHolder {
        private static SeanceManager instance = new SeanceManager();
    }

    public static SeanceManager getInstance() {
        return SeanceManagerHolder.instance;
    }

    public SeanceManager() {
    }

    private SeanceDao seanceDao = new SeanceDaoImpl();
    private RessourceDao ressourceDao = new RessourceDaoImpl();
    private RenduTpDao renduTpDao = new RenduTpDaoImpl();
    private BinomeDao binomeDao = new BinomeDaoImpl();
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

    public List<Seance> listerSeances() {
        List<Seance> listeCours = seanceDao.listerSeances();
        for (Seance seanceCourante : listeCours) {
            seanceCourante.setRessources(ressourceDao.getRessources(seanceCourante));
        }
        return listeCours;
    }

    public List<Seance> listerSeancesNotees() {
        return seanceDao.listerSeancesNotees();
	}

	public List<Seance> listerSeancesNoteesWithTravaux() {
		List<Seance> listeSeancesNotees = seanceDao.listerSeancesNotees();
		for(Seance seance : listeSeancesNotees){
			seance.setTravauxRendus(renduTpDao.listerRendusParSeance(seance.getId()));
		}
		return listeSeancesNotees;
    }


    public List<TpAvecTravaux> listerTPRenduAccessible(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("L'utlisateur ne peut pas être null.");
        }
        Date aujourdhui = new Date();
        List<Seance> listeTps = seanceDao.listerTPNotesParDateRendu(aujourdhui);
        List<TpAvecTravaux> listeTpsAvecTravaux = new ArrayList<>();
        for (Seance tp : listeTps) {
            TpAvecTravaux tpAvecTravaux = new TpAvecTravaux();
            tpAvecTravaux.setTp(tp);
            Binome binome = binomeDao.getBinome(tp.getId(), idUtilisateur);
            if(binome != null) {
                tpAvecTravaux.setBinome(binome);
                tpAvecTravaux.setTravaux(renduTpDao.listerRendusParBinome(binome.getUid()));
            } else if (tp.getNbMaxElevesParRendu() == 1) {
                Utilisateur eleve = utilisateurDao.getUtilisateur(idUtilisateur);
                Binome nouveauBinomeIndividuel = binomeDao.ajouterBinome(new Binome(null, tp,eleve));
                tpAvecTravaux.setBinome(nouveauBinomeIndividuel);
                tpAvecTravaux.setTravaux(new ArrayList<>());
            }
            listeTpsAvecTravaux.add(tpAvecTravaux);

        }
        return listeTpsAvecTravaux;
    }

    public SeanceAvecRendus getSeanceAvecTravaux(Long idSeance) {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        Seance seance = seanceDao.getSeance(idSeance);
        if (seance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est inconnu.");
        }
        SeanceAvecRendus seanceAvecRendus = new SeanceAvecRendus();
        seanceAvecRendus.setSeance(seance);
        List<RenduTp> rendus = renduTpDao.listerRendusParSeance(idSeance);
        this.ajouterInformationsBinomes(rendus);
        seanceAvecRendus.setRendus(rendus.stream().collect(Collectors.groupingBy(RenduTp::getBinome)));
        return seanceAvecRendus;
    }

    public Seance getSeanceAvecRessources(Long idSeance) {
        if (idSeance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est incorrect.");
        }
        Seance seance = seanceDao.getSeance(idSeance);
        if (seance == null) {
            throw new IllegalArgumentException("L'identifiant de la séance est inconnu.");
        }

        seance.setRessources(ressourceDao.getRessources(seance));

        return seance;
    }

    public void ajouterSeance(Seance seance) {
        if (seance == null) {
            throw new IllegalArgumentException("La séance est null.");
        }
        if (seance.getTitre() == null || "".equals(seance.getTitre())) {
            throw new IllegalArgumentException("Le titre de la séance doit être renseigné.");
        }
        if (seance.getDate() == null) {
            throw new IllegalArgumentException("La date de la séance doit être renseignée.");
        }
        if (seance.getType() == null) {
            throw new IllegalArgumentException("Le type de la séance doit être renseigné.");
        }

        seanceDao.ajouterSeance(seance);
    }

    public void modifierSeance(Seance seance) {
        if (seance == null) {
            throw new IllegalArgumentException("La séance est null.");
        }
        if (seance.getId() == null) {
            throw new IllegalArgumentException("L'identifiant de la séance doit être renseigné.");
        }
        if (seance.getTitre() == null || "".equals(seance.getTitre())) {
            throw new IllegalArgumentException("Le titre de la séance doit être renseigné.");
        }
        if (seance.getDate() == null) {
            throw new IllegalArgumentException("La date de la séance doit être renseignée.");
        }
        if (seance.getType() == null) {
            throw new IllegalArgumentException("Le type de la séance doit être renseigné.");
        }

        seanceDao.modifierSeance(seance);
    }
    public void calculerMoyenneSeance(List<Seance> seancesNotees, List<EleveAvecNotes> eleves) {
        for(EleveAvecNotes eleve : eleves){
            for(Seance seance  : seancesNotees) {
                Note noteSeance = eleve.getMapSeanceNote().get(seance.getId());
                if (noteSeance!=null){
                    seance.addNote(noteSeance.getValeur());
                }
            }
        }
    }

    private void ajouterInformationsBinomes(List<RenduTp> rendus) {
        for (RenduTp renduTp : rendus) {
            renduTp.setBinome(binomeDao.getBinome(renduTp.getBinome().getUid()));
        }
    }
}
