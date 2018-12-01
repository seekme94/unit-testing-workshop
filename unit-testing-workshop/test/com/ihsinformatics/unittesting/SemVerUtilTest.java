/**
 * 
 */
package com.ihsinformatics.unittesting;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class SemVerUtilTest {
	
	static List<String> invalidVersions;
	static List<String> validPreAlphaVersions;
	static List<String> validAlphaVersions;
	static List<String> validBetaVersions;
	static List<String> validReleaseVersions;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		invalidVersions = Arrays.asList(null, "", "1", "1.2", "1.0.0-alpha", "1.0.0-beta", "0.0.1-prealpha", "0.9.2+alpha", "2.1.0-rc", "-1.6.0", "a.b.c", "1.2.6-20181130");
		validPreAlphaVersions = Arrays.asList("0.0.0", "0.0.1", "0.0.99");
		validAlphaVersions = Arrays.asList("0.1.0-alpha", "0.1.0-ALPHA", "0.9.19-alpha");
		validBetaVersions = Arrays.asList("0.1.0-beta", "0.1.0-BETA", "0.9.19-beta");
		validReleaseVersions = Arrays.asList("1.0.0", "1.1.1", "3.2.10");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerExceptionOnNullVersion() {
		fail("Not yet implemented");
	}
	
	@Test(expected = NumberFormatException.class)
	public void shouldThrowNumberFormatExceptionOnInvalidVersion() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldValidatePreAlpha() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldValidateAlpha() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldValidateBeta() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldValidateRelease() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testIsPreAlpha() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsAlpha() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsBeta() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsRelease() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompareVersions() {
		fail("Not yet implemented");
	}

	@Test
	public void testBackwardsCompatibility() {
		fail("Not yet implemented");
	}

	@Test
	public void testBackwardsIncompatibility() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}
}
