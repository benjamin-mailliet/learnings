package learnings.managers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MotDePasseManagerTestCase {

	private MotDePasseManager motDePasseManager = new MotDePasseManager();

	@Test
	public void shouldGenererMotDePasse() throws Exception {
		// WHEN
		String motDePasse = motDePasseManager.genererMotDePasse("test");
		String motDePasse2 = motDePasseManager.genererMotDePasse("test");
		// THEN
		assertThat(motDePasse2).isNotEqualTo(motDePasse);
	}

	@Test
	public void testVerifierMotDePasse() throws Exception {
		// WHEN
		boolean valide = motDePasseManager.validerMotDePasse("test", "$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw");
		// THEN
		assertThat(valide).isTrue();
	}
}
