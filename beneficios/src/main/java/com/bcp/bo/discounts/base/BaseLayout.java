package com.bcp.bo.discounts.base;


import java.lang.reflect.Constructor;

import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.general.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.json.JSONObject;

import bcp.bo.service.ServiceLauncher.IService;
import bcp.bo.service.ServiceLauncher;

public abstract class BaseLayout implements IService {

    //type of animations
    public static final int ANIM_NONE = -1;
    public static final int ANIM_SLIDE_IN_RIGHT = 0;
    public static final int ANIM_SLIDE_OUT_RIGHT = 1;
    public static final int ANIM_SLIDE_IN_LEFT = 2;
    public static final int ANIM_SLIDE_OUT_LEFT = 3;
    public static final int ANIM_SLIDE_IN_BOTTOM = 4;
    public static final int ANIM_SLIDE_OUT_BOTTOM = 5;
    public static final int ANIM_SLIDE_IN_TOP = 6;
    public static final int ANIM_SLIDE_OUT_TOP = 7;
    public static final int ANIM_ALPHA_0_100 = 8;
    public static final int ANIM_ALPHA_100_0 = 9;

    /**
     * className used to check if this is the reference
     **/
    protected String className = null;

    /**
     * Data associated
     **/
    protected BaseLayoutData data = new BaseLayoutData();

    /**
     * Flag to keep in memory
     **/
    protected boolean keepAlive = false;

    /**
     * View of base
     **/
    public ViewGroup view = null;

    public boolean IsDebug;
    public StartActivity BaseActivity;
    public ServiceLauncher SLauncher;

    protected boolean viewsLoaded = false;
    //IMAGEDOWNLOADER


    public BaseLayout(BaseLayoutActivity baseLayoutActivity) {

        initGeneral(baseLayoutActivity.data);

        initWithParent(baseLayoutActivity.data);


    }

    public void recreate(BaseLayoutData dataRecreate) {

        this.data = dataRecreate.clone();
        this.view = dataRecreate.viewToRecreate;
        this.data.layBase = this;
        this.data.layBaseActivity = dataRecreate.layBaseActivity;
        this.data.state = BaseLayoutData.STATE_CREATED;
    }


    /**
     * Called when this base layout is attached to its parent
     */
    protected void onResume() {
        boolean recreated = false;
        if (view == null) {
            initGeneral(this.data.dataParent);
            recreated = true;
        }

        //initialize the views
        if (!viewsLoaded) {
            initViews();
        }

        //check if is loaded or created
        if (data.state == BaseLayoutData.STATE_CREATED || recreated) {
            initBaseLayouts();
        }
//		else{
//			data.layBaseActivity.loadLayoutsFromData(data);
//		}

        BaseActivity = (StartActivity) getBaseLayoutActivity();
        SLauncher = BaseActivity.SLauncher;
        IsDebug = BaseActivity.IsDebug;
        //initialize values
        initValues();

        //set the layout as its own listener
        view.setOnClickListener(clickListener);

        //initialize listeners
        initListeners();

        //other initializes
        init();

    }

    private void initGeneral(BaseLayoutData dataParent) {
        //assign the xml
        try {
            LayoutInflater factory = LayoutInflater.from(dataParent.layBaseActivity);
            view = (ViewGroup) factory.inflate(this.getXMLToInflate(), null, false);

//		//set the layout params
//		if(parent instanceof FrameLayout){
//			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//			view.setLayoutParams(layoutParams);
//		}

            this.className = this.getClass().getSimpleName();
        } catch (Exception e) {
            Log.d("errrr", e.getMessage());
        }


    }

    private void initWithParent(BaseLayoutData dataParent) {
        //connect the data
        data.layBase = this;
        data.layBaseActivity = dataParent.layBaseActivity;
        data.className = this.getClass().getName();

        //initialize the state of the data
        data.state = BaseLayoutData.STATE_CREATED;
    }

    protected void reinitWithData(BaseLayoutData baseLayoutData) {

        //connect the data
        data.layBase = null;
        data.layBaseActivity = null;
        data.className = null;

        this.data = baseLayoutData;
    }

    protected void onFinishInflate() {
        this.onResume();
    }

