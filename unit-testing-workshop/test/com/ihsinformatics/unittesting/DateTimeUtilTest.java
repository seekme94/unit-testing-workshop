/**
 * 
 */
package com.ihsinformatics.unittesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class DateTimeUtilTest {

	String sqlFormat = "yyyy-MM-dd HH:mm:ss";
	String isoFormat = "yyyy-MM-dd'T'HH:mm:ss.'Z'";
	String clientFormat = "dd/MM/yyyy HH:mm";

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromString(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldConvertStringDateInClientFormat() {
		Date date = DateTimeUtil.fromString("01/01/2018 00:00", clientFormat);
		assertNotNull(date);
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromString(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldConvertStringDateTimeInClientFormat() {
		Date date = DateTimeUtil.fromString("01/01/2018 16:55", clientFormat);
		assertNotNull(date);
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromString(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldConvertUnusualStringDatesInClientFormat() {
		List<String> list = Arrays.asList("-1/01/2018 00:00", "32/01/2018 00:00", "01/13/2018 00:00",
				"01/01/-800 00:00", "01/01/2018 24:00", "01/01/2018 00:60");
		list.forEach(dateString -> assertNotNull("Should not convert " + dateString,
				DateTimeUtil.fromString(dateString, clientFormat)));
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromString(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmbiguousClientFormatDatesFromString() {
		Date date1 = DateTimeUtil.fromString("12/05/2018 00:00", clientFormat);
		Date date2 = DateTimeUtil.fromString("05/12/2018 00:00", clientFormat);
		assertTrue(date1.before(date2));
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#toString(java.util.Date, java.lang.String)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void shouldConvertDatesToClientFormatString() {
		Map<Date, String> dates = new HashMap<>();
		dates.put(new Date(118, 4, 26, 0, 0), "26/05/2018 00:00");
		dates.put(new Date(118, 4, 26, 12, 30), "26/05/2018 12:30");
		dates.put(new Date(100, 0, 1, 0, 0), "01/01/2000 00:00");
		dates.forEach((date, dateString) -> assertEquals(DateTimeUtil.toString(date, clientFormat), dateString));
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromSqlDateString(java.lang.String)}.
	 */
	@Test
	public void testFromSqlDateString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#fromSqlDateTimeString(java.lang.String)}.
	 */
	@Test
	public void testFromSqlDateTimeString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#toSqlDateString(java.util.Date)}.
	 */
	@Test
	public void testToSqlDateString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#toSqlDateTimeString(java.util.Date)}.
	 */
	@Test
	public void testToSqlDateTimeString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#detectDateFormat(java.lang.String)}.
	 */
	@Test(expected = InvalidParameterException.class)
	public void shouldThrowInvalidParameterExceptionOnUnknownFormat() {
		String dateString = "14081947 010000";
		DateTimeUtil.detectDateFormat(dateString);
	}
	
	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#detectDateFormat(java.lang.String)}.
	 */
	@Test
	public void shouldDetectSQLDateFormat() {
		String dateString = "1947-08-14 01:00:00";
		String detectedFormat = DateTimeUtil.detectDateFormat(dateString);
		assertSame(detectedFormat, sqlFormat);
	}


	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#detectDateFormat(java.lang.String)}.
	 */
	@Test
	public void shouldDetectClientDateFormat() {
		String dateString = "14/08/1947 01:00";
		String detectedFormat = DateTimeUtil.detectDateFormat(dateString);
		assertSame(detectedFormat, clientFormat);
	}

	/**
	 * Test method for
	 * {@link com.ihsinformatics.unittesting.DateTimeUtil#detectDateFormat(java.lang.String)}.
	 */
	@Test
	public void shouldNotDetectClientDateFormat() {
		String dateString = "1947-08-14 01:00:00";
		String detectedFormat = DateTimeUtil.detectDateFormat(dateString);
		assertNotSame(detectedFormat, clientFormat);
	}
}
