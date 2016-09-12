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
		assertThat(motDePasse).hasSize(97);
		assertThat(motDePasse).contains(":");
		assertThat(motDePasse.indexOf(":")).isEqualTo(48);
		assertThat(motDePasse2).isNotEqualTo(motDePasse);
	}

	@Test
	public void testVerifierMotDePasse() throws Exception {
		// WHEN
		boolean valide = motDePasseManager.validerMotDePasse("test", "6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81");
		// THEN
		assertThat(valide).isTrue();
	}
}
