package com.bcp.bo.discounts.general;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.util.StringUtil;

import bcp.bo.service.model.request.Condition;
import bcp.bo.service.model.request.RatingTicket;
import bcp.bo.service.model.response.Promotion;


public class AlertMsg {
	
	public static final String DIALOG_TEXT_KEY = "DIALOG_TEXT_KEY";
	
	public static void showErrorDialog(final BaseLayoutActivity baseActivity, int messageRes) {
		showErrorWarningDialog(baseActivity, baseActivity.getString(messageRes), true, null, null, null, 0, 0);
	}
	
	public static void showErrorDialog(final BaseLayoutActivity baseActivity, final String message) {
		showErrorWarningDialog(baseActivity, message, true, null, null, null, 0, 0);
	}
	
	public static void showErrorDialog(final BaseLayoutActivity baseActivity, final String message, View.OnClickListener clickListener, int okButtonId) {
		showErrorWarningDialog(baseActivity, message, true, clickListener, null, null, okButtonId, 0);
	}
	
	public static void showWarningDialog(final BaseLayoutActivity baseActivity, int messageRes) {
		showErrorWarningDialog(baseActivity, baseActivity.getString(messageRes), false, null, null, null, 0, 0);
	}
	
	public static void showWarningDialog(final BaseLayoutActivity baseActivity, int messageRes, View.OnClickListener clickListener) {
		showErrorWarningDialog(baseActivity, baseActivity.getString(messageRes), false, clickListener, null, null, 0, 0);
	}
	
	public static void showWarningDialog(final BaseLayoutActivity baseActivity, final String message) {
		showErrorWarningDialog(baseActivity, message, false, null, null, null, 0, 0);
	}
	
	public static void showWarningDialog(final BaseLayoutActivity baseActivity, final String message, View.OnClickListener clickListener) {
		showErrorWarningDialog(baseActivity, message, false, clickListener, null, null, 0, 0);
	}
	
	public static void showErrorWarningDialog(final BaseLayoutActivity baseActivity, final String message, boolean error, 
			View.OnClickListener clickListener, String sOk, String sCancel, int clickOkId, int clickCancelId) {
		showErrorWarningDialog(baseActivity, message, error, clickListener, sOk, sCancel, clickOkId, clickCancelId, false);
	}
	
	public static void showErrorWarningDialog(final BaseLayoutActivity baseActivity, final String message, boolean error, 
				View.OnClickListener clickListener, String sOk, String sCancel, int clickOkId, int clickCancelId, boolean cancelable) {	    
		baseActivity.dismissDialog();
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(cancelable);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_error_warning);
		
		// set the custom dialog components - title, text, image and button
		final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		final ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
		if (error) {
			tvTitle.setText(R.string.ERROR);
			ivIcon.setImageResource(R.drawable.dlg_icon_error);
		} else {
			tvTitle.setText(R.string.INFORMATION);
			ivIcon.setImageResource(R.drawable.dlg_icon_warning);
		}
		
		final TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		tvMessage.setText(message);
		

		final View llButtons = dialog.findViewById(R.id.llButtons2);
		
