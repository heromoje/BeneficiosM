package com.bcp.bo.discounts.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	public final static Locale LOCALE=Locale.ENGLISH;
	JSONObject jsonObject;
	public JsonUtil(JSONObject jsonObject){
		this.jsonObject=jsonObject;
	}
	public JSONObject getJson(String key) throws JSONException{
		return jsonObject.getJSONObject(key);
	}
	public boolean getBool(String key) throws JSONException{
		return jsonObject.getBoolean(key);
	}
	public int getInt(String key) throws JSONException{
		return jsonObject.getInt(key);
	}
	public String getString(String key) throws JSONException{
		return jsonObject.getString(key);
	}
	public BigDecimal getDecimal(String key) throws JSONException, ParseException{
        DecimalFormat decimalFormat=(DecimalFormat)NumberFormat.getInstance(LOCALE);
        decimalFormat.setParseBigDecimal(true);
        return (BigDecimal)decimalFormat.parseObject(jsonObject.getString(key));
	}
	public double getDouble(String key) throws JSONException{
		return new Double(jsonObject.getString(key));
	}
	public Date getDate(String key) throws JSONException{
		
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	    	String date=jsonObject.getString(key);
			return format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return new Date();
	}
}
