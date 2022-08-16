package com.bcp.bo.discounts.layouts.discount.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.activities.StartActivity;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.layouts.discount.DscHomeListMapLayout;
import com.bcp.bo.discounts.util.StringUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.Contact;
import bcp.bo.service.model.response.Promotion;

public class DscMapLayout extends BaseLayout {

    protected static final float MY_POSITION_ZOOM = 14.5f;
    protected static final float POI_ZOOM = 15.0f;
    protected static final float ZOOM_MIN_ASK = 14f;
    //ids for popups
    private static final int ID_LOCATION_ACTIVATION_OK = 100;
    private static final int ID_LOCATION_ACTIVATION_CANCEL = 101;
    private static final double BOLIVIA_CENTER_LATITUDE = -16.745194;
    private static final double BOLIVIA_CENTER_LONGITUDE = -64.733664;
    private static final float BOLIVIA_ZOOM = 5.1f;
    private static final String KEY_LOCATIN_ASKED = "LOCATION_ASKED";
    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    private static final String KEY_ZOOM = "KEY_ZOOM";
    private static final String KEY_PROMOTIONS = "promotions";
    private static final String KEY_LOCATION_SELECTED = "KEY_LOCATION_SELECTED";
    private static final String KEY_GPS_ASKED = "KEY_GPS_ASKED";
    //ICONS
    BitmapDescriptor iconRestaurantes;
    BitmapDescriptor iconAccesorios;
    BitmapDescriptor iconDiversion;
    BitmapDescriptor iconOtros;
    //views
    private ImageButton ibLocation;
    //map
    private GoogleMap googleMap;
    private UiSettings usMap;
    //flag to know if map has been centered on my position before
    private boolean centeredOnMyPosition = false;
    private double latitudeInitial = BOLIVIA_CENTER_LATITUDE;
    private double longitudeInitial = BOLIVIA_CENTER_LONGITUDE;
    private float zoomInitial = BOLIVIA_ZOOM;
    //private List<Contact> contacts = new ArrayList<Contact>();
    private List<Promotion> _promotions = new ArrayList<>();
    //Markers
    private Map<Marker, Contact> mapMarkers = new HashMap<Marker, Contact>();
    private Marker markerSelected = null;
    //to save last location
    private Location lastLocation = null;

    //flag to know if gps has been asked
    private boolean gpsAsked = false;

    //reference to parent because this base layout it is always recreated
    private DscHomeListMapLayout homeListMapLayout = null;