    /**
     * Create a fragment base with the class name received
     *
     * @param className - String with full name of the class (with package)
     * @param tag       - String with tag information to set to fragment base
     * @return FrgBase - or null if error
     */
    public static BaseLayout createLayBase(BaseLayoutData layBaseData) {

        //fragment to return
        BaseLayout layBase = null;

        try {
            Class<?> classLay = Class.forName(layBaseData.className);
            Constructor<?> c = classLay.getConstructor(new Class[]{BaseLayoutActivity.class});

            layBase = (BaseLayout) c.newInstance(new Object[]{layBaseData.layBaseActivity});

            //reinit for load with data
            layBase.reinitWithData(layBaseData);

            //set tag
            if (layBaseData.tag != null) {
                layBase.data.tag = layBaseData.tag;
            }

            //call to load data
            layBase.dataReceived(layBaseData.bundle);

        } catch (Exception e) {
            //ERROR
        }

        //return the fragment
        return layBase;
    }

    /**
     * Must return the activity to inflate, the XML given by the R.layout.
     *
     * @return R.layout. associated to this layout
     */
    protected abstract int getXMLToInflate();

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
     * If this layout belongs to a BaseLayoutActivity class activity return this activity
     *
     * @return BaseLayoutActivity - LayoutActivity returned or NULL
     */
    public BaseLayoutActivity getBaseLayoutActivity() {
        //Keep reference to the BaseFragmentActivity object
        if (this.data != null) {
            Activity activity = this.data.layBaseActivity;
            if (activity != null && activity instanceof BaseLayoutActivity) {
                return (BaseLayoutActivity) activity;
            }
        }
        return null;
    }


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
    protected Handler handler = new LayHandler(this);

    /**
     * Handler of the activity
     */
    public static class LayHandler extends Handler {
        private BaseLayout layout;

        private LayHandler(BaseLayout baseLayout) {
            layout = baseLayout;
        }

        @Override
        public void handleMessage(Message msg) {
            // send this message as received
            layout.handleMessageReceived(msg.what, msg.arg1, msg.arg2, msg.obj);
        }
    }

    ;

    /**
     * Receive the messages sent to main handler of LLayBase
     *
     * @param what - what data from message
     * @param arg1 - value of arg1 from message
     * @param arg2 - value of arg2 from message
     * @param obj  - object received of message
     */
    protected abstract void handleMessageReceived(int what, int arg1, int arg2, Object obj);

    /**
     * Send a message to this activity, calling to handleMessageReceived
     *
     * @param msg Message to send to the handler
     */
    public void sendMessage(Message msg) {

        //send this message as received
        handleMessageReceived(msg.what, msg.arg1, msg.arg2, msg.obj);

    }

    //------- DATA -----------------

    /**
     * Initialize views of layout: findViewById(R.id. )
     */
    protected abstract void dataReceived(Bundle extras);

    /**
     * Must save the data required to be received when activity is resumed
     * extras.putString(...)
     *
     * @param extras
     */
    protected abstract void dataToSave(Bundle extras);

    /**
     * Must save the data required to be received when activity is going to return something
     * extras.putString(...)
     *
     * @param extras
     */
    protected abstract void dataToReturn(Bundle extras);

    /**
     * Free all the memory used in the Layout
     */
    public void freeMem() {

    }

    /**
     * Send a key event and receive if the event has been consumed or not
     *
     * @param keyCode - code of the key
     * @param event   - event of the key (up, down, ...)
     * @return TRUE if event consumed, FALSE otherwise
     */
    protected abstract boolean keyPressed(int keyCode, KeyEvent event);

    //------- Activity events --------

    /**
     * Load an activity from a layout to call it when returning
     *
     * @param activityDestiny - Class<?> of the activity to go
     * @param bundle          - Bundle to attach to the new activity
     * @param requestCode     - int request code that will be returned by activity
     * @param fragmentResult  - Fragment to be called when activity returns
     */
    public void startActivity(Class<?> activityDestiny, Bundle bundle, int requestCode, Fragment fragmentResult) {

        //get the activity base
        data.layBaseActivity.startActivity(activityDestiny, bundle, requestCode, fragmentResult);

    }

    /**
     * Receive the events from other activities
     *
     * @param data - Intent with data returned
     * @return false if we want to show this layout from the stack or true if we delete it and go to next one
     */
    public abstract boolean unloadLayoutResult(Bundle data);


    //----------------- SAVE AND LOAD DATA -------------

    public void onSaveInstanceState(Bundle outState) {


    }

    //---------------- WORK WITH FRAGMENTS -------------

    public void loadLayout(int idContainer, BaseLayout layBase) {
        loadLayout(idContainer, layBase, false);
    }

