/**
 * 
 */
package com.ihsinformatics.unittesting;

import java.util.stream.Stream;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class SemVerUtil {

	private static String VALID_PREALPHA_REGEX = "^0\\.0\\.[0-9]+";
	private static String VALID_ALPHA_REGEX = "^0\\.[0-9]+\\.[0-9]+\\-(alpha)";
	private static String VALID_BETA_REGEX = "^0\\.[0-9]+\\.[0-9]+\\-(beta)";
	private static String VALID_RELEASE_REGEX = "^[0-9]+\\.[0-9]+\\.[0-9]+";
	private static String VERSION_SPLITTER = "\\.";
	private static String EXTENSION_SEPARATOR = "-";

	private int major;
	private int minor;
	private int micro;
	private String extension;

	/**
	 * Pass a valid version in the string according to Semantic versioning scheme
	 * 
	 * @param version
	 */
	public SemVerUtil(String version) {
		this.parseVersion(version.toLowerCase());
	}

	/**
	 * Parse version in a string
	 * 
	 * @param version
	 */
	protected void parseVersion(String version) {
		if (version == null) {
			throw new NullPointerException("Input cannot be null");
		}
		String[] parts;
		if (version.matches(VALID_PREALPHA_REGEX)) {
			parts = version.split(VERSION_SPLITTER);
			setMajor(0);
			setMinor(0);
			setMicro(Integer.parseInt(parts[2]));
			extension = null;
		} else if (version.matches(VALID_ALPHA_REGEX)) {
			extension = "alpha";
			version = version.substring(0, version.lastIndexOf(EXTENSION_SEPARATOR));
			parts = version.split(VERSION_SPLITTER);
			setMajor(0);
			setMinor(Integer.parseInt(parts[1]));
			setMicro(Integer.parseInt(parts[2]));
		} else if (version.matches(VALID_BETA_REGEX)) {
			extension = "beta";
			version = version.substring(0, version.lastIndexOf(EXTENSION_SEPARATOR));
			parts = version.split(VERSION_SPLITTER);
			setMajor(0);
			setMinor(Integer.parseInt(parts[1]));
			setMicro(Integer.parseInt(parts[2]));
		} else if (version.matches(VALID_RELEASE_REGEX)) {
			extension = null;
			parts = version.split(VERSION_SPLITTER);
			setMajor(Integer.parseInt(parts[0]));
			setMinor(Integer.parseInt(parts[1]));
			setMicro(Integer.parseInt(parts[2]));
		} else {
			throw new NumberFormatException("Invalid input!");
		}
	}

	/**
	 * @return the major
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * @param major the major to set
	 */
	public void setMajor(int major) {
		this.major = major;
	}

	/**
	 * @return the minor
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @param minor the minor to set
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}

	/**
	 * @return the micro
	 */
	public int getMicro() {
		return micro;
	}

	/**
	 * @param micro the micro to set
	 */
	public void setMicro(int micro) {
		this.micro = micro;
	}

	/**
	 * Checks whether current version is pre-alpha or not
	 * 
	 * @return
	 */
	public boolean isPreAlpha() {
		return (getMajor() == 0 && getMinor() == 0 && extension == null);
	}

	/**
	 * Checks whether current version is alpha or not
	 * 
	 * @return
	 */
	public boolean isAlpha() {
		if (extension == null) {
			return false;
		}
		return extension.equals("alpha");
	}

	/**
	 * Checks whether current version is alpha or not
	 * 
	 * @return
	 */
	public boolean isBeta() {
		if (extension == null) {
			return false;
		}
		return extension.equals("beta");
	}

	/**
	 * Checks whether current version is a production release or not
	 * 
	 * @return
	 */
	public boolean isRelease() {
		return !(isPreAlpha() || isAlpha() || isBeta()) && extension == null;
	}

	/**
	 * Compares two versions and: returns 0 if both are same; 1 if newer is ahead of
	 * older; -1 if older is ahead of newer
	 * 
	 * @param older
	 * @param newer
	 * @return
	 */
	public static int compareVersions(SemVerUtil older, SemVerUtil newer) {
		if (older.toString().equals(newer.toString())) {
			return 0;
		}
		if (older.isPreAlpha() && !newer.isPreAlpha()) {
			return 1;
		}
		if (older.isAlpha() && (newer.isBeta() || newer.isRelease())) {
			return 1;
		}
		if (older.isBeta() && newer.isRelease()) {
			return 1;
		}
		boolean allFlags = older.isPreAlpha() == newer.isPreAlpha() 
				|| older.isAlpha() == newer.isAlpha()
				|| older.isBeta() == newer.isBeta()
				|| older.isRelease() == newer.isRelease();
		if (allFlags) {
			// If both are release, then compare major, minor and micro
			if (older.getMajor() < newer.getMajor()) {
				return 1;
			}
			if (older.getMajor() == newer.getMajor() && older.getMinor() < newer.getMinor()) {
				return 1;
			}
			if (older.getMajor() == newer.getMajor() && older.getMinor() == newer.getMinor()
					&& older.getMicro() < newer.getMicro()) {
				return 1;
			}
		}
		return -1;
	}

	/**
	 * @param semVerUtil
	 * @return
	 */
	public boolean isBackwardsCompatibleWith(SemVerUtil semVerUtil) {
		return getMajor() == semVerUtil.getMajor() && minor == semVerUtil.getMinor();
	}

	@Override
	public String toString() {
		String string = getMajor() + "." + getMinor() + "." + getMicro() + (extension == null ? "" : "-" + extension);
		return string;
	}
}
