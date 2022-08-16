package com.bcp.bo.discounts.layouts.home;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.layouts.discount.DscHomeListMapLayout;
import com.bcp.bo.discounts.layouts.discount.detail.DetailLayout;
import com.bcp.bo.discounts.layouts.menu.MenuLeftLayout;
import com.bcp.bo.discounts.util.Util;
import com.slidingmenu.lib.SlidingMenu;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bcp.bo.service.Sender;
import bcp.bo.service.ServiceLauncher;
import bcp.bo.service.model.request.ListPromotions;
import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.City;
import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.Contact;
import bcp.bo.service.model.response.PrivateToken;
import bcp.bo.service.model.response.Promotion;
import bcp.bo.service.model.response.Response;

public class HomeLayout extends BaseLayout {
    // category selected key
    private static final String CATEGORY_SELECTED = "CATEGORY_SELECTED";
    private static final String DETAIL_SHOWING = "DETAIL_SHOWING";

    private static final int CLOSE_AND_LOGOUT = 1;

    //buttons popup
    private static final int ID_CANCEL = 301;
    private static final int ID_SECTION_EXPIRED = 100;
    private static final int ID_SESSION_EXPIRED = 200;
    private static final int ID_TURNONGPS = 300;

    //views
    private ImageButton bLeftMenu;
    private TextView tvLeftTitle;

    //sliding menu
    private SlidingMenu slidingMenu;

    /**
     * Data source
     **/
    private AsyncTask currentTask;
    private List<Promotion> _promotions = new ArrayList<>();
    private List<Contact> _contacts = new ArrayList<>();

    private List<City> _cities;
    private List<Category> _categories;
    private List<Commerce> _commerces;
    private PrivateToken _privateToken;
    private String _document;
    private String _amountSaved;

    //reference to layouts
    private MenuLeftLayout menuLeftLayout = null;
    private DscHomeListMapLayout homeListMapLayout = null;

    /**
     * Flag to know if it is loading
     **/
    private boolean isLoading = false;
    private boolean _showToggleOnLoadCategory = false;

    /**
     * Flag to know if detail view is showed
     **/
    private boolean isDetailShowing = false;

    //category selected to show discounts when connection finishes
    private Category categorySelected = null;
    private Commerce commerceContacts = null;

    //dialog to get check state from popup
    private Dialog dialogCheck = null;

    public HomeLayout(BaseLayoutActivity baseLayoutActivity, List<City> cities, List<Category> categories, List<Commerce> commerces, PrivateToken privateToken, String document, String amountSaved) {
        super(baseLayoutActivity);
        _cities = cities;
        _categories = categories;
        _commerces = commerces;
        _privateToken = privateToken;
        _document = document;
        _amountSaved = amountSaved;
    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_home;
    }

    @Override
    protected void initViews() {
        //Get reference to bar view objects.
        tvLeftTitle = (TextView) view.findViewById(R.id.tvLeftTitle);
        bLeftMenu = (ImageButton) view.findViewById(R.id.bLeftMenu);

        // Initializes sliding menu.
        initSlidingMenu();
    }

    /**
     * This method is called from listmapLayout to advice this view that stack has changed and
     * have to get new references to views
     */
    public void restoreView(DscHomeListMapLayout homeListMapLayout) {

        //references to baselayouts
        this.homeListMapLayout = homeListMapLayout;

        //restore bar
        setHomeBar(null, null);
    }

    @Override
    protected void initBaseLayouts() {

        //list and map
        homeListMapLayout = new DscHomeListMapLayout(getBaseLayoutActivity());
        loadLayout(R.id.fragLayout, homeListMapLayout, true);

        // Menus
        menuLeftLayout = new MenuLeftLayout(getBaseLayoutActivity(), _cities, _categories, _privateToken, _document, _amountSaved);
        loadLayout(R.id.rlMenuContainer, menuLeftLayout, true);
    }

