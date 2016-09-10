package learnings;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class AbstractTestCase {
    protected Date getDate(int annee, int mois, int jour) {
        return new GregorianCalendar(annee, mois, jour).getTime();
    }

    protected Date getDate(int annee, int mois, int jour, int heure, int minute, int seconde) {
        GregorianCalendar cal = new GregorianCalendar(annee, mois, jour, heure, minute, seconde);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
