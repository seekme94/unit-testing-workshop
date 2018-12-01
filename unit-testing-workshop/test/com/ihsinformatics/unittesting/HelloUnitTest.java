/**
 * 
 */
package com.ihsinformatics.unittesting;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class HelloUnitTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass() is executed before first test");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass() is executed after all tests");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void runBeforeEachTest() throws Exception {
		System.out.println("runBeforeEachTest() is executed before each test");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void runAfterEachTest() throws Exception {
		System.out.println("runAfterEachTest() is executed after each test");
	}

	@Test
	public void boundToFail() {
		fail("boundToFail() will just fail the test case");
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerException() {
		Double.parseDouble(null);
	}

	@Test(timeout = 2000)
	public void shouldNotTakeMoreThanTwoSeconds() {
		for (int i = 0; i < 10000000; i++) {
			BigInteger bigInteger = new BigInteger(String.valueOf(i));
			bigInteger.multiply(bigInteger);
		}
	}

	@Test
	public void shouldConvertDouble() {
		try {
			Stream.of("0", "1.2", "2", "3.33D", "3.14F", "-1", "-99.99", "1234567890.987654321")
					.forEach(num -> Double.parseDouble(num));
		} catch (Exception e) {
			fail("Should convert all the numbers.");
		}
	}


	@Test
	@Ignore
	public void ignoreMe() {
		if (1 > 0) {
			fail("I don't existed");
		}
	}
}