		//show ok or ok and cancel buttons
		if(clickCancelId>0){
			
			//add ok listener
			final TextView tvOk2 = (TextView)dialog.findViewById(R.id.tvOk2);
			if(sOk!=null){
				tvOk2.setText(sOk);
			}
			tvOk2.setId(clickOkId);
			final DialogClickListener dialogListenerOk = new DialogClickListener(baseActivity, clickListener);
			tvOk2.setOnClickListener(dialogListenerOk);
			
			//add cancel listener
			final TextView tvCancel2 = (TextView)dialog.findViewById(R.id.tvCancel2);
			if(sCancel!=null){
				tvCancel2.setText(sCancel);
			}
			tvCancel2.setId(clickCancelId);
			final DialogClickListener dialogListenerCancel = new DialogClickListener(baseActivity, clickListener);
			tvCancel2.setOnClickListener(dialogListenerCancel);
			
		}else{
			//hide double buttons
			llButtons.setVisibility(View.GONE);
			
			//add ok listener
			final TextView tvOk = (TextView)dialog.findViewById(R.id.tvOk);
			if(sOk!=null){
				tvOk.setText(sOk);
			}
			tvOk.setId(clickOkId);
			final DialogClickListener dialogListener = new DialogClickListener(baseActivity, clickListener);
			tvOk.setOnClickListener(dialogListener);
		}
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));

		//show the dialog over the activity
		baseActivity.showDialog(dialog);
		
	}
	public static void showCondition(final BaseLayout baseLayout,final bcp.bo.service.ServiceLauncher.IService callback){
		final BaseLayoutActivity baseActivity=baseLayout.getBaseLayoutActivity();
		baseActivity.dismissDialog();
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_condition);
		final TextView textView_Link=(TextView)dialog.findViewById(R.id.textView_Link);
		final TextView textView_Continue=(TextView)dialog.findViewById(R.id.textView_Continue);
		final TextView textView_Cancel=(TextView)dialog.findViewById(R.id.textView_Cancel);
		final EditText editText_Phone=(EditText)dialog.findViewById(R.id.editText_Phone);
		final EditText editText_Email=(EditText)dialog.findViewById(R.id.editText_Email);

		textView_Continue.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String phone=editText_Phone.getText().toString();
				String email=editText_Email.getText().toString();
				if(phone.length()==0){
					editText_Phone.setError(baseActivity.getString(R.string.CON_ERROR_EMPTY_PHONE));
					editText_Phone.requestFocus();
					return;
				}else if (phone.length() != StringUtil.PHONE_LENGHT) {
					editText_Phone.setError(baseActivity.getString(R.string.CON_ERROR_PHONE));
					editText_Phone.requestFocus();
					return;
				}
				if (email.length()==0) {
					editText_Email.setError(baseActivity.getString(R.string.CON_ERROR_EMPTY_EMAIL));
					editText_Email.requestFocus();
					return;
				} else if(!email.matches("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$")){
					editText_Email.setError(baseActivity.getString(R.string.CON_ERROR_EMAIL));
					editText_Email.requestFocus();
					return;
				}
				Utils.hideKeyboard(baseActivity, editText_Phone);
				Utils.hideKeyboard(baseActivity, editText_Email);

				AlertMsg.showLoadingDialog(baseActivity, R.string.LOADING_SESSION);

				Condition condition=new Condition();
				condition.set(baseLayout.BaseActivity.publicPrivateTokenCIC);
				condition.Condition=1;
				condition.Email=email;
				condition.Phone=Integer.valueOf(phone);

				baseLayout.SLauncher.sendCondition(condition, callback);
				//ServiceLauncher.executeAcceptCondition(baseActivity.getBaseContext(),userLogedData.getJSONforAcceptCondition(),serviceCallback);
			}
		});
		textView_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				baseLayout.BaseActivity.dismissDialog();
			}
		});

		final String text=baseActivity.getString(R.string.CON_CONDITION);
		final String url= baseActivity.getString(R.string.CON_URL);
		Utils.setTextViewHTML(textView_Link,text, new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				baseActivity.startActivity(intent);
			}
		});

		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));

		//show the dialog over the activity
		baseActivity.showDialog(dialog);

	}
	public static void showRating(final BaseLayout baseLayout,final Promotion promotion,final float rating,final bcp.bo.service.ServiceLauncher.IService callback){
		final BaseLayoutActivity baseActivity=baseLayout.getBaseLayoutActivity();
		baseActivity.dismissDialog();

		final Dialog dialog = new Dialog(baseActivity);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_rating);

		final TextView textView_Send = (TextView)dialog.findViewById(R.id.textView_Send);
		final TextView textView_Cancel = (TextView)dialog.findViewById(R.id.textView_Cancelar);
		final RatingBar ratingBar=(RatingBar)dialog.findViewById(R.id.ratingBar);

		ratingBar.setRating(rating);

		textView_Send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertMsg.showLoadingDialog(baseActivity, R.string.SENDING_RATING);
				RatingTicket ratingTicket=new RatingTicket();
				ratingTicket.set(baseLayout.BaseActivity.publicPrivateTokenCIC);
				ratingTicket.NumberTicket=promotion.NumberTicket;
				ratingTicket.RatingTicket=String.valueOf(rating);
				baseLayout.SLauncher.sendRating(ratingTicket, callback);
			}
		});
		//LISTENER CANCEL
		textView_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				baseActivity.dismissDialog();
			}
		});

		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));

		//show the dialog over the activity
		baseActivity.showDialog(dialog);

	}
	/*private void sendCondition(){
		onBackground = false;
		// Store values at the time of the login attempt.s
		String phone=editText_Phone.getText().toString();
		String email=editText_Email.getText().toString();
		
		// Check for a valid nid.
		if (isValidPhone(phone) && isValidEmail(email)) {
			
			//If soft key board is open, close it 
			Utils.hideKeyboard(getBaseLayoutActivity(), editText_Phone);
			Utils.hideKeyboard(getBaseLayoutActivity(), editText_Email);
			
			//save dni in userlogeddata
			userLogedData.setEmail(email);
			userLogedData.setPhone(Integer.valueOf(phone));
			
			//SHOW LOADING
			AlertMsg.showLoadingDialog(getBaseLayoutActivity(), R.string.LOADING_VALIDATING_USER);
			
			//EXECUTE SERVICE
			ServiceLauncher.executeAcceptCondition(getBaseLayoutActivity().getBaseContext(),userLogedData.getJSONforAcceptCondition(),this);
			
		} else {			
			//bCicDel.setVisibility(View.GONE);
			//etCic.setTextAppearance(etCic.getContext(), R.style.loginTextCicError);
		}
	}*/
	
	/**
	 * Shows a custom Alert Dialog without title and with two buttons, Shown when an update is released.
	 * switch type of update button actions can change 
	 * @param baseActivity current activity in context
	 * @param title for the alert
	 * @param message info for user about the update 
	 * @param clickListener listener for the actions over buttons
	 * @param upperButton label 
	 * @param lowerButton label
	 * @param clickUpperButtonId upper button id
	 * @param clickLowerButtonId lower button id
	 */
	public static void showUpdateDialog(final BaseLayoutActivity baseActivity, final String title, final String message, 
			View.OnClickListener clickListener, String upperButton, String lowerButton, int clickUpperButtonId, int clickLowerButtonId, boolean closeAlertOnUpdatePressed) {	    
	
		baseActivity.dismissDialog();
		
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_update);
		
		// set the custom dialog components - title, text, image and button
		final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
	
		tvTitle.setText(title);
		
		final TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		tvMessage.setText(message);
	
		final View llButtons = dialog.findViewById(R.id.llButtons2);
		
		//show ok or ok and cancel buttons
		if(clickLowerButtonId>0){
			
			//add ok listener
			final TextView tvOk2 = (TextView)dialog.findViewById(R.id.tvOk2);
			if(upperButton!=null){
				tvOk2.setText(upperButton);
			}
			tvOk2.setId(clickUpperButtonId);
			final DialogClickListener dialogListenerOk = new DialogClickListener(baseActivity, clickListener, closeAlertOnUpdatePressed);
			tvOk2.setOnClickListener(dialogListenerOk);
			
			//add cancel listener
			final TextView tvCancel2 = (TextView)dialog.findViewById(R.id.tvCancel2);
			if(lowerButton!=null){
				tvCancel2.setText(lowerButton);
			}
			tvCancel2.setId(clickLowerButtonId);
			final DialogClickListener dialogListenerCancel = new DialogClickListener(baseActivity, clickListener);
			tvCancel2.setOnClickListener(dialogListenerCancel);
			
		}else{
			//hide double buttons
			llButtons.setVisibility(View.GONE);
			
			//add ok listener
			final TextView tvOk = (TextView)dialog.findViewById(R.id.tvOk);
			if(upperButton!=null){
				tvOk.setText(upperButton);
			}
			tvOk.setId(clickUpperButtonId);
			final DialogClickListener dialogListener = new DialogClickListener(baseActivity, clickListener);
			tvOk.setOnClickListener(dialogListener);
		}
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));
	
		//show the dialog over the activity
		baseActivity.showDialog(dialog);
		
}
	
	private static class DialogClickListener implements View.OnClickListener{

		private BaseLayoutActivity baseActivity;
		private View.OnClickListener clickListener;
		private boolean mHideModalWhenPressButton = true;
		
		public DialogClickListener(BaseLayoutActivity baseActivity, View.OnClickListener clickListener){
			this.baseActivity = baseActivity;
			this.clickListener = clickListener;
		}
		
		public DialogClickListener(BaseLayoutActivity baseActivity, View.OnClickListener clickListener, boolean hideModalWhenPressButton){
			this.baseActivity = baseActivity;
			this.clickListener = clickListener;
			mHideModalWhenPressButton = hideModalWhenPressButton;
		}
		
		@Override
		public void onClick(View v) {
			if(baseActivity!=null && mHideModalWhenPressButton){
				baseActivity.dismissDialog();
			}
			if(clickListener!=null){
				clickListener.onClick(v);
			}
		}
		
		
		
	}
	
	
	public static void showOkCancelDialog(final BaseLayoutActivity baseActivity, final String title,
			final String subTitle, final String message, final int icon, final OnClickListener ok) {
		baseActivity.dismissDialog();
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_base);
		
		// set the custom dialog components - title, text, image and button
		TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		tvTitle.setText(title);
		TextView tvSubTitle = (TextView) dialog.findViewById(R.id.tvSubTitle);
		tvSubTitle.setText(subTitle);
		
		TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
		tvMessage.setText(message);
		
		ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
		ivIcon.setImageResource(icon);

		final View accept = dialog.findViewById(R.id.vActionAccept);
		accept.setOnClickListener(ok);
		// If cancel is clicked, close the custom dialog
		final View cancel = dialog.findViewById(R.id.vActionCancel);
		cancel.setVisibility(View.VISIBLE);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				baseActivity.dismissDialog();
			}
		});
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));

		//show the dialog over the activity
		baseActivity.showDialog(dialog);
	}
	
	public static Dialog showCheckDialog(final BaseLayoutActivity baseActivity, final OnClickListener ok, final String specialMessage) {
		baseActivity.dismissDialog();
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_check);

		if (specialMessage != null && !specialMessage.equals("")){
			TextView tvMessage = (TextView)dialog.findViewById(R.id.tvMessage);
			tvMessage.setText(specialMessage);
		}
		
		final View accept = dialog.findViewById(R.id.vCheckActionAccept);
		accept.setOnClickListener(ok);
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));
		
		//show the dialog over the activity
		baseActivity.showDialog(dialog);
		
		return dialog;
	}
	
	public static Dialog showListDialog(final BaseLayoutActivity baseActivity, final String title, 
			final ListAdapter adapter, final OnItemClickListener listener) {
		baseActivity.dismissDialog();
		// Custom dialog
		final Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_list);

		// set the custom dialog components - title, list
		TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		tvTitle.setText(title);
		
		final OnItemClickListener itemListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				baseActivity.dismissDialog();
				listener.onItemClick(arg0, arg1, arg2, arg3);
			}	
		};
		
		ListView list = (ListView) dialog.findViewById(R.id.lvList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemListener);
			
		// If cancel is clicked, close the custom dialog
		final View cancel = dialog.findViewById(R.id.vActionCancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				baseActivity.dismissDialog();
			}
		});
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));

		//show the dialog over the activity
		baseActivity.showDialog(dialog);
		
		return dialog;
	}
	
	public static void showListDialog(BaseLayoutActivity baseActivity, String title, 
			ListAdapter adapter, OnItemClickListener listener, int buttonTextResourceId) {
		Dialog dialog = showListDialog(baseActivity, title, adapter, listener);
		((TextView)dialog.findViewById(R.id.tvCancel)).setText(buttonTextResourceId);
	}
	
	

	public static void showLoadingDialog(final BaseLayoutActivity baseActivity, int messageId) {
		baseActivity.dismissDialog();
		Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dlg_loading);
		
		TextView tvDialog = (TextView) dialog.findViewById(R.id.tvLoading);
		tvDialog.setText(messageId);
		final AnimationDrawable loadingAnimation = (AnimationDrawable) (tvDialog).getCompoundDrawables()[1];
		
		tvDialog.post(new Runnable(){ public void run(){
			loadingAnimation.start();
		}});
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));
		
		//show the dialog over the activity
		baseActivity.showDialog(dialog);
	}
	
	public static void showLoadingDialog(final BaseLayoutActivity baseActivity, String message) {		
		baseActivity.dismissDialog();
		Dialog dialog = new Dialog(baseActivity);
		// Prevent the system close the dialog when touch outside of it
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dlg_loading);
		
		TextView tvDialog = (TextView) dialog.findViewById(R.id.tvLoading);
		tvDialog.setText(message);
		final AnimationDrawable loadingAnimation = (AnimationDrawable) (tvDialog).getCompoundDrawables()[1];
		
		tvDialog.post(new Runnable(){ public void run(){
			loadingAnimation.start();
		}});
		
		//apply style
		StyleApp.setStyle(dialog.findViewById(R.id.llDialogGen));
		
		//show the dialog over the activity
		baseActivity.showDialog(dialog);
	}
	
	public static void showLoadingDialog(final BaseLayoutActivity baseActivity) {	
		
		showLoadingDialog(baseActivity, R.string.LOADING);

	}
	
}
