package com.bcp.bo.discounts.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Logger;
import com.bcp.bo.discounts.layouts.facebook.DscDetailFacebookDialog;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.layouts.home.LoginLayout;
import com.bcp.bo.discounts.layouts.home.SplashLayout;
import com.bcp.bo.discounts.social.OnOperationCompleted;
import com.bcp.bo.discounts.social.TwitterApi;
import com.bcp.bo.discounts.util.ImageDownloader;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bcp.bo.service.ServiceLauncher;
import bcp.bo.service.model.request.PublicPrivateTokenCIC;
import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.PrivateToken;
import bcp.bo.service.model.response.Promotion;

public class StartActivity extends BaseLayoutActivity {

    private static final String USER_LOGED_DATA_KEY = "USER_LOGED_DATA";

    private static final String KEY_SESSION = "SESSION_DATA";
    private static final String KEY_USER = "USER_DATA";

    /**
     * Data source
     **/
    //private UserLogedData userLogedData;

    private TwitterApi twitterApi;

    //callbacks for facebook
	/*private FcbCallBack fcbCallBack = null;
	private FcbStatusCallback fcbStatusCallback = null;*/
    private CallbackManager callbackManager;

    //generic receiver
    //private List<ServiceGenericReceiver> listServiceGenericReceiver = new ArrayList<ServiceGenericReceiver>();

    public ImageDownloader imageDownloader;
    public boolean IsDebug = true;
    private String HostLoc = "http://172.31.12.98:46468/Service.svc";
    //	private String HostDev="http://172.31.164.169/Beneficios.Extranet/Service.svc";
    private String HostDev = "http://172.31.164.117:3396/Beneficios.Extranet/Service.svc";
    private String HostExt = "http://186.121.207.166/Beneficios.Extranet/Service.svc";
    //private String HostPro="http://www2.bcp.com.bo/Beneficios.Extranet2/Service.svc"; // PROD ANTES DE 20200129
    //private String HostPro="http://172.31.12.88/Beneficios/srvExtranet/Service.svc"; // DEV
    private String HostPro="http://www2.bcp.com.bo/Beneficios.Extranet3/Service.svc"; // PROD DESDE 20200129

    public ServiceLauncher SLauncher = new ServiceLauncher(HostPro);

    public PrivateToken privateToken = new PrivateToken();
    public PublicPrivateTokenCIC publicPrivateTokenCIC = new PublicPrivateTokenCIC();
    public String document;
    public String amountSaved;
    public List<Category> categories = new ArrayList<>();
    public List<Commerce> commerces = new ArrayList<>();
    public List<Promotion> promotions = new ArrayList<>();

    @Override
    protected int getXMLToInflate() {
        return R.layout.act_start;
    }

    @Override
    protected void dataReceived(Bundle extras) {
        if (extras != null) {
            //REVISAR
            //userLogedData = (UserLogedData) extras.getSerializable(USER_LOGED_DATA_KEY);
        }
    }