    @Override
    protected void initValues() {


        //references to baselayouts
        homeListMapLayout = (DscHomeListMapLayout) findBaseLayoutSonByName(DscHomeListMapLayout.class);
        if (categorySelected == null) {

            if (homeListMapLayout != null) {
                List<Promotion> tickets = new ArrayList<>();
                List<Contact> contacts = new ArrayList<>();

                _promotions = new ArrayList<>();
                _contacts = new ArrayList<>();
                for (Commerce commerce : _commerces) {
                    for (Promotion promotion : commerce.Promotions) {
                        _promotions.add(promotion);
                    }
                    for (Contact contact : commerce.Contacts) {
                        _contacts.add(contact);
                    }
                }
                homeListMapLayout.loadCities(_cities);
//				homeListMapLayout.loadPromotionsTop(_promotions,contacts);
            }
        } else {
            //load the list of discounts
            if (homeListMapLayout != null) {
                List<Promotion> tickets = new ArrayList<>();
                List<Contact> contacts = new ArrayList<>();

                _promotions = new ArrayList<>();
                _contacts = new ArrayList<>();
                for (Commerce commerce : _commerces) {
                    for (Promotion promotion : commerce.Promotions) {
                        _promotions.add(promotion);
                    }
                    for (Contact contact : commerce.Contacts) {
                        _contacts.add(contact);
                    }
                }
                homeListMapLayout.loadPromotions(categorySelected, _promotions, contacts);
            }
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void init() {
        // ALERT AMOUNT SAVED 20200214
        AlertMsg.showCheckDialog(getBaseLayoutActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaseLayoutActivity().dismissDialog();
                LocationManager locationManager = (LocationManager) getBaseLayoutActivity().getSystemService(Service.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertMsg.showErrorWarningDialog(getBaseLayoutActivity(), getString(R.string.DSC_MAP_LOCATION_ACTIVATION), false, clickListener, getString(R.string.DSC_GO_TO_SETTINGS), getString(R.string.CANCEL), ID_TURNONGPS, ID_CANCEL, true);
                }
            }
        }, "Hasta el momento ahorraste Bs. " + _amountSaved);
        // END ALERT AMOUNT SAVED 20200214
    }

    @Override
    public void click(View view) {
        int id = view.getId();
        if (id == R.id.bLeftMenu) {
            toggleLeftMenu();
        } else if (id == R.id.imageButton_Close) {
            AlertMsg.showOkCancelDialog(getBaseLayoutActivity(), _privateToken.FullName, _document,
                    getString(R.string.HOM_CLOSE_DIALOG_MESSAGE), R.drawable.dlg_logout_icon_user, clickListener);
        } else if (id == R.id.vActionAccept) {
            //dismiss dialog and show loading
            getBaseLayoutActivity().dismissDialog();
            logout();
        } else if (id == R.id.vCheckActionAccept) {
            //update the state of the user with the check, if checked
            if (dialogCheck != null) {
                CheckBox box = (CheckBox) dialogCheck.findViewById(R.id.cbMemo);
            }
            getBaseLayoutActivity().dismissDialog();
            dialogCheck = null;
        } else if (id == CLOSE_AND_LOGOUT) {
            logout();
        } else if (id == ID_SECTION_EXPIRED) {
            logout();
        } else if (id == ID_SESSION_EXPIRED) {
            logout();
        } else if (id == ID_TURNONGPS) {
            final Intent intentGPS = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getBaseLayoutActivity()
                    .startActivity(intentGPS);
        }
    }

