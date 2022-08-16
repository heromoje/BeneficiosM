package com.bcp.bo.discounts.layouts.menu;

import android.animation.ValueAnimator;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.adapters.CitiesAdapter;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.City;
import bcp.bo.service.model.response.PrivateToken;

public class MenuLeftLayout extends BaseLayout implements CitiesAdapter.ICitiesAdapter {

    private static final String KEY_CATEGORY_ID_SELECTED = "CATEGORY_ID_SELECTED";

    // views
    private TextView textView_HeadTitle;
    private TextView textView_HeadSubTitle;
    private TextView textView_AmountSaved;
    private ListView listView_Menu;
    private ImageButton imageButton_Close;
    private View view_City;
    private View view_Expandable;
//    private RecyclerView recyclerView_Cities;

    //observers to refresh list
    private List<DataSetObserver> observers = new ArrayList<>();

    //CitiesView
    private int originalHeight = 0;
    private boolean isExpanded;

    //category selected in the list
    private int categoryIdSelected = 0;

    private List<City> _cities;
    private List<Category> _categories;
    private PrivateToken _privateToken;
    private String _document;
    private String _amountSaved;
    private BaseAdapter menLeftAdapter = new BaseAdapter() {

        @Override
        @SuppressWarnings("deprecation")
        public View getView(int position, View convertView, ViewGroup parent) {
            final Category category = getItem(position);

            //get view
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_hom_menu_cat_item, parent, false);
            }

            LinearLayout llCategory = (LinearLayout) convertView.findViewById(R.id.llCategory);
            TextView tvName = (TextView) convertView.findViewById(R.id.textView_Name);
            TextView tvCounter = (TextView) convertView.findViewById(R.id.textView_Counter);
            ImageView ivCategoryIcon = (ImageView) convertView.findViewById(R.id.imageView_CategoryIcon);

            tvName.setText(category.Name);
            tvCounter.setText(String.valueOf(category.CountPromotion));

            String categoryName = StringUtil.removeDiacriticalMarks(category.Name).toUpperCase();
            if (categoryName.contains(Category.CATEGORY_RESTAURANTES)) {
                ivCategoryIcon.setBackgroundResource(R.drawable.cat_1);
            } else if (categoryName.contains(Category.CATEGORY_ROPA)) {
                ivCategoryIcon.setBackgroundResource(R.drawable.cat_2);
            } else if (categoryName.contains(Category.CATEGORY_ACCESORIOS)) {
                ivCategoryIcon.setBackgroundResource(R.drawable.cat_2);
            } else if (categoryName.contains(Category.CATEGORY_DIVERSION)) {
                ivCategoryIcon.setBackgroundResource(R.drawable.cat_3);
            } else {
                ivCategoryIcon.setBackgroundResource(R.drawable.cat_4);
            }

            // set selected or not
            if (categoryIdSelected == category.Id) {
                llCategory.setSelected(true);
            } else {
                llCategory.setSelected(false);
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return _categories.size();
        }

        @Override
        public Category getItem(int position) {
            return _categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return _categories.get(position).Id;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
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
            return _categories.size() == 0;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            observers.add(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            observers.remove(observer);
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
     * Menu item click listener.
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            //get the category selected
            Object object = menLeftAdapter.getItem(position);
            if (object instanceof Category) {
                Category category = (Category) object;
                categoryIdSelected = category.Id;

                //refresh category
                HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
                homeLayout.loadCategory(category, true);

                //refresh menu
                notifyObservers();
                Log.d("SELECTED", category.Name);
            }
            Log.d("SELECTED", "ITEM");
        }
    };

    public MenuLeftLayout(BaseLayoutActivity baseLayoutActivity, List<City> cities, List<Category> categories, PrivateToken privateToken, String document, String amountSaved) {
        super(baseLayoutActivity);
        _cities = cities;
        _categories = categories;
        _privateToken = privateToken;
        _document = document;
        _amountSaved = amountSaved;
    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_menu_left;
    }

