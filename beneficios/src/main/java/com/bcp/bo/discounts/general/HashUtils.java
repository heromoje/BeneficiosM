package com.bcp.bo.discounts.general;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

import com.bcp.bo.discounts.general.Constants.BuildLevel;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public class HashUtils {

	/**
	 * obtains from this app signature SHA1 fingerprint as hexadecimal string "AA:BB:00..."
	 * @param context of app
	 * @return string with SHA1 fingerprint as hexadecimal "AA:BB:00..."
	 */
	private static String getCertificateSHA1Fingerprint(Context context) {
	    PackageManager pm = context.getPackageManager();
	    String packageName = context.getPackageName();
	    int flags = PackageManager.GET_SIGNATURES;
	    PackageInfo packageInfo = null;
	    try {
	        packageInfo = pm.getPackageInfo(packageName, flags);
	    } catch (PackageManager.NameNotFoundException e) {
	        e.printStackTrace();
	    }
	    Signature[] signatures = packageInfo.signatures;
	    byte[] cert = signatures[0].toByteArray();
	    InputStream input = new ByteArrayInputStream(cert);
	    CertificateFactory cf = null;
	    try {
	        cf = CertificateFactory.getInstance("X509");
	    } catch (CertificateException e) {
	        e.printStackTrace();
	    }
	    X509Certificate c = null;
	    try {
	        c = (X509Certificate) cf.generateCertificate(input);
	    } catch (CertificateException e) {
	        e.printStackTrace();
	    }
	    String hexString = null;
	    try {
	    	byte[] encodedKey = getSha1ByteArrayFromByteArray(c.getEncoded());
	        hexString = byte2HexFormatted(encodedKey, true);
	    } catch (CertificateEncodingException e) {
	    	L.e(HashUtils.class.getSimpleName(), "getSha1FromString()", "CertificateEncodingException --> " + e.getMessage());
	    }
	    return hexString;
	}
	
	/**
	 * Summarizes a received byte array into his hash byte array 
	 * @param element byte array
	 * @return element byte array hash
	 */
	private static byte[] getSha1ByteArrayFromByteArray(byte[] element){
		byte[] encodedElement = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        encodedElement = md.digest(element);
	    } catch (NoSuchAlgorithmException e1) {
	        L.e(HashUtils.class.getSimpleName(), "getSha1FromString()", "NoSuchAlgorithmException --> " + e1.getMessage());
	    }
	    return encodedElement;
	};
	
	/**
	 * calculate the hash of concatenate this app signature SHA1 fingerprint and the given param
	 * and returns this value as hexadecimal String "AABBCC00..."
	 * @param activity context
	 * @param param param that will be concatenated to fingerprint
	 * @return hexadecimal String "AABBCC00..." hash of fingerprint and param
	 */
	public static String calculateHsvFromParam(Activity activity, String param){
		String signatureFingerPrint = null;
		if (Constants.buildLevel == BuildLevel.PRODUCTION){
			signatureFingerPrint = getCertificateSHA1Fingerprint(activity);
		}else{
			signatureFingerPrint = Constants.SFP;
		}
		
		String sfpAndParam = signatureFingerPrint + param;
		byte[] sfpAndParamBA =getSha1ByteArrayFromByteArray(sfpAndParam.getBytes());
		sfpAndParam = byte2HexFormatted(sfpAndParamBA,false);
	    return sfpAndParam;
	};
	
	/**
	 * Converts a byte array into hexadecimal String
	 * @param arr byte array that will be converted
	 * @param twoPointsSeparator true if use of colon as separator and false otherwise 
	 * @return
	 */
	private static String byte2HexFormatted(byte[] arr, boolean twoPointsSeparator) {
	    StringBuilder str = new StringBuilder(arr.length * 2);
	    for (int i = 0; i < arr.length; i++) {
	        String h = Integer.toHexString(arr[i]);
	        int l = h.length();
	        if (l == 1) h = "0" + h;
	        if (l > 2) h = h.substring(l - 2, l);
	        str.append(h.toUpperCase(Locale.getDefault()));
	        if (i < (arr.length - 1) && twoPointsSeparator) str.append(':');
	    }
	    return str.toString();
	}
}