    @Override
    protected void dataToSave(Bundle extras) {
        //REVISAR
        //extras.putSerializable(USER_LOGED_DATA_KEY, userLogedData);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initBaseLayouts() {
        //REVISAR
		/*if(userLogedData==null){
			userLogedData = new UserLogedData();//MockUtilities.getUserLoggerData();
		}*/
		/*BaseLayout baseLayout = new DetailItemActive(this);
		((DetailItemActive)baseLayout).setDiscount(Promotion.init());
		((DetailItemActive)baseLayout).initWithOnResume();
		this.loadLayout(R.id.rlGenStart, baseLayout, false);*/


		/*try {
			String JSONDirections="{\"geocoded_waypoints\":[{\"geocoder_status\":\"OK\",\"place_id\":\"EjBNZXJjYWRvIDEzMzUsIE51ZXN0cmEgU2XDsW9yYSBkZSBMYSBQYXosIEJvbGl2aWE\",\"types\":[\"street_address\"]},{\"geocoder_status\":\"OK\",\"place_id\":\"EjBNZXJjYWRvIDEzMzUsIE51ZXN0cmEgU2XDsW9yYSBkZSBMYSBQYXosIEJvbGl2aWE\",\"types\":[\"street_address\"]}],\"routes\":[{\"bounds\":{\"northeast\":{\"lat\":-16.4981307,\"lng\":-68.1333902},\"southwest\":{\"lat\":-16.4982071,\"lng\":-68.13352019999999}},\"copyrights\":\"Map data ©2015 Google\",\"legs\":[{\"distance\":{\"text\":\"16 m\",\"value\":16},\"duration\":{\"text\":\"1 min\",\"value\":5},\"end_address\":\"Mercado 1335, La Paz, Bolivia\",\"end_location\":{\"lat\":-16.4981307,\"lng\":-68.13352019999999},\"start_address\":\"Mercado 1335, La Paz, Bolivia\",\"start_location\":{\"lat\":-16.4982071,\"lng\":-68.1333902},\"steps\":[{\"distance\":{\"text\":\"16 m\",\"value\":16},\"duration\":{\"text\":\"1 min\",\"value\":5},\"end_location\":{\"lat\":-16.4981307,\"lng\":-68.13352019999999},\"html_instructions\":\"Head <b>northwest</b>\",\"polyline\":{\"points\":\"xhucBthz~KOX\"},\"start_location\":{\"lat\":-16.4982071,\"lng\":-68.1333902},\"travel_mode\":\"DRIVING\"}],\"via_waypoint\":[]}],\"overview_polyline\":{\"points\":\"xhucBthz~KOX\"},\"summary\":\"\",\"warnings\":[],\"waypoint_order\":[]}],\"status\":\"OK\"}";
			JSONObject JsonResult = new JSONObject(JSONDirections.toString());
			Directions direction= Sender.fromJSON(JsonResult.toString(), new TypeReference<Directions>() {});

			if(direction!=null){
				Logger.Info(direction.Status);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}*/


        //ADD FIRST LAYOUT SPLASH
        SplashLayout splashLayout = new SplashLayout(this);
        this.loadLayout(R.id.rlGenStart, splashLayout, false);

        //ADD FIRST LAYOUT SPLASH
		/*privateToken.FullName="EDSON DAVID";
		privateToken.LastName="MAMANI CHUQUIMIA";
		HomeLayout homeLayout=new HomeLayout(this,new ArrayList<Category>(),privateToken,"8334657 LP","50.23 Bs.");
		this.loadLayout(R.id.rlGenStart, homeLayout, false);*/

        //ADD FIRST LAYOUT SPLASH
		/*LoginLayout loginLayout=new LoginLayout(this);
		this.loadLayout(R.id.rlGenStart, loginLayout, false);*/
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void init() {
        twitterApi = getTwitterApi();
    }

    @Override
    public void click(View view) {

    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, boolean afterFragment) {
    }

    /**
     * Receive the events from other activities
     *
     * @param requestCode - Request code that the activity sent
     * @param resultCode  - Result code of the activity
     * @param data        - Intent with data returned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//		if (requestCode == FACEBOOK_LOGIN_REQUEST_CODE) {
        // Set/Update facebook session
//			getFacebookApi().refreshSession(this, requestCode, resultCode, data);
        Logger.Tag("START ACTIVITY");
        Logger.Info(requestCode + "|" + resultCode + "|" + data.toString());
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //LoginManager.getInstance().
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//		}

    }

    @Override
    protected boolean keyPressed(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void showLoading() {
        AlertMsg.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {

        dismissDialog();
    }

    @Override
    public void setLoadingMessage(int messageId) {
        if (this.dialog != null) {
            TextView tvDialog = (TextView) dialog.findViewById(R.id.tvLoading);
            tvDialog.setText(messageId);
        }
    }

    public TwitterApi getTwitterApi() {
        if (twitterApi == null) {
            twitterApi = new TwitterApi();
        }
        return twitterApi;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //-------------------- Generic receivers --------------------
	
	/*public List<ServiceGenericReceiver> getListServiceGenericReceivers(){
		return listServiceGenericReceiver;
	}*/

    public void goToSplashForced() {


        BaseLayout layout = findBaseLayoutSonByName(HomeLayout.class);

        if (layout == null) {
            layout = findBaseLayoutSonByName(LoginLayout.class);
        }

        if (layout == null) {
            layout = findBaseLayoutSonByName(SplashLayout.class);
        }

        layout.setAnimationLoadHideId(BaseLayout.ANIM_NONE);

        //REVISAR
        //userLogedData.clearUserData();

        //add the first layout
        SplashLayout splashLayout = new SplashLayout(this);
        splashLayout.setAnimationLoadShowId(BaseLayout.ANIM_NONE);
        this.loadLayout(R.id.rlGenStart, splashLayout, true);

    }

    //------------- OVERRIDE LIFECYCLE ------------

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(StartActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(StartActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        //create new instance of status callback
      	/*fcbStatusCallback = new FcbStatusCallback(this, null);
        
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, fcbStatusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
//            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//            	initSession(session);
//            }
        }*/

        imageDownloader = new ImageDownloader(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Session.getActiveSession().addCallback(fcbStatusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Session.getActiveSession().removeCallback(fcbStatusCallback);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*Session session = Session.getActiveSession();
        Session.saveSession(session, outState);*/
    }

	/*private void initSession(Session session){
    	if (!session.isOpened() && !session.isClosed()) {
    		session.openForRead(new Session.OpenRequest(this).setCallback(fcbStatusCallback).setRequestCode(FACEBOOK_LOGIN_REQUEST_CODE));
        } else {
            Session.openActiveSession(this, true, fcbStatusCallback);
        }
    }
	
	public void logoutFacebook(){
		Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
	}

	public void loginFacebook(OnOperationCompleted operationCompleted){
		
		//create new instance of status callback
		fcbStatusCallback.operationCompleted = operationCompleted;
		
		initSession(Session.getActiveSession());
	}
	
	public void restartFacebookSession(){
		Session session = Session.getActiveSession();
		if(session.isOpened()){
//			session.closeAndClearTokenInformation();
		}
		session = new Session(this);
		Session.setActiveSession(session);
	}
	
	public void publishImageAndText(Session session, final String message, final InputStream isImage, OnOperationCompleted operationCompleted){
		
		//save listener
		fcbCallBack = new FcbCallBack(this, operationCompleted);
		
		//prepare data to publish
    	Bundle params = new Bundle();
	    params.putString("message", message);
	    params.putByteArray("picture", Utils.getBytes(isImage));
        
	    try {
	        Request request = new Request(session, "me/photos", params, HttpMethod.POST, fcbCallBack);
	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();

	    } catch (Exception e) {
	    	if(operationCompleted!=null){
	    		operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, this);
	    	}
	    }
    }*/


    /*private class FcbCallBack implements Request.Callback{
        private StartActivity startActivity = null;
        private OnOperationCompleted operationCompleted;

        private FcbCallBack(StartActivity startActivity, OnOperationCompleted operationCompleted){
            this.startActivity = startActivity;
            this.operationCompleted = operationCompleted;
        }

        @Override
        public void onCompleted(Response response) {
            if (response==null || response.getGraphObject() == null || response.getError()!=null) {
                if(operationCompleted!=null){
                    operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, startActivity);
                }
            }else{
                if(operationCompleted!=null){
                    operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, true, startActivity);
                }
            }

            startActivity.fcbCallBack = null;
        }

    }*/
    public void loginFacebook(OnOperationCompleted operationCompleted) {
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        //create new instance of status callback
		/*fcbStatusCallback.operationCompleted = operationCompleted;
		initSession(Session.getActiveSession());*/
    }

    public void logoutFacebook() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }

    public void publishImageAndText(final String message, final OnOperationCompleted onOperationCompleted) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.dsc_detail_img);

        //save listener
        //fcbCallBack = new FcbCallBack(this, operationCompleted);

        //prepare data to publish
        Bundle params = new Bundle();
        params.putString("link", "http://stackoverflow.com/");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        //params.putByteArray("picture", Utils.getBytes(byteArrayInputStream));
        if (AccessToken.getCurrentAccessToken().getPermissions().contains("public_profile")) {
            try {
                GraphRequest request = GraphRequest.newPostRequest(AccessToken.getCurrentAccessToken(), "me/feed", null, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        FacebookRequestError error = response.getError();
                        if (error == null) {
                            Logger.Info(response.toString());
                            if (onOperationCompleted != null) {
                                onOperationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, true, this);
                            }
                        } else {
                            Logger.Info(error.getErrorMessage());
                            if (onOperationCompleted != null) {
                                onOperationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, this);
                            }
                        }
                    }
                });
                request.setParameters(params);
                request.executeAsync();
            } catch (Exception e) {
                if (onOperationCompleted != null) {
                    onOperationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, this);
                }
            }
        } else {
            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            //onOperationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, this);
        }
    }
    //-------------- FACEBOOK --------------
