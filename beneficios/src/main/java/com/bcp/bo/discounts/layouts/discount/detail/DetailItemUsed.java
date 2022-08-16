package com.bcp.bo.discounts.layouts.discount.detail;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.layouts.discount.detail.DetailImageLoader.DetailImageLoaderInterface;
import com.bcp.bo.discounts.views.CustomImageView;

import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.Promotion;

@SuppressLint("ViewConstructor")
public class DetailItemUsed extends BaseLayout implements DetailImageLoaderInterface {
	
	/**
	 * Data source.
	 */
	private Promotion _promotion;
	
	/**
	 * Views 
	 */ 
	private ImageButton ibFacebook;
	private ImageButton ibTwitter;
	private TextView tvCompany;
	private TextView tvInfo;
	private TextView tvPercentDiscount;
	
	private CustomImageView civInfo;
//	private ImageView ivLoading;
	
	/** Animation for loading **/
//	private AnimationDrawable loadingAnimation;
	
	/** 
	 * Builder 
	 * @param baseLayoutActivity
	 */
	public DetailItemUsed(BaseLayoutActivity baseLayoutActivity) {
		super(baseLayoutActivity); 
	}
	
	/**
	 * Set the discount.
	 * @param promotion The discount.
	 */
	public void setDiscount(Promotion promotion) {
		_promotion=promotion;
	}
	
	//////////////////////////////////// 
	//   BASELAYOUT INHERIT METHODS   // 
	//////////////////////////////////// 
    /**
	 * Must return the activity to inflate, the XML given by the R.layout.
	 * @return R.layout. associated to this layout
	 */
	@Override
	protected int getXMLToInflate() { 
		return R.layout.lay_dsc_detail_item_used;
	} 
	
    /**
	 * Initialize views of layout: findViewById(R.id. )
	 */
	@Override
	protected void dataReceived(Bundle extras) {} 
	
	/**
	 * Must save the data required to be received when activity is resumed
	 * extras.putString(...)
	 * @param extras
	 */
	@Override
	protected void dataToSave(Bundle extras) {
	} 
	
	/**
	 * Must save the data required to be received when activity is going to return something
	 * extras.putString(...)
	 * @param extras
	 */
	protected void dataToReturn(Bundle extras) {
	}
	
    /**
	 * Initialize views of layout: findViewById(R.id. )
	 */
	@Override
	protected void initViews() { 
		// Buttons
		ibFacebook = (ImageButton) view.findViewById(R.id.imageButton_Facebook);
		ibTwitter = (ImageButton) view.findViewById(R.id.imageButton_Twitter);
		
		tvCompany = (TextView) view.findViewById(R.id.tvCompany);
		tvInfo = (TextView) view.findViewById(R.id.tvRestrictionMessage);
		tvPercentDiscount = (TextView) view.findViewById(R.id.tvPercentDiscount);
		
		//image top
		civInfo = (CustomImageView) view.findViewById(R.id.civInfo);
//		ivLoading = (ImageView) view.findViewById(R.id.ivLoading);
	} 
	
	/**
	 * Initialize the base layouts, add all base layouts here
	 */
	protected void initBaseLayouts() {} 
	
	/**
	 * Initialize values of fields, texts, data... v.setText(R.string. )
	 */
	@Override 
	protected void initValues() {
		final Commerce commerce=_promotion.getCommerce();
		tvCompany.setText(commerce.Name);
		tvPercentDiscount.setText(_promotion.DiscountBCP.add(_promotion.DiscountCommerce) +view.getResources().getString(R.string.PERCENT_SYMBOL));
		tvInfo.setText(view.getResources().getString(R.string.DSC_DETAIL_DISCOUNT_USED));
		
		//set the default image
		Drawable d = view.getResources().getDrawable(R.drawable.dsc_detail_ticket_img_top);
		civInfo.setDrawable(d);
		ViewGroup.LayoutParams vgrl = civInfo.getLayoutParams();
		vgrl.width = d.getIntrinsicWidth();
		vgrl.height = d.getIntrinsicHeight();
	} 
	
	/**
	 * Initialize all the listeners need for the values (touches, gps, etc... )
	 */
	@Override
	protected void initListeners() {
		ibFacebook.setOnClickListener(clickListener);
		ibTwitter.setOnClickListener(clickListener);
		
		//local broadcast to repaint
		Utils.registerBroadcastRepaint(getBaseLayoutActivity(), Promotion.INTENT_FILTER_DISCOUNT_IMAGE_DETAIL_REFRESH, repaintBroadcastReceiver);
	} 
	
	/**
	 * Initialize everything you need after the layout has been created
	 */
	@Override
	protected void init() {
		/*if( civInfo.getTag() != null && ((Integer)civInfo.getTag()).compareTo(promotion.getId()) == 0 ){
			civInfo.post(new Runnable(){ public void run(){
				civInfo.setDrawable(Drawable.createFromPath(promotion.getPortraitImage() + ServiceGenericReceiver.MASK_SUFIX));
			}});
		}*/
	} 
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		//unregister the repaint broadcast
		Utils.unregisterBroadcastRepaint(getBaseLayoutActivity(), repaintBroadcastReceiver);
	}
	
	
	private BroadcastReceiver repaintBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			//refresh image if id is correct 
			int idDiscount = intent.getExtras().getInt(Promotion.KEY_PRODUCT_ID_IMAGE_REFRESH);
			/*if(idDiscount==promotion.getId()){
				init();
			}*/
		}
	};
	
	/**
	 * Get the id of the discount
	 * @return
	 */
	/*public int getIdDiscount(){
		return Integer.parseInt(ticket.getId());
	}*/
	
	/**
	 * Load again the image on the top of discount
	 */
	public void refreshImage(){
		
		civInfo.setDrawable(civInfo.getDrawable());
		
	}
	
	/**
	 * Receive the events from buttons created in XML assigned as this name
	 * @param view - View clicked
	 */
	@Override
	public void click(View view) {
		final int id = view.getId();
		if (id == R.id.imageButton_Facebook) {
			executeShareOnFacebook();
		} else if(id == R.id.imageButton_Twitter) {
			executeShareOnTwitter();
		}
	} 

	private void executeShareOnFacebook() {	
//		new DscDetailFacebookDialog(getBaseLayoutActivity());
	}
	
	private void executeShareOnTwitter() {
//		new DscDetailTwitterDialog(getBaseLayoutActivity());
	}
	
	/**
     * Receive the events from other activities
     * @param data - Intent with data returned
     * @return true if we want to show this layout from the stack or false if we delete it and go to next one
     */
	@Override
	public boolean unloadLayoutResult(Bundle data) { 
		return false; 
	}

	@Override
	protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {}
	
	@Override
	protected boolean keyPressed(int keyCode, KeyEvent event) {
		return false;
	}
	
	/**
	 * Calls onResume to init view as if loadlayout was called
	 */
	public void initWithOnResume() {
		super.onResume();
	}
	
	/**
	 * Free all the memory used in the Layout
	 */
	@Override
	public void freeMem(){
		super.freeMem();
		//free memory of info image
		civInfo.freeMem();
	}
	
	@Override
	public CustomImageView findCustomImageView() {
		return civInfo;
	}
	
	@Override
	public void setBackgroundDrawableIvInfo(final Drawable dMixed) {
		
		civInfo.setDrawable(dMixed);
		
//		loadingAnimation.stop();
//		ivLoading.setVisibility(View.INVISIBLE);
	}
}