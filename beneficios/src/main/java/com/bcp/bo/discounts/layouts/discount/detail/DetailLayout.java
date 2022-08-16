package com.bcp.bo.discounts.layouts.discount.detail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.ImageUtils;
import com.bcp.bo.discounts.layouts.discount.detail.DetailImageLoader.DetailImageLoaderInterface;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.views.CustomImageView;
import com.bcp.bo.discounts.views.TicketImageTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.Promotion;

@SuppressLint("ViewConstructor")
public class DetailLayout extends BaseLayout {
    /**
     * Saved layout state constants
     */
    private static final String DSC_DETAIL_INDEX = "DSC_DETAIL_INDEX";
    private static final String KEY_PROMOTIONS = "LIST_PROMOTIONS";
    private static final String KEY_CATEGORY = "CATEGORY";

    /**
     * Discount index.
     */
    private int index;

    /**
     * Views
     */
    private TextView tvSize;
    private TextView tvIndex;
    /**
     * Pager indicator listener.
     */
    private final SimpleOnPageChangeListener pageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            index = position;
            tvIndex.setText(String.valueOf(index + 1));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);

        }
    };
    private ViewPager pager;
    private Category _category;
    private List<Promotion> _promotions = new ArrayList<>();
    //list of layouts used by adapter
    private List<BaseLayout> listLayouts = new ArrayList<>();

    /**
     * Data source
     **/
    //private UserLogedData userLogedData;
    //reference to him to send reference from pager
    private DetailLayout detailLayout = null;
    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final BaseLayout baseLayout;
            final Promotion promotion = _promotions.get(position);

            if (promotion.isUsed()) {
                baseLayout = new DetailItemUsed(getBaseLayoutActivity());
                ((DetailItemUsed) baseLayout).setDiscount(promotion);
                ((DetailItemUsed) baseLayout).initWithOnResume();

            } else if (promotion.isExpired()) {
                baseLayout = new DetailItemExpired(getBaseLayoutActivity());
                ((DetailItemExpired) baseLayout).setDiscount(promotion);
                ((DetailItemExpired) baseLayout).initWithOnResume();
            } else {
                baseLayout = new DetailItemActive(getBaseLayoutActivity(), detailLayout);
                ((DetailItemActive) baseLayout).setDiscount(promotion);
                ((DetailItemActive) baseLayout).initWithOnResume();
            }

            //add the base layout
            listLayouts.add(baseLayout);

//			//get the imageView and set the id of discount to set the image later
            final CustomImageView civImage = (CustomImageView) ((DetailImageLoaderInterface) baseLayout).findCustomImageView();
            civImage.setTag(promotion.Id);

            Picasso.with(getBaseLayoutActivity())
                    .load(promotion.ImageTicketURL)