    //flag to know if we have asked to the user if wants to activate the gps
    private boolean locationAsked = false;
    private GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {

            //save last location
            lastLocation = location;

            //move to my location if no points and first time
            if (!centeredOnMyPosition) {
                moveMap(location.getLatitude(), location.getLongitude(), MY_POSITION_ZOOM, true, true);
                centeredOnMyPosition = true;
            }
        }
    };
    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {

            //get Product
            Contact contact = mapMarkers.get(marker);
            if (contact == null) {
                return false;
            }

            markerSelected = marker;

            marker.showInfoWindow();

            //center the map at the point
            moveMap(marker.getPosition(), POI_ZOOM, true);

            //event consumed
            return true;
        }
    };
    private GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {

        @Override
        public View getInfoContents(Marker marker) {
            //GET CONTACT
            Contact contact = mapMarkers.get(marker);
            if (contact == null) {
                return null;
            }
            //get distance
            float distance = 0;
            if (lastLocation != null) {
                distance = Utils.getDistance(contact.Latitude, contact.Longitude, lastLocation.getLatitude(), lastLocation.getLongitude());
            }
            return getViewMarkerContent(contact, distance);
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }

    };
    private OnInfoWindowClickListener infoWindowClickListener = new OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            //get Product
            Contact contact = mapMarkers.get(marker);
            if (contact == null) {
                return;
            }
            Promotion promotion = contact.getCommerce().Promotions.get(0);

            //call to home layout with this discount
            homeListMapLayout.loadProductDetail(promotion, _promotions);
        }
    };

    public DscMapLayout(BaseLayoutActivity baseLayoutActivity, DscHomeListMapLayout homeListMapLayout) {
        super(baseLayoutActivity);

        this.homeListMapLayout = homeListMapLayout;
    }

    public DscMapLayout(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);

    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_map;
    }

    @Override
    protected void initViews() {
        ibLocation = (ImageButton) view.findViewById(R.id.ibLocation);

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseLayoutActivity()) == ConnectionResult.SUCCESS) {
            //map
            if (googleMap == null) {
                SupportMapFragment smfMap = (SupportMapFragment) getBaseLayoutActivity().getSupportFragmentManager().findFragmentById(R.id.smfMap);
                // TODO: Review Changes below
                //googleMap = smfMap.getMap();
                //usMap = googleMap.getUiSettings();
                smfMap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap result) {
                        googleMap = result;
                        usMap = googleMap.getUiSettings();
                        //initialize google map settings
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
                        moveMap(latitudeInitial, longitudeInitial, zoomInitial, false, false);
                        //start camera of map in Peru

                        if (googleMap != null) {
                            googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
                            googleMap.setOnMarkerClickListener(onMarkerClickListener);
                            googleMap.setInfoWindowAdapter(infoWindowAdapter);
                            googleMap.setOnInfoWindowClickListener(infoWindowClickListener);
                        }

                        ibLocation.setOnClickListener(clickListener);
                    }
                });
            }
        } else {

            //show error message
            AlertMsg.showErrorDialog(getBaseLayoutActivity(), R.string.ERROR_GOOGLE_PLAY_SERVICES);
            ibLocation.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initBaseLayouts() {

    }

    @Override
    protected void initValues() {

        //initialize google map settings
        /*
        if (usMap != null) {
            usMap.setCompassEnabled(true);
            usMap.setMyLocationButtonEnabled(true);
            usMap.setRotateGesturesEnabled(true);
            usMap.setZoomControlsEnabled(true);
            usMap.setZoomGesturesEnabled(true);
        }

        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }

        //start camera of map in Peru
        moveMap(latitudeInitial, longitudeInitial, zoomInitial, false, false);
        */
    }

    @Override
    protected void initListeners() {

        //map listeners
        /*
        if (googleMap != null) {
            googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
            googleMap.setOnMarkerClickListener(onMarkerClickListener);
            googleMap.setInfoWindowAdapter(infoWindowAdapter);
            googleMap.setOnInfoWindowClickListener(infoWindowClickListener);
        }

        ibLocation.setOnClickListener(clickListener);
         */
    }

    @Override
    protected void init() {

        //check if user has location activated
        if (!Utils.isLocationAvailable(getBaseLayoutActivity()) && !locationAsked) {
            locationAsked = true;

            //show popup
            AlertMsg.showErrorWarningDialog(getBaseLayoutActivity(), getString(R.string.DSC_MAP_LOCATION_ACTIVATION), false, clickListener,
                    getString(R.string.DSC_GO_TO_SETTINGS), getString(R.string.CANCEL), ID_LOCATION_ACTIVATION_OK, ID_LOCATION_ACTIVATION_CANCEL, true);
        }

        //load points if there are
        if (_promotions.size() > 0) {
            centeredOnMyPosition = true;

            //load points
            loadProductMarks();

            view.post(new Runnable() {
                public void run() {

                    //search the point selected and select it
                /*if(locationSelectedInitial!=null){
					for(Entry<Marker, Ticket> entry : mapMarkers.entrySet()){
						if(	entry.getValue().getPromotion().getCommerce().getcgetLatitude()==locationSelectedInitial.getLatitude() &&
							entry.getValue().getLongitude()==locationSelectedInitial.getLongitude() ){

//							onMarkerClickListener.onMarkerClick(entry.getKey());
							break;

						}
					}
				}*/


                }
            });
        }

    }

    /**
     * To initialize before adding
     * Basically prepare the view
     */
    public void resume(Bundle extras) {
        if (extras != null) {
            dataReceived(extras);
        }
        onResume();
    }

    public void destroy() {
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        //destroy map
        FragmentTransaction ft = getBaseLayoutActivity().getSupportFragmentManager().beginTransaction();
        SupportMapFragment smfMap = (SupportMapFragment) getBaseLayoutActivity().getSupportFragmentManager().findFragmentById(R.id.smfMap);
        ft.remove(smfMap);
        ft.commit();

        //destroy map
        googleMap = null;
    }

    @Override
    public void click(View view) {
        int id = view.getId();
        if (id == R.id.ibLocation) {

            if (lastLocation != null) {
                moveMap(lastLocation.getLatitude(), lastLocation.getLongitude(), MY_POSITION_ZOOM, true, true);
            }

        } else if (id == ID_LOCATION_ACTIVATION_OK) {
            Utils.goToGPSSettingsScreen(getBaseLayoutActivity(), 0);
        }
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void dataReceived(Bundle extras) {
        if (extras.containsKey(KEY_LATITUDE)) {
            latitudeInitial = extras.getDouble(KEY_LATITUDE);
        }
        if (extras.containsKey(KEY_LONGITUDE)) {
            longitudeInitial = extras.getDouble(KEY_LONGITUDE);
        }
        if (extras.containsKey(KEY_ZOOM)) {
            zoomInitial = extras.getFloat(KEY_ZOOM);
        }
        if (extras.containsKey(KEY_PROMOTIONS)) {
            _promotions = (List<Promotion>) extras.getSerializable(KEY_PROMOTIONS);
        }
        if (extras.containsKey(KEY_GPS_ASKED)) {
            gpsAsked = extras.getBoolean(KEY_GPS_ASKED);
        }
        if (extras.containsKey(KEY_LOCATIN_ASKED)) {
            locationAsked = extras.getBoolean(KEY_LOCATIN_ASKED);
        }
    }

    public void loadDataToSave(Bundle extras) {
        dataToSave(extras);
    }

    //------------ MAP LISTENERS ---------------------

    @Override
    protected void dataToSave(Bundle extras) {

        //save position in the map
        if (googleMap != null) {
            LatLng latLng = googleMap.getCameraPosition().target;
            extras.putDouble(KEY_LATITUDE, latLng.latitude);
            extras.putDouble(KEY_LONGITUDE, latLng.longitude);
            extras.putFloat(KEY_ZOOM, googleMap.getCameraPosition().zoom);
        }

        //save products
        extras.putSerializable(KEY_PROMOTIONS, new ArrayList<Promotion>(_promotions));
        //point selected

        //gps asked
        extras.putBoolean(KEY_GPS_ASKED, gpsAsked);

        extras.putBoolean(KEY_LOCATIN_ASKED, locationAsked);
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

    /**
     * Generate the view to show over the mark
     *
     * @param contact
     * @param distance
     * @return
     */
    private View getViewMarkerContent(Contact contact, float distance) {

        //generate the view
        View view = getBaseLayoutActivity().getLayoutInflater().inflate(R.layout.lay_dsc_map_info_window, null);

        TextView tvCompany = ((TextView) view.findViewById(R.id.tvCompany));
        TextView tvAddress = ((TextView) view.findViewById(R.id.tvAddress));
        TextView tvUnits = ((TextView) view.findViewById(R.id.tvUnits));
        TextView tvDistance = ((TextView) view.findViewById(R.id.tvDistance));

        //decimal format for distance
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        tvCompany.setText(contact.getCommerce().Name);
        tvAddress.setText(contact.Address);
        if (distance > 1000) {
            tvDistance.setText(decimalFormat.format(distance / 1000));
            tvUnits.setText(R.string.KILOMETER_UNIT);
        } else if (distance > 0) {
            tvDistance.setText(decimalFormat.format(distance));
            tvUnits.setText(R.string.METER_UNIT);
        }

        return view;
    }


    //---------- MAP PLAY ----------------

    public void moveMap(double latitude, double longitude, float zoom, boolean animated, boolean isMyPosition) {

        //save my position
        if (isMyPosition) {
            centeredOnMyPosition = true;
        }

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

    //----------------- PRODUCT AND POINTS -------------

    public void loadProducts(List<Promotion> promotions) {
        _promotions = promotions;
        loadProductMarks();
    }

    /**
     * Delete all actual marks and show new ones
     */
    private void loadProductMarks() {
        if (googleMap == null) {
            return;
        }
        //remove all markers
        googleMap.clear();
        mapMarkers.clear();

        if (iconRestaurantes == null) {
            Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_1);
            iconRestaurantes = BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        if (iconAccesorios == null) {
            Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_2);
            iconAccesorios = BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        if (iconDiversion == null) {
            Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_3);
            iconDiversion = BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        if (iconOtros == null) {
            Bitmap bitmap = ((StartActivity) getBaseLayoutActivity()).imageDownloader.LoadBitmap(R.drawable.dsc_map_poi_4);
            iconOtros = BitmapDescriptorFactory.fromBitmap(bitmap);
        }
        //add all points in the map
        for (Promotion promotion : _promotions) {
            for (Contact contact : promotion.getCommerce().Contacts) {
                String categoryName = StringUtil.removeDiacriticalMarks(contact.getCommerce().Category).toUpperCase();
                MarkerOptions mo = new MarkerOptions();
                double latitude = contact.Latitude;
                double longitude = contact.Longitude;

                if (latitude != 0 || longitude != 0) {
                    mo.position(new LatLng(contact.Latitude, contact.Longitude));

                    //set the icon of the point
                    //mo.icon(BitmapDescriptorFactory.fromBitmap(bitmapPoi));
                    if (categoryName.contains(Category.CATEGORY_RESTAURANTES)) {
                        mo.icon(iconRestaurantes);
                    } else if (categoryName.contains(Category.CATEGORY_ROPA)) {
                        mo.icon(iconAccesorios);
                    } else if (categoryName.contains(Category.CATEGORY_ACCESORIOS)) {
                        mo.icon(iconAccesorios);
                    } else if (categoryName.contains(Category.CATEGORY_DIVERSION)) {
                        mo.icon(iconDiversion);
                    } else {
                        mo.icon(iconOtros);
                    }

                    //add marker, do it visible if filter is ativated
                    Marker marker = googleMap.addMarker(mo);

                    //add marker created to the map
                    mapMarkers.put(marker, contact);
                }
            }
        }
    }
}