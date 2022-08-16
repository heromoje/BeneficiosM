package com.bcp.bo.discounts.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class Constants {
	public enum BuildLevel {
	    DEVELOPMENT,
	    PREPRODUCTION,
	    PRODUCTION
	}

	//connection environments
	private static final int ENV_MOCKUP = 0;
	private static final int ENV_DEVELOPMENT = 1;
	private static final int ENV_PREPRODUCTION = 2;
	private static final int ENV_PRODUCTION = 3;
	
	/** CURRENT APP VERSION **/
	public static final String APP_VERSION = "1.5.1";
	
	/** CONFIGURATION **/
	public static BuildLevel buildLevel = BuildLevel.PREPRODUCTION;
	//public static BuildLevel buildLevel = BuildLevel.DEVELOPMENT;
	
	/** AUTO FILL FIELDS **/
	public static final boolean AUTO_FILL = (buildLevel==BuildLevel.PRODUCTION? false : false);
	
	/** SERVICES EMULATION **/
	public static boolean SERVICES_FAKE = (buildLevel==BuildLevel.PRODUCTION? false : false);
	
	/** LOGS **/
	public static boolean SHOW_LOGS = buildLevel!=BuildLevel.PRODUCTION;
	
	/** SIGNATURE FINGER PRINT (comment this line and uncomment next two for productionPurposes) */
//	public static final String SFP = "DD:4A:FD:13:DF:62:F0:FE:E8:EC:65:71:D2:41:4D:D9:5F:C2:4D:33";
	public static final String SFP_PROD = null;
	public static final String SFP = SFP_PROD;
	
	
	/** URL CONNECTIONS **/
	
	//FROM AMEU
//  public static final String URL_BASE = "http://7.107.101.47/bancamovil-dto/rest"; // wifi
//	public static final String URL_BASE = "http://10.108.136.215/bancamovil-dto/rest"; // lan

	//FAKES
	
	//Fase 1.1
	public static String URL_BASE_DEV_FAKE = "http://ovh1.ameu8.com/bcp/services/fase11/fakeDiscounts"; //OVH
	
	//Fase 1.0
//	public static String URL_BASE_DEV_FAKE = "http://ovh1.ameu8.com/bcp/services/fakeDiscounts"; //OVH
	
//	public static String URL_BASE_DEV_FAKE = "http://192.168.2.1/bcp/fakeDiscounts"; //Shared
//	public static String URL_BASE_DEV_FAKE = "http://10.10.1.110/bcp/fakeDiscounts"; //Hotel
	
	//DEVELOPMENT
	public static String URL_BASE_DEV = "http://172.31.164.169/Beneficios.Extranet/Service.svc"; // development OK
//	public static final String URL_BASE_DEV = "http://192.168.2.1/bancamovil-dto-cer/rest"; // development
//	public static final String URL_BASE_DEV = "http://7.107.101.45/bancamovil-dto-cer/rest"; // development
//	public static final String URL_BASE_DEV = "http://172.29.172.48:8080/bancamovil-dto-desa/rest"; // development laptop
//	public static final String URL_BASE_DEV = "https://bmovildesa.lima.bcp.com.pe:445/bancamovil-dto/rest"; // development secure
//	public static String URL_BASE_DEV = "https://172.29.117.36:9449/bancamovil-dto/rest"; // development https new certificates test
//	public static String URL_BASE_DEV = "https://pwasaix7d01:9449/bancamovil/rest"; // development https new certificates test
	
	//CERTIFICATION
//	public static final String URL_BASE_CERTI = "http://172.29.101.15:9097/bancamovil-dto/rest"; // certification
//	public static String URL_BASE_CERTI = "http://192.168.201.185:9097/bancamovil-dto/rest"; // certification OK
//	public static final String URL_BASE_CERTI = "http://172.29.172.48:8080/bancamovil-dto-cert/rest"; // certification laptop fiddler
	public static final String URL_BASE_CERTI = "https://bmovilcert.lima.bcp.com.pe:445/bancamovil-dto/rest"; // certification secure
//	public static final String URL_BASE_CERTI = "http://pwasaix7c01:9097/bancamovil-dto/rest"; //certification
	
	//PRODUCTION
	public static final String URL_BASE_PROD = "https://bancamovil.viabcp.com/bancamovil-dto/rest"; // production
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String DATE_NONE = "1969-12-31T19:00:00.000-0500";
	
	
	//--------------------- STORAGE ------------------

	/**
	 * Secured prefs file name.
	 */
	public static final String SEC_PREFS_FILE = "secPrefStore"; // @CONFIG SECURED PREFS FILE NAME
	/**
	 * Secured prefs file key.
	 */
	public static final String SEC_PREFS_KEY = "l4Cen1ZientAviaJaba$ol@"; // @CONFIG SECURED PREFS KEY
	
	/**
	 * Prefrences file name.
	 */
	public static final String PREFS_FILE = "prefStore"; // @CONFIG PREFS FILE NAME
	
	/**
	 * Session images updated flag.
	 */
	public static final String PREFS_SESSION_IMAGES_UPDATED = "session_images_updated";
	
	/**
	 * How to know where the images was stored
	 */
	public final static String IMAGES_STORED = "IMAGES_STORED";	
	public final static String IMAGES_STORED_INTERNAL = "IMAGES_STORED_INTERNAL";
	public final static String IMAGES_STORED_EXTERNAL = "IMAGES_STORED_EXTERNAL";
	
	/**
	 * Path for Ken Burns widget.
	 */
	public static final String KEN_BURNS_PATH = "ken_burns_widget";

	/**
	 * Category icons path.
	 */
	public static final String CATEGORY_ICONS_PATH = "category_icons";
	
	/**
	 * Discount images path.
	 */
	public static final String DISCOUNT_IMAGES_PATH = "discount_images";
	
	/**
	 * � Build category icon name.
	 */
	public static final String CATEGORY_ICON_PREFIX = "cat_";
	public static final String CATEGORY_ICON_EXTENSION = ".png";
	
	/** Version to run animations **/
	public static final int SDK_START_ANIMATIONS = Build.VERSION_CODES.JELLY_BEAN;
	
	/**
	 * Preferences cic key.
	 */
	public static final String DNI_STRING = "cic";
	
	//instance for the url
	private static String urlInstance = null;
	
	/**
	 * Get URL_BASE for services
	 * @param context
	 * @return
	 */
	public static String getUrlConnection(Context context){
		
		if(urlInstance!=null){
			return urlInstance;
		}else{
			urlInstance = URL_BASE_PROD;
		}
		
		initUrlsAndEnvironments(context);
		
		return urlInstance;
	}
	
	/**
	 * Get URL_BASE for services
	 * @param context
	 * @return
	 */
	public static void setUrlConnection(String url){
		urlInstance = url;
	}
	
	public static void initUrlsAndEnvironments(Context context){
		
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			int environment = ai.metaData.getInt("environment");
			//environment=ENV_DEVELOPMENT;
			if(environment==ENV_MOCKUP){
				urlInstance = URL_BASE_DEV_FAKE;
				buildLevel = BuildLevel.DEVELOPMENT;
				SERVICES_FAKE = true;
				SHOW_LOGS = true;
				
			}else if(environment==ENV_DEVELOPMENT){
				urlInstance = URL_BASE_DEV;
				buildLevel = BuildLevel.DEVELOPMENT;
				SHOW_LOGS = true;
				
			}else if(environment==ENV_PREPRODUCTION){
				urlInstance = URL_BASE_CERTI;
				buildLevel = BuildLevel.PREPRODUCTION;
				SHOW_LOGS = true;
				
			}else if(environment==ENV_PRODUCTION){
				urlInstance = URL_BASE_PROD;
				buildLevel = BuildLevel.PRODUCTION;
				SHOW_LOGS = false;
				
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//------- DATA ----

	public static final String CP0 = "MIACAQMwgAYJKoZIhvcNAQcBoIAkgASCGKEwgDCABgkqhkiG9w0BBwGggCSABIIFcTCCBW0wggVp";
	public static final String CP1 = "BgsqhkiG9w0BDAoBAqCCBPYwggTyMCQGCiqGSIb3DQEMAQMwFgQQ9jl+mOhmkWr33Wn5RbYZlgIC";
	public static final String CP2 = "B9AEggTItCuRRf1mzX5nk6sqmhp7MQHDCsyDkEuSFWXCZz02cef2wDsDXXwTLAgvWTk8PBqnJZ1z";
	public static final String CP3 = "T/AdtWaDUDVW8QFaNA0KrqPEj2PIRnISKgRf7EQ1hheYWANoerWaYhxUpG7ixyJQ8JKwwtZmXKwx";
	public static final String CP4 = "aKMRN41Dvn3EepR1tEEC364nx+5tqWb4BOkFzXlunZGXoZUVp+tCzFf6MQhM/PtllF4M1bGfDygU";
	public static final String CP5 = "K4pqHHNSBsN39cCO8CQtskP1zAW9nsn0/4UMHekIt3cpkHLLb5ojWPm8CIc/FSkKbhYMqo1H53BQ";
	public static final String CP6 = "5w6gWdQtwj0g+Uk7ZMqi13/5W3rgH/svqTcC6akpJg/Ll1zBZaudyX1BP4sR8TpW1GYt6ELvgfG5";
	public static final String CP7 = "pIsyBFZ8voI8024v4CrJ8xFIaOrxSjMp/RsfnVyL8nOnc/HDfIdmgNFXX1EIk5yNRu2USwQySI7j";
	public static final String CP8 = "bfr1W/HbeFvqADkYMqbyBuyVFW0fjkjibfgEfzRMDMjNAkSltpDiW2oduzjMlH7RKb4coa360MFq";
	public static final String CP9 = "3b3vm1EW71bDnyyBwasnglB9ACB8swRZmkd3xLKwPByL1lZPahMNpEtQNkiY1WpS3kVFATo7ptUB";
	public static final String CP10 = "G4qaQE+GnG4mdprwXDxdGf8c00aWa3VGfgl+dsPAV9usT9TNGHnw0l4OVLEiULrA1EHBPUfOdGUd";
	public static final String CP11 = "hhj5bbU4xN37ebR+Z95QDznGrHsK1HhDPsfM5U0vxEA3nkLmB5Yp/LTvygFWklfb2otR9H0Mcsmr";
	public static final String CP12 = "PqaC1TxvkX20CdSndrvhTJmgyHv+sWehPLlVkByiCmmx58OStgxHIH7iUxE47GDWKkrqGUSi4TvO";
	public static final String CP13 = "vaERU9Lq0NTZjIU+wdQS2tO5qDvKrHpDYWjdW03+svT3d1i08Pzq6LhDCCulNcTkeNh/SlIjpkXI";
	public static final String CP14 = "aTLcqZArLsj33zXRdrWnyr3o2NzBVZ0B+YL+DEOLSjf01d/aukN1faSNc6GDEQWQxv6Hq5k1p0vY";
	public static final String CP15 = "Y3czsQGT0Kl4/RPBB2iioYhtEjYX9baX95K6uIlqJ36UMDR9gRARhzsRAFeEe3IXpt3WV/sdHowp";
	public static final String CP16 = "h8hVjMtU16i0+EgjCe9dZkNnEsBspXCtPXqn4Rvemt/Zcf2Lw9qQH3AcTUq80WHiUzLRtxgSNsA+";
	public static final String CP17 = "rN6sJsd72Dvj+d40XpE1bcYcjvZ9SPpNJ/o2tJUvCx7zywErn8vEnhbBsr8KlXK5kvGYcoppKUJr";
	public static final String CP18 = "BrGQSOZbKCrUXhoxCrC5AeXrSBE51fTqdefVMdheUcFwv8a+OPwEoFuCFDw5MxHyGiHk2VFSte6w";
	public static final String CP19 = "8UfvQPYMKDM3SZCFHbl64mQ8TS3ukQOL4EG4FLcRgjab9UX59eXGXea8pOu4cpdyVJOCyoUj5/pK";
	public static final String CP20 = "F9nBgdkBm/my2G4mAZeFBTvqMKj4VTziDGcPTriCZ4ZZlLTBBrHBFkZ2NY+ax4QMT2siIhN1FhII";
	public static final String CP21 = "aktF1iQl4aO3UJlYBYiHujXiI+L8xeyVLjHi778qFtl8S9MMkCeKfmbGYvw1s/6Cx/srYuwiiaM4";
	public static final String CP22 = "dVChNgOXS5faYsBD7i6/GZFpsMroAluKMfTZN6+U4UyUrNRKUvMjK0Ue++sWzxrRdeKkx+MpZu/H";
	public static final String CP23 = "nlw2XLt7npXzcTkUXFd71CFVYOFcyk2XYS0IRSCPvGGGMWAwOQYJKoZIhvcNAQkUMSweKgBiAGEA";
	public static final String CP24 = "bgBjAGEAbQBvAHYAaQBsAC4AdgBpAGEAYgBjAHAALgBjAG8AbTAjBgkqhkiG9w0BCRUxFgQU+pJS";
	public static final String CP25 = "Xcru2gxaTMKAXwdVVrULKz4AAAAAAAAwgAYJKoZIhvcNAQcGoIAwgAIBADCABgkqhkiG9w0BBwEw";
	public static final String CP26 = "JAYKKoZIhvcNAQwBBjAWBBDl7/5jkrSV6MEq5PrgPIa3AgIH0KCABIISsMAeVoxtimdT+z4Ny/4V";
	public static final String CP27 = "qM4OgYci5pEl884HkwyPThLlFDLIWZW0a3M3SjpNGw4vWhz0/Wkq2xTMQxCVc0yS4UqMTT2JRg+e";
	public static final String CP28 = "7n347NbJn64PvpOSC1Ue6hYkYfCBJguQoBosV532s2na+rMlO/xK3b4nYKgEhXTR0+pybUXan+3z";
	public static final String CP29 = "L6iYDzs5c6qUjHA0JFPcioKXwIzRkgsvBtTdd8opLb4ft5f8GcZsCTNG1Xrx7fgiKMQxX/vW/FL1";
	public static final String CP30 = "WFnwfd395f+JU51a3Wgj6cW3JGfL/nT8yUP5kEjlsxUB4ROIJilbGhnaPy82NYLf1OQJh33ZVpbd";
	public static final String CP31 = "AL8cLwcdjFWCpO3joFUiBJxgIre7bL3zu09lV8dvKjL/z918PQ8bsFefsbYG3sVxzArezWyKObXM";
	public static final String CP32 = "cujr2wqJczUN3jSHzYp9AmaS0JmLXmbHTBiOCrekwqRyj/xv09fEn8rapQDwMli3hHmKkV3hxPwD";
	public static final String CP33 = "46giotFTP8+P+5OkE1DUK9WsjBa2CLCFhXiNvCVRmzDwDz4W142fNQNEy6rqn2AYDsgexrBK3MUU";
	public static final String CP34 = "D4LvnmGL1+PHlzlZNnjKfDr0qwmVD78toNyJMM1h21KvTTqOCJEOxY08hYe+AKyY7szsi/izTKmx";
	public static final String CP35 = "C7hU2dklvo/q4pV2TJd0NnA56Y7cPgfWlrx4C4dQHLxUZ70I3eEEk3DSE8BsDRX0jgQnAShp4N8P";
	public static final String CP36 = "ehEvorKc7e316nK5WKqPq+sA+yqjzMSxuRnL1JepZJblR470KceStVd1CgTtJ0Di6soZ/hH/WloM";
	public static final String CP37 = "nB4ek4f5rzyHkNegfpEeUXOT1ekZ/JJvbuwlVeQ0CaBKI3PEoLAgBtzFrIFQwoj49OTxpH/OjOk7";
	public static final String CP38 = "q/KtJ+/k9BiGQM9i6yo1B3Ka19sMoEZA/RoOxauZxloUi/kRdy153aPHWldwgeEkwiRceznVpjHW";
	public static final String CP39 = "SHceSxXiEa1gDrUuRR+rbYzSwuTA8raHUuTZizQgoiFbyndBGiFzZjF2TJ06c/Vk3b4CNKkzHA6k";
	public static final String CP40 = "SgGSUThNNNyWqp225rUmvk85RN1etm0C34/YXyzgyGhLUCE53L7knP96Jjv00kRFwl8AFykn7OZp";
	public static final String CP41 = "eArA4uhYXp4KuqisM7GTu5GPrG/26pTLL0sUUweTphakKjnUgJdT6RNYivUUmF8uaK7nobVdBwla";
	public static final String CP42 = "xC81OhDzRWY63nP6KLvwnJD9M4Op93Gn5+tJ3X9AgysNwUUYMknzmkk4Zzcz5bkEVxVuUPTAMUnX";
	public static final String CP43 = "TCHb/1c/VcrI18hMIQIb/06sPhtpzbeRLnRjN95sUXBoyZcUhS0m/t5kTOwWh3Idsq9nC8GVSJVJ";
	public static final String CP44 = "wVCRzaWAceZ4wSkHeWhB8hfxt1AdTPaug+U+4gWQw7iAS5hPWrOkRCthFJdrqQETmT3wiYGbaQeH";
	public static final String CP45 = "1jlPxiy8Sk1X/OWCsSpE9wQgsiTt+RAdzWLXsLlVR9lP/g0tskuu3MH2oCKGCqMlbAqnryf8gcQy";
	public static final String CP46 = "/LDR1NNCxoWojncwfklsN89pdgFSNZ/kYdqt124lvmFUeZCzoE3llQ2mxjKj9ePW9/yVHMIoVTwo";
	public static final String CP47 = "IkUECWRvCfUkpf0DxykdbytI2L1hIwCuS20HhjsqSesLdSoIp0gTMhCsGDhW674OnT5+ZB/Zb2hc";
	public static final String CP48 = "gUcR9Bd2ku+JaFDJfPxSR6pIbdmd92zcGgpMWGjhAByhqN+ql/9E8GcJIrRAeW6eKvCLg4irh0vp";
	public static final String CP49 = "f08TyF+bQKjCnTnsftq4+pBYSDIDAqQakuQ8/DvcaJ2AWhUEeqeCNAfSIJQQSXz86zUey05gxEHL";
	public static final String CP50 = "7YuO0vX6CQ5rtJXbAcdRe8ahE9ElvFVquoAKjiAitq5bzhRhyPTwp/KfxM07WxKIii+cO1M1f680";
	public static final String CP51 = "X/eoMG9ztmBJ6EsWnqRat4HN8dLs1ZJVffMQd+9nCse77d/MkB7SI3buAeYGA6zKHSHs1HpiLcDs";
	public static final String CP52 = "39fVXYHP7GbN8AfGqjp4o3Vt1FjI62SghFZO/mwxXSayaHFSq/o4s5+Z144l2tKo8pRZHM1wFsCk";
	public static final String CP53 = "jhetZI6X99UjMLFh/x1AZu8E6nYejtfVzmzFxIcSJPd10WotSqFbQaBvrBElrpbsElpvClmJVyhr";
	public static final String CP54 = "kwk9Nc2FZOPupidPRBq4Du7USCCJxa04E/y87GBL/LNNa4lbXly/tZ/gmYd4ij4qDwPYEbnZdFxf";
	public static final String CP55 = "2irg662O4IWKC81wz9kvWXPGXN2p/d0nRQuv/xQDUYjUus9+ZDDl4dYFSO9Udlt9qDjTvPoJCCzC";
	public static final String CP56 = "2rZkCKvH1tm1fcUgFCM8Ecat9qgrgD3WNl7QzjXc5YBDZ+gf4a7DlwtLErNLEFyFluIGihCXfTDt";
	public static final String CP57 = "WLvJoaUQbsrekdHVu9EEyEBeDYiSuX/113cVgOtLHFyJzDqiySdVqvPEAAj9Sm4jXgRD8OaJZsmG";
	public static final String CP58 = "Nv4Z0EDZE+EkBRJEdqcpkuCLqX/5FOcVNdYkBlpJay2b/2aUjY/Eg2TFYkHvHzy4DXluuRRWJRcO";
	public static final String CP59 = "XwA3EikkF4bkLGElf4zXTjOHQA180KWiHHbV1P+vitsOw9+xZDGutDg4e0XJKLykx0lHc40YSJu2";
	public static final String CP60 = "lfvzxddJ1/E5vfu3VlOPpfsHo1YN9No0XQaRrBn7eXVGEEUYhP04TFYlhrUUb3BWkp/p/NaBc+WU";
	public static final String CP61 = "THnWTXx+sdAqRLDsalQIITLLFdrnL/tT/ze6B+W3lbqLH2vfmLwjj0uI6iCsyGWXqxKiCNDVTSXl";
	public static final String CP62 = "p5a2YGEnum5aX0Mpxi3dTVonqGcL2tgQGuQLOmIaUU3m2pMTL/ORfqQyCnaoqQCz9M2C/voguQni";
	public static final String CP63 = "rDeAuOgCqm/FbyfVMd2uL+/JeYlkk0yeilFaGy1XyfPwEoLrM0enljlawmsGU9oObB8oWWZz9mj1";
	public static final String CP64 = "QXVu8QXEASQAFjKO4UIOUOF6eGrN4/vQ+n+hVcOXZlFF1DndNgJgCub0KKRPgMOLs2O1eVP5/S+W";
	public static final String CP65 = "HSu7WwKS3g7CxdZjW0Uvged9Bd3VSPt8epzrnkZgPHkrdAtk5CjiHkT944aoraKysJox7bRSHmgK";
	public static final String CP66 = "y2aNxneF6L+d+XGSfxR0oWOSoEQUvy00rmEw+y3DhrMfVa+RIPqiKiczROcZ+JymUoBMp64biX+7";
	public static final String CP67 = "fm1699GVivM4JePki/GiDJgWxnx5n7V67QEp2GOatpP/eM13KgCIaXu7EnYDvqwn9OQHkVlSrtFj";
	public static final String CP68 = "62YAm5H9z1/GuIjCr91tMwfGflE7tR143fJsHNb0le9cF42vmlDS+bBfYAmhryWcearB2Fc1xiGC";
	public static final String CP69 = "uuWIRqyTLUmYp65PkCO8ULT9/OmlSiLqJnQVnopd/YEiSRjv7SmkPMNHpm152gJRK7FKS0P4bzzf";
	public static final String CP70 = "RnEx1EYMsX4nTyZR1UNYZSOdt2zBcLjfTeWKfEkmnOiO/LZnLOW+lHNrcJGELJcgxwoFZ6x0qx0s";
	public static final String CP71 = "Xc8lP2vxs4Z9pstzhEuNCAcY6w+f6mkuqPhWLi7vaIjSfzjdrLUvaanS/wHUyqyXIFxa1aaq9JY6";
	public static final String CP72 = "4Zev8rIxRHQDB9Y7sNaHTZn9cYvfd5uK28BfhSMtiA+E/LwSb91ghIfwBOB6pH1nD1IbvPrXblOc";
	public static final String CP73 = "MC752gXIyR3Ieer7Q8Tiy9Kn77/pedgH2zeZBfK7xu56OJjugqPD89dfDUnBtHJnKG1zfGtrcLtz";
	public static final String CP74 = "dnc2rqZ8A+Ik9niv8iNi+pwEKIoGshWpJer7cejhiF5t9vxDv3DkMXXch0ObTB9h+Y1XkaAgNSbM";
	public static final String CP75 = "IyV8iw41npN/YY/dz59mgyXeXsdAf4NhMRsQxfWw7ppot8Dd7tlmb5Ns+WZX8XYusN9bDlw079TL";
	public static final String CP76 = "hnx9iH/6N+nVjQupwWJhPSW1Xm5kId1STRN75QIv0O+56lt73RiOt6eRoTGD96mcfEq+oZl1mkVV";
	public static final String CP77 = "Bmw5b8Ui9hK4glKG25fDJbq9f2Gd1RIvP3bo+YAb/xCIkH2JfT7nncFRV2axivmvZ2QrrGHQ7hM/";
	public static final String CP78 = "aH3hZ6/GP0l7uZj5hfdL+y0/GpxB8uMs7SfTE0X9PDZuNV6kvErQzeQW4JMbwRh4it0v9k2sFr8t";
	public static final String CP79 = "bqjIJNoMi3Vk87Ee/ZHOpcsjCLakX8zdp3LqatW652aw6Qglf/ELkEyRNkZ9DbMmndNApJ9V2mmx";
	public static final String CP80 = "cAbHTD31KXWGFEopPDPKltMWGIGeImoXkz6ROCBlVmdb4s1eyNI+f2AcyZHTG73Y4lS6n5EYU2Qg";
	public static final String CP81 = "vsKf9yaClPDOtcqpmL9ryB6LYgBOxdFU2HSTCXFZijACgVC4Q1TOwtK/UgcUHJpwDoQzqcXIh7WK";
	public static final String CP82 = "FZp7TujqXmwI0gCl3f7oF3bTqXe5dZ4E807wwO61zcpi3y1OTqI2mydDrb/bHAkT/o6eCq6oWs24";
	public static final String CP83 = "5phzgvcRWGb/yiNGmqZwH+qJlpNW2LxQXDTLd9mjpw7DJvTM5b81NkWKtcohVC+hIRk1yVu69mVC";
	public static final String CP84 = "J2BIWUQUznVv9ggSc6+cEhEKZ3xKQ7ctcq8tKnaLjwYNoZXSwYxfiBD/Lkn8xhsuFglCJYPIXAiF";
	public static final String CP85 = "NIcSwNL4VJyqBwJ4X0XHfuulwPM1/4ODvf9/r6cJHGSfYQdt4ocfW9JOQ3Wt4sLnhE2rEsITC1Xl";
	public static final String CP86 = "1q6WJXB/efBcHJ3P45u1zMgdow6Opa0Cir2agqNxWhV2/cwAtOtjqk/6fG8CsC57JEdg8Yd2OjEn";
	public static final String CP87 = "HgKFb01WUPnMWE1TvIq545cuOYbCr0Ao7QWzeHRD9/ArI5Aku8hr02sDbID2rd2b9dByCO6tSdXx";
	public static final String CP88 = "Tiw8ombWeReofJEUcG9jHiLOk3j4ZP6Tc/P4PDfrD29xS4rCUuTRklNkz/WsfGKddycllzpmJxS3";
	public static final String CP89 = "84VHUkN7MYShPxm2Se6Tsnj4n0lZWf6bHV9X/WjxFTPsI2JKnzlVN620ckjMXvINX8Geh8QK4A1z";
	public static final String CP90 = "oNa+gtnpg4bC7xv1bA2cF8xsvNB/tX7D8XTrq9hedsKUwIFOJY1IS1fGwwbUXEAvFsA1CMZ8zNTm";
	public static final String CP91 = "ucJlXbKYU6Rq9FFMVTvtGCuUD1JrIlcszcFt/hO3PbaGSI5AO5cFpn0wMHl6HbH/9rLpvV7wOzVl";
	public static final String CP92 = "bD6+7fSPS++8uqMeNwMe0+gAIsOpnEcJK8wJKiOXmCq8XuKyp2IpO80rW9Ra+0Z0bk9Ycg8XNaMd";
	public static final String CP93 = "KB9tX05KFA5sS3Hgchy0EhT7Lt2qk54P0tgQ/04fg2LufzEq16o3zc87+WGX4sYFAw1sR8vTjjBk";
	public static final String CP94 = "ObHzOrKHFInOd8x8lNxaPWrEY9w4SaM/gMN1/lOMfK+KwHBca1naToRw6/16HedEgbKQ6NnmnOaz";
	public static final String CP95 = "GZEVQsmRwqWu2aYh2GzLkdCB7nDnYPcbgrpIGswHJfvcsjGYMS56rUXnJirjdTBXJ1ilPGlj8DDH";
	public static final String CP96 = "Yr7yCkNT3FLCWmg98PK3HcIqRDIFjpVCqVuQ01Ovo15BJFqpZg4iNbq3TBvOSLO7fcdLgDaigLum";
	public static final String CP97 = "6wGPw7E0Wrj/AwL5Jl3PJqJunEYwWypO0NqmcIrGzPEwpVnhcUpqcNdlYygMbBNCUuhQXYkEVfsF";
	public static final String CP98 = "iQ9IaqnUnrRicp01NcsQ13+tzbGL3M4a1VwzRCLSaIZ8o748tJQlhtmQfS3DWpX90YvtnlgDSkXr";
	public static final String CP99 = "zke77lCemKl8tTyhtvXiL9OVXuwqm1mGPrbhDlsLge5Pzkp6z9qA/T1Sk/Wg8lOzq4AoJA3yOiml";
	public static final String CP100 = "S44h3qvfhraqXxHRWgcQjLSquP0Wlvv2Id4XRycYZH6/w4Tx93nnKAincDjkQD9eqTc/gpSY+OSs";
	public static final String CP101 = "vAACjQghPL8kGv6CvETYyrDSGhC5hJu3V6o+TB7dQczid1FxZJuPw1g6A9mgylkRsj98pPe6ZVeP";
	public static final String CP102 = "eXMNzmRVcTaUfURdo4G88KWDv5uC38F3WpQWoporuAIDRMaaN0nfM64dnP+z66z3DK/yonQi92Ag";
	public static final String CP103 = "Ga0ZJ3jy3ucVflvRKNgLhLAwSB9jxROHx+wOYs4Y7WinHvu+yST3dHdogyBe3RNfS3xNoJ0Y+Yd9";
	public static final String CP104 = "fbIzZGgNSw8WLPVzhmtUpPMSIXh579r1RVY1rwUurzSnrxBC3T2qIKYei2RLf/utH7VtHHIqbPLI";
	public static final String CP105 = "H5cyCZdcxyD1GGxFCcqn0kwbBAhTjPHGxDb9NNW2dezguclW7TnqEc15WAM3hqehNd0V8TxRyay6";
	public static final String CP106 = "KjwDUEhtUiqmm4zfRbJZ5AntZ8LtDOmUWuNJOpUMuIggpm5I/MzNM+ksCLk4T5BGiDYTLDdcJ4IH";
	public static final String CP107 = "t+JYUbjY64GQM4phY5rHuFMsLgJ4GVxOwNtevZAhsmMIiOy10MpIzZ5wz0BpVk+0kZkFGGSg9M/w";
	public static final String CP108 = "BqthjTEtqtDqHcdYfWZ1uMFd9Y9d56cEFUsjf2VQa477Dde8PttZNyZrf3aoerNZ5D/xRAwfoFMR";
	public static final String CP109 = "9ECKio3DXeZmw4n5Cl/eNq35t/JFs7Ta/5HWMlw7wOzIKnBhKThFvcBvZ62fqPWf8yevAcs1VC7B";
	public static final String CP110 = "zCmEqbUPln30h84fdg2P72sE3XubmM9JPgogQsQEbcenrpAb36lDBAikU9nx4Px8nQAAAAAAAAAA";
	public static final String CP111 = "AAAAAAAAAAAAADA5MCEwCQYFKw4DAhoFAAQUJwC751EHHwZqlyvj9hnuestF43wEEFnzogYebJ9r";
	public static final String CP112 = "LLnwASMgp3wCAgfQAAA=";
	
	public static final String[] CP_IS = {CP0, CP1, CP2, CP3, CP4, CP5, CP6, CP7, CP8, CP9, CP10, CP11, CP12, CP13, CP14, CP15, CP16, CP17, CP18, CP19, CP20, CP21, CP22, CP23, CP24, CP25, CP26, CP27, CP28, CP29, CP30, CP31, CP32, CP33, CP34, CP35, CP36, CP37, CP38, CP39, CP40, CP41, CP42, CP43, CP44, CP45, CP46, CP47, CP48, CP49, CP50, CP51, CP52, CP53, CP54, CP55, CP56, CP57, CP58, CP59, CP60, CP61, CP62, CP63, CP64, CP65, CP66, CP67, CP68, CP69, CP70, CP71, CP72, CP73, CP74, CP75, CP76, CP77, CP78, CP79, CP80, CP81, CP82, CP83, CP84, CP85, CP86, CP87, CP88, CP89, CP90, CP91, CP92, CP93, CP94, CP95, CP96, CP97, CP98, CP99, CP100, CP101, CP102, CP103, CP104, CP105, CP106, CP107, CP108, CP109, CP110, CP111, CP112};;

	public static final String CPRE0 = "MIIOQgIBAzCCDgkGCSqGSIb3DQEHAaCCDfoEgg32MIIN8jCCCEcGCSqGSIb3DQEHBqCCCDgwggg0";
	public static final String CPRE1 = "AgEAMIIILQYJKoZIhvcNAQcBMBwGCiqGSIb3DQEMAQYwDgQImCQIzBs1xqMCAggAgIIIAJxWRLEO";
	public static final String CPRE2 = "syxfcZqFhElPdWgkjjyae9+TPxQsMH3enNRyDZf4T5p9b5j6hSqdxWLgsswEEL398TEmKLKz3q86";
	public static final String CPRE3 = "xr9LnGtkYKkeyq61tKpBvZCMOiU3JyySP2db6+7Du0ERg+UzHc60r8pBJlaWRCVys/I3Q6dESuMX";
	public static final String CPRE4 = "ao3o+Hafeyv6YiCSqOcBorSgN7pI+Q2BEVWzN+tkH5snK1cBVeI+mzzbRJT/q8s/RiA+P9HgjrUe";
	public static final String CPRE5 = "izJvqd5Q3jVsYqgs/s9zEixOD0UVVR4vSfQOEP/Ps5RjZiqFuQhjBT7afcNFYDYauuIVMr802qsA";
	public static final String CPRE6 = "ovh3txkrkgx6NWalxZSQsNATGAGPLfoZG1LshIn77TS2HxsASfvnzK3LuqiVkxw8MIOtlkpx9o8L";
	public static final String CPRE7 = "z88jx7oRo1l2NT5FB0IDM9vkUWSn3n01GhqaAe3sKriI5IzZdhC6FIA4g/X92QMN0ndYliHN6U56";
	public static final String CPRE8 = "+/PeHdmsMnulo82weC/+Eyh4ANdCZu+AV1nsYs5V5H9/6OHZAPXVLs4+1W++ejin/oE1ntp5Rfid";
	public static final String CPRE9 = "sZ5SsKDvOoJSKgFX2EQm2tYlkU9MhrhN/E6B/SSF2axB2MdVJxC0e5uA0ay3LdJCt7KZV2q9J01C";
	public static final String CPRE10 = "SdxWAVE2oE4bRbxhE3n9ltwCEnZ2RDe/p4S1bGTLThE286xvBIa8bX78ARjA2aL5MyWrUBFDMwkw";
	public static final String CPRE11 = "Qa5Go0zEvxkFjBorZwcGuQ3Te5RmgHoYMgc6rDzhV6k44TRFxJuXpg6B7X24A/QOuZIVNdpUgiTA";
	public static final String CPRE12 = "lMfAPr8uUBdUeq+KPAIY2b5VaBoK6ex5F1sZ8oXYtCW65h44cLI9cg738PUXgfK20OYYevSBQmk3";
	public static final String CPRE13 = "PKzbGFsqbBsNLkJwnz1pkSW4d2AuOKRdQ0/lqxHi73HTNsGOisqRZLIunit2r48flpXDj5Oip7Sw";
	public static final String CPRE14 = "ueIUnAHhcVidRkxRygwBOe4+4dZYVUz40EZaaZ5uM60kLc1EEOYXgxIA7YIFWpSQIPflBtwgpM8c";
	public static final String CPRE15 = "xWSahmA+4mNSEztwmIUJtoJHVuX1v0vreNWuU9q2sp41fesHcVJTQqiCBOg9iPSx7FtxD03EQiwZ";
	public static final String CPRE16 = "TTvSh1OcNLCmqnDIpnZPdHB8TOkG6l4G2bMM+jPzp3o7gDmOAKepR0lESufRvonO+xuQXAnngCj2";
	public static final String CPRE17 = "g9nBIZKam2sl1N0kcivwBGm+gBnAe9DEipVTjUftMOa/NSKPXIG3LbdHzX/UxaFJc3lIOjyQxWAp";
	public static final String CPRE18 = "sKgnZvZBQ38QLRvpmCcjRvOAj4ncFjhfW0LJZq9qs2TEXXb+i1NKKfxEGo0fHlajaogLxlqV1TAO";
	public static final String CPRE19 = "0ZgJleu5Jyaa0jOeA1ysgFGEkBvZEdkKdO47BTKXxZFMCAfR1uwApTjlXvb10z3Xswj2qAXMJNEO";
	public static final String CPRE20 = "iSA/F6Ge70aRhKpZ6bq3ousKbz2M9oSyh/3ocx7L9usuCKP29+nXCAY5oijvNcwKBofzB6+XM0Jq";
	public static final String CPRE21 = "va49aSixIYBgGRkxxs5uPFbhaTAgGdUtxEUGmONTt167Iptu3Lkmnnyy06ah8IapDLoqI2qXHLCs";
	public static final String CPRE22 = "8bf4SnFEq8Vil1Sd38MuXlx27cDjk/mZRGVRV4mZYdi/sYp5do/wCYq4ZHoeASt442Fvqv40mlfu";
	public static final String CPRE23 = "GOTrZjcrUqfEoqvwUsPrLAHgp5tNcFK9tzkCyfatp86MAjWubf/H74S+ZFPsYaCp++j4xcaA8z8n";
	public static final String CPRE24 = "sYRfYY3H37ZKkY2UAEsO3sHXafPSB/vQZJhIXd3v1FhgwGp74fO4TmkMdtdMi9b+kziDimiSa7Zz";
	public static final String CPRE25 = "1Uyaz57ucjWvT4+nxrx9XfnakucHz/ic0cGjD5DMdCo9rhRQazKYrKPbPREmOQ92GmlxvQIJ6f1p";
	public static final String CPRE26 = "qndvOV2dHE32aB+o56ROdHQaZHB8kviCkxZ6yE1kQACJT5avOlShuusPksnFJCx76pazbawPf/mP";
	public static final String CPRE27 = "3FXYc3IKwtrNLVuAJPPfdchzW1j3Ua+6VwAOG8vQI/KEbT6dCAE9sqWedIaehMK0UXd6icNCNKmF";
	public static final String CPRE28 = "EinMH3vRSDXCwrCNvYXL239gPj0m7p/cpFFqP55c9r6ghkC8ph7vagh+O17eGIeCtpRzv7uW+YIx";
	public static final String CPRE29 = "WHPpelBOVPJU2s77o6gS0YIpfNMKpYzea774Y6oDIZt9x7qTKx5VHUpxDwpxmIBNehl55hPOvXZa";
	public static final String CPRE30 = "khaUjsAjZDS/mUxVQpZ09aITgPBvfqzywqZ/7fDGL+MJfCBH+zVfJFFun9svw9TcgV/76fzdOnlX";
	public static final String CPRE31 = "fK/j8+P8amUiq27dHOZISENHvdehNcAgq2qZpYr6sA/p+ihkGvXCGelhMz7kHP1HHFYZEgU+xjB/";
	public static final String CPRE32 = "DEEOWz6vM7s9GqDUVo+a8eMh6gTva8CnzDlpdSMkE30jD1129FKfe9E4hqAxBovYHEjc8BcXVWbE";
	public static final String CPRE33 = "ARfFERYlLucCF6sPHZLNCKs1FjpWKFch0jvEb/C3tlKLO8pcU03SQkXiTO4etVrPJAbkwuu/SnA8";
	public static final String CPRE34 = "P65Sbl+94f3nmaEU/8jgXjbYBkMMsn5BuxwuyOCl5P2Trmcy9Ib2MG5NE+nf+q9KKwtQEZSN3bNY";
	public static final String CPRE35 = "Odq6koadFovGX5s9NPY3VQBIEIPf+6s5QYfq9T63qMQbNCgiysHUSxMWlqfkQYXgC9tFbRGNhovc";
	public static final String CPRE36 = "GT+LjoaI3cXszrMhlZdX4wbV5CiFDOytOpwgALFgRt5n3jKjahNUpKHN59k/jxmcEkJaL12B2Ufm";
	public static final String CPRE37 = "goKu4gMXI3U+RC/0o2EI1PzsheMIXj7qhDiJ9S1OGD2DRkgT4RhIrTVzgs5oZxm8MIIFowYJKoZI";
	public static final String CPRE38 = "hvcNAQcBoIIFlASCBZAwggWMMIIFiAYLKoZIhvcNAQwKAQKgggTuMIIE6jAcBgoqhkiG9w0BDAED";
	public static final String CPRE39 = "MA4ECBVeXZ69TMynAgIIAASCBMjhagDxtAxj2FU6Dj29fc1WyN73N1lC17p5huUfT15e3l8oqBtL";
	public static final String CPRE40 = "fmgzkkY20MurNycFy9UfUm/z8ekxXxqLFwC45tPiZ+Kodj7pim1UihTPKIEap0hFmDMzX17rqfjW";
	public static final String CPRE41 = "jVodup9ApSsbLbbJ+MRMJAZdNJOQHJBt5NeaAZR9FuiJVq+aXcTz5zJGLLHugKOspExGZR2+6xi/";
	public static final String CPRE42 = "2yLFP5zGoO6TEwnsPJ5wTKgeOI8tLvSuioKxVcOi85ENFhJ7iurm46nQfCYRx7A1ykUiji//dJOz";
	public static final String CPRE43 = "ETgOQ579IZahz6lkIV5gl1JCAXI4sgaHktOKiSqUvFrREp8XvSo3V6fLsGZPw414WAXCNnLu0/qJ";
	public static final String CPRE44 = "hECk1Y5orA/tBYu3iNA5WMxIgjzN1Wx+PN9VUAgbE4KiZU5f/u7bBvQJSG0VT4uu0/J2HwDGsfis";
	public static final String CPRE45 = "4M7D9RL8wyjC4A1RAjJlg4HyaTssvXtjR+YAENesHK/2Kffs2BTgZlnq6k1HR26JWDMpvOJWgnJl";
	public static final String CPRE46 = "tv+/g+CRpmTrT8Hygc2kU6Z7qlqCYOJUTICQAMmkZzz0jfUEfU6ewERtyCC+o3Wc3/5GmJPah0mv";
	public static final String CPRE47 = "B4Tu9aUd5OrobEexS4P8Idt4W3NhYEXye/5pKPreHVpNL4PSlOedZZ0N6kWK4UkDLlaJGB2P/SgN";
	public static final String CPRE48 = "EtrVGma90y8WwcawO0nbH6whdYOn0MEJumSSHVqhcNMir5P23KuD4bj2eywCDR7DxoZfd1hIr4oY";
	public static final String CPRE49 = "aUH+JreJ4AcbXdDH0bZllbXj6IojWmgAgu8eIWQSNopuQYjowP+PH0aD9X7lK09E962fzGv05buG";
	public static final String CPRE50 = "tBBchjvirQ9/aAQgdQsxehc9wcPLVNYgczrVTMbmMpPMOFQAm9+s7d4m+RuWewl1AsrL2yNl+mI3";
	public static final String CPRE51 = "FjoZ/48+Z7OWOCxmdizLbjUgAnXZ5+b2R2cSZNzwvugVauNyAYl5fmCAThyAHaWl9iGEyxLe458u";
	public static final String CPRE52 = "+Hf5ZTigVopuZc2WoJsejMpWI4nndfWUGzBhx1AeHnB5c/VfL8wAOazeyh7R+TxvPi06vqKYAeLH";
	public static final String CPRE53 = "q1fUCQW5KEiMiX/SJw9KZMAjXPuBpgYiOBimpew0moZbv+39A5VlSrRUHiDNBuj5RYUB0fzxfN7G";
	public static final String CPRE54 = "JF7E6xy6ygNqzCb/yOKhEfnQb9SdE7a6gGyACsyI6aeoGs4HP4+19ZhqfMahyWW/9vdivngF1T5J";
	public static final String CPRE55 = "oQWGYu37dNxap6Sqc/8SDdGm7shE/bx9bpxrKIuULJgep1MQQEfMgkFnoRpKFIioAAKMsbhvO8K1";
	public static final String CPRE56 = "qmORx2HroZ1Em24raZzpjefKc8PDk6KuWDlOdE1l7wPohHsMORc33ag+PbKItt8a2LxsSRHfaKEc";
	public static final String CPRE57 = "SN/BDaIUzasoVdOJXp72DrmzpYWUojKdHRyYuOb+VqwX4B5wWOqV9v52kuOFGsbUCTHaWM+6gJMR";
	public static final String CPRE58 = "xCH02D3EpvkFIgwTKVRjhcxfZ6bo7Gyy5fNbcU/U+5PCz0edDlkv0F7XYSLKApqvYKWSVu4slCnB";
	public static final String CPRE59 = "lwXrQS+9gxzQRjCxRvmmG4r0uAuocajEmr3rZELtiQ3AY5cO+PgYsxBJx5b1K9I5TuhrK/isOAll";
	public static final String CPRE60 = "pSpLqr8SJ90UWS3Fqi+7rIjw3B8mWZFmRmCAkRxVm2BT6cY8EkFyDxapAsBrN0MxgYYwXwYJKoZI";
	public static final String CPRE61 = "hvcNAQkUMVIeUABsAGUALQBhADUAYQA2AGQAMgAwADkALQAyAGUAMwAxAC0ANAAxADYAMQAtAGEA";
	public static final String CPRE62 = "NgAyAGMALQBhADYAMABjADQAZgAxADEAYgBkADYAMwAAMCMGCSqGSIb3DQEJFTEWBBTMWBxEB1Wj";
	public static final String CPRE63 = "6KeFYLWrEFIs06jlQTAwMCEwCQYFKw4DAhoFAAQUBV6DMRx1OIObZ3jNh8kyzKC1gfgECM5gl5UV";
	public static final String CPRE64 = "oC0MAgEB";

	public static final String[] CPRE_IS = {CPRE0, CPRE1, CPRE2, CPRE3, CPRE4, CPRE5, CPRE6, CPRE7, CPRE8, CPRE9, CPRE10, CPRE11, CPRE12, CPRE13, CPRE14, CPRE15, CPRE16, CPRE17, CPRE18, CPRE19, CPRE20, CPRE21, CPRE22, CPRE23, CPRE24, CPRE25, CPRE26, CPRE27, CPRE28, CPRE29, CPRE30, CPRE31, CPRE32, CPRE33, CPRE34, CPRE35, CPRE36, CPRE37, CPRE38, CPRE39, CPRE40, CPRE41, CPRE42, CPRE43, CPRE44, CPRE45, CPRE46, CPRE47, CPRE48, CPRE49, CPRE50, CPRE51, CPRE52, CPRE53, CPRE54, CPRE55, CPRE56, CPRE57, CPRE58, CPRE59, CPRE60, CPRE61, CPRE62, CPRE63, CPRE64};

	public static final String CDEV0 = "MIIOfwIBAzCCDjsGCSqGSIb3DQEHAaCCDiwEgg4oMIIOJDCCBgUGCSqGSIb3DQEHAaCCBfYEggXy";
	public static final String CDEV1 = "MIIF7jCCBeoGCyqGSIb3DQEMCgECoIIE9jCCBPIwHAYKKoZIhvcNAQwBAzAOBAgitVsY0VS70wIC";
	public static final String CDEV2 = "B9AEggTQUwES+L3mkNaQkPZcUEMuDMO3CYI7JAFwkkytyXpQ4CvIrzbxAYhEFc+fVYJjejwjhNZp";
	public static final String CDEV3 = "DFnp0zJW5m0ahc9NAoY5sPs1jBSFrxjodXp++TcZELAgKszha9a66igdB2aeHLn6WmRIhzZBmiTq";
	public static final String CDEV4 = "uOxyb9DkJSMvyBcLaoo+CyqZt9ntHlYlzx0+77BQ82qpI59auSm/4SwYEyseS9KNkGw6Xculg6Nq";
	public static final String CDEV5 = "iYMHV+L3ALUuvWcTzHi+CIp5uU3G1/qbiM/papEJnY+x0zvdSeJaTAIciHIaOLgYnK1vGNd8U+FF";
	public static final String CDEV6 = "NVW/IMkqM9ZI8CouY7e+su516ethGOFnO8q7dge73pOS8pFSbFkjRVgPjBnZtJIZXHqydVLLj1Kz";
	public static final String CDEV7 = "fWLnFvLf+ZjRB/y02EoDbOEGjpSKdr62dvy+GFGGUUvpywJ+Uez6+mFAlOJimgxHIFO6dj/yecbU";
	public static final String CDEV8 = "6uW0EzpSRBld6jv1GVip0C/nrdWgE1PzGOYYsIMe16WaH+OI47dg9mAXP4YNzoWBQxb4Czb+irL4";
	public static final String CDEV9 = "PH8JmWZkedw7WKo9riautvUEq0yRMXfE4Hbhmw1DBVmjCDc0SBEgENgAD+9oNg35jjC6P7i9KAiZ";
	public static final String CDEV10 = "lJyJ0Zd6Ht3m/ThknZEi2OvmrCgq1XWYMpYi5848OpbuNjsY/o82uZy8VbR9VV6QHEgdfgDHbPhw";
	public static final String CDEV11 = "mQJR6fZbKysjpwtSfWoBtLNoEXkbrppGkGv9nO31N0QX9ig0Un35dsfzx2Xwxhg5Hj9FXAj+HTjX";
	public static final String CDEV12 = "zMWk/Z+qX9sW2KsIEcrbLtCfmEho5XTc4jTCXAFqxoj1gPKuPu5xE0ZlVIiD6c0+bm+ummXbZ4qC";
	public static final String CDEV13 = "7JdUX/A6KsieIM9y72hFy8nuy/8VdvDPMw4kq/WJdo6Vjt03/Y7eRVOsLbA4y916ZtY+pN7Bq3bI";
	public static final String CDEV14 = "dFyfOqve++rnzCNn6eKCRA9OJmIRR6OcZxLL//RTlKMWzqk21PBWqAdjXFroJMllPZwO8J1P/nVy";
	public static final String CDEV15 = "sb2S0O6BADCz6YPMD4CdERRA96tpzqoLT2676YxGU2REn/NiCOwKcv4EORT9llHRFJM8LzyD93d8";
	public static final String CDEV16 = "U4bxqMPNpznOrRyTXftk9WPvPq/IWFl+iMMDVB08K38tLR/olLnodjBC1oOmJQm5KUyApLRpSECo";
	public static final String CDEV17 = "ScOYjj/tNUl667FP3P1naVzgif9o8qjbg+R/SJHlQavAKMbFPzTe7nNItUZAovQsu4+d66a/QS8I";
	public static final String CDEV18 = "v9xYnbkxelwrm3j/AyfpgLh0546TIWknbNTeZ2U3wzer8DweA9TgUtf87szVShSrF6afl5VFpZSp";
	public static final String CDEV19 = "QKhKWd5ODO9KHsKjqtXT7HyAUVdMKX0rI0QaAZJm4Y1zsFComvejAeX3f93Qg/HeBycgXs3vQQgc";
	public static final String CDEV20 = "d80K3pyHZPYiejLlCfhhcPBUDOqzPAYna/DRWkScbDNitieBkmFGVYPrLPGFiSPryVSU9tkj4DyA";
	public static final String CDEV21 = "0Z4he7JBSTJZwU+LmjUk+FXKU47hawcKU0RBV+XhjR4Vnmh7JEI73g88DKAuJ3nFECi9IIZA5Yup";
	public static final String CDEV22 = "Pmj9FJOnOzubppxULKu7NfxOhSbx4xvI9x7cSmAUWuEyGIPhGAetIvn19kDpdlc/JKi/bEqWvB15";
	public static final String CDEV23 = "bh6RA/9CdevCoWhRzHwZO96HyDki7tIttSq9Otj95+msKpULMShazEgxgeAwDQYJKwYBBAGCNxEC";
	public static final String CDEV24 = "MQAwEwYJKoZIhvcNAQkVMQYEBAEAAAAwTwYJKoZIhvcNAQkUMUIeQAAxAEYAMwA5ADMANQA3ADAA";
	public static final String CDEV25 = "QgBCADQAMQA0ADMARgAwAEIARAA0ADYAMQAxAEQAQQA1ADkAQgA3ADcAOQA5ADQwaQYJKwYBBAGC";
	public static final String CDEV26 = "NxEBMVweWgBNAGkAYwByAG8AcwBvAGYAdAAgAFIAUwBBACAAUwBDAGgAYQBuAG4AZQBsACAAQwBy";
	public static final String CDEV27 = "AHkAcAB0AG8AZwByAGEAcABoAGkAYwAgAFAAcgBvAHYAaQBkAGUAcjCCCBcGCSqGSIb3DQEHBqCC";
	public static final String CDEV28 = "CAgwgggEAgEAMIIH/QYJKoZIhvcNAQcBMBwGCiqGSIb3DQEMAQYwDgQI1ci2DgKL3ggCAgfQgIIH";
	public static final String CDEV29 = "0DFNsOG66l9mqvP1A3lCnLqqN8E7xupY0I9z0jkjmr6SDEn4RsKOfOHuD+np6/4yDmvCJ7RGibNp";
	public static final String CDEV30 = "Eq2zfgy2lbOnrsqoLdgB3muitnoHZby7uNrKOurCcmb7QciNiXkqAPSX9Cl737ERV82TU9cQar9k";
	public static final String CDEV31 = "RD3LFOSfEL75LlWmSWRzb/0N65fMHbekSmdgwrCKLiN5VZdD10r0NSCZk51xHs67xmzfsG7Wyt+9";
	public static final String CDEV32 = "Xe8BgnUQAKPjVyU29ImtK+PznQaGQNwC0ht9BoKeR3Mgh/vNxCW0F8Xpu2IiujXfBSbyC1OCPSIa";
	public static final String CDEV33 = "02mSrfcW4S1rNmrxubnd20r5MTtKtAfmkgQCBLx86dSUl2+nq0GuIe0sp+CQ+OxIXlylAohlnZNp";
	public static final String CDEV34 = "7JC664DHhf7QeBSdgLY9EfJwd1bRR31S69l+uhONA2VUJPQ+htOwsjg+RE2TqpykIRvViXq5Aw/8";
	public static final String CDEV35 = "vy2yto8FIbneoS+aqVyhg3JWHoGrTGnAHUkVa25xUEEWq23RXIc2yXXLe0DmxWH5znWg7Q64JWwy";
	public static final String CDEV36 = "siy+snO9yhxdkt9HybXm+otkoTZLlFczjGHYqEXw2/0o0t0u7ihHRv/WKBiaPoy7Z68pPzzvvAsa";
	public static final String CDEV37 = "SvPfpOiz+I61gNUEKX3gJQavJlS9yTvUi/kJARRY6s119z5cZ6fnwO83wOqiWm9RuwAVymTgcxm8";
	public static final String CDEV38 = "9l22YK0x2L1FBjvJAofOMq1VXIVavXZwYNCbC3ese3tW7L/O/XRUbaMu2wlGX3gKtv04liU8RGmG";
	public static final String CDEV39 = "Ii7rYEKI61mO9ffdn49EzRgh9R+KyVbwMK5xLNx5SaHpVLI1n4KoWU/SD4Vc7Ff7Wif9lMJMy83F";
	public static final String CDEV40 = "aMo/SQTTWL87wglFWkk0o5VGFpwLwse4sNRKNpAV8GYZwjm2m5Q7F0q4aEThj9fOz2Nclgw6GoJx";
	public static final String CDEV41 = "S8aiqdQpZwihDOHqtz3rw6PwUjvs4/AOFQd08WnI0x1zlqrkiJP/jEdEdMix1DXl2nLXC5mgs3DT";
	public static final String CDEV42 = "Oz18CBqG7BRsC0uybBlC78TdvDFvv7G5mRcXHIBJmESY4G+S+D8RmnUy94r2Pg/qDt9fsNZ1JKCG";
	public static final String CDEV43 = "2tDDxj6T9XXc76kF+DkjTPmy54fwNaspUOolUhcu4XUOWbuhpf9+9ncHyJ3Fl2Y1y5U4R7Tgiugc";
	public static final String CDEV44 = "P4wCPW6klZqKXQiJs1cJd5fOtp+yNYYUFEnr/b84q7dFXOLqOgvLRmlDcVvHiB17m/tjuuLSPZaV";
	public static final String CDEV45 = "yPDwz+ZiDpSZhKeNhirHbxIsrOqEDneOQZVVeNMpe/t7/zn84lsk7pQTn1VRsQdklDaqu/I0NH2P";
	public static final String CDEV46 = "YD8JGAyLCbpDxPC+XJZcqrFo2MbW4M9phv6E6Lva7VhhgtXKKwldjlmh0K8Fj74hBGVUp0zPZfa8";
	public static final String CDEV47 = "g4SbSIGKj++UTxC8X4a4VKKwLe37nCuXHuq+Y9X7ecgzgYv0NeDdwmqJayz2QnYZSDMRzfgIVcZW";
	public static final String CDEV48 = "5R54nRshHl3U1XY82nZezvQfi4jibTDtkGqmHNtG6oebl8G/lzZ2s4dw8Wcqf3le/XPHGGi3h7ZE";
	public static final String CDEV49 = "sIXncwsrnwbiUthokx7vO9AOy50wm+sKDnd4qIie5j/P2jpmWn+vlguK9ihhRp1uNoDGRaP+3sou";
	public static final String CDEV50 = "MzeR1gm4V0zpsErFDPdlv8NaREnZDRdtZ71nx5vO4La8OcUoDpV5ANYRXl2m9o2Ga2mstl9xbyRb";
	public static final String CDEV51 = "P0M6hUP431kftSIvr/WQnI0vTawVl7fls3VKRXJ6LZRXMUVL8duQrnffF1WjMgguuw9+P0i3UuU/";
	public static final String CDEV52 = "pFpL9dKCr9nLnKAJ9EEoqDMJra3d2/jRXVg0r7mmw95/bWg0h3gqr4y8Khs5aIRjI4/RnO2urAvp";
	public static final String CDEV53 = "glW5lzmjAKn1+OTIXUaahZlTi/w14yqR5Yo6pGSzu4EnIS5VoiacAFm9uJB4mlsuviB+nqkJpOb2";
	public static final String CDEV54 = "CmgOSIlvqj5p12Mq1wPp1CNaxWBjbt81e2MB6ndwgOape3ZqC2mBrcI9UGcfeJEYbJ5lrM+aIHC2";
	public static final String CDEV55 = "yO3AodhC4rOvch/TQgBdg5oM3Srouh7MF3ewe4XdTC1PexcBHdh+vXpqBthV2yvsQQZqXzXN3peW";
	public static final String CDEV56 = "ddR7fwhIJwnk/mXysjPOptDQxOMetRr4fLLSRgnikSvbjxgB7nBEDGlmNy6pbngV5rkBOxam3rny";
	public static final String CDEV57 = "DOfrLHdbbEPeu1CqszbD4U9Cf+1tRg/BA8H6OfhvI/xLxXSV7Ctj0fl90oyqFWOniAzL4Xz0OH0Z";
	public static final String CDEV58 = "Bzda+gHKI8Y1/FAm5R0wMDl5MxC+MWNi2G6VxN44rfnx3wTI5qnB44xuzFnK8FgQyUou0CqjU6/8";
	public static final String CDEV59 = "Rs5GkTJtgMC7C9bXHxqqwqlh+sM+u2oE08pu6eVde2VdgusqsBGKKVGBbo9s5jrOLgMCevoYhHYH";
	public static final String CDEV60 = "yibBsZEAA59CMn/JX1oHK+SFubf61EESWh6DdOVXJwAzCZ9u6A/uKs0OxSB9SGmx51XT6Wllo42C";
	public static final String CDEV61 = "rIJzIGEv5b8IZNdOZ8EqPDptbSNFtNpSDBUQHbXns6SsPIHPFHGBrTGg5IDlVW4MISe3nJJu4lX0";
	public static final String CDEV62 = "bJvybtqL6ZiQWEXjSUYM+IAv9lTmQfkxLIqRJUrs5Q6YxJreAkwIZ+1qHVTX13VYWBBeK3R2kxsx";
	public static final String CDEV63 = "xxT9beFoL8eC4RZt3SvOfmO7f4vr/4TTqKDcpv8JGn79ahFuJuYf96pFbvMeNcoeed74d5gP0pMH";
	public static final String CDEV64 = "dYypSbVuMDswHzAHBgUrDgMCGgQUfS6Db6/4K+ze0C3XyovtR958gCcEFMB7mi+W8FKvnhTppiot";
	public static final String CDEV65 = "kyyhSmVLAgIH0A==";
	
	public static final String[] CDEV_IS = {CDEV0, CDEV1, CDEV2, CDEV3, CDEV4, CDEV5, CDEV6, CDEV7, CDEV8, CDEV9, CDEV10, CDEV11, CDEV12, CDEV13, CDEV14, CDEV15, CDEV16, CDEV17, CDEV18, CDEV19, CDEV20, CDEV21, CDEV22, CDEV23, CDEV24, CDEV25, CDEV26, CDEV27, CDEV28, CDEV29, CDEV30, CDEV31, CDEV32, CDEV33, CDEV34, CDEV35, CDEV36, CDEV37, CDEV38, CDEV39, CDEV40, CDEV41, CDEV42, CDEV43, CDEV44, CDEV45, CDEV46, CDEV47, CDEV48, CDEV49, CDEV50, CDEV51, CDEV52, CDEV53, CDEV54, CDEV55, CDEV56, CDEV57, CDEV58, CDEV59, CDEV60, CDEV61, CDEV62, CDEV63, CDEV64, CDEV65};
	
	public static final String CNF0 = "PCEtLQ0KIG5hbWU6IFNUUklORw0KIHVybDogU1RSSU5HDQogaW5QYXJzZXJUeXBlOiAiUEFSU0VS";
	public static final String CNF1 = "X0pTT04iLCAiKlBBUlNFUl9YTUwiLCAiUEFSU0VSX05PTkUiDQogb3V0UGFyc2VyVHlwZTogIlBB";
	public static final String CNF2 = "UlNFUl9KU09OIiwgIipQQVJTRVJfWE1MIiwgIlBBUlNFUl9OT05FIiwgIlBBUlNFUl9NVUxUSVBB";
	public static final String CNF3 = "UlQiDQogcHJpb3JpdHk6IElOVEVHRVIgKCowIG1vc3QgcHJpb3JpdHkpDQogcmV0dXJuSW5mb0Ns";
	public static final String CNF4 = "YXNzOiBTVFJJTkcgKGNsYXNzKQ0KIHJlc3BvbmRJbk1haW5UaHJlYWQ6ICoiWUVTIiwgIk5PIg0K";
	public static final String CNF5 = "IGNhY2hlU2F2ZTogIkNBQ0hFX1NBVkUiLCAqIkNBQ0hFX05PX1NBVkUiDQogY2FjaGVMb2FkOiAi";
	public static final String CNF6 = "Q0FDSEVfTE9BRF9DSEVDSyIsICoiQ0FDSEVfTE9BRF9OT19DSEVDSyIsICJDQUNIRV9MT0FEX09O";
	public static final String CNF7 = "TFkiDQogY2FjaGVMb2FkVGltZUFnbzogSU5URUdFUiAoaW4gc2Vjb25kcywgMCAgbm8gY2hlY2sg";
	public static final String CNF8 = "dGltZSkNCiBtZXRob2RSZXN0OiAiKk1FVEhPRF9SRVNUX0dFVCIsICJNRVRIT0RfUkVTVF9QT1NU";
	public static final String CNF9 = "IiwgIk1FVEhPRF9SRVNUX1BVVCIsICJNRVRIT0RfUkVTVF9ERUxFVEUiLA0KIDcuMTA3LjEwMS41";
	public static final String CNF10 = "MyAtPiBFVEhFUk5FVA0KIDEwLjEwOC4xMzYuMjE1IC0+IFdJRkkNCiAxNzIuMjkuMTE3LjM2Ojk1";
	public static final String CNF11 = "MjAgLT4gQkNQDQogMTcyLjI5LjEwMS4xNTo5MDk3IC0+IEVudG9ybm8gY2VydGlmaWNhY2nDs24g";
	public static final String CNF12 = "QkNQDQogLz4NCiAtLT4NCjxzZXJ2aWNlcz4NCiAgICA8IS0tDQogICAgIDxzZXJ2aWNlDQogICAg";
	public static final String CNF13 = "IG5hbWU9IiINCiAgICAgdXJsPSIiDQogICAgIGluUGFyc2VyVHlwZT0iIg0KICAgICBvdXRQYXJz";
	public static final String CNF14 = "ZXJUeXBlPSIiDQogICAgIHByaW9yaXR5PSIiDQogICAgIHJldHVybkluZm9DbGFzcz0iIg0KICAg";
	public static final String CNF15 = "ICByZXNwb25kSW5NYWluVGhyZWFkPSIiDQogICAgIGNhY2hlU2F2ZT0iIg0KICAgICBjYWNoZUxv";
	public static final String CNF16 = "YWQ9IiINCiAgICAgY2FjaGVMb2FkVGltZUFnbz0iIg0KICAgICBtZXRob2RSZXN0PSIiDQogICAg";
	public static final String CNF17 = "IC8+DQogICAgIC0tPg0KICAgIA0KICAgIA0KICAgIDwhLS0gQkNQIERpc2NvdW50cyBTZXJ2aWNl";
	public static final String CNF18 = "cyAtLT4NCiAgICANCiAgICA8IS0tIFNlc3Npb25SZXN0U2VydmljZSAtLT4NCiAgICA8c2Vydmlj";
	public static final String CNF19 = "ZQ0KICAgICAgICBuYW1lPSJkZXZpY2VBY2Nlc3MiDQogICAgICAgIGluUGFyc2VyVHlwZT0iUEFS";
	public static final String CNF20 = "U0VSX0pTT04iDQogICAgICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9SRVNUX0dFVCINCiAgICAgICAg";
	public static final String CNF21 = "cHJpb3JpdHk9IjAiDQogICAgICAgIAl0aW1lb3V0PSIxMCINCiAgICAgICAgcmV0dXJuSW5mb0Nv";
	public static final String CNF22 = "bnZlcnRlcj0iQWNjZXNzUmVzcG9uc2UiDQogICAgICAgIHVybD0iaHR0cDovL292aDEuYW1ldTgu";
	public static final String CNF23 = "Y29tL2JjcC9zZXJ2aWNlcy9mYXNlMTEvZmFrZURpc2NvdW50cy9BY2Nlc3NSZXNwb25zZS5waHAi";
	public static final String CNF24 = "IC8+DQogICAgDQogICAgPHNlcnZpY2UNCiAgICBuYW1lPSJsb2dpblNlc3Npb24iDQogICAgdXJs";
	public static final String CNF25 = "PSJodHRwOi8vb3ZoMS5hbWV1OC5jb20vYmNwL3NlcnZpY2VzL2Zhc2UxMS9mYWtlRGlzY291bnRz";
	public static final String CNF26 = "L1Nlc3Npb25SZXNwb25zZS5waHAiDQogICAgaW5QYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAg";
	public static final String CNF27 = "ICBvdXRQYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAgICBwcmlvcml0eT0iMCINCiAgICAgICAg";
	public static final String CNF28 = "dGltZW91dD0iMTAiDQogICAgc2VuZENvbnZlcnRlcj0iU2Vzc2lvblJlc3QiDQogICAgcmV0dXJu";
	public static final String CNF29 = "SW5mb0NvbnZlcnRlcj0iU2Vzc2lvblJlc3BvbnNlIg0KICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9S";
	public static final String CNF30 = "RVNUX1BPU1QiDQogICAgLz4NCiAgICANCiAgICA8c2VydmljZQ0KICAgIG5hbWU9ImxvZ291dFNl";
	public static final String CNF31 = "c3Npb24iDQogICAgdXJsPSJodHRwOi8vb3ZoMS5hbWV1OC5jb20vYmNwL3NlcnZpY2VzL2Zhc2Ux";
	public static final String CNF32 = "MS9mYWtlRGlzY291bnRzL1Jlc3RSZXNwb25zZS5waHAiDQogICAgaW5QYXJzZXJUeXBlPSJQQVJT";
	public static final String CNF33 = "RVJfSlNPTiINCiAgICBwcmlvcml0eT0iMCINCiAgICAgICAgdGltZW91dD0iMTAiDQogICAgcmV0";
	public static final String CNF34 = "dXJuSW5mb0NvbnZlcnRlcj0iUmVzdFJlc3BvbnNlIg0KICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9S";
	public static final String CNF35 = "RVNUX0RFTEVURSINCiAgICAvPg0KICAgIA0KICAgIDxzZXJ2aWNlDQogICAgICAgIG5hbWU9ImRl";
	public static final String CNF36 = "dmljZVBlcm1pc3Npb25zIg0KICAgICAgICBpblBhcnNlclR5cGU9IlBBUlNFUl9KU09OIg0KICAg";
	public static final String CNF37 = "ICAgICBvdXRQYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAgICAgICAgbWV0aG9kUmVzdD0iTUVU";
	public static final String CNF38 = "SE9EX1JFU1RfUE9TVCINCiAgICAgICAgcHJpb3JpdHk9IjAiDQogICAgICAgIHNlbmRDb252ZXJ0";
	public static final String CNF39 = "ZXI9IlNlc3Npb25SZXN0Ig0KICAgICAgICByZXR1cm5JbmZvQ29udmVydGVyPSJSZXN0UmVzcG9u";
	public static final String CNF40 = "c2UiDQogICAgICAgIHVybD0ie0B1cmx9L1Jlc3RSZXNwb25zZS5waHAiIC8+DQogICAgDQogICAg";
	public static final String CNF41 = "PHNlcnZpY2UNCiAgICBuYW1lPSJpbWFnZXNTZXNzaW9uIg0KICAgIHVybD0iaHR0cDovL292aDEu";
	public static final String CNF42 = "YW1ldTguY29tL2JjcC9zZXJ2aWNlcy9mYXNlMTEvZmFrZURpc2NvdW50cy9SZXN0UmVzcG9uc2Uu";
	public static final String CNF43 = "cGhwIg0KICAgIGluUGFyc2VyVHlwZT0iUEFSU0VSX01VTFRJUEFSVCINCiAgICBvdXRQYXJzZXJU";
	public static final String CNF44 = "eXBlPSJQQVJTRVJfSlNPTiINCiAgICBwcmlvcml0eT0iLTMiDQogICAgICAgIHRpbWVvdXQ9IjMw";
	public static final String CNF45 = "Ig0KICAgIHNlbmRDb252ZXJ0ZXI9IlBhY2thZ2VJbWFnZVJlc3QiDQogICAgcmV0dXJuSW5mb0Nv";
	public static final String CNF46 = "bnZlcnRlcj0iSW1hZ2VSZXNwb25zZSINCiAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9QT1NU";
	public static final String CNF47 = "Ig0KICAgIC8+DQogICAgDQogICAgDQogICAgPCEtLSBVc2VyUmVzdFNlcnZpY2UgLS0+DQogICAg";
	public static final String CNF48 = "PHNlcnZpY2UNCiAgICBuYW1lPSJsb2dpblVzZXIiDQogICAgdXJsPSJodHRwOi8vb3ZoMS5hbWV1";
	public static final String CNF49 = "OC5jb20vYmNwL3NlcnZpY2VzL2Zhc2UxMS9mYWtlRGlzY291bnRzL1VzZXJSZXNwb25zZS5waHAi";
	public static final String CNF50 = "DQogICAgaW5QYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAgICBwcmlvcml0eT0iMCINCiAgICAg";
	public static final String CNF51 = "ICAgdGltZW91dD0iMzAiDQogICAgcmV0dXJuSW5mb0NvbnZlcnRlcj0iVXNlclJlc3BvbnNlIg0K";
	public static final String CNF52 = "ICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9SRVNUX0dFVCINCiAgICAvPg0KICAgIA0KICAgIDxzZXJ2";
	public static final String CNF53 = "aWNlDQogICAgbmFtZT0ibG9nb3V0VXNlciINCiAgICB1cmw9Imh0dHA6Ly9vdmgxLmFtZXU4LmNv";
	public static final String CNF54 = "bS9iY3Avc2VydmljZXMvZmFzZTExL2Zha2VEaXNjb3VudHMvUmVzdFJlc3BvbnNlLnBocCINCiAg";
	public static final String CNF55 = "ICBpblBhcnNlclR5cGU9IlBBUlNFUl9KU09OIg0KICAgIHByaW9yaXR5PSIwIg0KICAgICAgICB0";
	public static final String CNF56 = "aW1lb3V0PSIxMCINCiAgICByZXR1cm5JbmZvQ29udmVydGVyPSJSZXN0UmVzcG9uc2UiDQogICAg";
	public static final String CNF57 = "bWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfREVMRVRFIg0KICAgIC8+DQogICAgDQogICAgDQogICAg";
	public static final String CNF58 = "PCEtLSBTZWN0aW9uUmVzdFNlcnZpY2UgLS0+DQogICAgPHNlcnZpY2UNCiAgICBuYW1lPSJzZWN0";
	public static final String CNF59 = "aW9ucyINCiAgICB1cmw9Imh0dHA6Ly9vdmgxLmFtZXU4LmNvbS9iY3Avc2VydmljZXMvZmFzZTEx";
	public static final String CNF60 = "L2Zha2VEaXNjb3VudHMvU2VjdGlvblJlc3BvbnNlLnBocCINCiAgICBpblBhcnNlclR5cGU9IlBB";
	public static final String CNF61 = "UlNFUl9KU09OIg0KICAgIHByaW9yaXR5PSIwIg0KICAgICAgICB0aW1lb3V0PSIxMCINCiAgICBy";
	public static final String CNF62 = "ZXR1cm5JbmZvQ29udmVydGVyPSJTZWN0aW9uUmVzcG9uc2UiDQogICAgbWV0aG9kUmVzdD0iTUVU";
	public static final String CNF63 = "SE9EX1JFU1RfR0VUIg0KICAgIC8+DQogICAgDQogICAgPCEtLSBEaXNjb3VudFJlc3RTZXJ2aWNl";
	public static final String CNF64 = "IC0tPg0KICAgIDxzZXJ2aWNlDQogICAgbmFtZT0iZGlzY291bnRzQ2F0ZWdvcnkiDQogICAgdXJs";
	public static final String CNF65 = "PSJodHRwOi8vb3ZoMS5hbWV1OC5jb20vYmNwL3NlcnZpY2VzL2Zhc2UxMS9mYWtlRGlzY291bnRz";
	public static final String CNF66 = "L0Rpc2NvdW50UmVzcG9uc2U0LnBocCINCiAgICBpblBhcnNlclR5cGU9IlBBUlNFUl9KU09OIg0K";
	public static final String CNF67 = "ICAgIHByaW9yaXR5PSIwIg0KICAgICAgICB0aW1lb3V0PSIzMCINCiAgICByZXR1cm5JbmZvQ29u";
	public static final String CNF68 = "dmVydGVyPSJEaXNjb3VudFJlc3BvbnNlIg0KICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9SRVNUX0dF";
	public static final String CNF69 = "VCINCiAgICAvPg0KICAgIA0KICAgIDxzZXJ2aWNlDQogICAgbmFtZT0iZGlzY291bnRzTGlzdElt";
	public static final String CNF70 = "YWdlcyINCiAgICB1cmw9Imh0dHA6Ly9vdmgxLmFtZXU4LmNvbS9iY3Avc2VydmljZXMvZmFzZTEx";
	public static final String CNF71 = "L2Zha2VEaXNjb3VudHMvSW1hZ2VSZXNwb25zZS5odG1sIg0KICAgIGluUGFyc2VyVHlwZT0iUEFS";
	public static final String CNF72 = "U0VSX01VTFRJUEFSVCINCiAgICBvdXRQYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAgICBwcmlv";
	public static final String CNF73 = "cml0eT0iLTIiDQogICAgICAgIHRpbWVvdXQ9IjIwIg0KICAgIHNlbmRDb252ZXJ0ZXI9IkRlZmlu";
	public static final String CNF74 = "aXRpb25JbWFnZVJlc3QiDQogICAgcmV0dXJuSW5mb0NvbnZlcnRlcj0iSW1hZ2VSZXNwb25zZSIN";
	public static final String CNF75 = "CiAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9QT1NUIg0KICAgIC8+DQogICAgDQogICAgPHNl";
	public static final String CNF76 = "cnZpY2UNCiAgICBuYW1lPSJkaXNjb3VudHNEZXRhaWxJbWFnZXMiDQogICAgdXJsPSJodHRwOi8v";
	public static final String CNF77 = "b3ZoMS5hbWV1OC5jb20vYmNwL3NlcnZpY2VzL2Zhc2UxMS9mYWtlRGlzY291bnRzL0ltYWdlUmVz";
	public static final String CNF78 = "cG9uc2UuaHRtbCINCiAgICBpblBhcnNlclR5cGU9IlBBUlNFUl9NVUxUSVBBUlQiDQogICAgb3V0";
	public static final String CNF79 = "UGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iDQogICAgcHJpb3JpdHk9Ii0xIg0KICAgICAgICB0aW1l";
	public static final String CNF80 = "b3V0PSIzMCINCiAgICBzZW5kQ29udmVydGVyPSJEZWZpbml0aW9uSW1hZ2VSZXN0Ig0KICAgIHJl";
	public static final String CNF81 = "dHVybkluZm9Db252ZXJ0ZXI9IkltYWdlUmVzcG9uc2UiDQogICAgbWV0aG9kUmVzdD0iTUVUSE9E";
	public static final String CNF82 = "X1JFU1RfUE9TVCINCiAgICAvPg0KICAgICAgICANCiAgICA8c2VydmljZQ0KICAgIG5hbWU9ImRp";
	public static final String CNF83 = "c2NvdW50c0xvY2F0aW9uIg0KICAgIHVybD0iaHR0cDovL292aDEuYW1ldTguY29tL2JjcC9zZXJ2";
	public static final String CNF84 = "aWNlcy9mYXNlMTEvZmFrZURpc2NvdW50cy9SZXN0UmVzcG9uc2UucGhwIg0KICAgIGluUGFyc2Vy";
	public static final String CNF85 = "VHlwZT0iUEFSU0VSX0pTT04iDQogICAgcHJpb3JpdHk9IjAiDQogICAgICAgIHRpbWVvdXQ9IjMw";
	public static final String CNF86 = "Ig0KICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IlJlc3RSZXNwb25zZSINCiAgICBtZXRob2RSZXN0";
	public static final String CNF87 = "PSJNRVRIT0RfUkVTVF9HRVQiDQogICAgLz4NCiAgICANCiAgICA8c2VydmljZQ0KICAgICAgICBu";
	public static final String CNF88 = "YW1lPSJyZXNvbHZlQWRkcmVzcyINCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiIN";
	public static final String CNF89 = "CiAgICAgICAgbWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfR0VUIg0KICAgICAgICBwcmlvcml0eT0i";
	public static final String CNF90 = "MCINCiAgICAgICAgdGltZW91dD0iMzAiDQogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IkdH";
	public static final String CNF91 = "ZW9jb2RlUmVzcG9uc2UiDQogICAgICAgIHVybD0iaHR0cDovL21hcHMuZ29vZ2xlYXBpcy5jb20v";
	public static final String CNF92 = "bWFwcy9hcGkvZ2VvY29kZS9qc29uIiAvPg0KICAgIA0KICAgIDxzZXJ2aWNlDQogICAgICAgIG5h";
	public static final String CNF93 = "bWU9InJlc29sdmVSb3V0ZSINCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiINCiAg";
	public static final String CNF94 = "ICAgICAgbWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfR0VUIg0KICAgICAgICBwcmlvcml0eT0iMCIN";
	public static final String CNF95 = "CiAgICAgICAgdGltZW91dD0iMzAiDQogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IkdSb3V0";
	public static final String CNF96 = "ZVJlc3BvbnNlIg0KICAgICAgICB1cmw9Imh0dHA6Ly9tYXBzLmdvb2dsZWFwaXMuY29tL21hcHMv";
	public static final String CNF97 = "YXBpL2RpcmVjdGlvbnMvanNvbiIgLz4NCiAgICANCiAgICANCiAgICANCjwvc2VydmljZXM+";

	public static final String[] CNF_IS = {CNF0, CNF1, CNF2, CNF3, CNF4, CNF5, CNF6, CNF7, CNF8, CNF9, CNF10, CNF11, CNF12, CNF13, CNF14, CNF15, CNF16, CNF17, CNF18, CNF19, CNF20, CNF21, CNF22, CNF23, CNF24, CNF25, CNF26, CNF27, CNF28, CNF29, CNF30, CNF31, CNF32, CNF33, CNF34, CNF35, CNF36, CNF37, CNF38, CNF39, CNF40, CNF41, CNF42, CNF43, CNF44, CNF45, CNF46, CNF47, CNF48, CNF49, CNF50, CNF51, CNF52, CNF53, CNF54, CNF55, CNF56, CNF57, CNF58, CNF59, CNF60, CNF61, CNF62, CNF63, CNF64, CNF65, CNF66, CNF67, CNF68, CNF69, CNF70, CNF71, CNF72, CNF73, CNF74, CNF75, CNF76, CNF77, CNF78, CNF79, CNF80, CNF81, CNF82, CNF83, CNF84, CNF85, CNF86, CNF87, CNF88, CNF89, CNF90, CNF91, CNF92, CNF93, CNF94, CNF95, CNF96, CNF97};
	
	public static final String CV0 = "DQoNCjxjb252ZXJ0ZXJzPg0KDQogICAgPCEtLSBURVNUUyBXSVRIIENPTk5FQ1RJT05TIC0tPg0K";
	public static final String CV1 = "ICAgIA0KICAgIDwhLS0gUkVTUE9OU0VTIC0tPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0i";
	public static final String CV2 = "UmVzdFJlc3BvbnNlIiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLnJlc3QucmVzcG9uc2UuUmVz";
	public static final String CV3 = "dFJlc3BvbnNlIiA+DQogICAgICAgIDx2YXIgbmFtZT0iZXJyb3JDb2RlIiBlcT0iUmVzdFJlc3Bv";
	public static final String CV4 = "bnNlLmVycm9yQ29kZSIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJzdGF0ZSIgZXE9IlJlc3RSZXNw";
	public static final String CV5 = "b25zZS5zdGF0ZSIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJ3YXJuaW5nTGlzdCIgZXE9IlJlc3RS";
	public static final String CV6 = "ZXNwb25zZS53YXJuaW5nTGlzdCIgdHlwZT0iU3RyaW5nIiAvPg0KICAgIDwvY29udmVydGVyPg0K";
	public static final String CV7 = "ICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iU2Vzc2lvblJlc3BvbnNlIiBwYXJhbXM9ImNvbS5i";
	public static final String CV8 = "Y3AuZGlzY291bnRzLnJlc3QucmVzcG9uc2UuU2Vzc2lvblJlc3BvbnNlIiBleHRlbmRzPSIwOlJl";
	public static final String CV9 = "c3RSZXNwb25zZSI+DQogICAgICAgIDx2YXIgbmFtZT0ic2Vzc2lvbiIgZXE9IlNlc3Npb25SZXNw";
	public static final String CV10 = "b25zZS5zZXNzaW9uIiB0eXBlPSJTZXNzaW9uUmVzdCIgcmVjZWl2ZXI9IjAiIC8+DQogICAgPC9j";
	public static final String CV11 = "b252ZXJ0ZXI+DQogICAgICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iSW1hZ2VSZXNwb25zZSIg";
	public static final String CV12 = "cGFyYW1zPSJjb20uYmNwLmRpc2NvdW50cy5yZXN0LnJlc3BvbnNlLkltYWdlUmVzcG9uc2UiIGV4";
	public static final String CV13 = "dGVuZHM9IjA6UmVzdFJlc3BvbnNlIj4NCiAgICAgICAgPHZhciBuYW1lPSJzeW5jRGF0ZSIgZXE9";
	public static final String CV14 = "IkltYWdlUmVzcG9uc2Uuc3luY0RhdGUiIC8+DQogICAgICAgIDx2YXIgbmFtZT0iaW1hZ2VMaXN0";
	public static final String CV15 = "IiBlcT0iSW1hZ2VSZXNwb25zZS5pbWFnZUxpc3QiIHR5cGU9IkltYWdlUmVzdCIgcmVjZWl2ZXI9";
	public static final String CV16 = "IjAiIC8+DQogICAgPC9jb252ZXJ0ZXI+DQogICAgICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0i";
	public static final String CV17 = "VXNlclJlc3BvbnNlIiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLnJlc3QucmVzcG9uc2UuVXNl";
	public static final String CV18 = "clJlc3BvbnNlIiBleHRlbmRzPSIwOlJlc3RSZXNwb25zZSI+DQogICAgICAgIDx2YXIgbmFtZT0i";
	public static final String CV19 = "dXNlcklkIiBlcT0iVXNlclJlc3BvbnNlLnVzZXJJZCIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJs";
	public static final String CV20 = "YXJnZU5hbWUiIGVxPSJVc2VyUmVzcG9uc2UubGFyZ2VOYW1lIiAvPg0KICAgIDwvY29udmVydGVy";
	public static final String CV21 = "Pg0KICAgICAgICANCiAgICA8Y29udmVydGVyIG5hbWU9IlNlY3Rpb25SZXNwb25zZSIgcGFyYW1z";
	public static final String CV22 = "PSJjb20uYmNwLmRpc2NvdW50cy5yZXN0LnJlc3BvbnNlLlNlY3Rpb25SZXNwb25zZSIgZXh0ZW5k";
	public static final String CV23 = "cz0iMDpSZXN0UmVzcG9uc2UiPg0KICAgICAgICA8dmFyIG5hbWU9InN5bmNEYXRlIiBlcT0iU2Vj";
	public static final String CV24 = "dGlvblJlc3BvbnNlLnN5bmNEYXRlIiAvPg0KICAgICAgICA8dmFyIG5hbWU9InNlY3Rpb25MaXN0";
	public static final String CV25 = "IiBlcT0iU2VjdGlvblJlc3BvbnNlLnNlY3Rpb25MaXN0IiB0eXBlPSJTZWN0aW9uUmVzdCIgcmVj";
	public static final String CV26 = "ZWl2ZXI9IjAiIC8+DQogICAgPC9jb252ZXJ0ZXI+DQogICAgICAgIA0KICAgIDxjb252ZXJ0ZXIg";
	public static final String CV27 = "bmFtZT0iRGlzY291bnRSZXNwb25zZSIgcGFyYW1zPSJjb20uYmNwLmRpc2NvdW50cy5yZXN0LnJl";
	public static final String CV28 = "c3BvbnNlLkRpc2NvdW50UmVzcG9uc2UiIGV4dGVuZHM9IjA6UmVzdFJlc3BvbnNlIj4NCiAgICAg";
	public static final String CV29 = "ICAgPHZhciBuYW1lPSJzeW5jRGF0ZSIgZXE9IkRpc2NvdW50UmVzcG9uc2Uuc3luY0RhdGUiIC8+";
	public static final String CV30 = "DQogICAgICAgIDx2YXIgbmFtZT0iZGlzY291bnRMaXN0IiBlcT0iRGlzY291bnRSZXNwb25zZS5k";
	public static final String CV31 = "aXNjb3VudExpc3QiIHR5cGU9IkRpc2NvdW50UmVzdCIgcmVjZWl2ZXI9IjAiIC8+DQogICAgPC9j";
	public static final String CV32 = "b252ZXJ0ZXI+DQogICAgDQogICAgPGNvbnZlcnRlciBuYW1lPSJMb2NhdGlvblJlc3BvbnNlIiBw";
	public static final String CV33 = "YXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLnJlc3QucmVzcG9uc2UuTG9jYXRpb25SZXNwb25zZSIg";
	public static final String CV34 = "ZXh0ZW5kcz0iMDpSZXN0UmVzcG9uc2UiID4NCiAgICAgICAgPHZhciBuYW1lPSJsb2NhdGlvbkxp";
	public static final String CV35 = "c3QiIGVxPSJEaXNjb3VudC5sb2NhdGlvbkxpc3QiIHR5cGU9IkxvY2F0aW9uUmVzdCIgcmVjZWl2";
	public static final String CV36 = "ZXI9IjAiIC8+DQogICAgPC9jb252ZXJ0ZXI+DQogICAgDQogICAgPGNvbnZlcnRlciBuYW1lPSJH";
	public static final String CV37 = "R2VvY29kZVJlc3BvbnNlIiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLnJlc3QucmVzcG9uc2Uu";
	public static final String CV38 = "R0dlb2NvZGVSZXNwb25zZSIgZGlyZWN0VHJhbnNsYXRlPSJ0cnVlIiA+DQogICAgICAgIDx2YXIg";
	public static final String CV39 = "bmFtZT0icmVzdWx0cyIgZXE9IkdHZW9jb2RlUmVzcG9uc2UucG9zaXRpb25zIiB0eXBlPSJHR2Vv";
	public static final String CV40 = "Y29kZVJlc3QiIHJlY2VpdmVyPSIwIiAvPg0KICAgIDwvY29udmVydGVyPg0KICAgICAgICAgICAg";
	public static final String CV41 = "DQogICAgPGNvbnZlcnRlciBuYW1lPSJHUm91dGVSZXNwb25zZSIgcGFyYW1zPSJjb20uYmNwLmRp";
	public static final String CV42 = "c2NvdW50cy5yZXN0LnJlc3BvbnNlLkdSb3V0ZVJlc3BvbnNlIj4NCiAgICAgICAgPHZhciBuYW1l";
	public static final String CV43 = "PSJyb3V0ZXNbMF0ubGVnc1swXS5zdGFydF9sb2NhdGlvbiIgZXE9IkdSb3V0ZVJlc3BvbnNlLnN0";
	public static final String CV44 = "YXJ0UG9pbnQiIHR5cGU9IkdSb3V0ZVBvaW50UmVzdCIgcmVjZWl2ZXI9IjAiIC8+DQogICAgICAg";
	public static final String CV45 = "IDx2YXIgbmFtZT0icm91dGVzWzBdLmxlZ3NbMF0uZW5kX2xvY2F0aW9uIiBlcT0iR1JvdXRlUmVz";
	public static final String CV46 = "cG9uc2UuZW5kUG9pbnQiIHR5cGU9IkdSb3V0ZVBvaW50UmVzdCIgcmVjZWl2ZXI9IjAiIC8+DQog";
	public static final String CV47 = "ICAgICAgIDx2YXIgbmFtZT0icm91dGVzWzBdLmxlZ3NbMF0uc3RlcHMiIGVxPSJHUm91dGVSZXNw";
	public static final String CV48 = "b25zZS5zdGVwcyIgdHlwZT0iR1JvdXRlU3RlcFJlc3QiIHJlY2VpdmVyPSIwIiAvPg0KICAgIDwv";
	public static final String CV49 = "Y29udmVydGVyPg0KICAgIA0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iQWNjZXNzUmVzcG9u";
	public static final String CV50 = "c2UiIHBhcmFtcz0iY29tLmJjcC5kaXNjb3VudHMucmVzdC5yZXNwb25zZS5BY2Nlc3NSZXNwb25z";
	public static final String CV51 = "ZSIgZXh0ZW5kcz0iMDpSZXN0UmVzcG9uc2UiPg0KICAgICAgICA8dmFyIG5hbWU9InBhcmFtIiBl";
	public static final String CV52 = "cT0iQWNjZXNzUmVzcG9uc2UucGFyYW0iIC8+DQogICAgPC9jb252ZXJ0ZXI+DQoNCiAgICANCiAg";
	public static final String CV53 = "ICA8IS0tIFJFU1RTIC0tPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iQmFzZVJlc3RFbnRp";
	public static final String CV54 = "dHlSZXN0IiBwYXJhbXM9IkJhc2VSZXN0RW50aXR5IiA+DQogICAgICAgIDx2YXIgbmFtZT0iYXBw";
	public static final String CV55 = "bGljYXRpb25JZCIgZXE9IkJhc2VSZXN0RW50aXR5LmFwcGxpY2F0aW9uSWQiIC8+DQogICAgICAg";
	public static final String CV56 = "IDx2YXIgbmFtZT0idXNlcklkIiBlcT0iQmFzZVJlc3RFbnRpdHkudXNlcklkIiAvPg0KICAgIDwv";
	public static final String CV57 = "Y29udmVydGVyPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iVmFsaWRhdGlvblJlc3QiIHBh";
	public static final String CV58 = "cmFtcz0iY29tLmJjcC5kaXNjb3VudHMubW9kZWwuVmFsaWRhdGlvbiIgZGlyZWN0VHJhbnNsYXRl";
	public static final String CV59 = "PSJ0cnVlIj4NCiAgICA8L2NvbnZlcnRlcj4NCiAgICANCiAgICA8Y29udmVydGVyIG5hbWU9IlNl";
	public static final String CV60 = "c3Npb25SZXN0IiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLm1vZGVsLkxvZ2luU2Vzc2lvbiIg";
	public static final String CV61 = "ZXh0ZW5kcz0iMDpCYXNlUmVzdEVudGl0eVJlc3QiPg0KICAgICAgICA8dmFyIG5hbWU9InNlc3Np";
	public static final String CV62 = "b25JZCIgZXE9IkxvZ2luU2Vzc2lvbi5zZXNzaW9uSWQiIC8+DQogICAgICAgIDx2YXIgbmFtZT0i";
	public static final String CV63 = "ZGV2aWNlSWQiIGVxPSJMb2dpblNlc3Npb24uZGV2aWNlSWQiIC8+DQogICAgICAgIDx2YXIgbmFt";
	public static final String CV64 = "ZT0ib3NOYW1lIiBlcT0iTG9naW5TZXNzaW9uLm9zTmFtZSIgLz4NCiAgICAgICAgPHZhciBuYW1l";
	public static final String CV65 = "PSJvc1ZlcnNpb24iIGVxPSJMb2dpblNlc3Npb24ub3NWZXJzaW9uIiAvPg0KICAgICAgICA8dmFy";
	public static final String CV66 = "IG5hbWU9ImJyYW5kIiBlcT0iTG9naW5TZXNzaW9uLmJyYW5kIiAvPg0KICAgICAgICA8dmFyIG5h";
	public static final String CV67 = "bWU9Im1vZGVsIiBlcT0iTG9naW5TZXNzaW9uLm1vZGVsIiAvPg0KICAgICAgICA8dmFyIG5hbWU9";
	public static final String CV68 = "Im1vZGVsVmVyc2lvbiIgZXE9IkxvZ2luU2Vzc2lvbi5tb2RlbFZlcnNpb24iIC8+DQogICAgICAg";
	public static final String CV69 = "IDx2YXIgbmFtZT0iYXBwbGljYXRpb25WZXJzaW9uIiBlcT0iTG9naW5TZXNzaW9uLmFwcGxpY2F0";
	public static final String CV70 = "aW9uVmVyc2lvbiIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJ1dWlkIiBlcT0iTG9naW5TZXNzaW9u";
	public static final String CV71 = "LnV1aWQiIC8+DQogICAgICAgIDx2YXIgbmFtZT0idXBkYXRlVHlwZSIgZXE9IkxvZ2luU2Vzc2lv";
	public static final String CV72 = "bi51cGRhdGVUeXBlIiAvPg0KICAgICAgICA8dmFyIG5hbWU9ImNvbnRhY3RQaG9uZSIgZXE9Ikxv";
	public static final String CV73 = "Z2luU2Vzc2lvbi5jb250YWN0UGhvbmUiIC8+DQogICAgICAgIDx2YXIgbmFtZT0ic3RvcmVVcmwi";
	public static final String CV74 = "IGVxPSJMb2dpblNlc3Npb24uc3RvcmVVcmwiIC8+DQogICAgICAgIDx2YXIgbmFtZT0iaHN2IiBl";
	public static final String CV75 = "cT0iTG9naW5TZXNzaW9uLmhzdiIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJ2YWxpZGF0aW9ucyIg";
	public static final String CV76 = "ZXE9IkxvZ2luU2Vzc2lvbi52YWxpZGF0aW9ucyIgdHlwZT0iVmFsaWRhdGlvblJlc3QiIHJlY2Vp";
	public static final String CV77 = "dmVyPSIwIi8+DQogICAgPC9jb252ZXJ0ZXI+DQogICAgDQogICAgDQogICAgPGNvbnZlcnRlciBu";
	public static final String CV78 = "YW1lPSJTZWN0aW9uUmVzdCIgcGFyYW1zPSJjb20uYmNwLmRpc2NvdW50cy5tb2RlbC5TZWN0aW9u";
	public static final String CV79 = "IiBleHRlbmRzPSIwOkJhc2VSZXN0RW50aXR5UmVzdCI+DQogICAgICAgIDx2YXIgbmFtZT0iZGVm";
	public static final String CV80 = "YXVsdEl0ZW0iIGVxPSJTZWN0aW9uLmRlZmF1bHRJdGVtIiAvPg0KICAgICAgICA8dmFyIG5hbWU9";
	public static final String CV81 = "ImV4cGlyYXRpb25EYXRlIiBlcT0iU2VjdGlvbi5leHBpcmF0aW9uRGF0ZSIgLz4NCiAgICAgICAg";
	public static final String CV82 = "PHZhciBuYW1lPSJpY29uIiBlcT0iU2VjdGlvbi5pY29uIiAvPg0KICAgICAgICA8dmFyIG5hbWU9";
	public static final String CV83 = "Im5hbWUiIGVxPSJTZWN0aW9uLm5hbWUiIC8+DQogICAgICAgIDx2YXIgbmFtZT0icG9zaXRpb24i";
	public static final String CV84 = "IGVxPSJTZWN0aW9uLnBvc2l0aW9uIiAvPg0KICAgICAgICA8dmFyIG5hbWU9InNlY3Rpb25JZCIg";
	public static final String CV85 = "ZXE9IlNlY3Rpb24uc2VjdGlvbklkIiAvPg0KICAgICAgICA8dmFyIG5hbWU9InN5bmNEYXRlIiBl";
	public static final String CV86 = "cT0iU2VjdGlvbi5zeW5jRGF0ZSIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJjYXRlZ29yeUxpc3Qi";
	public static final String CV87 = "IGVxPSJTZWN0aW9uLmNhdGVnb3JpZXMiIHR5cGU9IkNhdGVnb3J5UmVzdCIgcmVjZWl2ZXI9IjAi";
	public static final String CV88 = "IC8+DQogICAgPC9jb252ZXJ0ZXI+DQogICAgDQogICAgDQogICAgPGNvbnZlcnRlciBuYW1lPSJD";
	public static final String CV89 = "YXRlZ29yeVJlc3QiIHBhcmFtcz0iY29tLmJjcC5kaXNjb3VudHMubW9kZWwuQ2F0ZWdvcnkiIGV4";
	public static final String CV90 = "dGVuZHM9IjA6QmFzZVJlc3RFbnRpdHlSZXN0Ij4NCiAgICAgICAgPHZhciBuYW1lPSJjYXRlZ29y";
	public static final String CV91 = "eUlkIiBlcT0iQ2F0ZWdvcnkuY2F0ZWdvcnlJZCIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJkZWZh";
	public static final String CV92 = "dWx0SXRlbSIgZXE9IkNhdGVnb3J5LmRlZmF1bHRJdGVtIiAvPg0KICAgICAgICA8dmFyIG5hbWU9";
	public static final String CV93 = "ImRpc2NvdW50VG90YWwiIGVxPSJDYXRlZ29yeS5wcm9kdWN0VG90YWwiIC8+DQogICAgICAgIDx2";
	public static final String CV94 = "YXIgbmFtZT0iZXhwaXJhdGlvbkRhdGUiIGVxPSJDYXRlZ29yeS5leHBpcmF0aW9uRGF0ZSIgLz4N";
	public static final String CV95 = "CiAgICAgICAgPHZhciBuYW1lPSJpY29uIiBlcT0iQ2F0ZWdvcnkuaWNvbiIgLz4NCiAgICAgICAg";
	public static final String CV96 = "PHZhciBuYW1lPSJuYW1lIiBlcT0iQ2F0ZWdvcnkubmFtZSIgLz4NCiAgICAgICAgPHZhciBuYW1l";
	public static final String CV97 = "PSJvcmRlciIgZXE9IkNhdGVnb3J5LmlPcmRlciIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJzcGVj";
	public static final String CV98 = "aWFsIiBlcT0iQ2F0ZWdvcnkuc3BlY2lhbCIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJzcGVjaWFs";
	public static final String CV99 = "TWVzc2FnZSIgZXE9IkNhdGVnb3J5LnNwZWNpYWxNZXNzYWdlIiAvPg0KICAgICAgICA8dmFyIG5h";
	public static final String CV100 = "bWU9InN5bmNEYXRlIiBlcT0iQ2F0ZWdvcnkuc3luY0RhdGUiIC8+DQogICAgICAgIDx2YXIgbmFt";
	public static final String CV101 = "ZT0ic3RhdHVzIiBlcT0iQ2F0ZWdvcnkuc3RhdHVzIiAvPg0KICAgIDwvY29udmVydGVyPg0KICAg";
	public static final String CV102 = "IA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iUHJvZHVjdFJlc3QiIHBhcmFtcz0iY29tLmJjcC5kaXNj";
	public static final String CV103 = "b3VudHMubW9kZWwuUHJvZHVjdCIgZXh0ZW5kcz0iMDpCYXNlUmVzdEVudGl0eVJlc3QiPg0KICAg";
	public static final String CV104 = "ICAgICA8dmFyIG5hbWU9ImNhdGVnb3J5SWQiIGVxPSJQcm9kdWN0LmNhdGVnb3J5SWQiIC8+DQog";
	public static final String CV105 = "ICAgICAgIDx2YXIgbmFtZT0icHJvZHVjdElkIiBlcT0iUHJvZHVjdC5wcm9kdWN0SWQiIC8+DQog";
	public static final String CV106 = "ICAgICAgIDx2YXIgbmFtZT0ic3RhdHVzUHJvZHVjdCIgZXE9IlByb2R1Y3Quc3RhdHVzIiAvPg0K";
	public static final String CV107 = "ICAgICAgICA8dmFyIG5hbWU9InVzZWQiIGVxPSJQcm9kdWN0LnVzZWQiIC8+DQogICAgICAgIDx2";
	public static final String CV108 = "YXIgbmFtZT0ic3RhdHVzSW1hZ2UiIGVxPSJQcm9kdWN0LnN0YXR1c0ltYWdlIiAvPg0KICAgIDwv";
	public static final String CV109 = "Y29udmVydGVyPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iRGlzY291bnRSZXN0IiBwYXJh";
	public static final String CV110 = "bXM9ImNvbS5iY3AuZGlzY291bnRzLm1vZGVsLkRpc2NvdW50IiBleHRlbmRzPSIwOlByb2R1Y3RS";
	public static final String CV111 = "ZXN0Ij4NCiAgICAgICAgPHZhciBuYW1lPSJjb21tZXJjZU5hbWUiIGVxPSJEaXNjb3VudC5jb21w";
	public static final String CV112 = "YW55IiAvPg0KICAgICAgICA8dmFyIG5hbWU9InBlcmNlbnQiIGVxPSJEaXNjb3VudC5kaXNjb3Vu";
	public static final String CV113 = "dFBlcmNlbnQiIC8+DQogICAgICAgIDx2YXIgbmFtZT0icmVzdHJpY3Rpb25zIiBlcT0iRGlzY291";
	public static final String CV114 = "bnQucmVzdHJpY3Rpb24iIC8+DQogICAgICAgIDx2YXIgbmFtZT0ic2hvcnRJbmZvIiBlcT0iRGlz";
	public static final String CV115 = "Y291bnQuc2hvcnRJbmZvIiAvPg0KICAgICAgICA8dmFyIG5hbWU9ImxvY2F0aW9uTGlzdCIgZXE9";
	public static final String CV116 = "IkRpc2NvdW50LmxvY2F0aW9uQWRkcmVzcyIgdHlwZT0iTG9jYXRpb25SZXN0IiByZWNlaXZlcj0i";
	public static final String CV117 = "MCIvPg0KICAgIDwvY29udmVydGVyPg0KICAgIA0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0i";
	public static final String CV118 = "TG9jYXRpb25SZXN0IiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLm1vZGVsLkxvY2F0aW9uQWRk";
	public static final String CV119 = "cmVzcyIgZXh0ZW5kcz0iMDpCYXNlUmVzdEVudGl0eVJlc3QiPg0KICAgICAgICA8dmFyIG5hbWU9";
	public static final String CV120 = "ImFkZHJlc3MiIGVxPSJMb2NhdGlvbkFkZHJlc3MuYWRkcmVzcyIgLz4NCiAgICAgICAgPHZhciBu";
	public static final String CV121 = "YW1lPSJsYXR0aXR1ZGUiIGVxPSJMb2NhdGlvbkFkZHJlc3MubGF0aXR1ZGUiIC8+DQogICAgICAg";
	public static final String CV122 = "IDx2YXIgbmFtZT0ibG9uZ2l0dWRlIiBlcT0iTG9jYXRpb25BZGRyZXNzLmxvbmdpdHVkZSIgLz4N";
	public static final String CV123 = "CiAgICAgICAgPHZhciBuYW1lPSJwaG9uZU51bWJlcjEiIGVxPSJMb2NhdGlvbkFkZHJlc3MucGhv";
	public static final String CV124 = "bmUiIC8+DQogICAgICAgIDx2YXIgbmFtZT0icGhvbmVOdW1iZXIyIiBlcT0iTG9jYXRpb25BZGRy";
	public static final String CV125 = "ZXNzLnBob25lRXh0IiAvPg0KICAgIDwvY29udmVydGVyPg0KICAgIA0KICAgIA0KICAgIDxjb252";
	public static final String CV126 = "ZXJ0ZXIgbmFtZT0iUGFja2FnZUltYWdlUmVzdCIgcGFyYW1zPSJjb20uYmNwLmRpc2NvdW50cy5t";
	public static final String CV127 = "b2RlbC5QYWNrYWdlSW1hZ2UiIGV4dGVuZHM9IjA6QmFzZVJlc3RFbnRpdHlSZXN0Ij4NCiAgICAg";
	public static final String CV128 = "ICAgPHZhciBuYW1lPSJzeW5jRGF0ZSIgZXE9IlBhY2thZ2VJbWFnZS5zeW5jRGF0ZSIgLz4NCiAg";
	public static final String CV129 = "ICAgICAgPHZhciBuYW1lPSJjb25mSW1hZ2VzTGlzdCIgZXE9IlBhY2thZ2VJbWFnZS5jb25mSW1h";
	public static final String CV130 = "Z2VzTGlzdCIgdHlwZT0iRGVmaW5pdGlvbkltYWdlUmVzdCIgcmVjZWl2ZXI9IjAiLz4NCiAgICA8";
	public static final String CV131 = "L2NvbnZlcnRlcj4NCiAgICANCiAgICANCiAgICA8Y29udmVydGVyIG5hbWU9IkRlZmluaXRpb25J";
	public static final String CV132 = "bWFnZVJlc3QiIHBhcmFtcz0iY29tLmJjcC5kaXNjb3VudHMubW9kZWwuRGVmaW5pdGlvbkltYWdl";
	public static final String CV133 = "IiBleHRlbmRzPSIwOkJhc2VSZXN0RW50aXR5UmVzdCI+DQogICAgICAgIDx2YXIgbmFtZT0icHJv";
	public static final String CV134 = "ZHVjdElkIiBlcT0iRGVmaW5pdGlvbkltYWdlLnByb2R1Y3RJZCIgLz4NCiAgICAgICAgPHZhciBu";
	public static final String CV135 = "YW1lPSJyZXNvbHV0aW9uSWRzIiBlcT0iRGVmaW5pdGlvbkltYWdlLnJlc29sdXRpb25JZHMiIC8+";
	public static final String CV136 = "DQogICAgPC9jb252ZXJ0ZXI+DQoNCiAgICA8Y29udmVydGVyIG5hbWU9IkltYWdlUmVzdCIgcGFy";
	public static final String CV137 = "YW1zPSJjb20uYmNwLmRpc2NvdW50cy5tb2RlbC5JbWFnZUluZm8iIGV4dGVuZHM9IjA6QmFzZVJl";
	public static final String CV138 = "c3RFbnRpdHlSZXN0Ij4NCiAgICAgICAgPHZhciBuYW1lPSJ1cGRhdGVEYXRlIiBlcT0iSW1hZ2VJ";
	public static final String CV139 = "bmZvLnVwZGF0ZURhdGUiIC8+DQogICAgICAgIDx2YXIgbmFtZT0iaWQiIGVxPSJJbWFnZUluZm8u";
	public static final String CV140 = "aW1hZ2VJZCIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJpbWFnZU5hbWUiIGVxPSJJbWFnZUluZm8u";
	public static final String CV141 = "aW1hZ2VOYW1lIiAvPg0KICAgICAgICA8dmFyIG5hbWU9InJlc29sdXRpb25JZCIgZXE9IkltYWdl";
	public static final String CV142 = "SW5mby5yZXNvbHV0aW9uSWQiIC8+DQogICAgICAgIDx2YXIgbmFtZT0ic3RhdHVzSW1hZ2UiIGVx";
	public static final String CV143 = "PSJJbWFnZUluZm8uc3RhdHVzSW1hZ2UiIC8+DQogICAgICAgIDx2YXIgbmFtZT0iaW1hZ2VGaWxl";
	public static final String CV144 = "IiBlcT0iSW1hZ2VJbmZvLmltYWdlRmlsZSIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJwcm9kdWN0";
	public static final String CV145 = "SWQiIGVxPSJJbWFnZUluZm8ucHJvZHVjdElkIiAvPg0KICAgIDwvY29udmVydGVyPg0KICAgIA0K";
	public static final String CV146 = "ICAgIDxjb252ZXJ0ZXIgbmFtZT0iR0dlb2NvZGVSZXN0IiBwYXJhbXM9ImNvbS5iY3AuZGlzY291";
	public static final String CV147 = "bnRzLm1vZGVsLkxhdExuZ1BvaW50Ij4NCiAgICAgICAgPHZhciBuYW1lPSJnZW9tZXRyeS5sb2Nh";
	public static final String CV148 = "dGlvbi5sYXQiIGVxPSJMYXRMbmdQb2ludC5sYXRpdHVkZSIgLz4NCiAgICAgICAgPHZhciBuYW1l";
	public static final String CV149 = "PSJnZW9tZXRyeS5sb2NhdGlvbi5sbmciIGVxPSJMYXRMbmdQb2ludC5sb25naXR1ZGUiIC8+IA0K";
	public static final String CV150 = "ICAgIDwvY29udmVydGVyPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iR1JvdXRlUG9pbnRS";
	public static final String CV151 = "ZXN0IiBwYXJhbXM9ImNvbS5iY3AuZGlzY291bnRzLm1vZGVsLkxhdExuZ1BvaW50Ij4NCiAgICAg";
	public static final String CV152 = "ICAgPHZhciBuYW1lPSJsYXQiIGVxPSJMYXRMbmdQb2ludC5sYXRpdHVkZSIgLz4NCiAgICAgICAg";
	public static final String CV153 = "PHZhciBuYW1lPSJsbmciIGVxPSJMYXRMbmdQb2ludC5sb25naXR1ZGUiIC8+IA0KICAgIDwvY29u";
	public static final String CV154 = "dmVydGVyPg0KICAgIA0KICAgIDxjb252ZXJ0ZXIgbmFtZT0iR1JvdXRlU3RlcFJlc3QiIHBhcmFt";
	public static final String CV155 = "cz0iY29tLmJjcC5kaXNjb3VudHMubW9kZWwuU3RlcFBvaW50Ij4NCiAgICAgICAgPHZhciBuYW1l";
	public static final String CV156 = "PSJzdGFydF9sb2NhdGlvbiIgZXE9IlN0ZXBQb2ludC5zdGFydFBvaW50IiB0eXBlPSJHUm91dGVQ";
	public static final String CV157 = "b2ludFJlc3QiIHJlY2VpdmVyPSIwIiAvPg0KICAgICAgICA8dmFyIG5hbWU9ImVuZF9sb2NhdGlv";
	public static final String CV158 = "biIgZXE9IlN0ZXBQb2ludC5lbmRQb2ludCIgdHlwZT0iR1JvdXRlUG9pbnRSZXN0IiByZWNlaXZl";
	public static final String CV159 = "cj0iMCIgLz4NCiAgICAgICAgPHZhciBuYW1lPSJwb2x5bGluZS5wb2ludHMiIGVxPSJTdGVwUG9p";
	public static final String CV160 = "bnQucG9seWxpbmUiIC8+IA0KICAgIDwvY29udmVydGVyPg0KICAgIA0KPC9jb252ZXJ0ZXJzPg==";

	public static final String[] CV_IS = {CV0, CV1, CV2, CV3, CV4, CV5, CV6, CV7, CV8, CV9, CV10, CV11, CV12, CV13, CV14, CV15, CV16, CV17, CV18, CV19, CV20, CV21, CV22, CV23, CV24, CV25, CV26, CV27, CV28, CV29, CV30, CV31, CV32, CV33, CV34, CV35, CV36, CV37, CV38, CV39, CV40, CV41, CV42, CV43, CV44, CV45, CV46, CV47, CV48, CV49, CV50, CV51, CV52, CV53, CV54, CV55, CV56, CV57, CV58, CV59, CV60, CV61, CV62, CV63, CV64, CV65, CV66, CV67, CV68, CV69, CV70, CV71, CV72, CV73, CV74, CV75, CV76, CV77, CV78, CV79, CV80, CV81, CV82, CV83, CV84, CV85, CV86, CV87, CV88, CV89, CV90, CV91, CV92, CV93, CV94, CV95, CV96, CV97, CV98, CV99, CV100, CV101, CV102, CV103, CV104, CV105, CV106, CV107, CV108, CV109, CV110, CV111, CV112, CV113, CV114, CV115, CV116, CV117, CV118, CV119, CV120, CV121, CV122, CV123, CV124, CV125, CV126, CV127, CV128, CV129, CV130, CV131, CV132, CV133, CV134, CV135, CV136, CV137, CV138, CV139, CV140, CV141, CV142, CV143, CV144, CV145, CV146, CV147, CV148, CV149, CV150, CV151, CV152, CV153, CV154, CV155, CV156, CV157, CV158, CV159, CV160};
	
	public static final String CO0 = "PCEtLQogbmFtZTogU1RSSU5HCiB1cmw6IFNUUklORwogaW5QYXJzZXJUeXBlOiAiUEFSU0VSX0pT";
	public static final String CO1 = "T04iLCAiKlBBUlNFUl9YTUwiLCAiUEFSU0VSX05PTkUiCiBvdXRQYXJzZXJUeXBlOiAiUEFSU0VS";
	public static final String CO2 = "X0pTT04iLCAiKlBBUlNFUl9YTUwiLCAiUEFSU0VSX05PTkUiLCAiUEFSU0VSX01VTFRJUEFSVCIK";
	public static final String CO3 = "IHByaW9yaXR5OiBJTlRFR0VSICgqMCBtb3N0IHByaW9yaXR5KQogcmV0dXJuSW5mb0NsYXNzOiBT";
	public static final String CO4 = "VFJJTkcgKGNsYXNzKQogcmVzcG9uZEluTWFpblRocmVhZDogKiJZRVMiLCAiTk8iCiB0aW1lb3V0";
	public static final String CO5 = "OiBmbG9hdCAoaW4gc2Vjb25kcykKIGNhY2hlU2F2ZTogIkNBQ0hFX1NBVkUiLCAqIkNBQ0hFX05P";
	public static final String CO6 = "X1NBVkUiCiBjYWNoZUxvYWQ6ICJDQUNIRV9MT0FEX0NIRUNLIiwgKiJDQUNIRV9MT0FEX05PX0NI";
	public static final String CO7 = "RUNLIiwgIkNBQ0hFX0xPQURfT05MWSIKIGNhY2hlTG9hZFRpbWVBZ286IElOVEVHRVIgKGluIHNl";
	public static final String CO8 = "Y29uZHMsIDAgIG5vIGNoZWNrIHRpbWUpCiBtZXRob2RSZXN0OiAiKk1FVEhPRF9SRVNUX0dFVCIs";
	public static final String CO9 = "ICJNRVRIT0RfUkVTVF9QT1NUIiwgIk1FVEhPRF9SRVNUX1BVVCIsICJNRVRIT0RfUkVTVF9ERUxF";
	public static final String CO10 = "VEUiLAogNy4xMDcuMTAxLjUzIC0+IEVUSEVSTkVUCiAxMC4xMDguMTM2LjIxNSAtPiBXSUZJCiAx";
	public static final String CO11 = "NzIuMjkuMTE3LjM2Ojk1MjAgLT4gQkNQCiAxNzIuMjkuMTAxLjE1OjkwOTcgLT4gRW50b3JubyBj";
	public static final String CO12 = "ZXJ0aWZpY2FjacOzbiBCQ1AKIC8+Ci0tPgo8c2VydmljZXM+CgogICAgPCEtLQogICAgIDxzZXJ2";
	public static final String CO13 = "aWNlCiAgICAgbmFtZT0iIgogICAgIHVybD0iIgogICAgIGluUGFyc2VyVHlwZT0iIgogICAgIG91";
	public static final String CO14 = "dFBhcnNlclR5cGU9IiIKICAgICBwcmlvcml0eT0iIgogICAgIHRpbWVvdXQ9IiIKICAgICByZXR1";
	public static final String CO15 = "cm5JbmZvQ2xhc3M9IiIKICAgICByZXNwb25kSW5NYWluVGhyZWFkPSIiCiAgICAgY2FjaGVTYXZl";
	public static final String CO16 = "PSIiCiAgICAgY2FjaGVMb2FkPSIiCiAgICAgY2FjaGVMb2FkVGltZUFnbz0iIgogICAgIG1ldGhv";
	public static final String CO17 = "ZFJlc3Q9IiIKICAgICAvPgogICAgLS0+CgoKICAgIDwhLS0gQkNQIERpc2NvdW50cyBTZXJ2aWNl";
	public static final String CO18 = "cyAtLT4KCgogICAgPCEtLSBTZXNzaW9uUmVzdFNlcnZpY2UgLS0+CiAgICA8c2VydmljZQogICAg";
	public static final String CO19 = "ICAgIG5hbWU9ImRldmljZUFjY2VzcyIKICAgICAgICBpblBhcnNlclR5cGU9IlBBUlNFUl9KU09O";
	public static final String CO20 = "IgogICAgICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9SRVNUX0dFVCIKICAgICAgICBwcmlvcml0eT0i";
	public static final String CO21 = "MCIKICAgICAgICB0aW1lb3V0PSIxMCIKICAgICAgICByZXR1cm5JbmZvQ29udmVydGVyPSJBY2Nl";
	public static final String CO22 = "c3NSZXNwb25zZSIKICAgICAgICB1cmw9IntAdXJsfS9kZXZpY2VTZXJ2aWNlL2FjY2VzcyIgLz4K";
	public static final String CO23 = "ICAgIAogICAgPHNlcnZpY2UKICAgICAgICBuYW1lPSJsb2dpblNlc3Npb24iCiAgICAgICAgaW5Q";
	public static final String CO24 = "YXJzZXJUeXBlPSJQQVJTRVJfSlNPTiIKICAgICAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9Q";
	public static final String CO25 = "T1NUIgogICAgICAgIG91dFBhcnNlclR5cGU9IlBBUlNFUl9KU09OIgogICAgICAgIHByaW9yaXR5";
	public static final String CO26 = "PSIwIgogICAgICAgIHRpbWVvdXQ9IjEwIgogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IlNl";
	public static final String CO27 = "c3Npb25SZXNwb25zZSIKICAgICAgICBzZW5kQ29udmVydGVyPSJTZXNzaW9uUmVzdCIKICAgICAg";
	public static final String CO28 = "ICB1cmw9IntAdXJsfS9zZXNzaW9uU2VydmljZS9zZXNzaW9uIiAvPgogICAgCiAgICA8c2Vydmlj";
	public static final String CO29 = "ZQogICAgICAgIG5hbWU9ImxvZ291dFNlc3Npb24iCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQQVJT";
	public static final String CO30 = "RVJfSlNPTiIKICAgICAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9ERUxFVEUiCiAgICAgICAg";
	public static final String CO31 = "cHJpb3JpdHk9IjAiCiAgICAgICAgdGltZW91dD0iMTAiCiAgICAgICAgcmV0dXJuSW5mb0NvbnZl";
	public static final String CO32 = "cnRlcj0iUmVzdFJlc3BvbnNlIgogICAgICAgIHVybD0ie0B1cmx9L3Nlc3Npb25TZXJ2aWNlL3Nl";
	public static final String CO33 = "c3Npb24ve0BhcHBJZH0ve0B1c2VySWR9L3tAZGV2aWNlSWR9IiAvPgogICAgCiAgICA8c2Vydmlj";
	public static final String CO34 = "ZQogICAgICAgIG5hbWU9ImRldmljZVBlcm1pc3Npb25zIgogICAgICAgIGluUGFyc2VyVHlwZT0i";
	public static final String CO35 = "UEFSU0VSX0pTT04iCiAgICAgICAgb3V0UGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iCiAgICAgICAg";
	public static final String CO36 = "bWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfUE9TVCIKICAgICAgICBwcmlvcml0eT0iMCIKICAgICAg";
	public static final String CO37 = "ICB0aW1lb3V0PSIzMCIKICAgICAgICBzZW5kQ29udmVydGVyPSJTZXNzaW9uUmVzdCIKICAgICAg";
	public static final String CO38 = "ICByZXR1cm5JbmZvQ29udmVydGVyPSJSZXN0UmVzcG9uc2UiCiAgICAgICAgdXJsPSJ7QHVybH0v";
	public static final String CO39 = "ZGV2aWNlU2VydmljZS9wZXJtaXNzaW9ucy97QGFwcElkfSIgLz4KICAgIAogICAgCiAgICA8c2Vy";
	public static final String CO40 = "dmljZQogICAgICAgIG5hbWU9ImltYWdlc1Nlc3Npb24iCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQ";
	public static final String CO41 = "QVJTRVJfTVVMVElQQVJUIgogICAgICAgIG1ldGhvZFJlc3Q9Ik1FVEhPRF9SRVNUX1BPU1QiCiAg";
	public static final String CO42 = "ICAgICAgb3V0UGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iCiAgICAgICAgcHJpb3JpdHk9Ii0zIgog";
	public static final String CO43 = "ICAgICAgIHRpbWVvdXQ9IjMwIgogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IkltYWdlUmVz";
	public static final String CO44 = "cG9uc2UiCiAgICAgICAgc2VuZENvbnZlcnRlcj0iUGFja2FnZUltYWdlUmVzdCIKICAgICAgICB1";
	public static final String CO45 = "cmw9IntAdXJsfS9zZXNzaW9uU2VydmljZS9zZXNzaW9uL2ltYWdlcyIgLz4KCiAgICA8IS0tIFVz";
	public static final String CO46 = "ZXJSZXN0U2VydmljZSAtLT4KICAgIDxzZXJ2aWNlCiAgICAgICAgbmFtZT0ibG9naW5Vc2VyIgog";
	public static final String CO47 = "ICAgICAgIGluUGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iCiAgICAgICAgbWV0aG9kUmVzdD0iTUVU";
	public static final String CO48 = "SE9EX1JFU1RfR0VUIgogICAgICAgIHByaW9yaXR5PSIwIgogICAgICAgIHRpbWVvdXQ9IjMwIgog";
	public static final String CO49 = "ICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IlVzZXJSZXNwb25zZSIKICAgICAgICB1cmw9IntA";
	public static final String CO50 = "dXJsfS91c2VyU2VydmljZS91c2VyL3tAYXBwSWR9L3tAZG5pfSIgLz4KICAgIAogICAgPHNlcnZp";
	public static final String CO51 = "Y2UKICAgICAgICBuYW1lPSJsb2dvdXRVc2VyIgogICAgICAgIGluUGFyc2VyVHlwZT0iUEFSU0VS";
	public static final String CO52 = "X0pTT04iCiAgICAgICAgbWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfREVMRVRFIgogICAgICAgIHBy";
	public static final String CO53 = "aW9yaXR5PSIwIgogICAgICAgIHRpbWVvdXQ9IjEwIgogICAgICAgIHJldHVybkluZm9Db252ZXJ0";
	public static final String CO54 = "ZXI9IlJlc3RSZXNwb25zZSIKICAgICAgICB1cmw9IntAdXJsfS91c2VyU2VydmljZS91c2VyL3tA";
	public static final String CO55 = "YXBwSWR9L3tAdXNlcklkfSIgLz4KCiAgICA8IS0tIFNlY3Rpb25SZXN0U2VydmljZSAtLT4KICAg";
	public static final String CO56 = "IDxzZXJ2aWNlCiAgICAgICAgbmFtZT0ic2VjdGlvbnMiCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQ";
	public static final String CO57 = "QVJTRVJfSlNPTiIKICAgICAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9HRVQiCiAgICAgICAg";
	public static final String CO58 = "cHJpb3JpdHk9IjAiCiAgICAgICAgdGltZW91dD0iMTAiCiAgICAgICAgcmV0dXJuSW5mb0NvbnZl";
	public static final String CO59 = "cnRlcj0iU2VjdGlvblJlc3BvbnNlIgogICAgICAgIHVybD0ie0B1cmx9L3NlY3Rpb25TZXJ2aWNl";
	public static final String CO60 = "L3NlY3Rpb25zL3tAYXBwSWR9L3tAdXNlcklkfS97QGRhdGVVcGRhdGV9L3tAcmVzb2x1dGlvbklk";
	public static final String CO61 = "fSIgLz4KCiAgICA8IS0tIERpc2NvdW50UmVzdFNlcnZpY2UgLS0+CiAgICA8c2VydmljZQogICAg";
	public static final String CO62 = "ICAgIG5hbWU9ImRpc2NvdW50c0NhdGVnb3J5IgogICAgICAgIGluUGFyc2VyVHlwZT0iUEFSU0VS";
	public static final String CO63 = "X0pTT04iCiAgICAgICAgbWV0aG9kUmVzdD0iTUVUSE9EX1JFU1RfR0VUIgogICAgICAgIHByaW9y";
	public static final String CO64 = "aXR5PSIwIgogICAgICAgIHRpbWVvdXQ9IjMwIgogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9";
	public static final String CO65 = "IkRpc2NvdW50UmVzcG9uc2UiCiAgICAgICAgdXJsPSJ7QHVybH0vZGlzY291bnRTZXJ2aWNlL2Rp";
	public static final String CO66 = "c2NvdW50L3tAYXBwSWR9L3tAdXNlcklkfS97QGNhdGVnb3J5SWR9L3tAZGF0ZVVwZGF0ZX0iIC8+";
	public static final String CO67 = "CiAgICAKICAgIDxzZXJ2aWNlCiAgICAgICAgbmFtZT0iZGlzY291bnRzTGlzdEltYWdlcyIKICAg";
	public static final String CO68 = "ICAgICBpblBhcnNlclR5cGU9IlBBUlNFUl9NVUxUSVBBUlQiCiAgICAgICAgbWV0aG9kUmVzdD0i";
	public static final String CO69 = "TUVUSE9EX1JFU1RfUE9TVCIKICAgICAgICBvdXRQYXJzZXJUeXBlPSJQQVJTRVJfSlNPTiIKICAg";
	public static final String CO70 = "ICAgICBwcmlvcml0eT0iLTIiCiAgICAgICAgdGltZW91dD0iMjAiCiAgICAgICAgcmVzcG9uZElu";
	public static final String CO71 = "TWFpblRocmVhZD0iZmFsc2UiCiAgICAgICAgcmV0dXJuSW5mb0NvbnZlcnRlcj0iSW1hZ2VSZXNw";
	public static final String CO72 = "b25zZSIKICAgICAgICBzZW5kQ29udmVydGVyPSJQYWNrYWdlSW1hZ2VSZXN0IgogICAgICAgIHVy";
	public static final String CO73 = "bD0ie0B1cmx9L2Rpc2NvdW50U2VydmljZS9kaXNjb3VudC9pbWFnZXMiIC8+CiAgICAKICAgIDxz";
	public static final String CO74 = "ZXJ2aWNlCiAgICAgICAgbmFtZT0iZGlzY291bnRzRGV0YWlsSW1hZ2VzIgogICAgICAgIGluUGFy";
	public static final String CO75 = "c2VyVHlwZT0iUEFSU0VSX01VTFRJUEFSVCIKICAgICAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVT";
	public static final String CO76 = "VF9QT1NUIgogICAgICAgIG91dFBhcnNlclR5cGU9IlBBUlNFUl9KU09OIgogICAgICAgIHByaW9y";
	public static final String CO77 = "aXR5PSItMSIKICAgICAgICB0aW1lb3V0PSIzMCIKICAgICAgICByZXNwb25kSW5NYWluVGhyZWFk";
	public static final String CO78 = "PSJmYWxzZSIKICAgICAgICByZXR1cm5JbmZvQ29udmVydGVyPSJJbWFnZVJlc3BvbnNlIgogICAg";
	public static final String CO79 = "ICAgIHNlbmRDb252ZXJ0ZXI9IlBhY2thZ2VJbWFnZVJlc3QiCiAgICAgICAgdXJsPSJ7QHVybH0v";
	public static final String CO80 = "ZGlzY291bnRTZXJ2aWNlL2Rpc2NvdW50L2ltYWdlcyIgLz4KICAgICAgICAKICAgIDxzZXJ2aWNl";
	public static final String CO81 = "CiAgICAgICAgbmFtZT0iZGlzY291bnRzTG9jYXRpb24iCiAgICAgICAgaW5QYXJzZXJUeXBlPSJQ";
	public static final String CO82 = "QVJTRVJfSlNPTiIKICAgICAgICBtZXRob2RSZXN0PSJNRVRIT0RfUkVTVF9HRVQiCiAgICAgICAg";
	public static final String CO83 = "cHJpb3JpdHk9IjAiCiAgICAgICAgdGltZW91dD0iMzAiCiAgICAgICAgcmV0dXJuSW5mb0NvbnZl";
	public static final String CO84 = "cnRlcj0iUmVzdFJlc3BvbnNlIgogICAgICAgIHVybD0ie0B1cmx9L2Rpc2NvdW50U2VydmljZS9k";
	public static final String CO85 = "aXNjb3VudC9sb2NhdGlvbi97QGFwcElkfS97QHVzZXJJZH0ve0BjYXRlZ29yeUlkfS97QGRpc2Nv";
	public static final String CO86 = "dW50SWR9IiAvPgogICAgCiAgICA8c2VydmljZQogICAgICAgIG5hbWU9InJlc29sdmVBZGRyZXNz";
	public static final String CO87 = "IgogICAgICAgIGluUGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iCiAgICAgICAgbWV0aG9kUmVzdD0i";
	public static final String CO88 = "TUVUSE9EX1JFU1RfR0VUIgogICAgICAgIHByaW9yaXR5PSIwIgogICAgICAgIHRpbWVvdXQ9IjMw";
	public static final String CO89 = "IgogICAgICAgIHJldHVybkluZm9Db252ZXJ0ZXI9IkdHZW9jb2RlUmVzcG9uc2UiCiAgICAgICAg";
	public static final String CO90 = "dXJsPSJodHRwOi8vbWFwcy5nb29nbGVhcGlzLmNvbS9tYXBzL2FwaS9nZW9jb2RlL2pzb24iIC8+";
	public static final String CO91 = "CiAgICAKICAgIDxzZXJ2aWNlCiAgICAgICAgbmFtZT0icmVzb2x2ZVJvdXRlIgogICAgICAgIGlu";
	public static final String CO92 = "UGFyc2VyVHlwZT0iUEFSU0VSX0pTT04iCiAgICAgICAgbWV0aG9kUmVzdD0iTUVUSE9EX1JFU1Rf";
	public static final String CO93 = "R0VUIgogICAgICAgIHByaW9yaXR5PSIwIgogICAgICAgIHRpbWVvdXQ9IjMwIgogICAgICAgIHJl";
	public static final String CO94 = "dHVybkluZm9Db252ZXJ0ZXI9IkdSb3V0ZVJlc3BvbnNlIgogICAgICAgIHVybD0iaHR0cDovL21h";
	public static final String CO95 = "cHMuZ29vZ2xlYXBpcy5jb20vbWFwcy9hcGkvZGlyZWN0aW9ucy9qc29uIiAvPgogICAgCiAgICAK";
	public static final String CO96 = "Cjwvc2VydmljZXM+";

	public static final String[] CO_IS = {CO0, CO1, CO2, CO3, CO4, CO5, CO6, CO7, CO8, CO9, CO10, CO11, CO12, CO13, CO14, CO15, CO16, CO17, CO18, CO19, CO20, CO21, CO22, CO23, CO24, CO25, CO26, CO27, CO28, CO29, CO30, CO31, CO32, CO33, CO34, CO35, CO36, CO37, CO38, CO39, CO40, CO41, CO42, CO43, CO44, CO45, CO46, CO47, CO48, CO49, CO50, CO51, CO52, CO53, CO54, CO55, CO56, CO57, CO58, CO59, CO60, CO61, CO62, CO63, CO64, CO65, CO66, CO67, CO68, CO69, CO70, CO71, CO72, CO73, CO74, CO75, CO76, CO77, CO78, CO79, CO80, CO81, CO82, CO83, CO84, CO85, CO86, CO87, CO88, CO89, CO90, CO91, CO92, CO93, CO94, CO95, CO96};
	
}
