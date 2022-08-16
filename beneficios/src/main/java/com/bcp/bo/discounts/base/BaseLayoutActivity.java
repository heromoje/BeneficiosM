package com.bcp.bo.discounts.base;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.bcp.bo.discounts.general.Constants;
import com.bcp.bo.discounts.general.StyleApp;
import com.bcp.bo.discounts.util.ImageDownloader;

public abstract class BaseLayoutActivity extends AppCompatActivity {
	
	private static final int STATE_INITLAYOUTS_ONCREATE = 0;
	private static final int STATE_INITLAYOUTS_ONRESUME = 1;
	private static final int STATE_INITLAYOUTS_ONRESTORE = 2;
	private static final int STATE_INITLAYOUTS_ONSAVED = 3;
	
	private static final boolean BLOCK_VIEW_ACTIVATED = true;
	
	/** Toast to show or hide **/
	protected Toast toast = null;
	
	/** Key used to save the fragment result **/
	private static final String KEY_LAYOUT_RESULT_ID = "LAYOUTT_RESULT_ID";
	
	/** Key used to save the list of fragments **/
	private static final String KEY_LAYOUT_DATA = "LAYOUT_DATA";
	
	/** Fragment where to call when an activity call to onactivityresult **/
	protected String layoutResultTag = null;
	
	/** List of fragment data in the stack **/
	protected BaseLayoutData data = new BaseLayoutData();
	
	/** Flag to know that we should create the fragments **/
	private int initLayouts = STATE_INITLAYOUTS_ONCREATE;
	
	private StyleApp styleApp = null;
		
	/** FrameLayout parent of all **/
	private FrameLayout flGen = null;
	
	/** View to block screen during animations **/
	private View vBlock = null;
	private int blockCounter = 0;
	
	/** List of views over all the activity **/
	private List<BaseLayout> baseLayoutModals = new ArrayList<BaseLayout>();
	
	/** Next dialog to show **/ 
	protected Dialog dialog = null;

	public ImageDownloader imageDownloader;
	
	/**
	 * Construct of this class
	 * @param savedInstanceState - data
	 */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set style app listener
        styleApp = new StyleApp();
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        viewGroup.setOnHierarchyChangeListener(styleApp);
        
        //create a FrameLayout and add it to windowManager
        flGen = new FrameLayout(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); 
        flGen.setLayoutParams(params);
//        getWindowManager().addView(flGen, params);
        setContentView(flGen);
        
        //add the xml to inflate to general view
        LayoutInflater.from(this).inflate(this.getXMLToInflate(), flGen);
        
//        //associate XML
//        setContentView(this.getXMLToInflate());
        
        //get data
        this.dataReceived(getIntent().getExtras());
		
        //initialize the data
        this.data.layBaseActivity = this;
        
        //create block view
        if(BLOCK_VIEW_ACTIVATED){
        	createBlockView();
        }
        
        //initialize views
        initViews();
        