/*
	private static final int FACEBOOK_LOGIN_REQUEST_CODE = 654;

	private class FcbStatusCallback implements AccessToken.StatusCallback{

		private StartActivity startActivity = null;
		private OnOperationCompleted operationCompleted;

		private FcbStatusCallback(StartActivity startActivity, OnOperationCompleted operationCompleted){
			this.startActivity = startActivity;
			this.operationCompleted = operationCompleted;
		}

		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if(exception!=null){
				if(operationCompleted!=null){
					operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_LOGIN, false, startActivity);
				}
				return;
			}
			if(session.isOpened()){
				if(!session.getPermissions().contains("publish_actions")){
					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(startActivity, Arrays.asList("publish_actions")));
					return;

				}else{

					//call to operation complete
					if(operationCompleted!=null){
						operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_LOGIN, true, startActivity);
						operationCompleted = null;
					}
				}
			}


		}

	}

	private class FcbCallBack implements Request.Callback{
		private StartActivity startActivity = null;
		private OnOperationCompleted operationCompleted;

		private FcbCallBack(StartActivity startActivity, OnOperationCompleted operationCompleted){
			this.startActivity = startActivity;
			this.operationCompleted = operationCompleted;
		}

		@Override
		public void onCompleted(Response response) {
			if (response==null || response.getGraphObject() == null || response.getError()!=null) {
				if(operationCompleted!=null){
					operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, false, startActivity);
				}
			}else{
				if(operationCompleted!=null){
					operationCompleted.onCompleted(DscDetailFacebookDialog.FACEBOOK_SHARE_MEDIA, true, startActivity);
				}
			}

			startActivity.fcbCallBack = null;
		}

	}*/
}
