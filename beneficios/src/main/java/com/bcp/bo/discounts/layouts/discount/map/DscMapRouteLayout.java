package com.bcp.bo.discounts.layouts.discount.map;

import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.layouts.home.HomeLayout;
import com.bcp.bo.discounts.util.StringUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import bcp.bo.service.Sender;
import bcp.bo.service.ServiceLauncher;
import bcp.bo.service.ServiceLauncher.IService;
import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.Contact;
import bcp.bo.service.model.response.Directions;
import bcp.bo.service.model.response.PrivateToken;
import bcp.bo.service.model.response.Response;
import bcp.bo.service.model.response.Step;

public class DscMapRouteLayout extends BaseLayout implements OnMyLocationChangeListener, IService {

    private static final double BOLIVIA_CENTER_LATITUDE = -16.745194;//-9.838979d;
    private static final double BOLIVIA_CENTER_LONGITUDE = -64.733664;//-75.153351d;
    private static final float PERU_ZOOM = 5.1f;

    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_CATEGORY_ID = "CATEGORY_ID";

    protected static final float MY_POSITION_ZOOM = 14.5f;

    private static final int ID_CANCEL = 301;
    private static final int ID_TURNONGPS = 300;

    //views
    private RelativeLayout rlLoading;
    private ImageView ivLoading;

    //map
    private GoogleMap googleMap;
    private UiSettings usMap;

    //latitude and longitude to calculate the route
    //Contact to calculate route
    private Contact _contact;
    private double _latitude = 0;
    private double _longitude = 0;

    //category id to paint poi
    private int categoryId = 0;

    //flag to know if route has been asked or not
    private boolean asking = false;

    public DscMapRouteLayout(BaseLayoutActivity baseLayoutActivity, Contact contact) {
        super(baseLayoutActivity);
        _contact = contact;
        _latitude = contact.Latitude;
        _longitude = contact.Longitude;
    }

