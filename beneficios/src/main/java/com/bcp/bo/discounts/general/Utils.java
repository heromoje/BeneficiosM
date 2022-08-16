package com.bcp.bo.discounts.general;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class Utils {
	
	private static final String TELEPHONE_PREFIX_FOR_ANDROID = "tel:";
	private static final String STORE_PREFIX = "market://details?id=";
	
	public static String parseDate(Date date, SimpleDateFormat formatter) {
		return formatter.format(date);
	}

	/**
	 * Check if string is a number with a minimun size
	 * @param number String to check
	 * @param minSize Min size (you can write -1 if no want to check)
	 * @return true if it is a number with min size given
	 */
	public static boolean isNumberMinSize(String number, int minSize){
		
		if(number.length()<minSize){
			return false;
		}
		
		char[] numbers = number.toCharArray();
				
		try{
			for(char digit : numbers){
				Integer.parseInt(""+digit);
			}
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Generate open xml tag
	 * @param name tag name
	 * @return tag name with < > simbols
	 */
	public static String openTag(String name){
		return "<" + name + ">";
	}
	
	
	/**
	 * Generate close xml tag
	 * @param name tag name
	 * @return tag name with </ > simbols
	 */
	public static String closeTag(String name){
		return "</" + name + ">";
	}


	/***
	 * Convert from text to Codes
	 * @param text text to convert
	 */
	public static void convert(String text){
		
	}
	
	/**
	 * Get the integer of an String and return -1 in case of error
	 * @param s String
	 * @return number if OK / -1 if error
	 */
	public final static int getInt(String s){
		return getInt(s, -1);
	}
	
	/**
	 * Get the integer of an String and return -1 in case of error
	 * @param s String
	 * @param iError number to return if occurs and error
	 * @return number if OK / iError if error
	 */
	public final static int getInt(String s, int iError){
		try{
			return Integer.parseInt(s);
		}catch(NumberFormatException e){
			L.d("Utils", "getInt", "NumberFormatException parsing to Int: " + s);
			return iError;
		}
	}
	
	/**
	 * Get the long of an String and return -1 in case of error
	 * @param s String
	 * @return number if OK / -1 if error
	 */
	public final static long getLong(String s){
		return getLong(s, -1);
	}
	
	/**
	 * Get the long of an String and return -1 in case of error
	 * @param s String
	 * @param iError number to return if occurs and error
	 * @return number if OK / iError if error
	 */
	public final static long getLong(String s, int lError){
		try{
			return Long.parseLong(s);
		}catch(NumberFormatException e){
			L.d("Utils", "getLong", "NumberFormatException parsing to Long: " + s);
			return lError;
		}
	}
	
	/**
	 * Get the float of an String and return -1 in case of error
	 * @param s String
	 * @return number if OK / -1 if error
	 */
	public final static float getFloat(String s){
		return getFloat(s, -1);
	}
	
	/**
	 * Get the long of an String and return -1 in case of error
	 * @param s String
	 * @param fError number to return if occurs and error
	 * @return number if OK / fError if error
	 */
	public final static float getFloat(String s, int fError){
		try{
			return Float.parseFloat(s);
		}catch(NumberFormatException e){
			L.d("Utils", "getFloat", "NumberFormatException parsing to Float: " + s);
			return fError;
		}
	}
	
	/**
	 * Get the double of an String and return -1 in case of error
	 * @param s String
	 * @return number if OK / -1 if error
	 */
	public final static double getDouble(String s){
		return getDouble(s, -1);
	}
	
	/**
	 * Get the double of an String and return -1 in case of error
	 * @param s String
	 * @param iError number to return if occurs and error
	 * @return number if OK / iError if error
	 */
	public final static double getDouble(String s, double iError){
		try{
			return Double.parseDouble(s);
		}catch(NumberFormatException e){
			return iError;
		}
	}
	
	/**
	 * Make a call to the number selected
	 * @param ctx Context
	 * @param number number to call
	 */
	public static void makeACall(Context ctx, String number){
		String url = "tel:"+number;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(url));
		ctx.startActivity(intent);
	}
	
	/**
	 * Open the dialer with the number given to make a call
	 * @param ctx Context
	 * @param number number to call
	 */
	public static void openToCall(Context ctx, String number){		
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}
	
	/**
	 * Hide the keyboard
	 * @param ctx
	 * @param editText
	 */
	public static void hideKeyBoard(Context ctx, View editText){
		//esconde el teclado virtual
		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	/**
	 * Check that was only possible to add numbers in the edittext
	 */
	public static TextWatcher textNumericWatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable arg0) {
			if(arg0.length()>0){
				int value = Utils.getInt(""+arg0.charAt(arg0.length()-1), -1);
				if(value==-1){
					arg0.replace(0, arg0.length(), arg0.subSequence(0, arg0.length()-1));
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	};
	
	/**
	 * Load the GPS Settings android screen to activate, deactivate the GPS
	 * @param ctx Context of the application to can call the screen
	 */
	public static void goToGPSSettingsScreen(Activity ctx, int requestValue){
		ctx.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), requestValue);
	}
	
	/**
	 * Read a String from an input stream
	 * The format is number of characters and group of chars
	 * @param dis
	 * @return the string read
	 * @throws Exception
	 */
	public static String readString(DataInputStream dis) throws Exception{
		//getting number of letters
		int num = dis.readInt();
		final int MAX_SIZE_STRING = 50000;
		
		//reading letters
		if(num<0 || num>MAX_SIZE_STRING){
			return null;
		}
		char[] chars = new char[num];
		for(int i=0; i<num; i++){
			chars[i] = dis.readChar();
		}
		
		return new String(chars);
	}
	
	/**
	 * Write a String in an outputstream
	 * With the format number of chars and group of chars
	 * @param dos
	 * @param str string to save
	 * @throws Exception if problem saving
	 */
	public static void writeString(DataOutputStream dos, String str) throws Exception{
		
		if(str==null){
			dos.writeInt(-1);
			return;
		}
		
		//getting number of letters
		dos.writeInt(str.length());
		
		//reading letters
		char[] strChar = str.toCharArray();
		for(int i=0; i<strChar.length; i++){
			dos.writeChar(strChar[i]);
		}
		
		return;
	}
	
	/**
	 * Read an image from an inputstream
	 * @param dis inputstream
	 * @return Drawable of the image
	 * @throws Exception if it was impossible to convert to an image or no bytes
	 */
    public static Drawable readImage(DataInputStream dis) throws Exception{
    	//getting size of image
		int num = dis.readInt();

        //reading bytes
		byte[] bytes = new byte[num];
		dis.read(bytes);
		
		return Drawable.createFromStream(new ByteArrayInputStream(bytes), "");
    }
    
    public static void startAnimation(Context ctx, View v, int idRAnimation){
		Animation anim = AnimationUtils.loadAnimation(ctx, idRAnimation);
		anim.setFillAfter(true);
    	v.startAnimation(anim);
	}
	
	public static void startAnimation(Context ctx, View v, int idRAnimation, Animation.AnimationListener animationListener){
		Animation anim = AnimationUtils.loadAnimation(ctx, idRAnimation);
		anim.setAnimationListener(animationListener);
		anim.setFillAfter(true);
    	v.startAnimation(anim);
	}
	
	
	public static boolean gotoActivity(Context ctx, String sPackage, String sActivity, String[] parameters, String[] values){
		try{
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			intent.setComponent(new ComponentName(sPackage, sPackage + sActivity));
			if(parameters!=null && values!=null && parameters.length>0 && values.length==parameters.length){
				for(int i=0;i<parameters.length; i++){
					intent.putExtra(parameters[i], values[i]);
				}
			}
			ctx.startActivity(intent);
		}catch(Exception e){
			L.d("Utils", "gotoActivity", "Impossible throw: " + sPackage + " with Activity: " + sActivity);
			return false;
		}
		
		return true;
	}
	
	public static boolean isPackageInstalled(Context ctx, String sPackage){
		if(sPackage==null || "".equalsIgnoreCase(sPackage)){
			return false;
		}
		
		try{
			PackageInfo pI = ctx.getPackageManager().getPackageInfo(sPackage, PackageManager.MATCH_DEFAULT_ONLY);
			if(pI==null){
				return false;
			}else{
				L.d("Utils", "isPackageInstalled", "Version de Tick2go isntalada: " + pI.versionName);
			}			
		}catch(PackageManager.NameNotFoundException nnfe){
			L.d("Utils", "isPackageInstalled", "Exception getting package: " + sPackage + ": " + nnfe.toString());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Send an email (open to send) to the account selected
	 * @param ctx Context of the activity
	 * @param account Account email to send
	 */
	public static void sendMail(Context ctx, String account, String sText){
		Intent i = new Intent(Intent.ACTION_SEND);
	    i.setType("text/plain");
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.putExtra(Intent.EXTRA_EMAIL, new String[]{account});
	    ctx.startActivity(Intent.createChooser(i, sText));
	}
	
	/**
	 * Add the data as a contact (open the screen to add)
	 * NOTE: Don't send null fields, if you don't have all data send empty fields ""
	 * @param ctx Context of application
	 * @param name Name of contact
	 * @param number First number phone (mobile)
	 * @param number2 Second number phone (phone)
	 * @param mail Email of contact
	 * @param address Address, postal code...
	 */
	public static void addToContacts(Context ctx, String name, String number, String number2, String number3, String mail, String address, 
			int typeNumber, int typeNumber2, int typeNumber3, int typeEmail){
		
		
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

		intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
		if(number!=null && number.length()>0){
			intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
			intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, typeNumber);
		}
		if(number2!=null && number2.length()>0){
			intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, number2);
			intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, typeNumber2);
		}
		if(number3!=null && number3.length()>0){
			intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, number3);
			intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, typeNumber3);
		}
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mail);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, typeEmail);
		intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
		
		try{
			ctx.startActivity(intent);
		}catch(Exception e){
			L.d("Utils", "addToContacts", "Exception adding contact: " + e.toString());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Send a sms to a number (open the program to send it)
	 * @param ctx Context of application
	 * @param number Number phone to send the sms
	 */
	public static void sendSMS(Context ctx, String number){
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("address", number);
		smsIntent.putExtra("sms_body","");
		ctx.startActivity(smsIntent);
	}
	
	/**
	 * Open a url in the browser
	 */
	public static void openUrl(Context ctx, String url){	
		try{
			ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
		}catch(Exception e1){
			System.out.println("Exception Opening URL: " + e1.toString());
		}
	}
	
	/**
	 * Devuelve el numero de telefono del terminal
	 * 
	 * @return String numero de telefono obtenido
	 */
	public static final String detectTelephone(Context ctx) {

		TelephonyManager mTelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
		//String imei = mTelephonyMgr.getDeviceId();
		String phoneNumber=mTelephonyMgr.getLine1Number();
		if(phoneNumber==null){
			phoneNumber = "";
		}else if(!isNumberMinSize(phoneNumber,9) || phoneNumber.length()>9){
			if(phoneNumber.length()>9){
				phoneNumber = phoneNumber.substring(phoneNumber.length()-9, phoneNumber.length());
			}
		}
		
		return phoneNumber;
	}
	
	/**
	 * Obtiene el IMEI del telefono
	 * @param ctx Contexto de la actividad
	 * @return IMEI | Null
	 */
	public static String setGlobalImei(Context ctx){
		TelephonyManager mTelephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
		if(mTelephonyMgr!=null){
			return mTelephonyMgr.getDeviceId();
		}
		
		return null;
	}
		
	/**
	 * Detecta si es un numero de telefono
	 * @param number numero de telefono a comprobar
	 * @return true: es un numero de telefono
	 */
	public static boolean isTelephoneNumber(String number){
		
		if(number.length()!=9){
			return false;
		}
		
		char[] numbers = number.toCharArray();
		if(numbers[0]!='6'){
			return false;			
		}
		
		try{
			for(char digit : numbers){
				Integer.parseInt(""+digit);
			}
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	public static String firstLetterUppercase(String text){
		if(text!=null && text.length()>1){
			return new String(text.substring(0, 1)).toUpperCase(Locale.getDefault()) + text.substring(1, text.length()); 
		}
		return text;
	}
	
	/**
	 * Capitalize all the words of a sentence. Catch only words separated by space
	 * @param text
	 * @return
	 */
	public static String getTextCapitalized(String text){
		String str = "";
		
		String[] aText = text.split(" ");
		for(int i=0; i<aText.length; i++){
			if(aText[i].length()>0){
				
				//add space
				if(i>0){
					str += " ";
				}
				
				//capitalize
				str += 	aText[i].substring(0, 1).toUpperCase(Locale.getDefault()) +
						aText[i].substring(1).toLowerCase(Locale.getDefault());
			}
		}
		
		return str;
	}
	
	public static boolean isEMail(String sEmail)
	{
		if( sEmail.contains("@") && sEmail.substring(sEmail.indexOf('@')).contains(".")){
			return true;
		}
		return false;
	}
	
	/**
	 * Carga el texto de un archivo y devuelve el valor en el handler
	 * @param file archivo a cargar
	 * @param handler Manejador utilizado
	 * @param valueHandler Valor para el manejador
	 */
	public static void loadFile(final InputStream file, final Handler handler, final int valueHandler){
		Thread t = new Thread() {
			@Override
			public void run() {
				//crea mensaje para el handler
				Message msg = new Message();
				msg.what = valueHandler;
				
				//variables de texto para guardar
				String linea;
				String text = "";
				
				try {
					//leemos el archivo
					InputStreamReader isr = new InputStreamReader(file);
					BufferedReader br = new BufferedReader(isr);
					while ((linea = br.readLine()) != null) {
						text += linea + "\n";
					}
					file.close();
					
					//enviamos el mensaje
					msg.arg1 = 0;
					msg.obj = text;
					handler.sendMessage(msg);
				} catch (IOException e) {
					System.out.println("Error " + e.getMessage());
					msg.arg1 = -1;
					msg.obj = text;
					handler.sendMessage(msg);
				}
			}
		};
		t.start();
		return;
	}
	
	/**
	 * Comprueba si es null o esta vacio el texto dado
	 * @param str texto
	 * @return true if OK, false otherwise
	 */
	public static boolean isWritten(String str){
		if(str!=null && !"".equalsIgnoreCase(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Fuerza un idioma en resources
	 * @param ctx Contecto de la aplicacion
	 * @param sLocale string del locale que vamos a cargar
	 */
	public static void forceLanguage(Context ctx, String sLocale){
		Resources res = ctx.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(sLocale);
		res.updateConfiguration(conf, dm);
	}
	
	

	
	
	/**
	 * Hide the keyboard 
	 * @param ctx Context of application
	 * @param eText EditText focused
	 */
	public static void hideKeyboard(Context ctx, EditText eText){ 
		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(eText.getWindowToken(), 0); 
	}
	
	public static String getOSVersion(){
		return android.os.Build.VERSION.RELEASE;//System.getProperty("os.version");
	}
	
	public static String getAppVersion(Context context){
        try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		};
		
		return "";
	}
	
	public static String getConnectionType(Context context){
		ConnectivityManager   conectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo activeNetInfo = conectivity.getActiveNetworkInfo(); 
        
        if (activeNetInfo != null){
	       return activeNetInfo.getSubtypeName() ;
        }
        
        return "";
	}
	
	public static String getDeviceModel(){
		return android.os.Build.MODEL;
	}
		
	public static String getUDIDHash(Context context){
		try {
			return Sha1.SHA1(getIdInterface(context, 1,false) + getIdInterface(context, 4,false));
		} catch (NoSuchAlgorithmException e) {
			L.d("ActExample", "getUDIDHash", "Exception1 getting hash of UDID: " + e.toString());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			L.d("ActExample", "getUDIDHash", "Exception2 getting hash of UDID: " + e.toString());
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getIdInterface(Context context, int type, boolean hashCode){
    	String id = "";
    	
    	switch(type) { 
    	
	    	case 1: {
	    		
	    		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    		id = TelephonyMgr.getDeviceId();
	    		break;
	    		
	    	}
	    	
	    	case 2:{
	    		
	    		id = "35" + 
	    	        	Build.BOARD.length()%10+ Build.BRAND.length()%10 +
	    	        	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
	    	        	Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
	    	        	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
	    	        	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
	    	        	Build.TAGS.length()%10 + Build.TYPE.length()%10 +
	    	        	Build.USER.length()%10 ; //13 digits
	    		
	    		break;
	    		
	    	}
	    	
	    	case 3:{
	    		
	    		id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	    		break;
	    		
	    	}
	    	
	    	case 4:{
	    		
	    		context.getApplicationContext();
				WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	    		id = wm.getConnectionInfo().getMacAddress();
	    		break;
	    		
	    	}
	    	
	    	case 5:{
	    		
	    		BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
	        	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        	id = m_BluetoothAdapter.getAddress(); 
	        	break;
	        	
	    	}
	    	
	    	case 6:{
	    		
	    		String m_szLongID = getIdInterface(context, 7,false);
	        	// compute md5
	        	MessageDigest m = null;
	    		
	        	try {
	    			m = MessageDigest.getInstance("MD5");
	    		} catch (NoSuchAlgorithmException e) {
	    			e.printStackTrace();
	    		}
	    		
	        	m.update(m_szLongID.getBytes(),0,m_szLongID.length());
	    		
	        	// get md5 bytes
	    		byte p_md5Data[] = m.digest();
	    		
	    		// create a hex string
	    		String m_szUniqueID = new String();
	    		
	    		for (int i=0;i<p_md5Data.length;i++) {
	    			
	    			int b =  (0xFF & p_md5Data[i]);
	    			
	    			// if it is a single digit, make sure it have 0 in front (proper padding)	    			
	    			if (b <= 0xF) m_szUniqueID+="0";
	    			
	    			// add number to string
	    			m_szUniqueID+=Integer.toHexString(b);
	    			
	    		}
	    		
	    		// hex string to uppercase
	    		id = m_szUniqueID.toUpperCase(Locale.getDefault());
	    		
	    		break;
	    	}
	    	
	    	case 7:{
	    		
	    		for (int i = 0;i < 6;i++)
	    			id = id + getIdInterface(context, i,false);
	    		
	    	}
	    }
    	
    	if (hashCode){
    		
    		id.hashCode();
    		
    	}
    	
    	return id;
    	
    }
	
	public static class Sha1 {
	
		private static String convertToHex(byte[] data) { 
	        StringBuffer buf = new StringBuffer();
	       
	        for (int i = 0; i < data.length; i++) { 
	            
	        	int halfbyte = (data[i] >>> 4) & 0x0F;
	           
	        	int two_halfs = 0;
	            do{ 
	            	
	                if ((0 <= halfbyte) && (halfbyte <= 9)) 
	                    buf.append((char) ('0' + halfbyte));
	                else 
	                    buf.append((char) ('a' + (halfbyte - 10)));
	                
	                halfbyte = data[i] & 0x0F;
	            
	            } while(two_halfs++ < 1);
	       
	        } 
	       
	        return buf.toString();
	    } 
	 
	    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
		   
	    	MessageDigest md;
		    
	    	md = MessageDigest.getInstance("SHA-1");
		    
	    	byte[] sha1hash = new byte[40];
		    
	    	md.update(text.getBytes("iso-8859-1"), 0, text.length());
		    
	    	sha1hash = md.digest();
		    
	    	return convertToHex(sha1hash);
	    } 
		
	}
	
	@SuppressWarnings("rawtypes")
	public final static Method getMethod(Class container, String methodName) { 
		if ( container == null || methodName == null ) { 
			return null; 
		} 
		Method[] methods = container.getMethods(); 
		for (int ii = 0; ii < methods.length; ii++) { 
			if ( methodName.equalsIgnoreCase( methods[ii].getName() ) ) { 
				return methods[ii]; 
			}
		} 
		return null; 
	} 
	
	@SuppressWarnings("rawtypes")
	public final static Field getField(Class container, String constantName) { 
		if ( container == null || constantName == null ) { 
			return null; 
		} 
		Field[] fields = container.getFields(); 
		for (int ii = 0; ii < fields.length; ii++) { 
			if ( constantName.compareTo( fields[ii].getName() ) == 0 ) { 
				return fields[ii]; 
			}
		} 
		return null; 
	} 
	
	public final static Object executeMethod(Object instance, String methodName, Object... args) { 
		if ( instance == null || methodName == null ) { 
			return null; 
		}
		Method method = getMethod(instance.getClass(), methodName); 
		if ( method == null ) { 
			return null; 
		} 
		try { 
			return method.invoke(instance, args); 
		} catch (Exception exc) { 
			return null; 
		} 
	} 
	
	public static boolean isNID(String nid) {
	    boolean exit = false;
	    final Pattern pattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
	    final Matcher matcher = pattern.matcher(nid);
	    if (matcher.matches()) {
	        String c = matcher.group(2);
	        String chars = "TRWAGMYFPDXBNJZSQVHLCKE";
	        int index = Integer.parseInt(matcher.group(1));
	        index = index % 23;
	        String reference = chars.substring(index, index + 1);

	        exit = reference.equalsIgnoreCase(c);
	    } 
	    return exit;
	}
	
	
	
	
	
	public static void startAnimatedImage(ImageView iv){
		//show animation
		final AnimationDrawable loadingAnimation = (AnimationDrawable) iv.getDrawable();
		iv.post(new Runnable(){ public void run(){
			loadingAnimation.start(); 
		}});
	}
	
	public static void stopAnimatedImage(ImageView iv){
		//stop animation
		final AnimationDrawable loadingAnimation = (AnimationDrawable) iv.getDrawable();
		iv.post(new Runnable(){ public void run(){
			loadingAnimation.stop();
		}});
	}
	
	/**
	 * Check if location is available
	 * @param context
	 * @return
	 */
	public static boolean isLocationAvailable(Context context){
		//get location manager
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				
		if (locationManager!=null && 
			( 	locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
				locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) ){
			return true;
		}else{
			return false;
		}
					
	}
	
	public static Location getLocation(Context context){
		
		//variable to set the location
		Location location = null;
		
		//get location manager
		LocationManager _locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		//create criteria
		Criteria criteria = new Criteria();
		
		if (_locationManager != null){
			if (_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
		        criteria.setAltitudeRequired(false);
		        criteria.setBearingRequired(false);
		        criteria.setCostAllowed(true);
		        criteria.setPowerRequirement(Criteria.POWER_HIGH);
		        location = _locationManager.getLastKnownLocation(_locationManager.getBestProvider(criteria, false));
			}
			
			else if(_locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		        criteria.setAltitudeRequired(false);
		        criteria.setBearingRequired(false);
		        criteria.setCostAllowed(true);
		        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		        Log.e("INFO", "network" );
		        location = _locationManager.getLastKnownLocation(_locationManager.getBestProvider(criteria, false));
			
			}
		}
		
		//return the location
		return location;
	}
	
	/**
	 * Calculate the distance between two pair of coordenates
	 * @param latitude0 - double 
	 * @param longitude0 - double
	 * @param latitudeF - double
	 * @param longitudeF - double
	 * @return float - distance between points
	 */
	public static float getDistance(double latitude0, double longitude0, double latitudeF, double longitudeF){
		
		//point origin
		Location loc0 = new Location("");
		loc0.setLatitude(latitude0);
		loc0.setLongitude(longitude0);
		
		//point destiny
		Location locF = new Location("");
		locF.setLatitude(latitudeF);
		locF.setLongitude(longitudeF);
		
		//return distance calculated
		return loc0.distanceTo(locF);
	}
	
	//-------------- LOCAL BROADCASTS ----------- 
	
	public static void registerBroadcastRepaint(Context context, String filterName, BroadcastReceiver repaintBroadcastReceiver){
		LocalBroadcastManager.getInstance(context).registerReceiver(repaintBroadcastReceiver, new IntentFilter(filterName));
	}
	
	public static void unregisterBroadcastRepaint(Context context,BroadcastReceiver repaintBroadcastReceiver){
		LocalBroadcastManager.getInstance(context).unregisterReceiver(repaintBroadcastReceiver);
	}
	
	public static void sendBroadcast(Context context, String filterName){
		sendBroadcast(context, filterName, null);
	}
	
	public static void sendBroadcast(Context context, String filterName, Bundle bundle){
		Intent intent = new Intent(filterName);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	//----------------- DATE ----------------
	
	/**
	 * Return a calendar with format specified
	 * @param str String to parse
	 * @param format String with format to use
	 * @return Calendar calendar parsed or getInstance in case of error
	 */
	public static Calendar getCalendar(String str, String format){
		
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			date = sdf.parse(str);
		} catch (ParseException e) {
		}
		
		//assign date to calendar
		Calendar calendar = Calendar.getInstance();
		if(date!=null){
			calendar.setTime(date);
		}
		
		//return calendar
		return calendar;
	}
	
	public static byte[] getBytes(InputStream is){
		   ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		   int nRead;
		   byte[] data = new byte[16384];

		   try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
			     buffer.write(data, 0, nRead);
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		   try {
			buffer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return buffer.toByteArray();
	   }

	/**
	 * Opens dialer in order to execute a phone call
	 * @param activity current activity in context
	 * @param tel phone number to be called
	 */
	public static void systemPhoneCall(Activity activity, String tel) {
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(TELEPHONE_PREFIX_FOR_ANDROID+tel)); 
		activity.startActivity(callIntent);
	}

	/**
	 * Opens the google play store activity with the page of the current app. In case of activity not found
	 * opens the url for app update in a browser 
	 * @param activity current activity in context
	 * @param url for opening app in browser
	 */
	public static void openStore(Activity activity, String url) {
		final String appPackageName = activity.getPackageName(); 
		try {
		    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(STORE_PREFIX + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}
	}
	private static void makeLinkClickable(SpannableStringBuilder stringBuilder,final URLSpan span,ClickableSpan callback){
		int start=stringBuilder.getSpanStart(span);
		int end=stringBuilder.getSpanEnd(span);
		int flags=stringBuilder.getSpanFlags(span);
		stringBuilder.setSpan(callback,start,end,flags);
		stringBuilder.removeSpan(span);
	}
	public static void setTextViewHTML(TextView textView,String text,ClickableSpan callback){
		CharSequence sequence= Html.fromHtml(text);
		SpannableStringBuilder stringBuilder=new SpannableStringBuilder(sequence);
		//stringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.setSpan(callback, 0,text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(stringBuilder);
	}
}