        //flag to know that we have to create the fragments from initViews
        this.initLayouts = STATE_INITLAYOUTS_ONRESUME;
        
    }
    
    private void createBlockView(){
    	
    	vBlock = new View(this);
//    	vBlock.setBackgroundColor(Color.argb(125, 0, 255, 0));
    	vBlock.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	vBlock.setOnTouchListener(blockTouchListener);
    	
    	//add the layout
    	flGen.addView(vBlock);
    	
    }
    
    /**
	 * Must return the activity to inflate, the XML given by the R.layout.
	 * @return R.layout. associated to this layout
	 */
	protected abstract int getXMLToInflate();
    
    /**
	 * Initialize views of layout: findViewById(R.id. )
	 */
	protected abstract void dataReceived(Bundle extras);
	
	/**
	 * Must save the data required to be received when activity is resumed
	 * extras.putString(...)
	 * @param extras
	 */
	protected abstract void dataToSave(Bundle extras);
    
    /**
	 * Initialize views of layout: findViewById(R.id. )
	 */
	protected abstract void initViews();
	
	/**
	 * Initialize the base layouts, add all base layouts here
	 */
	protected abstract void initBaseLayouts();
	
	/**
	 * Initialize values of fields, texts, data... v.setText(R.string. )
	 */
	protected abstract void initValues();
	
	/**
	 * Initialize all the listeners need for the values (touches, gps, etc... )
	 */
	protected abstract void initListeners();
	
	/**
	 * Initialize everything you need after the layout has been created
	 */
	protected abstract void init();
	
	/**
	 * Receive the events from buttons created in XML assigned as this name
	 * @param view - View clicked
	 */
	public abstract void click(View view);
	
	/**
	 * Click listener to show with buttons
	 */
	protected View.OnClickListener clickListener = new View.OnClickListener() { 
		
		@Override
		public void onClick(View arg0) {
			click(arg0);
		}
	};
	
    
    /**
     * Handler instance
     */
    protected Handler handler = new ActHandler(this);
    
    /**
     * Handler of the activity
     */
	public static class ActHandler extends Handler {
		private BaseLayoutActivity activity;

		private ActHandler(BaseLayoutActivity baseLayoutActivity) {
			activity = baseLayoutActivity;
		}

		@Override
		public void handleMessage(Message msg) {
			// send this message as received
			activity.handleMessageReceived(msg.what, msg.arg1, msg.arg2, msg.obj);
		}
	};
    
    /**
     * Receive the messages sent to main handler of LLayBase
     * @param what - what data from message
     * @param arg1 - value of arg1 from message
     * @param arg2 - value of arg2 from message
     * @param obj - object received of message
     */
    protected abstract void handleMessageReceived(int what, int arg1, int arg2, Object obj);
	
    /**
     * Send a message to this activity, calling to handleMessageReceived
     * @param msg Message to send to the handler
     */
    public void sendMessage(Message msg){
    	
    	//send this message as received
		handleMessageReceived(msg.what, msg.arg1, msg.arg2, msg.obj);
    	
    }
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	Constants.initUrlsAndEnvironments(this);
    	
    	if(this.initLayouts>STATE_INITLAYOUTS_ONCREATE){
    		
	    	//initialize fragments if we are not loading them
	    	if(this.initLayouts==STATE_INITLAYOUTS_ONRESTORE){
	    		this.loadLayoutsFromData(this.data);
	    	}else if(this.initLayouts==STATE_INITLAYOUTS_ONRESUME){
	    		initBaseLayouts();
	    	}else if(this.initLayouts==STATE_INITLAYOUTS_ONSAVED){
	    		this.data.updateStateForAllTree(BaseLayoutData.STATE_LOADED, BaseLayoutData.STATE_ADDED);
	    		this.data.updateStateForAllTree(BaseLayoutData.STATE_REPLACED_SON, BaseLayoutData.STATE_LOADED);
	    	}
	    	this.initLayouts = STATE_INITLAYOUTS_ONRESUME;
	    	    	
	    	//initialize values
	        initValues();
	        
	        //initialize listeners
	        initListeners();
	        
	        //other initializes
	        init();
	        
    	}else{
    		this.data.updateStateForAllTree(BaseLayoutData.STATE_LOADED, BaseLayoutData.STATE_ADDED);
    		this.data.updateStateForAllTree(BaseLayoutData.STATE_REPLACED_SON, BaseLayoutData.STATE_LOADED);
    	}
    }
    
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	//initialize the flag to rebuild layouts
    	this.initLayouts = 0;
    	    	
    }
    
    /**
     * Save the instance of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle){
    	
    	//add the fragmet to respond
    	if(this.layoutResultTag!=null){
    		bundle.putString(KEY_LAYOUT_RESULT_ID, this.layoutResultTag);
    	}
    	
    	//clone the data to save
    	this.data.updateDataToSave(false);
    	
    	//save data fragments updated
    	bundle.putParcelable(KEY_LAYOUT_DATA, this.data);
    	
    	this.dataToSave(bundle);
    	
    	this.initLayouts = STATE_INITLAYOUTS_ONSAVED;
    }
    
    /**
     * Restore de instance of the activity, calling getBundleData, initValues, initListeners and init
     */
    @Override
    protected void onRestoreInstanceState(Bundle bundle){
    	
    	if(bundle==null){
    		return;
    	}
    	
    	//get the fragmet to respond
    	if(bundle.containsKey(KEY_LAYOUT_RESULT_ID)){
    		this.layoutResultTag  = bundle.getString(KEY_LAYOUT_RESULT_ID);
    	}
    	
    	//get the list of fragments
    	if(bundle.containsKey(KEY_LAYOUT_DATA)){
    		this.data = bundle.getParcelable(KEY_LAYOUT_DATA);
            this.data.layBaseActivity = this;
    		
    		//not init fragments
    		this.initLayouts = STATE_INITLAYOUTS_ONRESTORE;
    		
    	}
    	
    	this.dataReceived(bundle);
    	
    }
    
    //--------- Play with activities---------
    
    /**
     * Load an activity from a fragment to call it when returning
     * @param activityDestiny - Class<?> of the activity to go
     * @param bundle - Bundle to attach to the new activity
     * @param requestCode - int request code that will be returned by activity
     * @param fragmentResult - Fragment to be called when activity returns
     */
    public void startActivity(Class<?> activityDestiny, Bundle bundle, int requestCode, Fragment fragmentResult){
    	
    	//save the fragment result
    	if(fragmentResult!=null){
    		this.layoutResultTag = fragmentResult.getTag();
    	}
    	
    	//create intent
    	Intent intent = new Intent(this, activityDestiny);
    	if(bundle!=null){
    		intent.putExtras(bundle);
    	}
    	
    	//start activity
    	startActivityForResult(intent, requestCode);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	
//    	boolean fragmentCalled = false;
//    	
//    	//check if there is a fragment for result
//    	if(this.layoutResultTag!=null){
//    		
//    		//get the fragment
//    		FragmentManager fragmentManager = this.getSupportFragmentManager();
//    		Fragment fragment = fragmentManager.findFragmentByTag(layoutResultTag);
//    		
//    		BaseLayout baseLayout = (BaseLayout) findViewById()
//    		
//			//call to activity result of fragment
//			baseLayout.onActivityResult(requestCode, resultCode, data);
//			
//			//call to onactivity result for the activity
//	    	fragmentCalled = true;
//    		
//    	}
//    	
//    	//call to onactivity result for the activity
//    	this.onActivityResult(requestCode, resultCode, data, fragmentCalled);
//    	
//    	//remove the fragment result
//    	this.layoutResultTag = null;
    }
    
    /**
     * Receive the events from other activities
     * @param requestCode - Request code that we sent
     * @param resultCode - Result code of the activity
     * @param data - Intent with data returned
     * @param afterFragment - boolean if this was called after call a fragment
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data, boolean afterFragment);
    
    
    //--------- Key events ---------
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

    	//check if it s doing something to block key events
    	if(blockCounter>0){
    		return true;
    	}
    	
    	//first, check if modal to hide it
    	if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
			if(baseLayoutModals.size()>0){
				if(baseLayoutModals.get(baseLayoutModals.size()-1).canHideModal()){
					baseLayoutModals.get(baseLayoutModals.size()-1).hideModal();
				}
				return true;
			}
    	}
    	
    	//ask to all the baselayouts in the stack if want to catch the key back event
    	BaseLayoutData lastBaseLayoutData = this.data.getLastLayoutData(false);
    	if((lastBaseLayoutData!=null && lastBaseLayoutData.checkKeyPressed(keyCode, event)) || this.keyPressed(keyCode, event)){
    		return true;
    	}else{
    	 	if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
    	 		
    	 		lastBaseLayoutData = this.data.getLastLayoutData(true);
    	 		
    	 		//unload the last fragment
    	 		if(lastBaseLayoutData!=null){
    	 			
    	 			//get the bundle to return
    	 			Bundle bundle = new Bundle();
    	 			lastBaseLayoutData.layBase.dataToReturn(bundle);
    	 			
    	 			//unload
    	 			this.unloadLayoutData(lastBaseLayoutData, bundle);
    	 			
    	 			//return true because we have done the back
    	 			return true;
    	 		}
    		}
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Send a key event and receive if the event has been consumed or not
     * @param keyCode - code of the key
     * @param event - event of the key (up, down, ...)
     * @return TRUE if event consumed, FALSE otherwise
     */
    protected abstract boolean keyPressed(int keyCode, KeyEvent event);
    
    //--------- Loading ---------
    
    /**
     * Show a loding message
     */
    public abstract void showLoading();
    
    /**
     * Show a loding message
     */
    public abstract void hideLoading();
    
    
    //---------- Toast ----------
    
    /**
     * Show a toast on screen
     * @param text - String with text to show
     * @param duration - int duration: Toast.LENGTH_SHORT, Toast.LENGTH_LONG
     */
    public void showToast(String text, int duration){
    	
    	//create or stop the actual toast
    	if(toast==null){
    		toast = new Toast(getApplicationContext());
    	}else{
    		toast.cancel();
    	}
    	
    	//set the text and duration
    	toast.setText(text);
    	toast.setDuration(duration);
    	
    	//show it
    	toast.show();
    }
    
    
    //--------- Fragments ---------
        
    /**
     * Load a layout from the id container, this is only valid for activity
     * @param idContainer - int with id of container
     * @param layoutToShow - BaseLayout to show
     * @param cleanStack - boolean with true if we want to clean the stack of container after adding
     */
    public void loadLayout(int idContainer, BaseLayout layoutToShow, boolean cleanStack){
    	FrameLayout layoutContainer = (FrameLayout) findViewById (idContainer);
    	loadLayout(this.data, layoutContainer, layoutToShow, cleanStack);
    }
    
    /**
     * Load a Layout
     * @param dataParent - BaseLayoutData of the parent
     * @param layoutContainer - FrameLayout container where to add the layout
     * @param layBase - BaseLayout to add or replace, can be null but will do nothing
     * @param cleanStack - boolean with true if we want to clean the stack of container after adding
     */
    @SuppressLint("NewApi") 
    protected void loadLayout(BaseLayoutData dataParent, FrameLayout layoutContainer, BaseLayout layoutToShow, boolean cleanStack){
    	
    	//check layout null
    	if(layoutToShow==null || layoutContainer==null){
    		return;
    	}
    	
    	//set the id of container
    	layoutToShow.data.idContainer = layoutContainer.getId();
    	layoutToShow.data.dataParent = dataParent;
    	
    	//get data to hide
    	BaseLayoutData dataToHide = BaseLayoutActivity.getLayoutDataInContainer(dataParent, layoutContainer.getId());
    	
    	//check these layouts are already being animated
    	if(dataToHide!=null){
	    	if(layoutToShow.data.animating || dataToHide.animating){
	    		return;
	    	}
	    	
	    	//mark flag as animating to block other load  or unloads
	    	layoutToShow.data.animating = true;
	    	dataToHide.animating = true;
    	}
    	
    	//show block
	    showBlock();
    	
    	//add the fragment data to the list
    	if(layoutToShow.data.state==BaseLayoutData.STATE_CREATED){
    		dataParent.listLayBaseData.add(layoutToShow.data);
    	}
    	
    	//add the fragment
    	if((dataToHide!=null || layoutToShow.data.forceAnimation)  && layoutToShow.data.state!=BaseLayoutData.STATE_LOADED && layoutToShow.data.state!=BaseLayoutData.STATE_REPLACED_SON){
    		
    		if(dataToHide!=null){
	    		//update state of fragment we will hide
	    		dataToHide.state = BaseLayoutData.STATE_REPLACED;
	    		dataToHide.updateDataToSave(false);
	    		dataToHide.layBase.onDestroy();
    		}
    		
    		layoutToShow.onResume();
    		if(layoutToShow.view.getParent()==null){
    			layoutContainer.addView(layoutToShow.view);
    		}
    		
    		runAnimations(true, layoutContainer, layoutToShow.data, dataToHide, cleanStack);
    		
    	}else{
    		
    		if(dataToHide!=null && layoutToShow.data.state!=BaseLayoutData.STATE_LOADED){
//    			layoutContainer.removeAllViews();
    			layoutContainer.removeView(dataToHide.layBase.view);
    			dataToHide.updateDataToSave(false);
    			dataToHide.layBase.onDestroy();
    			dataParent.listLayBaseData.remove(dataToHide);
    			dataToHide.unloadData();
    			dataToHide = null;
    		}
    		
    		layoutToShow.onResume();
    		if(layoutContainer.indexOfChild(layoutToShow.view)==-1){
    			layoutContainer.addView(layoutToShow.view);
    		}
    		
    		//hide block
    		hideBlock();
    		
    		layoutToShow.data.animating = false;
    		if(dataToHide!=null){
    			dataToHide.animating = false;
    		}
    		
    	}
    	
    	//add layoutparams
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		layoutToShow.view.setLayoutParams(layoutParams);
    	
//    	//add the fragment data to the list
//    	if(layoutToShow.data.state==BaseLayoutData.STATE_CREATED){
//    		dataParent.listLayBaseData.add(layoutToShow.data);
//    	}
    	
    	//update the data to add
    	layoutToShow.data.state = BaseLayoutData.STATE_ADDED;
    	
    }
    
    private class BaseAnimationListener implements AnimationListener{

    	private FrameLayout animationLayoutContainer = null;
        private BaseLayoutData animationDataToHide = null;
        private BaseLayoutData animationDataToShow = null;
        
        private BaseLayout animationDataToHideLayBase = null;
        private boolean cleanStack = false;
        
        public BaseAnimationListener(FrameLayout animationLayoutContainer, BaseLayoutData animationDataToHide, 
        		BaseLayoutData animationDataToShow, boolean cleanStack){
        	this.animationLayoutContainer = animationLayoutContainer;
        	this.animationDataToHide = animationDataToHide;
        	this.animationDataToShow = animationDataToShow;
        	if(animationDataToHide!=null){
        		animationDataToHideLayBase = animationDataToHide.layBase;
        	}
        	this.cleanStack = cleanStack;
        }
        
		@Override
		public void onAnimationEnd(Animation arg0) {
			if(animationDataToHide!=null){
				animationDataToHide.layBase = animationDataToHideLayBase;
			}
			animationEnd(animationLayoutContainer, animationDataToHide, animationDataToShow, cleanStack);
			
			//clean
        	animationLayoutContainer = null;
        	animationDataToHide = null;
        	animationDataToShow = null;
        	Runtime.getRuntime().gc();
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			
		}
    	
    }
	
	
	/**
	 * Called when animation finish to delete layouts
	 */
	private void animationEnd(final FrameLayout animationLayoutContainer, final BaseLayoutData animationDataToHide, final BaseLayoutData animationDataToShow, boolean cleanStack){
    	if(animationLayoutContainer!=null && animationDataToHide!=null){
    		
    		//unload the view if not keep alive
    		if(!animationDataToHide.layBase.keepAlive){ 
    			DeleteViewRunnable deleteViewRunnable = new DeleteViewRunnable(animationLayoutContainer, animationDataToHide.layBase.view);
    			animationLayoutContainer.post(deleteViewRunnable);
    			animationDataToHide.unloadData();
    			
    		}
    		
    		//stop animating
    		animationDataToHide.animating = false;
        	if(animationDataToShow!=null){
        		animationDataToShow.animating = false;
        	}
    		
    	}
		
    	//clean stack
    	if(cleanStack){
    		animationDataToShow.layBase.clearStackBrothers();
    	}
    	
		//hide block
		hideBlock();
		
	}
	
	private class DeleteViewRunnable implements Runnable{

		//view to hide
		private ViewGroup viewParent = null;
		private View viewSon = null;
		
		public DeleteViewRunnable(ViewGroup viewParent, View viewSon){
			this.viewParent = viewParent;
			this.viewSon = viewSon;
		}
		
		@Override
		public void run() {
			viewParent.removeView(viewSon);
		}
		
	}
	
	/**
	 * Unload the last layout that can do back 
	 * @param bundleReturn - Bundle with data to return to previous layout
	 */
	public void unloadLastLayoutToBack(Bundle bundleReturn){
		BaseLayout baseLayout = getLastLayoutToBack();
		if(baseLayout!=null){
			baseLayout.unload(bundleReturn);
		}
	}
        
    /**
     * Unload the last fragment loaded
     */
    public void unloadLayoutData(BaseLayoutData dataToHide, Bundle bundleReturn){
    	
    	//check it is ok the data to hide
    	if(dataToHide==null || dataToHide.layBase==null){
    		return;
    	}
    	
    	//bundle to return
    	Bundle bundleData = bundleReturn;
    	if(bundleData==null){
    		bundleData = new Bundle();
    	}
    	
    	//get the container
    	FrameLayout layoutContainer = null;
    	if(dataToHide.dataParent.layBase!=null){
    		layoutContainer = (FrameLayout) dataToHide.dataParent.layBase.view.findViewById (dataToHide.idContainer);
    	}else{
    		layoutContainer = (FrameLayout) dataToHide.layBaseActivity.findViewById (dataToHide.idContainer);
    	}
    	if(layoutContainer==null){
    		return;
    	}
    	
    	BaseLayoutData dataToShow = null;
		boolean result = true; //used to know if we want to show a layout from the stack or not
		do{
			
			if(dataToShow!=null){
				if(dataToShow.layBase!=null){
					bundleData = new Bundle();
					dataToShow.layBase.dataToReturn(bundleData);
					dataToShow.unloadData();
				}
				dataToHide.dataParent.listLayBaseData.remove(dataToShow);
				dataToShow = null;
			}
			
			//get the next layout in stack
	    	dataToShow = BaseLayoutActivity.getNextLayoutDataInStack(dataToHide.dataParent, dataToHide);
	    	if(dataToShow!=null){
	    		
    			//create the layout
    			dataToShow.loadData(dataToHide.dataParent);
	    		
	    		if(dataToShow.layBase!=null){
	    			//call to activity result
	    			result = dataToShow.layBase.unloadLayoutResult(bundleData);
	    		}
	    	}else{
	    		//no more data in stack so we search in parent, changing the dataToHide to the parent
//	    		unloadLayoutData(dataToHide.dataParent, bundleReturn);
//	    		return;
	    	}
	    	
		}while(dataToShow!=null && result);
		
		//check these layouts are already being animated
    	if(dataToShow!=null){
	    	if(dataToShow.animating || dataToHide.animating){
	    		return;
	    	}
	    	
	    	//mark flag as animating to block other load  or unloads
	    	dataToShow.animating = true;
	    	dataToHide.animating = true;
    	}
    	
    	//show block
	    showBlock();
    	
    	//call to ondestroy of data hide
    	dataToHide.layBase.onDestroy();
		
    	if(dataToShow!=null || dataToHide.forceAnimation){
    		
    		//prepare the views of the base layout
    		if(dataToShow!=null && dataToShow.layBase!=null){
	    		dataToShow.layBase.initViews();
	    		dataToShow.layBase.viewsLoaded = true;
	    		
	    		this.loadLayoutsFromData(dataToShow);
	    		
	    		//layout to show
	    		if(!dataToShow.layBase.keepAlive){
	    			dataToShow.layBase.onResume();
	    		}
	    		
	    		//add layout if it is not added (could have been alive)
	    		if(layoutContainer.indexOfChild(dataToShow.layBase.view)==-1){
	    			layoutContainer.addView(dataToShow.layBase.view, layoutContainer.indexOfChild(dataToHide.layBase.view));
	    		}
	    		dataToShow.state = BaseLayoutData.STATE_ADDED;
    		}
    		
    		//the hide one don't keep alive (could be dangerous, leaks)
    		dataToHide.layBase.keepAlive = false;
    		
    		runAnimations(false, layoutContainer, dataToShow, dataToHide, false);
    		
    		//remove the view to hide
    		if(dataToHide.layBase!=null){
    			layoutContainer.removeView(dataToHide.layBase.view);
    		}
    		
    	}else{
    	
	    	layoutContainer.removeView(dataToHide.layBase.view);
			dataToHide.unloadData();
			
			//show block
		    hideBlock();
	    	
	    	dataToHide.animating = false;
			
    	}
    	
    	dataToHide.dataParent.listLayBaseData.remove(dataToHide);
    
    }
    
    
    @SuppressLint("NewApi") 
    private void runAnimations(boolean bLoad, FrameLayout layoutContainer, BaseLayoutData dataToShow, BaseLayoutData dataToHide, boolean cleanStack){
    	
    	//Get the animations to run
		Animation animationOut = null;
		Animation animationIn = null;
		
		int showAnimation = BaseLayout.ANIM_NONE;
		int hideAnimation = BaseLayout.ANIM_NONE;
		
		if(dataToShow!=null){
			showAnimation = dataToShow.animationLoadShowId;
			if(!bLoad){
				showAnimation = dataToShow.animationUnloadShowId;
			}
			animationIn = BaseLayoutData.getAnimation(showAnimation);
		}
		
		if(dataToHide!=null){
			hideAnimation = dataToHide.animationLoadHideId;
			if(!bLoad){
				hideAnimation = dataToHide.animationUnloadHideId;
			}
			animationOut = BaseLayoutData.getAnimation(hideAnimation);
		}
		    		
		//save data for animations
		BaseAnimationListener animationListener = new BaseAnimationListener(layoutContainer, dataToHide, dataToShow, cleanStack);
		
		//load animation In
		if(animationIn!=null){
			if(animationOut==null){
				animationIn.setAnimationListener(animationListener);
			}
			dataToShow.layBase.view.startAnimation(animationIn);
		}
		
		//load animation Out
		if(animationOut!=null){
			animationOut.setAnimationListener(animationListener);
			dataToHide.layBase.view.startAnimation(animationOut);
		}
		
		//call to end animation if no animations to load
		if(animationIn==null && animationOut==null){
			animationListener.onAnimationEnd(null);
		}
    	
    }
    
    /**
     * Get the last data loaded in the container given
     * @param idContainer - int identifier of the container where to search
     * @return BaseLayoutData - last data in the stack for that container
     */
    public BaseLayoutData getLastLayoutData(int idContainer){
    	return BaseLayoutActivity.getLastLayoutData(this.data, idContainer);
    }
    
    /**
     * Get the last data loaded in the container given
     * @param idContainer - int identifier of the container where to search
     * @return BaseLayoutData - last data in the stack for that container
     */
    private static BaseLayoutData getLastLayoutData(BaseLayoutData dataParent, int idContainer){
    	
    	//search in the general stack the last fragment
    	for(int i=dataParent.listLayBaseData.size()-1; i>=0; i--){
    		
    		if(dataParent.listLayBaseData.get(i).idContainer==idContainer){
    			return dataParent.listLayBaseData.get(i);
    		}
    	}
    	
    	//return BaseFragment found
    	return null;
    }
    
    /**
     * Return the next data in the stack with the same container
     * @param idContainer - int id of container 
     * @param baseData - BaseLayoutData that is the reference to search the next one
     * @return BaseLayoutData or null if not found
     */
    private static BaseLayoutData getNextLayoutDataInStack(BaseLayoutData dataParent, BaseLayoutData baseData){
    	
    	//first search the base data
    	for(int i=dataParent.listLayBaseData.size()-1; i>=0; i--){
    		
    		if(dataParent.listLayBaseData.get(i)==baseData){
    			
    			//second search the next one of that container
    			for(i--;i>=0; i--){
    				if(dataParent.listLayBaseData.get(i).idContainer==baseData.idContainer){
    	    			return dataParent.listLayBaseData.get(i);
    	    		}
    			}
    			
    		}
    	}
    	
    	//return BaseFragment found
    	return null;
    	
    }
    
    //---------- DATA Fragments management --------
    
    /**
     * Search in the list if there is a container being shown in that container
     * If there are two fragments added in a container it is returned the last one
     * @param idContainer - int with the identifier of the container
     * @return FrgBaseData - if is found a fragment, null if not found
     */
    private static BaseLayoutData getLayoutDataInContainer(BaseLayoutData dataParent, int idContainer){
    	
    	//data to return
    	BaseLayoutData layBaseData = null;
    	
    	//search in the list
    	for(int i=dataParent.listLayBaseData.size()-1; i>=0; i--){
    		BaseLayoutData bld = dataParent.listLayBaseData.get(i);
    		if(bld.idContainer==idContainer && bld.state==BaseLayoutData.STATE_ADDED){
    			layBaseData = bld;
    		}
    	}
    	
    	//return the data
    	return layBaseData;
    }
    
    /**
     * Load the layouts from the list of data that we have saved
     * @param dataParent - BaseLayoutData
     */
    protected void loadLayoutsFromData(BaseLayoutData dataParent){
    	
    	//create the instance of all children to load
    	dataParent.loadData(dataParent);
    	
    	//load the views
    	loadLayoutsViewsFromData(dataParent);
    }
    
    /**
     * Load the views
     * @param dataParent
     */
    private void loadLayoutsViewsFromData(BaseLayoutData dataParent){
    	
    	//search the layouts added without layout
    	for(BaseLayoutData bld : dataParent.listLayBaseData){
    		
			//get layout container
			if(bld.layBase!=null){
				
				//prepare the views of the base layout
				bld.layBase.initViews();
				bld.layBase.viewsLoaded = true;
    			
				//load children
    			loadLayoutsViewsFromData(bld);
				
				//load the layout
				FrameLayout layoutParent = null;
				if(dataParent.layBase!=null){
					layoutParent = (FrameLayout) dataParent.layBase.view.findViewById(bld.idContainer);
				}else{
					layoutParent = (FrameLayout) bld.layBaseActivity.findViewById(bld.idContainer);
				}
    			this.loadLayout(dataParent, layoutParent, bld.layBase, false);
    			
			}
    		
    	}
    }
       
    /**
     * Get the last layout to make back
     * @return
     */
    public BaseLayout getLastLayoutToBack(){
    	
    	//search the last data with return
    	BaseLayoutData data = this.data.getLastLayoutData(true);
    	if(data!=null){
    		return data.layBase;
    	}
    	
    	//return error, there isn't
    	return null;
    }
    
    /**
     * Get a layout with the class name given
     * @param className - String name of the class
     * @return BaseLayout if found or null if not found
     */
    public BaseLayout findBaseLayoutSonByName(Class<?> c){
    	
    	//not found, so we continue searching
    	for(int i=0; i<this.data.listLayBaseData.size(); i++){
    		BaseLayoutData baseLayoutDataSon = this.data.listLayBaseData.get(i);
    		BaseLayout baseLayoutSon = baseLayoutDataSon.layBase;
    		
    		if(baseLayoutSon!=null){
	    		//check name
	    		if(baseLayoutDataSon.state==BaseLayoutData.STATE_ADDED){
	    			if(baseLayoutSon!=null && baseLayoutSon.className.equalsIgnoreCase(c.getSimpleName())){
	    				return baseLayoutSon;
	    			}
	    		}
	    		
	    		//check its children
	    		BaseLayout baseLayout = baseLayoutSon.findBaseLayoutSonByName(c);
	    		if(baseLayout!=null){
	    			return baseLayout;
	    		}
    		}
    	}
    	
    	//return null because no more children have it
    	return null;
    }
    
    //---------------- STACKS --------------------
    
    /**
     * Clear the stack of baseviewcontrollers no visible
     * @param vContainer - UIView container to clear the stack
     **/
    protected void clearStack(BaseLayoutData data, int idContainer){
        
        //list of baseDatas to delete
    	List<BaseLayoutData> listToDelete = new ArrayList<BaseLayoutData>();
        
        //search base controllers with that container
    	for(BaseLayoutData bld : data.listLayBaseData){
    		if(bld.idContainer==idContainer){
	            listToDelete.add(bld);
            }
        }
        
        //once we have all, delete all no visible
    	for(BaseLayoutData bld : listToDelete){
    		if(bld.layBase==null){
    			data.listLayBaseData.remove(bld);
    		}
    	}
        
    }
        
    //----------------- BLOCK -------------
    
    /**
     * Show a transparent block on the screen
     */
    public void showBlock(){
    	
    	if(BLOCK_VIEW_ACTIVATED){
	    	synchronized(vBlock){
		    	//add a block
		    	blockCounter++;
//		    	if(blockCounter>0){
//		    		vBlock.setBackgroundColor(Color.argb(170, 255, 0, 0));
//		    	}
	    	}
    	}
    	
    }
    
    private View.OnTouchListener blockTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			synchronized (vBlock) {
				return blockCounter>0;
			}
			
		}
	};
    
	/**
     * Hide the view blocking the screen
     */
	public void hideBlock(){
		hideBlock(false);
	}
	
    /**
     * Hide the view blocking the screen
     * @param forceHide - boolean if want to clean hide block counter
     */
    public void hideBlock(boolean forceHide){

    	if(BLOCK_VIEW_ACTIVATED){
	    	synchronized(vBlock){
	    		//decrease counter of block
	    		if(forceHide){
	    			blockCounter = 0;
	    		}else{
	    			blockCounter--;
	    		}
//		    	if(blockCounter==0){
//		    		vBlock.setBackgroundColor(Color.argb(50, 0, 255, 0));
//		    	}
	    	}
    	}
    }
    
    
    //----------------- MODAL --------------
    
    /**
	 * Show this view as a modal
	 * If there is one on screen it will not be shown
	 */
	public void showModal(BaseLayout baseLayout){
		
		//get the animation
		Animation animationIn = BaseLayoutData.getAnimation(baseLayout.data.animationModalShowId);
		
		//show block
		showBlock();
		
		//load all
		baseLayout.onResume();
		
		//add the layout as modal
		baseLayoutModals.add(baseLayout);
		
		//add the layout
		flGen.addView(baseLayout.view);
		
		//show it as an animation
		if(animationIn!=null){
			baseLayout.view.startAnimation(animationIn);
		}
		
	}
	
	/**
	 * Hide the last modal
	 */
	public void hideLastModal(){
		
		if(baseLayoutModals.size()>0){
			hideModal(baseLayoutModals.get(baseLayoutModals.size()-1));
		}
		
	}
	
	/**
	 * Hide all modals shown
	 */
	public void hideAllModals(){
		
		for(BaseLayout bl : new ArrayList<BaseLayout>(baseLayoutModals)){
			hideModal(bl);
		}
		
	}
	
	/**
	 * Hide this view from modal state
	 */
	public void hideModal(BaseLayout baseLayout){
		
		//get the animation
		Animation animationOut = BaseLayoutData.getAnimation(baseLayout.data.animationModalHideId);
		
		//data for end of animation
		BaseAnimationListener animationListener = new BaseAnimationListener(flGen, baseLayout.data, null, false);
		
		//deactivate the keep alive because has to be deleted
		baseLayout.data.layBase.keepAlive = false;
		
		//remove modal from list
		baseLayoutModals.remove(baseLayout);
		
		//animationIn
		if(animationOut!=null){
			animationOut.setAnimationListener(animationListener);
			baseLayout.view.startAnimation(animationOut);
		}else{
			animationListener.onAnimationEnd(null);
		}
		
		//remove the view to hide
    	flGen.removeView(baseLayout.view);
	}
	
	//---------------- SHOW DIALOG -----------------
	
	/**
	 * Call to show a dialog
	 * @param dialog
	 */
	@SuppressWarnings("deprecation")
	public void showDialog(Dialog dialog){
		this.dialog = dialog;
		showDialog(0, null);
	}
	
	
	@SuppressWarnings("deprecation")
	public void dismissDialog(){
		if(dialog!=null){
			removeDialog(0);
			dialog = null;
		}
	}
	
	/**
	 * Create dialogs
	 */
	@Override
	protected Dialog onCreateDialog (int id, Bundle args){
		return dialog;
	}
	
	public abstract void setLoadingMessage(int messageId);

}
