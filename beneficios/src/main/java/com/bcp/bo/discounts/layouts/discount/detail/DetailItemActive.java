package com.bcp.bo.discounts.layouts.discount.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.animations.Flip3dLayouts;
import com.bcp.bo.discounts.animations.Flip3dLayouts.Flip3dLayoutsListener;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Constants;
import com.bcp.bo.discounts.general.StyleApp;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.layouts.discount.detail.DetailImageLoader.DetailImageLoaderInterface;
import com.bcp.bo.discounts.layouts.discount.map.DscMapRouteLayout;
import com.bcp.bo.discounts.layouts.facebook.DscDetailFacebookDialog;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.layouts.twitter.DscDetailTwitterDialog;
import com.bcp.bo.discounts.views.CustomImageView;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.util.List;

import bcp.bo.service.Sender;
import bcp.bo.service.ServiceLauncher;
import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.Contact;
import bcp.bo.service.model.response.PrivateToken;
import bcp.bo.service.model.response.Promotion;
import bcp.bo.service.model.response.Response;

@SuppressLint("ViewConstructor")
public class DetailItemActive extends BaseLayout implements DetailImageLoaderInterface, Flip3dLayoutsListener {
    private static final String IS_VISIBLE_RESTRICTIONS_KEY = "IS_VISIBLE_RESTRICTIONS_KEY";

    /**
     * Data source.
     */
    private Promotion _promotion;
    private Commerce _commerce;
    private float _rating;

    /**
     * Views
     */
    private ImageButton ibInfo;
    private ImageButton ibFacebook;
    private ImageButton ibTwitter;
    private ImageButton ibLocations;
    private ImageButton ibPhone;
    private RatingBar ratingBar;
    private TextView tvTicket;
    private TextView tvCompany;
    private TextView tvShortInfo;
    private TextView tvPercentDiscount;
    private ImageButton ibCloseInfo;
    private TextView tvRestrictionMessage;
    private CustomImageView civInfo;
//	private ImageView ivLoading;
//	private AnimationDrawable loadingAnimation;

    //layouts
    private RelativeLayout rlGen;
    private RelativeLayout rlCardInfo;
    private RelativeLayout rlCardRestrictions;

    /**
     * Flag to block events if animating
     **/
    private boolean animating = false;

    /**
     * Flag to know if restrictions isShowing
     **/
    private boolean isVisibleRestrictions = false;

    /**
     * Reference to parent
     **/
    private DetailLayout detailLayout;


    public DetailItemActive(BaseLayoutActivity baseLayoutActivity, DetailLayout detailLayout) {
        super(baseLayoutActivity);

        this.detailLayout = detailLayout;
    }

