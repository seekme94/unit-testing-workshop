/**
 * 
 */
package com.ihsinformatics.unittesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		invalidVersions = Arrays.asList(null, "", "1", "1.2", "1.0.0-alpha", "1.0.0-beta", "0.0.1-prealpha",
				"0.9.2+alpha", "2.1.0-rc", "-1.6.0", "a.b.c", "1.2.6-20181130");
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
		new SemVerUtil(null);
	}

	@Test(expected = NumberFormatException.class)
	public void shouldThrowNumberFormatExceptionOnInvalidVersion() {
		invalidVersions.forEach(version -> new SemVerUtil(version == null ? "" : version));
	}

	@Test
	public void shouldValidatePreAlpha() {
		validPreAlphaVersions.forEach(version -> assertTrue(new SemVerUtil(version).isPreAlpha()));
	}

	@Test
	public void shouldValidateAlpha() {
		validAlphaVersions.forEach(version -> assertTrue(new SemVerUtil(version).isAlpha()));
	}

	@Test
	public void shouldValidateBeta() {
		validBetaVersions.forEach(version -> assertTrue(new SemVerUtil(version).isBeta()));
	}

	@Test
	public void shouldValidateRelease() {
		validReleaseVersions.forEach(version -> assertTrue(new SemVerUtil(version).isRelease()));
	}

	@Test
	public void testCompareVersions() {
		String[] olders = { "0.0.1", "0.0.8", "0.2.3-alpha", "1.2.3", "1.2.3", "1.2.3" };
		String[] newers = { "0.0.2", "0.0.8-alpha", "0.2.3-beta", "1.2.4", "1.3.3", "2.2.3" };
		for (int i = 0; i < olders.length; i++) {
			System.out.println(olders[i] + " " + newers[i]);
			assertTrue(SemVerUtil.compareVersions(new SemVerUtil(olders[i]), new SemVerUtil(newers[i])) == 1);
		}
	}

	@Test
	public void testCompareSameVersions() {
		assertTrue(SemVerUtil.compareVersions(new SemVerUtil("0.0.1"), new SemVerUtil("0.0.1")) == 0);
		assertTrue(SemVerUtil.compareVersions(new SemVerUtil("0.5.3-beta"), new SemVerUtil("0.5.3-beta")) == 0);
	}

	@Test
	public void testBackwardsCompatibility() {
		SemVerUtil version = new SemVerUtil("1.5.5");
		assertTrue(version.isBackwardsCompatibleWith(new SemVerUtil("1.5.4")));
	}

	@Test
	public void testBackwardsIncompatibility() {
		SemVerUtil version = new SemVerUtil("1.5.5");
		assertFalse(version.isBackwardsCompatibleWith(new SemVerUtil("1.6.5")));
	}

	@Test
	public void testToString() {
		String version = "0.2.16-beta";
		assertEquals(new SemVerUtil(version).toString(), version);
	}
}