    public DscMapRouteLayout(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);

    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_map_route;
    }

    @Override
    protected void initViews() {

        //views
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        ivLoading = (ImageView) view.findViewById(R.id.ivLoading);

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseLayoutActivity()) == ConnectionResult.SUCCESS) {
            //map
            if (googleMap == null) {
                SupportMapFragment smfMap = (SupportMapFragment) getBaseLayoutActivity().getSupportFragmentManager().findFragmentById(R.id.smfMapRoute);
                // TODO: Review Changes below
                //googleMap = smfMap.getMap();
                //usMap = googleMap.getUiSettings();
                smfMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap result) {
                        googleMap = result;
                        usMap = googleMap.getUiSettings();

                        Utils.startAnimatedImage(ivLoading);
                        if (usMap != null) {
                            usMap.setCompassEnabled(true);
                            usMap.setMyLocationButtonEnabled(true);
                            usMap.setRotateGesturesEnabled(true);
                            usMap.setZoomControlsEnabled(true);
                            usMap.setZoomGesturesEnabled(true);
                        }

                        if (googleMap != null) {
                            if (ContextCompat.checkSelfPermission(getBaseLayoutActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getBaseLayoutActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 419);
                            } else {
                                googleMap.setMyLocationEnabled(true);
                            }
                        }

                        //start camera of map in Peru
                        moveMap(BOLIVIA_CENTER_LATITUDE, BOLIVIA_CENTER_LONGITUDE, PERU_ZOOM, false);

                        if (googleMap != null) {
                            googleMap.setOnMyLocationChangeListener(DscMapRouteLayout.this);

                        }
                    }
                });
            }
        }
    }


    @Override
    protected void initBaseLayouts() {

    }

    @Override
    protected void initValues() {

        //show loading
        //initialize google map settings
        // El siguiente código se movió al método onMapReady por ser configuraciones del mapa
		/*
		if(usMap!=null){
			usMap.setCompassEnabled(true);
			usMap.setMyLocationButtonEnabled(true);
			usMap.setRotateGesturesEnabled(true);
			usMap.setZoomControlsEnabled(true);
			usMap.setZoomGesturesEnabled(true);
		}
		if(googleMap!=null){
			googleMap.setMyLocationEnabled(true);
		}

		//start camera of map in Peru
		moveMap(BOLIVIA_CENTER_LATITUDE, BOLIVIA_CENTER_LONGITUDE, PERU_ZOOM, false);
		 */
    }

    @Override
    protected void initListeners() {
        //map listeners
        // El siguiente código se movió al método onMapReady por ser configuraciones del mapa
        /*
        if (googleMap != null) {
            googleMap.setOnMyLocationChangeListener(this);
        }
        */
    }

    @Override
    protected void init() {
        //show loading
        //AlertMsg.showLoadingDialog(getBaseLayoutActivity());

        //Set the screen title corresponding to this fragment.
        HomeLayout homeLayout = (HomeLayout) findBaseLayoutParentByName(HomeLayout.class);
        homeLayout.setHomeBar(null, clickListener);

        LocationManager locationManager = (LocationManager) getBaseLayoutActivity().getSystemService(Service.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertMsg.showErrorWarningDialog(getBaseLayoutActivity(), getString(R.string.DSC_MAP_LOCATION_ACTIVATION), false, clickListener, getString(R.string.DSC_GO_TO_SETTINGS), getString(R.string.CANCEL), ID_TURNONGPS, ID_CANCEL, true);
        }
    }

    @Override
    protected void onDestroy() {
        //destroy map
        FragmentTransaction ft = getBaseLayoutActivity().getSupportFragmentManager().beginTransaction();
        SupportMapFragment smfMap = (SupportMapFragment) getBaseLayoutActivity().getSupportFragmentManager().findFragmentById(R.id.smfMapRoute);
        ft.remove(smfMap);
        ft.commit();

        //destroy map
        googleMap = null;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void click(View view) {
        if (view.getId() == R.id.bLeftMenu) {
            unload(null);
        } else if (view.getId() == ID_TURNONGPS) {
            final Intent intentGPS = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getBaseLayoutActivity()
                    .startActivity(intentGPS);
        } else if (view.getId() == ID_CANCEL) {
            unload(null);
        }
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {
    }

    @Override
    protected void dataReceived(Bundle extras) {
        if (extras.containsKey(KEY_LATITUDE)) {
            _latitude = extras.getDouble(KEY_LATITUDE);
        }
        if (extras.containsKey(KEY_LONGITUDE)) {
            _longitude = extras.getDouble(KEY_LONGITUDE);
        }
        if (extras.containsKey(KEY_CATEGORY_ID)) {
            categoryId = extras.getInt(KEY_CATEGORY_ID);
        }

    }

    @Override
    protected void dataToSave(Bundle extras) {
        extras.putDouble(KEY_LATITUDE, _latitude);
        extras.putDouble(KEY_LONGITUDE, _longitude);
        extras.putInt(KEY_CATEGORY_ID, categoryId);
    }

    @Override
    protected void dataToReturn(Bundle extras) {

    }

    @Override
    protected boolean keyPressed(int keyCode, KeyEvent event) {
        unload(null);
        return true;
    }

    @Override
    public boolean unloadLayoutResult(Bundle data) {

        return false;
    }

    //---------- MAP LISTENERS -------------------

    @Override
    public void onMyLocationChange(Location location) {
        Log.d("MAP ROUTE", "ASKING ");
        if (!asking) {
            Log.d("MAP ROUTE", "ASKING B");
            //activate flag
            asking = true;

            //go to my location
            moveMap(location.getLatitude(), location.getLongitude(), MY_POSITION_ZOOM, true);
            Log.d("MAP ROUTE", "ASKING C");

            SLauncher.ResolveRoute(location.getLatitude(), location.getLongitude(), _latitude, _longitude, this);

            //ServiceLauncher.executeResolveRoute(getBaseLayoutActivity().getBaseContext(), location.getLatitude(),location.getLongitude(),_latitude,_longitude, this);
            //launch connection for route
            //ServiceLauncher.launchResolveRouteService(getBaseLayoutActivity(), this, location.getLatitude(), location.getLongitude(), latitude, longitude);
        }
    }
    //---------- MAP PLAY ----------------

    public void moveMap(double latitude, double longitude, float zoom, boolean animated) {

        //move the map
        moveMap(new LatLng(latitude, longitude), zoom, animated);
    }

    public void moveMap(LatLng latLng, float zoom, boolean animated) {
        if (googleMap != null) {
            if (animated) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        }
    }

    @Override
    public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        super.onCompleted(status, message, service, jsonObject);
        Utils.stopAnimatedImage(ivLoading);
        rlLoading.setVisibility(View.GONE);
        if (status) {
            if (service.equals(ServiceLauncher.ResolveRoute)) {
                Directions direction = Sender.fromJSON(jsonObject.toString(), new TypeReference<Directions>() {
                });
                if (direction.isOK()) {
                    List<Step> steps = direction.Routes.get(0).Legs.get(0).Steps;

                    final PolylineOptions rectLines = new PolylineOptions().width(5).color(Color.RED);

					/*rectLines.add(new LatLng(
							direction.Routes.get(0).Legs.get(0).StartLocation.Latitude,
							direction.Routes.get(0).Legs.get(0).StartLocation.Longitude
					));*/
                    for (Step step : steps) {
                        rectLines.add(new LatLng(step.StartLocation.Latitude, step.StartLocation.Longitude));
                        rectLines.add(new LatLng(step.EndLocation.Latitude, step.EndLocation.Longitude));
                    }
					/*rectLines.add(new LatLng(
							direction.getRoutes().get(0).getLegs().get(0).getEndLocation().getLatitude(),
							direction.getRoutes().get(0).getLegs().get(0).getEndLocation().getLongitude()
					));*/

                    googleMap.addPolyline(rectLines);

                    //ORIGIN
                    MarkerOptions mOrigin = new MarkerOptions();
                    mOrigin.position(new LatLng(steps.get(0).StartLocation.Latitude, steps.get(0).StartLocation.Longitude));
                    googleMap.addMarker(mOrigin);

                    //DESTINY
                    MarkerOptions mDestiny = new MarkerOptions();
                    String categoryName = StringUtil.removeDiacriticalMarks(_contact.getCommerce().Category).toUpperCase();
                    Log.d("ROUTE LAYOUT", categoryName);
                    if (categoryName.contains(Category.CATEGORY_RESTAURANTES)) {
                        Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_1);
                        mDestiny.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    } else if (categoryName.contains(Category.CATEGORY_ROPA)) {
                        Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_2);
                        mDestiny.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    } else if (categoryName.contains(Category.CATEGORY_ACCESORIOS)) {
                        Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_2);
                        mDestiny.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    } else if (categoryName.contains(Category.CATEGORY_DIVERSION)) {
                        Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_3);
                        mDestiny.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    } else {
                        Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_4);
                        mDestiny.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }
                    mDestiny.position(new LatLng(steps.get(steps.size() - 1).EndLocation.Latitude, steps.get(steps.size() - 1).EndLocation.Longitude));
                    googleMap.addMarker(mDestiny);
                } else {
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), getString(R.string.DSC_MAP_LOCATION_ADDRESS_NOT_FOUND), clickListener, 0);
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