    /**
     * Logout removing all user data
     */
    private void logout() {
        Log.d("LOGOUT", "A");
        //show loading
        showLoading(R.string.LOADING_LOGOUT);

        //clear data
        //userLogedData.clearUserData();

        // logout social
        ((StartActivity) getBaseLayoutActivity()).logoutFacebook();
        ((StartActivity) getBaseLayoutActivity()).getTwitterApi().logout();
        userLogout();
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    protected void dataReceived(Bundle extras) {
        if (extras.containsKey(CATEGORY_SELECTED)) {
            categorySelected = (Category) extras.getSerializable(CATEGORY_SELECTED);
        }

        if (extras.containsKey(DETAIL_SHOWING)) {
            isDetailShowing = extras.getBoolean(DETAIL_SHOWING);
        }
    }

    @Override
    protected void dataToSave(Bundle extras) {
//		if (categorySelected != null) {
//			extras.putSerializable(CATEGORY_SELECTED, categorySelected);
//		}

        extras.putBoolean(DETAIL_SHOWING, isDetailShowing);
    }

    @Override
    protected void dataToReturn(Bundle extras) {

    }

    @Override
    protected boolean keyPressed(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && isLoading) {
                return true;
            } else {
                if (slidingMenu.isMenuShowing()) {
                    slidingMenu.toggle(true);
                } else {
                    AlertMsg.showOkCancelDialog(
                            getBaseLayoutActivity(), _privateToken.FullName, _document,
                            getString(R.string.HOM_CLOSE_DIALOG_MESSAGE),
                            R.drawable.dlg_logout_icon_user,
                            clickListener);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean unloadLayoutResult(Bundle data) {
        return false;
    }

    public void showLoading(int messageId) {

        //activate flag
        isLoading = true;

        AlertMsg.showLoadingDialog(getBaseLayoutActivity(), messageId);
    }

    public void hideLoading() {

        //deactivate flag
        isLoading = false;

        getBaseLayoutActivity().dismissDialog();
    }

    //--------------- LOAD DATA ---------------------

    /**
     * Load a category.
     *
     * @param category Category id to load.
     */
    public void loadCategory(Category category, boolean toggle) {
        //save the category selected
        categorySelected = category;

        //close menu
        if (_showToggleOnLoadCategory && toggle)
            slidingMenu.toggle();
        //toggleLeftMenu();

        // list is showing
        isDetailShowing = false;

        //ask for discounts
        AlertMsg.showLoadingDialog(getBaseLayoutActivity(), getString(R.string.LOADING_CATEGORY) + " " + category.Name);

        //LOAD CATEGORY
        ListPromotions listPromotions = new ListPromotions();
        listPromotions.set(BaseActivity.publicPrivateTokenCIC);
        listPromotions.IdCategory = String.valueOf(category.Id);

        SLauncher.loadCategory(listPromotions, this);
        //BaseActivity.SLauncher.Send(listPromotions, ServiceLauncher.UserPromotions, this);

        //ServiceLauncher.executePromotions(getBaseLayoutActivity().getBaseContext(),categorySelected.getJSONforPromotions(userLogedData),this);
        _showToggleOnLoadCategory = true;
    }

    /**
     * Show detail of discount
     *
     * @param promotion
     */
    public void showPromotion(Promotion promotion, List<Promotion> promotions) {
        //CANCEL CURRENT TASK
        if (currentTask != null && currentTask.getStatus().equals(Status.RUNNING)) {
            currentTask.cancel(true);
        }
        //create the screen with detail of discount
        DetailLayout detailLayout = new DetailLayout(getBaseLayoutActivity());
        detailLayout.loadPromotion(categorySelected, promotion, promotions);
        loadLayout(R.id.fragLayout, detailLayout);

        // detail is showing
        isDetailShowing = true;

    }

    public void showCities() {
        slidingMenu.toggle();
        homeListMapLayout.loadCities(_cities);
    }

    public void loadCategories(boolean showToggleOnLoadCategory) {
        _showToggleOnLoadCategory = showToggleOnLoadCategory;
        AlertMsg.showLoadingDialog(getBaseLayoutActivity(), getString(R.string.LOADING_CATEGORY));
        SLauncher.loadCategories(BaseActivity.publicPrivateTokenCIC, this);
    }
    //--------------- MENU BAR ------------------

    public void setHomeBar(String title, View.OnClickListener clickListener) {
        //set the title
        if (title != null) {
            tvLeftTitle.setText(title);
            tvLeftTitle.setVisibility(View.VISIBLE);
        }

        //set the menu button
        if (clickListener != null) {
            bLeftMenu.setOnClickListener(clickListener);
            bLeftMenu.setImageResource(R.drawable.hom_bmenu_ico_goback);

            //disable sliding menu
            slidingMenu.setSlidingEnabled(false);

        } else {
            bLeftMenu.setOnClickListener(this.clickListener);
            bLeftMenu.setImageResource(R.drawable.hom_bmenu_ico_left);

            //enable sliding menu
            slidingMenu.setSlidingEnabled(true);
        }
    }

    //--------------- SLIDING MENU ---------------------

    /**
     * Open/Close left menu.
     */
    public void toggleLeftMenu() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle(true);
        } else {
            slidingMenu.showMenu(true);
        }
    }

    /**
     * Initializes sliding menu.
     */
    private void initSlidingMenu() {

        // There is no xml layout about it.
        // There is a default resource values about it.
        // Attachs the menu to the activity.

        slidingMenu = new SlidingMenu(this, SlidingMenu.SLIDING_CONTENT);

        // Offset
        slidingMenu.setBehindOffset(view.getResources().getDimensionPixelSize(R.dimen.default_slidingmenu_offset));
        // Menus
        slidingMenu.setMenu(R.layout.lay_hom_menu_left);

        // Fade
        final float fade = Float.valueOf(view.getResources().getString(R.string.default_slidingmenu_fadeDegree));
        if (fade > 0) {
            slidingMenu.setFadeEnabled(true);
            slidingMenu.setFadeDegree(fade);
        } else {
            slidingMenu.setFadeEnabled(false);
        }
        // Touch mode
        final String touchMode = view.getResources().getString(R.string.default_slidingmenu_touchMode);
        if ("fullscreen".compareToIgnoreCase(touchMode) == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        } else if ("margin".compareToIgnoreCase(touchMode) == 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
        }
        // Mode
        slidingMenu.setMode(SlidingMenu.LEFT);

        // Shadow
        final int shadowWidth = view.getResources().getDimensionPixelOffset(R.dimen.default_slidingmenu_shadowWidth);
        if (shadowWidth > 0) {
            slidingMenu.setShadowDrawable(R.drawable.default_slidingmenu_leftShadow);
            slidingMenu.setSecondaryShadowDrawable(R.drawable.default_slidingmenu_rightShadow);
            slidingMenu.setShadowWidth(shadowWidth);
        }

        //The sliding menu touch event is only enabled in menu bar screen zone.
        slidingMenu.setHeightEnabledZone(getBaseLayoutActivity().getWindow().getDecorView().getHeight());//view.getResources().getDimensionPixelSize(R.dimen.hom_bmenu_side) );
        slidingMenu.setSlidingEnabled(true);
    }

    //SWIPE REFRESH
    public void loadTopPromotions() {
        Util.StopTask(currentTask);
        SLauncher.loadPromotionsTop(BaseActivity.publicPrivateTokenCIC, this);
        //currentTask=ServiceLauncher.executeTopPromotions(getBaseLayoutActivity().getBaseContext(),Category.getJSONforTopPromotions(userLogedData),this);
    }

    private void updateReceived(boolean isTopPromotions) {
        _promotions = new ArrayList<>();
        _contacts = new ArrayList<>();
        for (Commerce commerce : _commerces) {
            for (Promotion promotion : commerce.Promotions) {
                _promotions.add(promotion);
            }
            for (Contact contact : commerce.Contacts) {
                _contacts.add(contact);
            }
        }
        //load the discounts into the list and the map
        if (isTopPromotions) {
            //setHomeBar(getString(R.string.DSC_TOP_PROMOTIONS), null);
            homeListMapLayout.loadPromotionsTop(_promotions, _contacts);
        } else {
            //setHomeBar(categorySelected.Name, null);
            homeListMapLayout.loadPromotions(categorySelected, _promotions, _contacts);
        }
    }

    private void userLogout() {
        //hide loading
        getBaseLayoutActivity().dismissDialog();

        //clean and back
        //userLogedData.clearUserData();
        unload(null);
    }

    public void goToLoginForced() {
        //stop loading
        userLogout();
    }

    @Override
    public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        super.onCompleted(status, message, service, jsonObject);
        getBaseLayoutActivity().dismissDialog();
        if (status) {
            if (service.equals(ServiceLauncher.UserPromotions)) {
                Response<List<Commerce>> responsePromotions = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<Commerce>>>() {
                });
                if (responsePromotions.Data != null) {
                    _commerces = responsePromotions.Data;
                } else {
                    _commerces = new ArrayList<>();
                }
                updateReceived(false);
                /*if(responsePromotions.State){
                    _commerces=responsePromotions.Data;
					updateReceived(false);
				}
				else{
					getBaseLayoutActivity().dismissDialog();
					AlertMsg.showErrorDialog(getBaseLayoutActivity(), message);
				}*/
            } else if (service.equals(ServiceLauncher.UserCategories)) {
                Response<List<Category>> responseCategories = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<Category>>>() {
                });
                if (responseCategories.State) {
                    _categories = responseCategories.Data;
                    if (responseCategories.Data.size() > 0) {
                        menuLeftLayout.reloadCategories(_categories);
                        Category category = _categories.get(0);
                        loadCategory(category, true);
//                        homeListMapLayout.loadPromotions(category,category.);
                    } else {
                        getBaseLayoutActivity().dismissDialog();
                        AlertMsg.showWarningDialog(getBaseLayoutActivity(), R.string.ERROR_SECTION_EXPIRED);
                    }

                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseCategories.Message);
                }
            } else if (service.equals(ServiceLauncher.UserPromotionsTop)) {
                Response<List<Commerce>> responsePromotions = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<Commerce>>>() {
                });
                if (responsePromotions.State) {
                    _commerces = responsePromotions.Data;
                    updateReceived(true);
                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), message);
                }
            }
        } else {//ERROR DE CONEXION
            AlertMsg.showErrorDialog(getBaseLayoutActivity(), message);
        }
    }

    @Override
    public void onCancelled() {

    }
}