package com.bcp.bo.discounts.general;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Debug;
import android.util.Log;

public class L {
	
	private static final String PREFIX_LOG = "BCP";
	
	/** Text saved for remote logs **/
	@SuppressWarnings("unused")
	private static String sLog = "";
		
	public static void d(String text){
		
		String sClass = "";
		String sMethod = "";
		
		//get the class and the method names
		StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
		if(stackTraceElements.length>1){
			sClass = stackTraceElements[1].getClassName();
			int pos = sClass.lastIndexOf(".");
			if(pos!=-1){
				sClass = sClass.substring(pos+1);
			}
			sMethod = stackTraceElements[1].getMethodName();
		}
		
		//call to old log
		d(sClass, sMethod, text);
	}
	
	/**
	 * Write a log if SHOW_LOGS is activated in Constants
	 * @param sClass - Name of the class
	 * @param sMethod - Name of the method
	 * @param text - Text to write
	 */
	public static void d(String sClass, String sMethod, String text) {
		if (Constants.SHOW_LOGS) {
			try {
				String str = getTextLog(sClass, sMethod, text);
				Log.d(PREFIX_LOG, str);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Write a log if SHOW_LOGS is activated in Constants
	 * @param sClass - Name of the class
	 * @param sMethod - Name of the method
	 * @param text - Text to write
	 */
	public static void d2(String sClass, String sMethod, String text) {
		if (Constants.SHOW_LOGS) {
			String str = getTextLog(sClass, sMethod, text);
			Log.d(PREFIX_LOG + "_2", str);
		}
	}

	/**
	 * Write a log, it doesn't matter if SHOW_LOGS is deactivated
	 * @param sClass - Name of the class
	 * @param sMethod - Name of the method
	 * @param text - Text to write
	 */
	public static void e(String sClass, String sMethod, String text) {
		String str = getTextLog(sClass, sMethod, text);
		Log.e(PREFIX_LOG + "_2", str);
	}
	
	/**
	 * Get the text to write in the log
	 * @param sClass - Name of the class
	 * @param sMethod - Name of the method
	 * @param text - Text to write in the log
	 * @return String - Text with the time, class, method and text
	 */
	private static String getTextLog(String sClass, String sMethod, String text){
		try {
			
			Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);

//            String memMessage = String.format(Locale.getDefault(), "[T(%.2f), P(%.2f), S(%.2f)]",
//                    memoryInfo.getTotalPss() / 1024.0,
//                    memoryInfo.getTotalPrivateDirty() / 1024.0,
//                    memoryInfo.getTotalSharedDirty() / 1024.0);
            
            String memMessageShort = String.format(Locale.getDefault(), "[T(%.2f)]",
                    memoryInfo.getTotalPss() / 1024.0);
			
			Calendar c = Calendar.getInstance();
			DecimalFormat df = new DecimalFormat("00");
			String sDate = c.get(Calendar.YEAR) + "/"
					+ (c.get(Calendar.MONTH) + 1) + "/"
					+ df.format(c.get(Calendar.DAY_OF_MONTH)) + " "
					+ df.format(c.get(Calendar.HOUR_OF_DAY)) + ":"
					+ df.format(c.get(Calendar.MINUTE)) + ":" + df.format(c.get(Calendar.SECOND));
			return "[" + sDate + "] [" + memMessageShort + "] [" + sClass + "] [" + sMethod
					+ "] " + ""/*memMessage*/ + " : " + text + "\n";
			
		} catch (Exception e) {
		}
		
		return "";
	}
	
	
}
