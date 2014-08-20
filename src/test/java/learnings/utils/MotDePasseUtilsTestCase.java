package learnings.utils;

import org.junit.Assert;
import org.junit.Test;

public class MotDePasseUtilsTestCase {

	@Test
	public void testGenererMotDePasse() throws Exception {
		System.out.println(MotDePasseUtils.genererMotDePasse("test"));
	}

	@Test
	public void testVerifierMotDePasse() throws Exception {
		Assert.assertTrue(MotDePasseUtils.validerMotDePasse("test",
				"6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81"));
	}
}
