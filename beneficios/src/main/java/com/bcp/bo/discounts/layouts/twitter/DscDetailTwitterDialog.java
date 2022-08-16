package com.bcp.bo.discounts.layouts.twitter;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.managers.FileManager;
import com.bcp.bo.discounts.social.OnOperationCompleted;
import com.bcp.bo.discounts.social.TwitterApi;

import bcp.bo.service.model.response.Promotion;


public class DscDetailTwitterDialog extends Dialog implements OnOperationCompleted { 
	
	private static final int TWITTER_MAX_CHARS = 137;
	
	/**
	 * Views.
	 */
	private WebView wvLogin;
	private LinearLayout llContent;
	private EditText etMessage;
    private Button btnCancel;
    private Button btnShare;
    private TextView tvCharsCounter;
    private TextView tvFixedText;
    private ImageView ivDiscount;
//    private TextView tvUserName;
    
    /**
	 * Parent base layout.
     */
    private BaseLayout _baseLayout;
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
	 * Avoid to open links out of the app.
	 */
	private final WebViewClient webViewClient = new WebViewClient() {	
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {		
			if (url != null && url.startsWith(TwitterApi.TWITTER_CALLBACK_URL)) {
				// Get url to load an load it on webview.
				loadAccessToken(url);
				return true;
			} else {
				return false;
			}
		}
	};
	
	/**
	 * Support for progress notifications.
	 */
	private final WebChromeClient chromeClient = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int progress) {
			if (progress == 100) {
				show();
				hideLoading();
			} 
		}
	};
	
	/**
	 * Text counter watcher.
	 */
	private final TextWatcher etMessageWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// Chars counter
			tvCharsCounter.setText(String.valueOf(TWITTER_MAX_CHARS - tvFixedText.getText().length() - s.length()));
		}
		public void afterTextChanged(Editable s) {
		}
	};

	/** 
	 * Builder 
	 * @param baseLayout
	 * @param promotion
	 */
	public DscDetailTwitterDialog(BaseLayout baseLayout, Promotion promotion) {
		super(baseLayout.getBaseLayoutActivity());
		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dlg_share_twitter);
		// Prevent the system close the dialog when touch outside of it

		_baseLayout=baseLayout;
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
		wvLogin = (WebView) findViewById(R.id.wvLogin);
		llContent = (LinearLayout) findViewById(R.id.llContent);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnShare = (Button) findViewById(R.id.btnShare);
		tvCharsCounter = (TextView) findViewById(R.id.tvCharsCounter);
		tvFixedText = (TextView) findViewById(R.id.tvFixedText);
		ivDiscount = (ImageView) findViewById(R.id.ivDiscount);
