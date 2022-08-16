package com.bcp.bo.discounts.layouts.discount;

import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.layouts.discount.list.DscListLayout;
import com.bcp.bo.discounts.layouts.discount.map.DscMapLayout;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.views.CustomSliderLLayout;
import com.bcp.bo.discounts.views.CustomSliderLLayout.CustomSliderListener;

import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.City;
import bcp.bo.service.model.response.Contact;
import bcp.bo.service.model.response.Promotion;

public class DscHomeListMapLayout extends BaseLayout{
	private static final String DSC_HOME_SLIDER_OPENED = "DSC_HOME_SLIDER_OPENED";
	
	//reference to base layouts
	private DscListLayout listLayout = null;
	private DscMapLayout mapLayout = null;
	//custom slider for map
	private CustomSliderLLayout customMapSlider;
	/**
	 * Flag to know if slider is open or close when return from other layout.
	 * Close by default: init.
	 */
	private boolean customMapSliderOpened = false;
	/** Extras received, used to set map later **/
	private Bundle extrasReceived = null;
	
	public DscHomeListMapLayout(BaseLayoutActivity baseLayoutActivity) {
		super(baseLayoutActivity);
	}

	@Override
	protected int getXMLToInflate() {
		return R.layout.lay_dsc_home_listmap;
	}

	@Override
	protected void initViews() {
		customMapSlider = (CustomSliderLLayout) view.findViewById(R.id.csllMap);
		customMapSlider.setPullView(R.drawable.dsc_list_handle, FrameLayout.LayoutParams.MATCH_PARENT);
		
		//create view map
		mapLayout = new DscMapLayout(getBaseLayoutActivity(), this);
		mapLayout.resume(extrasReceived);
		
		//remove content view if there was one
		customMapSlider.removeContentView();
		
		if(customMapSliderOpened && customMapSlider.getContentView() == null){
			customMapSlider.setContentView(mapLayout.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		}
	
	}

	@Override
	protected void initBaseLayouts() {
		//List
		listLayout = new DscListLayout(getBaseLayoutActivity());
		loadLayout(R.id.vDscList, listLayout, true); 
	}

	@Override
	protected void initValues() {
		//configure customMapSlider for version of Android
		if(Build.VERSION.SDK_INT<14){
			customMapSlider.setAnimatedTouch(false);
			customMapSlider.setAnimated(false);
		}
		
		//get reference to list layout
		listLayout = (DscListLayout) findBaseLayoutSonByName(DscListLayout.class);
		
		//check if slider has to be opened
		if(customMapSliderOpened){
			customMapSlider.forceOpen(false);
		}
	}

	@Override
	protected void initListeners() {
		// Captures sliding events
		customMapSlider.setCustomSliderListener(customSliderListener);
	}

	@Override
	protected void init() {
		
		//restore view for this one
		HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
		homeLayout.restoreView(this);
		
	}
	
	@Override
	protected void onDestroy(){

		//call to map because it is not added to stack
		mapLayout.destroy();
		mapLayout = null;
		
		super.onDestroy();
	}

	@Override
	public void click(View view) {
		
	}

	@Override
	protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {
		
	}

	@Override
	protected void dataReceived(Bundle extras) {
		customMapSliderOpened = extras.getBoolean(DSC_HOME_SLIDER_OPENED);
		
		//save extras to set map later
		extrasReceived = extras;
	}

	@Override
	protected void dataToSave(Bundle extras) {
		extras.putBoolean(DSC_HOME_SLIDER_OPENED, customMapSliderOpened);
		
		mapLayout.loadDataToSave(extras);
	}

	@Override
	protected void dataToReturn(Bundle extras) {
		
	}
	
	@Override
	protected boolean keyPressed(int keyCode, KeyEvent event) {

		return false;
	}

	@Override
	public boolean unloadLayoutResult(Bundle data) {

		return false;
	}
	
	//----------- LOAD DATA --------------

	public void loadCities(List<City> cities){
		listLayout.loadCities(cities);
	}
	public void loadPromotionsTop(List<Promotion> promotions,List<Contact> contacts){
		//load the list of discounts
		listLayout.loadPromotionsTop(promotions);
		mapLayout.loadProducts(promotions);


		HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
		homeLayout.setHomeBar(getBaseLayoutActivity().getString(R.string.DSC_VIEW_OFFERS_FROM_),null);
	}
	public void loadPromotions(Category category, List<Promotion> promotions,List<Contact> contacts){
		//load the list of discounts
		listLayout.loadPromotions(category, promotions);
		mapLayout.loadProducts(promotions);


		HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
		homeLayout.setHomeBar(category.Name,null);
	}
	/**
	 * Load detail in home layout
	 * @param promotion
	 * @param promotions
	 */
	public void loadProductDetail(Promotion promotion, List<Promotion> promotions){
		//call to home layout with this discount
		HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
		homeLayout.showPromotion(promotion,promotions);
	}

	//------------- SLIDER ---------------
	
	/**
	 * Listen for slider changes.
	 */
	private final CustomSliderListener customSliderListener = new CustomSliderListener(){
		@Override
		public void onSliderAction(int action, int moment) {
			
			if(action == CustomSliderLLayout.ACTION_OPEN) {
				
				if(moment==CustomSliderLLayout.MOMENT_START) {
					if(customMapSlider.getContentView() == null) {
						customMapSlider.setContentView(mapLayout.view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
					}
				}	
								
				customMapSliderOpened = true;
				
			} else {		
				customMapSliderOpened = false;
				
			}
			
		}	
	};
}