    /**
     * Load a layout
     *
     * @param idContainer - int R of container to add the fragment
     * @param layBase     - Layout to add or replace, can be null but will do nothing
     * @param cleanStack  - boolean with true if we want to clean the stack of container after adding
     */
    public void loadLayout(int idContainer, BaseLayout layBase, boolean cleanStack) {

        //get container
        FrameLayout rlContainer = (FrameLayout) view.findViewById(idContainer);
        if (rlContainer != null) {
            data.layBaseActivity.loadLayout(this.data, rlContainer, layBase, cleanStack);
        } else {
            data.layBaseActivity.loadLayout(idContainer, layBase, cleanStack);
        }
    }

    public void replaceLayout(BaseLayout layBase) {
        replaceLayout(layBase, false);
    }

    public void replaceLayout(BaseLayout layBase, boolean cleanStack) {
        if (data.dataParent.layBase != null) {
            data.dataParent.layBase.loadLayout(this.data.idContainer, layBase, cleanStack);
        } else {
            data.layBaseActivity.loadLayout(data.idContainer, layBase, cleanStack);
        }
    }

    /**
     * Unload this layout from its container
     *
     * @param bundleReturn - Bundle to return to previous layout in stack
     */
    public void unload(Bundle bundleReturn) {

        //get the activity
        this.data.layBaseActivity.unloadLayoutData(this.data, bundleReturn);

    }

    /**
     * Get the last layout to make back
     *
     * @return
     */
    public BaseLayout getLastLayoutToBack() {

        //search the last data with return
        BaseLayoutData data = this.data.getLastLayoutData(true);
        if (data != null) {
            return data.layBase;
        }

        //return error, there isn't
        return null;
    }

    /**
     * Get a layout with the class name given
     *
     * @param className - String name of the class
     * @return BaseLayout if found or null if not found
     */
    public BaseLayout findBaseLayoutActivityByName(Class<?> c) {
        return this.data.layBaseActivity.findBaseLayoutSonByName(c);
    }

    /**
     * Get a layout with the class name given
     *
     * @param className - String name of the class
     * @return BaseLayout if found or null if not found
     */
    public BaseLayout findBaseLayoutParentByName(Class<?> c) {

        //check if it is this one to return
        if (this.className.equalsIgnoreCase(c.getSimpleName())) {
            return this;
        } else if (this.data.dataParent != null && this.data.dataParent.layBase != null) {
            //if this is not we go up
            return this.data.dataParent.layBase.findBaseLayoutParentByName(c);
        } else {
            return null;
        }
    }

    /**
     * Get a layout with the class name given
     *
     * @param className - String name of the class
     * @return BaseLayout if found or null if not found
     */
    public BaseLayout findBaseLayoutSonByTag(String tag) {

        //try to find a direct son with that name
        BaseLayout baseLayout = findBaseLayoutDirectSonByTag(tag);
        if (baseLayout != null) {
            return baseLayout;
        }

        //not found, so we continue searching
        for (int i = 0; i < this.data.listLayBaseData.size(); i++) {
            BaseLayout baseLayoutSon = this.data.listLayBaseData.get(i).layBase;
            if (baseLayoutSon != null) {
                baseLayout = baseLayoutSon.findBaseLayoutDirectSonByTag(tag);
                if (baseLayout != null) {
                    return baseLayout;
                }
            }
        }

        //return null because no more children have it
        return null;
    }

    /**
     * Find a base layout of direct son from the received baselayout
     *
     * @param className - String with name to search
     * @return BaseLayout if found a son with that className or null
     */
    private BaseLayout findBaseLayoutDirectSonByTag(String tag) {

        for (int i = 0; i < this.data.listLayBaseData.size(); i++) {
            BaseLayoutData baseData = this.data.listLayBaseData.get(i);
            if (baseData.state == BaseLayoutData.STATE_ADDED || baseData.state == BaseLayoutData.STATE_LOADED || baseData.state == BaseLayoutData.STATE_REPLACED_SON) {
                if (baseData.layBase != null && baseData.tag.equalsIgnoreCase(tag)) {
                    return baseData.layBase;
                }
            }
        }

        //return error
        return null;
    }

    /**
     * Get a layout with the class name given
     *
     * @param className - String name of the class
     * @return BaseLayout if found or null if not found
     */
    public BaseLayout findBaseLayoutSonByName(Class<?> c) {

        //try to find a direct son with that name
        BaseLayout baseLayout = findBaseLayoutDirectSonByName(c);
        if (baseLayout != null) {
            return baseLayout;
        }

        //not found, so we continue searching
        for (int i = 0; i < this.data.listLayBaseData.size(); i++) {
            BaseLayout baseLayoutSon = this.data.listLayBaseData.get(i).layBase;
            if (baseLayoutSon != null) {
                baseLayout = baseLayoutSon.findBaseLayoutSonByName(c);
                if (baseLayout != null) {
                    return baseLayout;
                }
            }
        }

        //return null because no more children have it
        return null;
    }

