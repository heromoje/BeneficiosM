package com.bcp.bo.discounts.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtil {
	public static int PHONE_LENGHT=8;
	public static String removeDiacriticalMarks(String string) {
	    return Normalizer.normalize(string, Form.NFD)
	        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