//		tvUserName = (TextView) findViewById(R.id.tvUserName);
	} 
	
	/**
	 * Initialize values of fields, texts, data... v.setText(R.string. )
	 */
	@SuppressLint("SetJavaScriptEnabled")
	protected void initValues() {
		// Web view values
		wvLogin.getSettings().setJavaScriptEnabled(true); // Twitter needs it
		wvLogin.getSettings().setBuiltInZoomControls(true);
		
		// Chars counter
		tvCharsCounter.setText(String.valueOf(TWITTER_MAX_CHARS - tvFixedText.getText().length()));

		final InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(TWITTER_MAX_CHARS - tvFixedText.getText().length());
		etMessage.setFilters(filter);
		
		if ("".compareTo(getDiscountImageToShare()) != 0) {
			final Drawable dImage = Drawable.createFromPath(getDiscountImageToShare());
			ivDiscount.setImageDrawable(dImage);
		}
	} 
	
	/**
	 * Initialize all the listeners need for the values (touches, gps, etc... )
	 */
	protected void initListeners() {
		wvLogin.setWebViewClient(webViewClient);
		wvLogin.setWebChromeClient(chromeClient);
		btnCancel.setOnClickListener(clicklistener);
		btnShare.setOnClickListener(clicklistener);
		etMessage.addTextChangedListener(etMessageWatcher);
	} 
	
	/**
	 * Initialize everything you need after the layout has been created
	 */
	protected void init() {
		// Active session
		if (checkTwitterSession()) {
			wvLogin.setVisibility(View.GONE);
			llContent.setVisibility(View.VISIBLE);
//			tvUserName.setText(
//					getContext().getResources().getString(R.string.AT_SYMBOL) +
//					getTwitterScreenName());
			show();
			showKeyboard();
		} else {
			showLoading();
			// First step
			loginOnTwitter();
		}
	} 
	
	/**
	 * Show the keyboard
	 */
	private void showKeyboard(){
		
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
			executeShareOnTwitter();
		} else if(R.id.btnCancel == id) {
			dismissDialog();
		}
	} 
	
	private void executeShareOnTwitter() {
		final String message = etMessage.getText().toString() + "\n" + tvFixedText.getText().toString();
		showLoading();
		
		if ("".compareTo(getDiscountImageToShare()) == 0) {
			
			/*shareOnTwitter(
					message, 
					_baseLayout.getBaseLayoutActivity().getResources().openRawResource(R.drawable.dsc_detail_img));*/
			
		} else {
			
			shareOnTwitter( 
					message, 
					FileManager.getFile(getDiscountImageToShare()));
			
		}
		
	}
	
	private void loadAccessToken(final String url) {
		// Second step
		loadTwitterAccessToken(url, getContext());
	}
	
	// ***********************************************
	// *
	// * CALLBACK API
	// *
	// ***********************************************
	
	public void loadUrl(String url) {
		wvLogin.loadUrl(url);
	}

	public void loadUrlCompleted() {
		wvLogin.setVisibility(View.GONE);
		llContent.setVisibility(View.VISIBLE);
//		tvUserName.setText(
//				getContext().getResources().getString(R.string.AT_SYMBOL) +
//				dscDetailCtrlr.getTwitterScreenName());
		hideLoading();
		
		//show keyboard
		showKeyboard();
	}
	
	public void shareCompleted() {
		hideLoading();
		dismissDialog();
	}
	
	public void unload() {
		hideLoading();
		dismissDialog();
	}
	
	public void showLoading() {
		_baseLayout.getBaseLayoutActivity().showLoading();
	}

	public void hideLoading() {
		_baseLayout.getBaseLayoutActivity().hideLoading();
	}
	
	
	// ***********************************************
	// *
	// * TWITTER
	// *
	// ***********************************************
	
	/**
	 * Check twitter session active.
	 * @return True session active and logged in.
	 */
	public boolean checkTwitterSession() {
		return getTwitterApi().isLoggedInAlready();
	}

	/**
	 * Get user screen name.
	 * @return Twitter user screen name.
	 */
	public String getTwitterScreenName() {
		return getTwitterApi().getUserScreenName();
	}
	
	/**
	 * Share media on twitter.
	 * @param message Message to share, null for empty message.
	 * @param media Image to share, null for nothing.
	 */
	public void shareOnTwitter(final String message, final InputStream media) {
		
		getTwitterApi().share(this, message, media);
		
	}

	public void loginOnTwitter() {
		
		getTwitterApi().login(this);
		
	}

	public void loadTwitterAccessToken(String url, Context context) {

		getTwitterApi().loadAccessToken(this, url, context);	
		
	}
	
	/**
	 * Get twitter api.
	 * @return Twitter api.
	 */
	private TwitterApi getTwitterApi() {
		return ( (StartActivity)_baseLayout.getBaseLayoutActivity() ).getTwitterApi();
		
	}
		
	private String getDiscountImageToShare() {
		return _promotion.ImageURL;
	}
	
	private void loginOnTwitterFail(final String exceptionMessgae) {
		hideLoading();
		unload();
//		AlertMsg.showErrorDialog(baseLayout.getBaseLayoutActivity(), exceptionMessgae);
		AlertMsg.showErrorDialog(_baseLayout.getBaseLayoutActivity(), R.string.TWITTER_ERROR_LOGIN);
	}

	private void loginOnTwitterOk(final String url) {
		loadUrl(url);
	}
	
	private void loadTokenOnTwitterFail(final String exceptionMessage) {
		hideLoading();
		unload();
		if(TwitterApi.LOAD_ACCES_TOKEN_DENIED.compareTo(exceptionMessage) != 0) {
//			AlertMsg.showErrorDialog(baseLayout.getBaseLayoutActivity(), exceptionMessage);
			AlertMsg.showErrorDialog(_baseLayout.getBaseLayoutActivity(), R.string.TWITTER_ERROR_LOGIN);
		}		
	}

	private void loadTokenOnTwitterOk() {
		loadUrlCompleted();
	}
	
	private void shareMediaOnTwitterFail(String exceptionMessage) {
		unload();
//		AlertMsg.showErrorDialog(baseLayout.getBaseLayoutActivity(), exceptionMessage);
		AlertMsg.showErrorDialog(_baseLayout.getBaseLayoutActivity(), _baseLayout.getString(R.string.TWITTER_ERROR_POST));
	}

	private void shareMediaOnTwitterOk() {
		shareCompleted();	
	}
	
	
	// ***********************************************
	// *
	// * OnOperationCompleted
	// *
	// ***********************************************
	
	@Override
	public void onCompleted(String serviceName, boolean success, Object result) {
		// Twitter share media
		if(serviceName.compareTo(TwitterApi.TWITTER_SHARE_MEDIA) == 0) {			
			if(success) {	
				shareMediaOnTwitterOk();
			} else {
				shareMediaOnTwitterFail((String)result);
			}		
			
		// Twitter login
		} else if(serviceName.compareTo(TwitterApi.TWITTER_LOGIN) == 0) {			
			if(success) {	
				loginOnTwitterOk((String)result);
			} else {
				loginOnTwitterFail((String)result);
			}
		
		// Twitter load token
		} else if(serviceName.compareTo(TwitterApi.TWITTER_LOAD_TOKEN) == 0) {			
			if(success) {	
				loadTokenOnTwitterOk();
			} else {
				loadTokenOnTwitterFail((String)result);
			}
					
		}
		
	}
	
}
