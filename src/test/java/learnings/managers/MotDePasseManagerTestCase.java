package learnings.managers;

import learnings.managers.MotDePasseManager;

import org.junit.Assert;
import org.junit.Test;

public class MotDePasseManagerTestCase {

	private MotDePasseManager motDePasseManager = new MotDePasseManager();

	@Test
	public void testGenererMotDePasse() throws Exception {
		String motDePasse = motDePasseManager.genererMotDePasse("test");
		Assert.assertTrue(97 == motDePasse.length());
		Assert.assertTrue(motDePasse.contains(":"));
		Assert.assertTrue(motDePasse.indexOf(":") == 48);

		String motDePasse2 = motDePasseManager.genererMotDePasse("test");
		Assert.assertNotEquals(motDePasse, motDePasse2);
	}

	@Test
	public void testVerifierMotDePasse() throws Exception {
		Assert.assertTrue(motDePasseManager.validerMotDePasse("test",
				"6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81"));
	}
}
