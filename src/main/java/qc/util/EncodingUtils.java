package qc.util;

import java.io.UnsupportedEncodingException;

public class EncodingUtils {
	public static String getCharset(String source) {
		try {
			String charsetName = "ISO-8859-1";
			if (isCharset(source, charsetName)) {
				return charsetName;
			}

			charsetName = "UTF-8";
			if (isCharset(source, charsetName)) {
				return charsetName;
			}

			charsetName = "GB2312";
			if (isCharset(source, charsetName)) {
				return charsetName;
			}

			charsetName = "GB2312";
			if (isCharset(source, charsetName)) {
				return charsetName;
			}

			charsetName = "BIG5";
			if (isCharset(source, charsetName)) {
				return charsetName;
			}
		} catch (UnsupportedEncodingException e) {
			return "unsupportedEncodingException";
		}

		return "unknowed";
	}

	public static boolean isCharset(String source, String charsetName)
			throws UnsupportedEncodingException {
		return source.equals(new String(source.getBytes(charsetName),
				charsetName));
	}
}
