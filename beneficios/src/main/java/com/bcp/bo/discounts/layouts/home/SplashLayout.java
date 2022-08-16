package com.bcp.bo.discounts.layouts.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcp.bo.discounts.R;

import bcp.bo.service.Sender;
import bcp.bo.service.ServiceLauncher.IService;
import bcp.bo.service.model.request.Application;
import bcp.bo.service.model.response.Response;

import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.managers.FileManager;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

public class SplashLayout extends BaseLayout implements IService {
	
	/**
	 * Session images updated flag.
	 */
	private static final String PREFS_SESSION_IMAGES_UPDATED = "session_images_updated";
	
	/** Key to catch ok button in dialogs **/
	private static final int DIALOG_RETRY_ID = 100;
	private static final int DIALOG_EXIT_ID = 101;
	private static final int DIALOG_OPTIONAL_UPDATE_ID = 102;
	private static final int DIALOG_MANDATORY_UPDATE_ID = 103;
	private static final int DIALOG_CALL_ID = 104;
	private static final int DIALOG_CONTINUE_ID= 105;
	
	/** Type of updates **/
	private static final int MANDATORY_UPDATE = 1;
	private static final int OPTIONAL_UPDATE = 2;
	private static final int NO_UPDATE_AVAILABLE = 0;
	
	//views
	private ImageView imageView_Loading;
	private TextView textView_Loading;
	
	/** Flag to know if it is loading **/
	private boolean isLoading = false;
	
	private FileManager fileManager;
	
	public SplashLayout(BaseLayoutActivity baseLayoutActivity) {
		super(baseLayoutActivity);
		
		fileManager = new FileManager();
	}

	@Override
	protected int getXMLToInflate() {
		return R.layout.lay_splash;
	}

	@Override
	protected void initViews() {
		imageView_Loading = (ImageView) view.findViewById(R.id.ivLoading);
		textView_Loading = (TextView) view.findViewById(R.id.tvLoading);
	}

	@Override
	protected void initBaseLayouts() {
	}

	@Override
	protected void initValues() {
		//userLogedData = ((StartActivity)getBaseLayoutActivity()).getUserLogedData();
	}

	@Override
	protected void initListeners() {
	}

	@Override
	protected void init() {
        initAuthentication();
	}
    private void initAuthentication(){
        showLoading();
        textView_Loading.setText(R.string.LOADING_SESSION);

        Application application=new Application("E77529484D5E9C987BC4AA86BFCBED","F6646F051B48D2C166CEC3DDF09BA216D2DC7A4C44");	//PROD
		//Application application=new Application("1929AD7A507A8866E01E","1929AD0A0C2D");	//DEV
        SLauncher.authenticate(application,this);
    }
	@SuppressWarnings("ResourceType")
	@Override
	public void click(View view) {
		final int id=view.getId();
		if(id==DIALOG_RETRY_ID){
			initAuthentication();
		}else if(id==DIALOG_EXIT_ID){
			getBaseLayoutActivity().finish();
		}/*else if(id==DIALOG_CALL_ID){
			callBcp(mAuxLoginSession.getContactPhone());
			mAuxLoginSession = null;
			getBaseLayoutActivity().finish();
		}else if(id==DIALOG_OPTIONAL_UPDATE_ID){
			goToStore(mAuxLoginSession.getStoreUrl());
			//sessionReceived(mAuxLoginSession);
			mAuxLoginSession = null;
		}else if(id==DIALOG_MANDATORY_UPDATE_ID){
			goToStore(mAuxLoginSession.getStoreUrl());
		}else if(id==DIALOG_CONTINUE_ID){
			mAuxLoginSession = null;
		}*/
	}

	private void goToStore(String url) {
		Utils.openStore(getBaseLayoutActivity(), url);
	}

	private void callBcp(String tel) {
		Utils.systemPhoneCall(getBaseLayoutActivity(), tel);
	}

	@Override
	protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {
	}

	@Override
	protected void dataReceived(Bundle extras) {
	}

	@Override
	protected void dataToSave(Bundle extras) {
		
	}

	@Override
	protected void dataToReturn(Bundle extras) {
		
	}

	@Override
	protected boolean keyPressed(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && isLoading){
			return true;
		}
		return false;
	}

	@Override
	public boolean unloadLayoutResult(Bundle data) {
		return false;
	}
	
	private void showLoading(){
		//deactivate flag
		isLoading = true;
		//show loading animation
		Utils.startAnimatedImage(imageView_Loading);
	}
	
	private void hideLoading(){
		//deactivate flag
		isLoading = false;
		//stop loading animation
		Utils.stopAnimatedImage(imageView_Loading);
	}
	/**
	 * Checks if exists any update and show the advice if necessary, 
	 * returns false if app cannot continue from this point in advance.  
	 * @param session LoginSession object response
	 * @return false if app cannot continue from this point in advance true otherwise.
	 */
	/*private boolean checkUpdate(LoginSession session) {
		switch (session.getUpdateType()) {
		case NO_UPDATE_AVAILABLE:
			return true;
		case OPTIONAL_UPDATE:
			mAuxLoginSession = session;
			showOptionalUpdateAlert(session);
			return false;
		case MANDATORY_UPDATE:
			mAuxLoginSession = session;
			showMandatoryUpdateAlert(session);
			return false;
		default:
			return true;
		}		
	}*/

	@Override
	public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        super.onCompleted(status, message, service, jsonObject);
        hideLoading();
        if(status){
            Response<String> responseAuthToken= Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<String>>() {
            });
            if(responseAuthToken.State){//CONFORME
                BaseActivity.publicPrivateTokenCIC.PublicToken=responseAuthToken.Data;
                //SHOW LOGIN
                LoginLayout loginLayout = new LoginLayout(getBaseLayoutActivity());
                loginLayout.setAnimationLoadShowId(ANIM_NONE);
                setAnimationLoadHideId(ANIM_NONE);
                replaceLayout(loginLayout, true);
            }else{//NO CONFORME
                AlertMsg.showErrorWarningDialog(
                        getBaseLayoutActivity(),
                        responseAuthToken.Message,
                        true, clickListener,
                        getString(R.string.RETRY),
                        getString(R.string.EXIT),
                        DIALOG_RETRY_ID,
                        DIALOG_EXIT_ID,
                        false);
            }
        }
        else{//ERROR DE CONEXION
            AlertMsg.showErrorWarningDialog(
                    getBaseLayoutActivity(),
                    message,
                    true, clickListener,
                    getString(R.string.RETRY),
                    getString(R.string.EXIT),
                    DIALOG_RETRY_ID,
                    DIALOG_EXIT_ID,
                    false);
        }
	}

	@Override
	public void onCancelled() {

	}
}