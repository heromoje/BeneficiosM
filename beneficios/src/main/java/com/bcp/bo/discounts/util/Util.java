package com.bcp.bo.discounts.util;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

public class Util {
	public static void StopTask(AsyncTask asyncTask){
		if(asyncTask!=null && asyncTask.getStatus().equals(Status.RUNNING)){
			asyncTask.cancel(true);
		}
	}
}