    @Override
    protected void initViews() {
        textView_HeadTitle = (TextView) view.findViewById(R.id.textView_HeadTitle);
        textView_HeadSubTitle = (TextView) view.findViewById(R.id.textView_HeadSubTitle);
        textView_AmountSaved = (TextView) view.findViewById(R.id.textView_AmountSaved);
        imageButton_Close = (ImageButton) view.findViewById(R.id.imageButton_Close);
        listView_Menu = (ListView) view.findViewById(R.id.listView_Menu);
        view_City = view.findViewById(R.id.view_city);
        //view_Expandable = view.findViewById(R.id.expandable);
//        recyclerView_Cities = (RecyclerView) view.findViewById(R.id.recyclerView_Cities);
    }

    @Override
    protected void initBaseLayouts() {

    }

    @Override
    protected void initValues() {
        textView_HeadTitle.setText(_privateToken.FullName);
        textView_HeadSubTitle.setText(_document);
        textView_AmountSaved.setText(_amountSaved + " Bs.");
        listView_Menu.setAdapter(menLeftAdapter);
//        recyclerView_Cities.setAdapter(new CitiesAdapter(this, _cities));
    }

    @Override
    protected void initListeners() {
        imageButton_Close.setOnClickListener(clickListener);
        listView_Menu.setOnItemClickListener(itemClickListener);
        view_City.setOnClickListener(clickListener);
    }

    @Override
    protected void init() {

    }

    public int getCategoryIdSelected() {
        return categoryIdSelected;
    }

    @Override
    public void click(View view) {
        final int id = view.getId();
        HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
        switch (id) {
            case R.id.imageButton_Close:
                homeLayout.click(view);
                break;
            case R.id.view_city:
//                BaseActivity.publicPrivateTokenCIC.City = city.Code;
                homeLayout.showCities();
//                expandCities();
                break;
        }
    }

    private void expandCities() {
        if (originalHeight == 0) {
//            originalHeight = recyclerView_Cities.getMeasuredHeight();
        }
//        originalHeight = recyclerView_Cities.getMeasuredHeight();
        originalHeight = 300;
        Log.d("SIZE", "" + originalHeight);
        ValueAnimator valueAnimator;
        if (!isExpanded) {
            isExpanded = true;
            valueAnimator = ValueAnimator.ofInt(0, originalHeight);
            Animation animation = new AlphaAnimation(0.00f, 1.00f);
            animation.setDuration(200);
            view_Expandable.startAnimation(animation);
        } else {
            isExpanded = false;
            valueAnimator = ValueAnimator.ofInt(originalHeight, 0);
            Animation animation = new AlphaAnimation(1.00f, 0.00f);
            animation.setDuration(200);
            view_Expandable.startAnimation(animation);
        }
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view_Expandable.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                view_Expandable.requestLayout();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2,
                                         Object obj) {

    }

    @Override
    protected void dataReceived(Bundle extras) {
        if (extras.containsKey(KEY_CATEGORY_ID_SELECTED)) {
            categoryIdSelected = extras.getInt(KEY_CATEGORY_ID_SELECTED);
        }
    }

    @Override
    protected void dataToSave(Bundle extras) {
        extras.putInt(KEY_CATEGORY_ID_SELECTED, categoryIdSelected);
    }

    @Override
    protected void dataToReturn(Bundle extras) {
    }

    //--------------------------- LISTENERS ----------------------

    @Override
    protected boolean keyPressed(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public boolean unloadLayoutResult(Bundle data) {

        return false;
    }

    // -------------------------- ADAPTER -------------------------

    public void reloadCategories(List<Category> categories) {
        _categories = categories;
        menLeftAdapter.notifyDataSetChanged();
        notifyObservers();
    }

    private void notifyObservers() {
        for (DataSetObserver observer : observers) {
            observer.onChanged();
        }
    }

    @Override
    public void onSelectCity(City city) {

    }
}