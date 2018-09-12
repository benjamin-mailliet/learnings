package learnings.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RessourceTestCase {

    @Test
    public void shouldIsLienReturnTrue() {
        // THEN
        Assertions.assertThat(new Ressource(null, null,"http://somewebsite.com",null,null).isLien()).isTrue();
        Assertions.assertThat(new Ressource(null, null,"https://somesecuredwebsite.com",null,null).isLien()).isTrue();
    }

    @Test
    public void shouldIsLienReturnFalse() {
        // THEN
        Assertions.assertThat(new Ressource(null, null,"Ceci est un lien : http://somewebsite.com",null,null).isLien()).isFalse();
        Assertions.assertThat(new Ressource(null, null,"/path/to/file",null,null).isLien()).isFalse();
    }
}
