package com.bcp.bo.discounts.layouts.facebook;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.social.OnOperationCompleted;
import com.facebook.AccessToken;

import bcp.bo.service.model.response.Promotion;
//import com.facebook.widget.ProfilePictureView;


public class DscDetailFacebookDialog extends Dialog implements OnOperationCompleted {
	
	public static final String FACEBOOK_LOGIN = "FacebookLogin";
	public static final String FACEBOOK_SHARE_MEDIA = "FacebookShareMedia";
	
	/**
	 * Views.
	 */
//	private ProfilePictureView profilePictureView;
	private EditText etMessage;
    private Button btnCancel;
    private Button btnShare;
    private ImageView ivDiscount;
	
	/**
	 * Parent base layout.
	 */
	private BaseLayout _baseLayout;
	
	/**
	 * Data source.
	 */
	private Promotion _promotion;
	
	/**
	 * Listen for button clicks.
	 */
	private final View.OnClickListener clicklistener = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			click(v);
		}
	};
	
	/** 
	 * Builder
	 * @param baseLayout
	 * @param promotion
	 */
	public DscDetailFacebookDialog(BaseLayout baseLayout, final Promotion promotion) {
		super(baseLayout.getBaseLayoutActivity());
		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dlg_share_facebook);

		_baseLayout = baseLayout;
		_promotion=promotion;
		
		onResume();
	} 
	
	private void onResume(){
		
		//initialize the views
      	initViews();
		
		//initialize values
        initValues();
        
        //initialize listeners
        initListeners();
        
        //other initializes
        init();
		
	}
	
    /**
	 * Initialize views of layout: findViewById(R.id. )
	 */
	protected void initViews() {
//		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnShare = (Button) findViewById(R.id.btnShare);
		ivDiscount = (ImageView) findViewById(R.id.ivDiscount);
		
	} 
	
	/**
	 * Initialize values of fields, texts, data... v.setText(R.string. )
	 */
	protected void initValues() {
		Drawable drawable=((StartActivity)_baseLayout.getBaseLayoutActivity()).imageDownloader.getDrawable(_promotion.ImageTicketURL);
		if (drawable!=null){
			//final Drawable dImage = Drawable.createFromPath(getDiscountImageToShare());
			ivDiscount.setImageDrawable(drawable);
		}
	} 

	/**
	 * Initialize all the listeners need for the values (touches, gps, etc... )
	 */
	protected void initListeners() {
		btnCancel.setOnClickListener(clicklistener);
		btnShare.setOnClickListener(clicklistener);
	} 
	
	/**
	 * Initialize everything you need after the layout has been created
	 */
	protected void init() {
		// Active session
		if (AccessToken.getCurrentAccessToken()==null) {
			//showDialog();
			// Login
			((StartActivity)_baseLayout.getBaseLayoutActivity()).loginFacebook(this);
		} 
		else {
			showDialog();
		}
		
	} 
	
	/**
	 * Make dialog visible
	 */
	private void showDialog(){
		//show dialog
		show();
		
		//show keyboard
		etMessage.requestFocus();
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	/**
	 * Dismiss the dialog closing the keuboard
	 */
	private void dismissDialog(){
		Utils.hideKeyboard(getContext(), etMessage);
		dismiss();
	}
	

	/**
	 * Receive the events from buttons created in XML assigned as this name
	 * @param view - View clicked
	 */
	public void click(View view) {
		int id = view.getId();
		
		if(R.id.btnShare == id) {
			Utils.hideKeyboard(getContext(), etMessage);
			
			executeShareOnFacebook();
			
		} else if(R.id.btnCancel == id) {

			dismissDialog();
			
		}		
		
	} 
	
	private void executeShareOnFacebook() {
		final String message = etMessage.getText().toString() + "\n" + _baseLayout.getBaseLayoutActivity().getResources().getString(R.string.FB_DEFAULT_TEXT);
		showLoading();
		((StartActivity)_baseLayout.getBaseLayoutActivity()).publishImageAndText(message,this);
		/*if ("".compareTo(getDiscountImageToShare()) == 0) {
			((StartActivity)_baseLayout.getBaseLayoutActivity()).publishImageAndText(
					Session.getActiveSession(),
					message, 
					_baseLayout.getBaseLayoutActivity().getResources().openRawResource(R.drawable.dsc_detail_img), this);
			
		} else {
			//Drawable drawable=((StartActivity)_baseLayout.getBaseLayoutActivity()).imageDownloader.getBitmap(_promotion.ImageTicketURL);
			((StartActivity)_baseLayout.getBaseLayoutActivity()).publishImageAndText(
					Session.getActiveSession(),
					message, 
					FileManager.getFile(getDiscountImageToShare()),
					this);
		}*/
	}
	


    
	// ***********************************************
	// *
	// * CALLBACK API
	// *
	// ***********************************************
	
	public void unload() {
		hideLoading();
		dismissDialog();
	}
	
	public void showLoading() {_baseLayout.getBaseLayoutActivity().showLoading();
	}

	public void hideLoading() {_baseLayout.getBaseLayoutActivity().hideLoading();
	}

	public void updateProfileImage(final String profileId) {
//		profilePictureView.setProfileId(profileId);
		showDialog();
	}
	
	public void shareCompleted() {
		hideLoading();
		dismissDialog();
	}
	
	// ***********************************************
	// *
	// * FACEBOOK
	// *
	// ***********************************************
		
	private String getDiscountImageToShare() {
		return _promotion.ImageListURL;
	}
	
	// ***********************************************
	// *
	// * OnOperationCompleted
	// *
	// ***********************************************
	
	@Override
	public void onCompleted(String serviceName, boolean success, Object result) {
		// Facebook login
		if (serviceName.compareTo(FACEBOOK_LOGIN) == 0) {
			if (success) {
				//open dialog
				showDialog();
			} else {
				AlertMsg.showErrorDialog((StartActivity)result, R.string.FB_LOGIN_ERROR);
			}
		// Facebook share media
		} else if (serviceName.compareTo(FACEBOOK_SHARE_MEDIA) == 0) {
			
			hideLoading();
			dismissDialog();
			
			if (!success) {
				AlertMsg.showErrorDialog((BaseLayoutActivity)result, R.string.FB_SENDING_POST_ERROR);
			}
		} 
	}
	
}
