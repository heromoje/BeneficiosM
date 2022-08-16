package com.bcp.bo.discounts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.general.StyleApp;

import java.util.List;

import bcp.bo.service.model.response.City;

/**
 * Created by S57973 on 3/13/2017.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityHolder> {
    private ICitiesAdapter _callback;
    private List<City> _cities;

    public CitiesAdapter(ICitiesAdapter callback,List<City> cities) {
        _callback = callback;
        _cities = cities;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lay_cities_list_item, viewGroup, false);
        CityHolder vh = new CityHolder(view);
        StyleApp.setStyle(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CityHolder viewHolder, int position) {
        final City city = _cities.get(position);
        viewHolder.setup(city);
    }

    @Override
    public int getItemCount() {
        return _cities.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private City _city;
        final TextView textView_CityName;

        public CityHolder(View view) {
            super(view);
            textView_CityName = (TextView) view.findViewById(R.id.textView_CityName);
            view.setOnClickListener(this);
        }

        public void setup(City city) {
            _city = city;
            textView_CityName.setText(city.Name);
        }

        @Override
        public void onClick(View v) {
            _callback.onSelectCity(_city);
        }
    }

    public interface ICitiesAdapter {
        void onSelectCity(City city);
    }
}
