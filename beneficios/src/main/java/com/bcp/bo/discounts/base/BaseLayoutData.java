package com.bcp.bo.discounts.base;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class BaseLayoutData implements Parcelable{
	
	public static final int STATE_CREATED = 0;
	public static final int STATE_ADDED = 1;
	public static final int STATE_REPLACED = 2;
	public static final int STATE_REPLACED_SON = 3;
	public static final int STATE_LOADED = 4;
	
	private static final int ANIMATION_DURATION = 200;
	
	//layout associated
	public BaseLayout layBase = null;
	protected BaseLayoutActivity layBaseActivity = null;
	public BaseLayoutData dataParent = null;
	
	//state 
	protected int state = STATE_CREATED;
	
	//data
	protected String className = null;
	protected String tag = "";
	protected int idContainer = 0;
	protected boolean forceAnimation = false;
	protected Bundle bundle = null;
	
	//View to recreate
	protected ViewGroup viewToRecreate = null;
	
	/** Animations for showing and hiding **/
	protected int animationLoadShowId = BaseLayout.ANIM_SLIDE_IN_RIGHT;
	protected int animationLoadHideId = BaseLayout.ANIM_SLIDE_OUT_LEFT;
	protected int animationUnloadShowId = BaseLayout.ANIM_SLIDE_IN_LEFT;
	protected int animationUnloadHideId = BaseLayout.ANIM_SLIDE_OUT_RIGHT;
	protected int animationModalShowId = BaseLayout.ANIM_SLIDE_IN_BOTTOM;
	protected int animationModalHideId = BaseLayout.ANIM_SLIDE_OUT_BOTTOM;
	
	/** Flag to block load or unloads during animations **/
	protected boolean animating = false;
	
	/** List of fragment data in the stack **/
	protected ArrayList<BaseLayoutData> listLayBaseData = new ArrayList<BaseLayoutData>();
	
	public BaseLayoutData(){
		
	}
	
	/**
	 * Update the data
	 * @param isSon - boolean indicates if this view will be saved as son, to change the state
	 */
	protected void updateDataToSave(boolean isSon){
		updateDataToSave(isSon, this.state);
	}
	
	/**
	 * Update the data 
	 * @param isSon - boolean indicates if this view will be saved as son, to change the state
	 * @param stateParent - int with state of parent to convert children depending from parent state
	 */
	private void updateDataToSave(boolean isSon, int stateParent){
		
		//for each data in this stack we do the same
		for(BaseLayoutData bld : this.listLayBaseData){
			bld.updateDataToSave(true, stateParent);
		}
		
		if(layBase!=null){
			this.bundle = new Bundle(); 
			if(isSon && this.state==STATE_ADDED){
				if(stateParent==STATE_REPLACED){
					this.state = STATE_REPLACED_SON;
				}else{
					this.state = STATE_LOADED; //STATE_LOADED
				}
			}
			
			layBase.dataToSave(this.bundle);
		}
	}
	
	/**
	 * Update the state of this data and all its children
	 * @param stateOrigin - int with state to search
	 * @param stateDestiny - int with state to set
	 */
	public void updateStateForAllTree(int stateOrigin, int stateDestiny){
		
		//change state
		if(this.state==stateOrigin){
			this.state = stateDestiny;
		}
		
		//call to change state of all children
		for(BaseLayoutData bld : this.listLayBaseData){
			bld.updateStateForAllTree(stateOrigin, stateDestiny);
		}
	}
	
	/**
	 * Load the data, create an instance of the layout
	 */
	public void loadData(BaseLayoutData dataParent){
		
		//inflate the layout (create it)
		if(layBaseActivity==null || layBase==null){
			
			this.dataParent = dataParent;
			this.layBaseActivity = dataParent.layBaseActivity;
			this.layBase = BaseLayout.createLayBase(this/*, (ViewGroup)dataParent.layBase.view.findViewById(this.idContainer)*/);
			
		}
		
		//load data from all children
		for(BaseLayoutData bld : this.listLayBaseData){
			
			if((bld.state==BaseLayoutData.STATE_LOADED || bld.state==BaseLayoutData.STATE_REPLACED_SON) && bld.layBase==null){
				bld.loadData(this);
			}else if(bld.state==BaseLayoutData.STATE_ADDED && bld.layBase==null){
				bld.loadData(this);
				bld.state = BaseLayoutData.STATE_LOADED;
			}
			
		}
		
	}
	
	/**
	 * Delete the reference to elements that we don't want to use more
	 */
	public void unloadData(){
		
		for(int i=listLayBaseData.size()-1; i>=0; i--){
			listLayBaseData.get(i).unloadData();
		}
		
		if(this.layBase!=null){
			this.layBase.freeMem();
			this.layBase.data = null;
			this.layBase.view = null;
			this.layBase = null;
		}
		
		this.layBaseActivity = null;
		Runtime.getRuntime().gc();
		
	}
	
	//-------- WORKING WITH LIST OF DATAS -------
	
	/**
     * Get the last fragment in the stack
     * @return BaseFragmentData - last data in the stack
     */
	protected BaseLayoutData getLastLayoutData(boolean withReplacement){
		
		BaseLayoutData dataReturn = null;
		
		//
		for(int i=listLayBaseData.size()-1; i>=0; i--){
			BaseLayoutData data = listLayBaseData.get(i);
			BaseLayoutData dataChildren = data.getLastLayoutData(withReplacement);
			if(dataChildren!=null){
				dataReturn = dataChildren;
				break;
			}else if(!withReplacement || data.getNextLayoutDataInStack()!=null){
				dataReturn = data;
				break;
			}
		}
		
		//return the last data from all stacks
		return dataReturn;
	}
	
	/**
     * Return the next data in the stack with the same container
     * @param idContainer - int id of container 
     * @param baseData - BaseLayoutData that is the reference to search the next one
     * @return BaseLayoutData or null if not found
     */
    private BaseLayoutData getNextLayoutDataInStack(){
    	
    	//first search the base data
    	for(int i=dataParent.listLayBaseData.size()-1; i>=0; i--){
    		BaseLayoutData data = dataParent.listLayBaseData.get(i);
			if(data.idContainer==idContainer && data!=this){
    			return data;
    		}
			
    	}
    	
    	//return BaseFragment found
    	return null;
    	
    }
    
    protected boolean checkKeyPressed(int keyCode, KeyEvent event){
    	
    	//check if this is not the activity
    	if(this.layBase==null || this.dataParent==null){
    		return false;
    	}
    	
    	//ask to this baseLayout if want to catch the event
    	if(this.layBase.keyPressed(keyCode, event)){
    		return true;
    	}
    	
    	//if not cached return parent 
    	return this.dataParent.checkKeyPressed(keyCode, event);
    }
		

	//---------- PARCELABLE ---------
	
	public static final Parcelable.Creator<BaseLayoutData> CREATOR = new Parcelable.Creator<BaseLayoutData>() {
		@Override
		public BaseLayoutData createFromParcel(Parcel parcel) {
			
			//base data
			BaseLayoutData frgBaseData = new BaseLayoutData(parcel);
			return frgBaseData;
		}

		@Override
		public BaseLayoutData[] newArray(int size) {
			return new BaseLayoutData[size];
		}
	};
	
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		
		//save data
		parcel.writeString(this.className);
		parcel.writeString(this.tag);
		parcel.writeInt(this.idContainer);
		parcel.writeValue(forceAnimation);
		parcel.writeBundle(this.bundle);
		parcel.writeInt(this.state);
		parcel.writeList(this.listLayBaseData);
		
		//save data animations
		parcel.writeInt(animationLoadShowId);
		parcel.writeInt(animationLoadHideId);
		parcel.writeInt(animationUnloadShowId);
		parcel.writeInt(animationUnloadHideId);
		parcel.writeInt(animationModalShowId);
		parcel.writeInt(animationModalHideId);
		
		
	}
	
	private BaseLayoutData(Parcel parcel) {
		
		//load data
        this.className = parcel.readString();
        this.tag = parcel.readString();
        this.idContainer = parcel.readInt();
        this.forceAnimation = (Boolean) parcel.readValue(Boolean.class.getClassLoader());
        this.bundle = parcel.readBundle();
        this.state = parcel.readInt();
        parcel.readList(this.listLayBaseData, BaseLayoutData.class.getClassLoader());
        
        //load data from animations
        this.animationLoadShowId = parcel.readInt();
        this.animationLoadHideId = parcel.readInt();
        this.animationUnloadShowId = parcel.readInt();
        this.animationUnloadHideId = parcel.readInt();
        this.animationModalShowId = parcel.readInt();
        this.animationModalHideId = parcel.readInt();
    }
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public BaseLayoutData clone(){
		BaseLayoutData layBaseData = new BaseLayoutData();
		layBaseData.className = this.className;
		layBaseData.tag = this.tag;
		layBaseData.idContainer = this.idContainer;
		layBaseData.forceAnimation = this.forceAnimation;
		layBaseData.bundle = this.bundle;
		layBaseData.state = this.state;
		
		//animations
		layBaseData.animationLoadShowId = this.animationLoadShowId;
		layBaseData.animationLoadHideId = this.animationLoadHideId;
		layBaseData.animationUnloadShowId = this.animationUnloadShowId;
		layBaseData.animationUnloadHideId = this.animationUnloadHideId;
		layBaseData.animationModalShowId = this.animationModalShowId;
		layBaseData.animationModalHideId = this.animationModalHideId;
		
		return layBaseData;
	}

	public BaseLayout getLayBase() {
		return layBase;
	}

	public BaseLayoutData getDataParent() {
		return dataParent;
	}
	
	public int getNumLayoutsInStack(){
		return listLayBaseData.size();
	}
	
	//------------------- ANIMATIONS --------------------
	
	
	public static Animation getAnimation(int typeAnimation){
		
		//animation to return
		Animation anim = null;
		
		//check if it is a translate animation
		anim = getTranslateAnimation(typeAnimation);
		
		//if no translate check if it is alpha
		if(anim==null){
			anim = getAlphaAnimation(typeAnimation);
		}
		
		//set the time
		if(anim!=null){
			anim.setDuration(ANIMATION_DURATION);
//			view.setAnimation(anim);
		}
		
		//return the animation
		return anim;
	}
	
	private static float[] getTranslateAnimationData(int typeAnimation){
		
		//variables to move
		float xIni = 0.0f;
		float xEnd = 0.0f;
		float yIni = 0.0f;
		float yEnd = 0.0f;
		
		switch(typeAnimation){
		case BaseLayout.ANIM_SLIDE_IN_RIGHT: xIni = 1.0f;  break;
		case BaseLayout.ANIM_SLIDE_OUT_RIGHT: xEnd = 1.0f; break;
		case BaseLayout.ANIM_SLIDE_IN_LEFT: xIni = -1.0f; break;
		case BaseLayout.ANIM_SLIDE_OUT_LEFT: xEnd = -1.0f; break;
		case BaseLayout.ANIM_SLIDE_IN_BOTTOM: yIni = 1.0f; break;
		case BaseLayout.ANIM_SLIDE_OUT_BOTTOM: yEnd = 1.0f; break;
		case BaseLayout.ANIM_SLIDE_IN_TOP: yIni = -1.0f; break;
		case BaseLayout.ANIM_SLIDE_OUT_TOP: yEnd = -1.0f; break;
		}
		
		if(xIni!=0 || xEnd!=0 || yIni!=0 || yEnd!=0){
			return new float[]{xIni, xEnd, yIni, yEnd};
		}else{
			return null;
		}
	}
	
	private static Animation getTranslateAnimation(int typeAnimation){
		
		//animation
		TranslateAnimation tAnim =  null;
		
		float[] values = getTranslateAnimationData(typeAnimation);
		if(values!=null){
			tAnim = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, values[0], Animation.RELATIVE_TO_PARENT, values[1],
					Animation.RELATIVE_TO_PARENT, values[2], Animation.RELATIVE_TO_PARENT, values[3]); 
		}
		
		//return animation
		return tAnim;
	}
	
	
	
	private static Animation getAlphaAnimation(int typeAnimation){
		//animation
		AlphaAnimation tAnim =  null;
		
		switch(typeAnimation){
		case BaseLayout.ANIM_ALPHA_0_100: tAnim = new AlphaAnimation(0.0f, 1.0f); break;
		case BaseLayout.ANIM_ALPHA_100_0: tAnim = new AlphaAnimation(1.0f, 0.0f); break;
		}
		
		//return animation
		return tAnim;
	}
	
}
