package com.bcp.bo.discounts.layouts.discount.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.adapters.CitiesAdapter;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.views.CustomImageView;
import com.bcp.bo.discounts.views.TicketImageTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.City;
import bcp.bo.service.model.response.Promotion;

public class DscListLayout extends BaseLayout implements View.OnClickListener, CitiesAdapter.ICitiesAdapter {
    private static final String LIST_SCROLL_OFFSET_KEY = "LIST_SCROLL_OFFSET_KEY";
    private static final String LIST_CITIES = "LIST_CITIES";
    private static final String LIST_PROMOTIONS = "LIST_PRODUCTS";
    private static final String CATEGORY = "CATEGORY";
    private static final String ISTOPPROMOTIONS = "TOPPROMOTIONS";

    //views
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycleView_List;

    /**
     * Scroll received to apply
     **/
    private int scrollY = 0;

    private Category _category;
    private List<Promotion> _promotions = new ArrayList<Promotion>();
    private List<City> _cities;
    private boolean _isTopPromotions = true;


    //observers to refresh the list
    private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

    public DscListLayout(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);
    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_list;
    }

    @Override
    protected void initViews() {
        swipeRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
        swipeRefreshLayout.setColorSchemeResources(R.color.orange_bcp, R.color.blue_bcp);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseLayoutActivity(), LinearLayoutManager.VERTICAL, false);
        recycleView_List = (RecyclerView) view.findViewById(R.id.recycleView_List);
        recycleView_List.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initBaseLayouts() {

    }

    @Override
    protected void initValues() {
        recycleView_List.setAdapter(listAdapter);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void initListeners() {
        //SWIPE LISTENER
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        //local broadcast to repaint
        Utils.registerBroadcastRepaint(getBaseLayoutActivity(), Promotion.INTENT_FILTER_DISCOUNT_IMAGE_LIST_REFRESH, repaintBroadcastReceiver);
    }

    OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (_isTopPromotions) {
                HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
                homeLayout.loadTopPromotions();
            } else {
                HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
                homeLayout.loadCategory(_category,true);
            }
        }
    };

    @Override
    protected void init() {
        if (scrollY != 0) {
            recycleView_List.smoothScrollToPosition(scrollY);
            scrollY = 0;
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

            //refresh list, doesn't matter the ids
            notifyObservers();

        }
    };

    @Override
    public void click(View view) {

    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void dataReceived(Bundle extras) {
        //load offset list
        if (extras.containsKey(LIST_SCROLL_OFFSET_KEY)) {
            scrollY = extras.getInt(LIST_SCROLL_OFFSET_KEY);
        }
        if (extras.containsKey(LIST_CITIES)) {
            _cities = (List<City>) extras.getSerializable(LIST_CITIES);
        }
        if (extras.containsKey(CATEGORY)) {
            _category = (Category) extras.getSerializable(CATEGORY);
        }
        if (extras.containsKey(LIST_PROMOTIONS)) {
            _promotions = (List<Promotion>) extras.getSerializable(LIST_PROMOTIONS);
        }
        if (extras.containsKey(ISTOPPROMOTIONS)) {
            _isTopPromotions = extras.getBoolean(ISTOPPROMOTIONS);
        }
    }

    @Override
    protected void dataToSave(Bundle extras) {
        //save the scroll
        scrollY = ((LinearLayoutManager) recycleView_List.getLayoutManager()).findFirstVisibleItemPosition();
        //scrollY = recycleView_List.getFirstVisiblePosition();
        extras.putInt(LIST_SCROLL_OFFSET_KEY, scrollY); //lvDiscounts.getFirstVisiblePosition());


        // save the category
        extras.putSerializable(LIST_CITIES, new ArrayList<>(_cities));
        extras.putSerializable(CATEGORY, _category);
        extras.putSerializable(LIST_PROMOTIONS, new ArrayList<>(_promotions));
        extras.putBoolean(ISTOPPROMOTIONS, _isTopPromotions);
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

    //----------------- LOAD DATA --------------

    public void loadCities(List<City> cities) {
        Resources r = BaseActivity.getResources();
        int pxTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, r.getDisplayMetrics());
        _category = null;
        _isTopPromotions = true;
        _cities = cities;
        recycleView_List.setPadding(0, pxTop, 0, 0);
        recycleView_List.setAdapter(new CitiesAdapter(this, _cities));
    }

    public void loadPromotionsTop(List<Promotion> promotions) {
        _category = null;
        _promotions = promotions;
        _isTopPromotions = true;

        Collections.sort(_promotions, new Promotion.PromotionComparator());

        listAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void loadPromotions(Category category, List<Promotion> promotions) {
        _category = category;
        _promotions = promotions;
        _isTopPromotions = false;

        Collections.sort(_promotions, new Promotion.PromotionComparator());
        Adapter adapter = recycleView_List.getAdapter();
        if (adapter instanceof CitiesAdapter) {
            recycleView_List.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }
        recycleView_List.setPadding(0, 0, 0, 0);
        swipeRefreshLayout.setRefreshing(false);
    }
    //----------------- ADAPTER FOR LIST --------------

    private void notifyObservers() {
        for (DataSetObserver observer : observers) {
            observer.onChanged();
        }
    }

    @Override
    public void onClick(View view) {
        //GET CLICKED TICKET
        int position = recycleView_List.getChildPosition(view);
        final Promotion promotion = _promotions.get(position);
        //GO TO TICKET LAYOUT
        HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
        homeLayout.showPromotion(promotion, _promotions);
    }

    @Override
    public void onSelectCity(City city) {
        BaseActivity.publicPrivateTokenCIC.City = city.Code;
//        getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_CATEGORY);
//        SLauncher.loadCategories(BaseActivity.publicPrivateTokenCIC, this);

        HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
        homeLayout.loadCategories(false);
    }

    public class ViewHolderTicket extends RecyclerView.ViewHolder {
        final TextView textView_Title;
        final TextView textView_Description;
        final TextView textView_Discount;
        final CustomImageView customImageView_Image;
        final RelativeLayout relativeLayout_TopText;
        final TextView textView_State;
        final ImageView imageView_State;
        final RelativeLayout relativeLayout_BottomText;
        final ImageView imageView_BackgroundMask;
        final ImageView imageView_Background;

        public ViewHolderTicket(View view) {
            super(view);
            textView_Title = (TextView) view.findViewById(R.id.tvTitle);
            textView_Description = (TextView) view.findViewById(R.id.tvDescription);
            textView_Discount = (TextView) view.findViewById(R.id.tvDiscount);
            customImageView_Image = (CustomImageView) view.findViewById(R.id.civImage);
            relativeLayout_TopText = (RelativeLayout) view.findViewById(R.id.rlTopText);
            textView_State = (TextView) view.findViewById(R.id.textView_State);
            imageView_State = (ImageView) view.findViewById(R.id.imageView_State);
            relativeLayout_BottomText = (RelativeLayout) view.findViewById(R.id.rlBottomText);
            imageView_BackgroundMask = (ImageView) view.findViewById(R.id.ivBgMask);
            imageView_Background = (ImageView) view.findViewById(R.id.ivBg);

            view.setOnClickListener(DscListLayout.this);
        }
    }

    private Adapter<ViewHolderTicket> listAdapter = new Adapter<DscListLayout.ViewHolderTicket>() {
        @Override
        public ViewHolderTicket onCreateViewHolder(ViewGroup viewGroup, int arg1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lay_dsc_list_item, viewGroup, false);
            ViewHolderTicket vh = new ViewHolderTicket(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolderTicket viewHolder, int position) {
            final Context context = getBaseLayoutActivity().getBaseContext();
            final Promotion promotion = _promotions.get(position);

            viewHolder.textView_Title.setText(promotion.getCommerce().Name);
            viewHolder.textView_Description.setText(promotion.LegendPromotion);
            viewHolder.textView_Discount.setText(String.valueOf(promotion.DiscountBCP.add(promotion.DiscountCommerce)) + context.getString(R.string.PERCENT_SYMBOL));
            viewHolder.textView_Discount.setVisibility(View.VISIBLE);
            viewHolder.customImageView_Image.setTag(promotion.NumberTicket);

            //CONVERT TO USED, EXPIRED OR VALID
            if (promotion.isUsed()) {
                viewHolder.imageView_BackgroundMask.setVisibility(View.VISIBLE);

                viewHolder.relativeLayout_TopText.setVisibility(View.VISIBLE);
                viewHolder.textView_State.setText(R.string.DSC_DETAIL_DISCOUNT_USED);
                viewHolder.imageView_State.setImageResource(R.drawable.dsc_detail_icon_used);

                viewHolder.relativeLayout_BottomText.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.imageView_Background.setImageResource(R.drawable.dsc_list_bg_img_cell_used);
                viewHolder.textView_Title.setTextAppearance(context, R.style.dscListTitleUsed);
                viewHolder.textView_Description.setTextAppearance(context, R.style.dscListDescriptionUsed);
                viewHolder.textView_Discount.setTextAppearance(context, R.style.dscListPercentDiscountUsed);
                viewHolder.textView_Discount.setBackgroundResource(R.drawable.dsc_list_discount_grey_bg);
            } else if (promotion.isExpired()) {
                viewHolder.imageView_BackgroundMask.setVisibility(View.VISIBLE);

                viewHolder.relativeLayout_TopText.setVisibility(View.VISIBLE);
                viewHolder.textView_State.setText(R.string.DSC_DETAIL_DISCOUNT_EXPIRED);
                viewHolder.imageView_State.setImageResource(R.drawable.dsc_detail_icon_expired);

                viewHolder.relativeLayout_BottomText.setBackgroundColor(Color.TRANSPARENT);
                viewHolder.imageView_Background.setImageResource(R.drawable.dsc_list_bg_img_cell_used);
                viewHolder.textView_Title.setTextAppearance(context, R.style.dscListTitleUsed);
                viewHolder.textView_Description.setTextAppearance(context, R.style.dscListDescriptionUsed);
                viewHolder.textView_Discount.setTextAppearance(context, R.style.dscListPercentDiscountUsed);
                viewHolder.textView_Discount.setBackgroundResource(R.drawable.dsc_list_discount_grey_bg);
            } else {
                viewHolder.imageView_BackgroundMask.setVisibility(View.INVISIBLE);

                viewHolder.relativeLayout_TopText.setVisibility(View.INVISIBLE);

                viewHolder.relativeLayout_BottomText.setBackgroundResource(R.drawable.dsc_list_mask_bg_text);
                viewHolder.imageView_Background.setImageResource(R.drawable.dsc_list_bg_img_cell);
                viewHolder.textView_Title.setTextAppearance(context, R.style.dscListTitle);
                viewHolder.textView_Description.setTextAppearance(context, R.style.dscListDescription);
                viewHolder.textView_Discount.setTextAppearance(context, R.style.dscListPercentDiscount);
                viewHolder.textView_Discount.setBackgroundResource(R.drawable.dsc_list_discount_blue_bg);
            }
            Picasso.with(getBaseLayoutActivity())
                    .load(promotion.ImageListURL)
                    .fit()
                    .into(viewHolder.customImageView_Image);
            ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadImageList(viewHolder.customImageView_Image, promotion.ImageListURL);
        }

        @Override
        public int getItemCount() {
            return _promotions.size();
        }
    };
}