    /**
     * Builder
     *
     * @param baseLayoutActivity
     */
    public DetailItemActive(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);
    }

    /**
     * Set the discount.
     *
     * @param promotion The discount.
     */
    public void setDiscount(Promotion promotion) {
        _promotion = promotion;
        _commerce = promotion.getCommerce();
    }

    ////////////////////////////////////
    //   BASELAYOUT INHERIT METHODS   //
    ////////////////////////////////////

    /**
     * Must return the activity to inflate, the XML given by the R.layout.
     *
     * @return R.layout. associated to this layout
     */
    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_detail_item_active;
    }

    /**
     * Initialize views of layout: findViewById(R.id. )
     */
    @Override
    protected void dataReceived(Bundle extras) {
        isVisibleRestrictions = extras.getBoolean(IS_VISIBLE_RESTRICTIONS_KEY);
    }

    /**
     * Must save the data required to be received when activity is resumed
     * extras.putString(...)
     *
     * @param extras
     */
    @Override
    protected void dataToSave(Bundle extras) {
        extras.putBoolean(IS_VISIBLE_RESTRICTIONS_KEY, isVisibleRestrictions);
    }

    /**
     * Must save the data required to be received when activity is going to return something
     * extras.putString(...)
     *
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
        ibInfo = (ImageButton) view.findViewById(R.id.ibInfo);
        ibLocations = (ImageButton) view.findViewById(R.id.ibLocations);
        ibPhone = (ImageButton) view.findViewById(R.id.ibPhone);
        ibCloseInfo = (ImageButton) view.findViewById(R.id.ibCloseInfo);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        // Detail
        tvTicket = (TextView) view.findViewById(R.id.textView_TicketNumber);
        tvCompany = (TextView) view.findViewById(R.id.tvCompany);
        tvShortInfo = (TextView) view.findViewById(R.id.tvShortInfo);
        tvPercentDiscount = (TextView) view.findViewById(R.id.tvPercentDiscount);

        // Restriction
        tvRestrictionMessage = (TextView) view.findViewById(R.id.tvRestrictionMessage);

        //image buttons
        rlGen = (RelativeLayout) view.findViewById(R.id.rlGen);
        rlCardInfo = (RelativeLayout) view.findViewById(R.id.rlCardInfo);
        rlCardRestrictions = (RelativeLayout) view.findViewById(R.id.rlCardRestrictions);

        civInfo = (CustomImageView) view.findViewById(R.id.civInfo);
    }

    /**
     * Initialize the base layouts, add all base layouts here
     */
    protected void initBaseLayouts() {
    }

    /**
     * Initialize values of fields, texts, data... v.setText(R.string. )
     */
    @Override
    protected void initValues() {
        ratingBar.setRating(_promotion.RatingTicket);
        tvTicket.setText(_promotion.NumberTicket);
        tvCompany.setText(_commerce.Name);
        tvPercentDiscount.setText(_promotion.DiscountBCP.add(_promotion.DiscountCommerce) + view.getResources().getString(R.string.PERCENT_SYMBOL));
        //showPhoneIcon();

        tvShortInfo.setText(_promotion.MessagePromotion);
        tvRestrictionMessage.setText(_promotion.DescriptionPromotion);

        //show info for first time
        rlCardRestrictions.setVisibility(View.INVISIBLE);
        rlCardInfo.setVisibility(View.VISIBLE);

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
        ibInfo.setOnClickListener(clickListener);
        ibLocations.setOnClickListener(clickListener);
        ibPhone.setOnClickListener(clickListener);
        ibCloseInfo.setOnClickListener(clickListener);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                _rating = rating;
                AlertMsg.showRating(DetailItemActive.this, _promotion, rating, DetailItemActive.this);
            }
        });

        //local broadcast to repaint
        Utils.registerBroadcastRepaint(getBaseLayoutActivity(), Promotion.INTENT_FILTER_DISCOUNT_IMAGE_DETAIL_REFRESH, repaintBroadcastReceiver);
    }

    /**
     * Initialize everything you need after the layout has been created
     */
    @Override
    protected void init() {

        if (civInfo.getTag() != null && ((Integer) civInfo.getTag()).compareTo(_promotion.Id) == 0) {
            civInfo.post(new Runnable() {
                public void run() {
                    //civInfo.setDrawable(Drawable.createFromPath(promotion.getPortraitImage() + ServiceGenericReceiver.MASK_SUFIX));
                }
            });
        }

        if (isVisibleRestrictions) {
            //direct show
            rlCardInfo.setVisibility(View.INVISIBLE);
            rlCardRestrictions.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregister the repaint broadcast
        Utils.unregisterBroadcastRepaint(getBaseLayoutActivity(), repaintBroadcastReceiver);
    }


    private BroadcastReceiver repaintBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
			/*
			//refresh image if id is correct 
			int idDiscount = intent.getExtras().getInt(Discount.KEY_PRODUCT_ID_IMAGE_REFRESH);
			if(idDiscount==promotion.getId()){
				init();
			}
			*/
        }
    };

    /**
     * Receive the events from buttons created in XML assigned as this name
     *
     * @param view - View clicked
     */
    @Override
    public void click(View view) {
        final int id = view.getId();
        if (id == R.id.ibInfo) {
            executeShowRestriction();
        } else if (id == R.id.ibCloseInfo) {
            executeHideRestriction();
        } else if (id == R.id.imageButton_Facebook) {
            executeShareOnFacebook();
        } else if (id == R.id.imageButton_Twitter) {
            executeShareOnTwitter();
        } else if (id == R.id.ibPhone) {
            if (_commerce.Contacts.size() > 0) {
                AlertMsg.showListDialog(getBaseLayoutActivity(),
                        getString(R.string.DSC_CALL),
                        phonesListAdapter, phoneItemClickListener, R.string.DSC_GO_BACK);
            }
        } else if (id == R.id.ibLocations) {
            if (_commerce.Contacts.size() > 0) {
                AlertMsg.showListDialog(getBaseLayoutActivity(),
                        getString(R.string.DSC_DETAIL_LOCATIONS_DIALOG_TITLE),
                        locationsListAdapter, locationItemClickListener, R.string.DSC_GO_BACK);
            }
        }
    }

    private void executeShareOnFacebook() {
        new DscDetailFacebookDialog(this, _promotion);
    }

    private void executeShareOnTwitter() {
        ((StartActivity) getBaseLayoutActivity()).logoutFacebook();
        //new DscDetailTwitterDialog(this, _promotion);
    }

    @SuppressLint("NewApi")
    private void executeShowRestriction() {
        //show animated
        if (!animating) {
            if (Build.VERSION.SDK_INT >= Constants.SDK_START_ANIMATIONS && rlCardInfo.isHardwareAccelerated()) {
                Log.d("ANIMATION", "OK");
                animating = true;
                Flip3dLayouts flip3dLayouts = new Flip3dLayouts(rlGen);
                flip3dLayouts.flip(rlCardInfo, rlCardRestrictions, Flip3dLayouts.DIRECTION_NEXT, this);

            } else {
                //direct show
                Log.d("ANIMATION", "DIRECT");
                rlCardInfo.setVisibility(View.INVISIBLE);
                rlCardRestrictions.setVisibility(View.VISIBLE);
            }

            isVisibleRestrictions = true;
        }
    }

    @SuppressLint("NewApi")
    private void executeHideRestriction() {
        //show animated
        if (!animating) {
            if (Build.VERSION.SDK_INT >= Constants.SDK_START_ANIMATIONS && rlCardInfo.isHardwareAccelerated()) {
                animating = true;
                Flip3dLayouts flip3dLayouts = new Flip3dLayouts(rlGen);
                flip3dLayouts.flip(rlCardRestrictions, rlCardInfo, Flip3dLayouts.DIRECTION_BACK, this);

            } else {

                //direct show
                rlCardInfo.setVisibility(View.VISIBLE);
                rlCardRestrictions.setVisibility(View.INVISIBLE);
            }

            isVisibleRestrictions = false;
        }
    }

    @Override
    public void onEndFlipAnimation() {
        animating = false;
    }

    /**
     * Receive the events from other activities
     *
     * @param data - Intent with data returned
     * @return true if we want to show this layout from the stack or false if we delete it and go to next one
     */
    @Override
    public boolean unloadLayoutResult(Bundle data) {
        return false;
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {
    }

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


    // ***********************************************
    // *
    // * CALLBACK API
    // *
    // ***********************************************

    /**
     * Free all the memory used in the Layout
     */
    @Override
    public void freeMem() {
        super.freeMem();

        //free memory of info image
        civInfo.freeMem();

    }

    //--------------------- ADAPTER LOCATIONS -------------------------

    /**
     * Called when item location pressed
     **/
    private OnItemClickListener locationItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            //get location
            Contact contact = _commerce.Contacts.get(position);

            //replace layout detail (with pagination)
            DscMapRouteLayout dscMapRouteLayout = new DscMapRouteLayout(getBaseLayoutActivity(), contact);
            detailLayout.replaceLayout(dscMapRouteLayout);
        }

    };

    /**
     * Adapter for popup with locations
     **/
    private ListAdapter locationsListAdapter = new ListAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dlg_list_item, parent, false);
            }
            final Contact contact = getItem(position);

            TextView tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);

            tvCompany.setText(_commerce.Name);
            tvAddress.setText(contact.Address);

            //apply style
            StyleApp.setStyle(convertView);

            return convertView;
        }

        @Override
        public int getCount() {
            return _commerce.Contacts.size();
        }

        @Override
        public Contact getItem(int position) {
            return _commerce.Contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return _commerce.Contacts.size() == 0;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int arg0) {
            return true;
        }
    };
    private ListAdapter phonesListAdapter = new ListAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dlg_list_item, parent, false);
            }
            final Contact contact = getItem(position);

            TextView tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);

            tvCompany.setText(String.valueOf(contact.Phone));
            tvAddress.setText(contact.Address);

            //apply style
            StyleApp.setStyle(convertView);

            return convertView;
        }

        @Override
        public int getCount() {
            return _commerce.Contacts.size();
        }

        @Override
        public Contact getItem(int position) {
            return _commerce.Contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return _commerce.Contacts.size() == 0;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int arg0) {
            return true;
        }
    };
    /**
     * Called when item phone pressed
     **/
    private OnItemClickListener phoneItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            //11/15/2019 Se adiciono la peticion de permisos para llamadas
            if (ContextCompat.checkSelfPermission(getBaseLayoutActivity(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getBaseLayoutActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, 421);
            } else {
                String tel = ((TextView) arg1.findViewById(R.id.tvCompany)).getText().toString();
                Utils.systemPhoneCall(getBaseLayoutActivity(), tel);
            }
        }
    };

    @Override
    public CustomImageView findCustomImageView() {
        return civInfo;
    }

    @Override
    public void setBackgroundDrawableIvInfo(Drawable dMixed) {
        //set the height of the layout to keep it when the image changes
//		ViewGroup.LayoutParams rlp = ivInfo.getLayoutParams();
//		rlp.height = ivInfo.getHeight();
//		rlp.width = ivInfo.getWidth();
//
//		ivInfo.setImageDrawable(null);
//		ivInfo.setBackgroundDrawable(dMixed);	
        civInfo.setDrawable(dMixed);

//		ivInfo.setImageDrawable(dMixed);
//		ivLoading.post(new Runnable(){ public void run(){
//			loadingAnimation.stop();
//		}});
//		ivLoading.setVisibility(View.INVISIBLE);
//		ivInfo.setImageDrawable(null);
    }

    @Override
    public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        super.onCompleted(status, message, service, jsonObject);
        getBaseLayoutActivity().dismissDialog();
        if (status) {
            if (service.equals(ServiceLauncher.UserRatingTicket)) {
                Response<String> responseRating = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<String>>() {
                });
                if (responseRating.State) {
                } else {
                    ratingBar.setRating(_promotion.RatingTicket);
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseRating.Message);
                }
            }
        } else {//ERROR DE CONEXION
            ratingBar.setRating(_promotion.RatingTicket);
            AlertMsg.showErrorDialog(getBaseLayoutActivity(), message);
        }
    }

    @Override
    public void onCancelled() {

    }
}