//                    .fit()
//                    .transform(new TicketImageTransform(getBaseLayoutActivity().getResources(),R.drawable.dsc_detail_ticket_top_mask))
//                    .placeholder(R.drawable.dsc_detail_ticket_img_top)
                    .into(civImage, new Callback() {
                @Override
                public void onSuccess() {
//                    civImage.setImageBitmap(
//                           ImageUtils.generateDrawableMask(getBaseLayoutActivity().getResources(),
//                                    civImage.getDrawable(),
//                                   getBaseLayoutActivity().getResources().getDrawable(R.drawable.dsc_detail_ticket_top_mask),
//                                    Color.argb(255, 58, 233, 28)));
                }

                @Override
                public void onError() {

                }
            });
			//((StartActivity)getBaseLayoutActivitys()).imageDownloader.LoadImageTicket(civImage, promotion.ImageTicketURL);

            container.addView(baseLayout.view);
            return baseLayout;
        }

        @Override
        public int getCount() {
            return _promotions.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((BaseLayout) object).view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //remove and free memory of base layout
            BaseLayout baseLayout = (BaseLayout) object;
            baseLayout.freeMem();
            ((ViewPager) container).removeView(baseLayout.view);
            listLayouts.remove(baseLayout);
        }
    };

    ////////////////////////////////////
    //   BASELAYOUT INHERIT METHODS   //
    ////////////////////////////////////

    /**
     * Builder
     *
     * @param baseLayoutActivity
     */
    public DetailLayout(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);

        detailLayout = this;
    }

    /**
     * Set pager position.
     *
     * @param position The position.
     */
    public void setIndex(int position) {
        this.index = position;
    }

    /**
     * Must return the activity to inflate, the XML given by the R.layout.
     *
     * @return R.layout. associated to this layout
     */
    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_detail;
    }

    /**
     * Initialize views of layout: findViewById(R.id. )
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void dataReceived(Bundle extras) {
        this.index = extras.getInt(DSC_DETAIL_INDEX);
        //load the category
        if (extras.containsKey(KEY_CATEGORY)) {
            _category = (Category) extras.getSerializable(KEY_CATEGORY);
        }
        //load products
        if (extras.containsKey(KEY_PROMOTIONS)) {
            _promotions = (List<Promotion>) extras.getSerializable(KEY_PROMOTIONS);
        }
    }

    /**
     * Must save the data required to be received when activity is resumed
     * extras.putString(...)
     *
     * @param extras
     */
    @Override
    protected void dataToSave(Bundle extras) {
        extras.putInt(DSC_DETAIL_INDEX, index);
        // save the category
        extras.putSerializable(KEY_CATEGORY, _category);
        //save the products
        extras.putSerializable(KEY_PROMOTIONS, new ArrayList<Promotion>(_promotions));
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
        // Pagination
        tvIndex = (TextView) view.findViewById(R.id.tvIndex);
        tvSize = (TextView) view.findViewById(R.id.tvSize);

        // Pager
        pager = (ViewPager) view.findViewById(R.id.pager);

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
        tvIndex.setText(String.valueOf(index + 1));
        tvSize.setText(String.valueOf(_promotions.size()));

        //userLogedData = ((StartActivity)getBaseLayoutActivity()).getUserLogedData();

        loadPager();
    }

    /**
     * Initialize all the listeners need for the values (touches, gps, etc... )
     */
    @Override
    protected void initListeners() {
        // Updates page change
        pager.setOnPageChangeListener(pageChangeListener);
    }

    /**
     * Initialize everything you need after the layout has been created
     */
    @Override
    protected void init() {
        //Set the screen title corresponding to this fragment.
        HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
        if (_category == null) {
            homeLayout.setHomeBar(getString(R.string.DSC_TOP_PROMOTIONS), clickListener);
        } else {
            homeLayout.setHomeBar(_category.Name, clickListener);
        }


        // Set the current item
        pager.setCurrentItem(index, true);
    }

    /**
     * Receive the events from buttons created in XML assigned as this name
     *
     * @param view - View clicked
     */
    @Override
    public void click(View view) {
        final int id = view.getId();
        if (id == R.id.bLeftMenu) {
            exit();
        }
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }

        return false;
    }

    //----------------- LOAD DATA --------------

    private void exit() {

        //unload
        unload(null);
    }

    //------------------ LISTENERS -----------------------

    /**
     * Load pager adapter.
     */
    private void loadPager() {

        //free memory
        freeMem();

        //set adapter again
        pager.setAdapter(pagerAdapter);
    }

    //------------------------ PAGER ADAPTER -----------------------------

    public void loadPromotion(Category category, Promotion promotion, List<Promotion> promotions) {
        _category = category;
        _promotions = promotions;
        index = promotions.indexOf(promotion);
        if (index < 0) {
            index = 0;
        }

        if (pager != null) {
            pager.setAdapter(pagerAdapter);
        }
    }


    // ***********************************************
    // *
    // * CALLBACK API
    // *
    // ***********************************************

    /**
     * Free all the memory used by images
     */
    @Override
    public void freeMem() {
        super.freeMem();

        //free memory of all in the list
        for (BaseLayout baseLayout : listLayouts) {
            baseLayout.freeMem();
        }
    }

}