    /**
     * Find a base layout of direct son from the received baselayout
     *
     * @param className - String with name to search
     * @return BaseLayout if found a son with that className or null
     */
    private BaseLayout findBaseLayoutDirectSonByName(Class<?> c) {

        for (int i = 0; i < this.data.listLayBaseData.size(); i++) {
            BaseLayoutData baseData = this.data.listLayBaseData.get(i);
            if (baseData.state == BaseLayoutData.STATE_ADDED || baseData.state == BaseLayoutData.STATE_LOADED || baseData.state == BaseLayoutData.STATE_REPLACED_SON) {
                if (baseData.layBase != null && baseData.layBase.className.equalsIgnoreCase(c.getSimpleName())) {
                    return baseData.layBase;
                }
            }
        }

        //return error
        return null;
    }

    /**
     * Clear the stack of this controller.
     * Go to parent and call to clear the stack of all view controllers
     * with this container
     **/
    public void clearStackBrothers() {

        //get the parent
        if (data.dataParent != null) {
            getBaseLayoutActivity().clearStack(data.dataParent, data.idContainer);
        }

    }

    //---- SET ANIMATIONS TYPES ------

    public void setAnimationLoadShowId(int animationLoadShowId) {
        this.data.animationLoadShowId = animationLoadShowId;
    }

    public void setAnimationLoadHideId(int animationLoadHideId) {
        this.data.animationLoadHideId = animationLoadHideId;
    }

    public void setAnimationUnloadShowId(int animationUnloadShowId) {
        this.data.animationUnloadShowId = animationUnloadShowId;
    }

    public void setAnimationUnloadHideId(int animationUnloadHideId) {
        this.data.animationUnloadHideId = animationUnloadHideId;
    }

    public void setAnimationModalShowId(int animationModalShowId) {
        this.data.animationModalShowId = animationModalShowId;
    }

    public void setAnimationModalHideId(int animationModalHideId) {
        this.data.animationModalHideId = animationModalHideId;
    }

    public void setForceAnimation(boolean forceAnimation) {
        this.data.forceAnimation = forceAnimation;
    }

    /**
     * Flag to keep alive the layout and no delete from parent afdter the animation
     *
     * @param keepAlive - boolean true for keeping alive, false for delete after animation
     */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * Set a tag for this base layout
     *
     * @param tag - String tag
     */
    public void setLayoutTag(String tag) {
        this.data.tag = tag;
    }

    //----- modal

    /**
     * Show the layout as modal
     */
    public void showAsModal() {
        this.data.layBaseActivity.showModal(this);
    }

    /**
     * Called when the base layout is being shown as a model and system want to remove it
     *
     * @return boolean - true if can hide it, false if cannot hide it
     */
    public boolean canHideModal() {
        return true;
    }

    /**
     * Hide the modal view
     */
    public void hideLastModal() {
        this.data.layBaseActivity.hideLastModal();
    }

    public void hideModal() {
        if (this.data != null && this.data.layBaseActivity != null) {
            this.data.layBaseActivity.hideModal(this);
        }
    }

    /**
     * Get the data cloned to recreate later
     *
     * @return
     */
    public BaseLayoutData getDataToRecreate() {
        BaseLayoutData dataCloned = this.data.clone();

        dataCloned.layBaseActivity = getBaseLayoutActivity();
        dataCloned.layBase = this;
        dataCloned.viewToRecreate = this.view;

        return dataCloned;
    }

    /**
     * Get the string
     *
     * @param rId int identifier of string
     * @return String text
     */
    public String getString(int rId) {
        return getBaseLayoutActivity().getString(rId);
    }

    /**
     * To override, called at the end of the life of a baselayout, during unload
     */
    protected void onDestroy() {
        for (BaseLayoutData bld : data.listLayBaseData) {
            if (bld.layBase != null) {
                bld.layBase.onDestroy();
            }
        }
    }

    @Override
    public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        if (IsDebug) {
            Logger.Tag("ON-COMPLETED");
            Logger.Info("Status: " + status);
            Logger.Info("Message: " + message);
            Logger.Info("Service: " + service);
            Logger.Info("JSONObject: " + jsonObject);
        }
    }

    @Override
    public void onCancelled() {

    }

}