package com.example.campusdiscovery.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusdiscovery.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

//osmdroid is in the C:\...\GitHub\S1\app\src\main\res

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventMapFragment} factory method to
 * create an instance of this fragment.
 */
public class EventMapFragment extends Fragment implements LocationListener {

    private static EventMapFragment INSTANCE = null;
    View view;

    MapView map = null;
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private CompassOverlay compassOverlay;
    private DirectedLocationOverlay locationOverlay;
    private static final int TECH = 1;
    double lat = 33.7760; //GT coordinates
    double lon = -84.4016;

    public EventMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventMapFragment.
     */
    public static EventMapFragment getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new EventMapFragment();
        return INSTANCE;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set layout
        view = inflater.inflate(R.layout.fragment_event_map, container, false);

        osm = (MapView) view.findViewById(R.id.map);

        //load/initialize the osmdroid configuration, this can be done
        osm.setUseDataConnection(true);
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapView osm = view.findViewById(R.id.map);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);

        mc = (MapController) osm.getController();
        mc.setZoom(15);
        GeoPoint startPoint = new GeoPoint(lat, lon);
        mc.animateTo(startPoint);

        Marker smark = new Marker(osm);
        smark.setPosition(startPoint);
        smark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        osm.getOverlays().add(smark);

        //MyLocationOverlay locOverlay = MyLocationNewOverlay();
        //Bitmap icon = BitmapFactory.decodeResource(getResources(), com.example.campusdiscovery.R.drawable.loc);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Inflate the layout for this fragment
        //inflate and create the map
        osm.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i("Script", "onScroll()");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i("Script", "onZoom()");
                return false;
            }
        });
        return view;
    }

    public void addMarker (GeoPoint center){
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.loc));
        osm.getOverlays().clear();
        osm.getOverlays().add(marker);
        osm.invalidate();
        marker.setTitle("Georgia Tech");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case TECH: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getActivity().recreate();

                }

            }
        }
    }

    public void onResume(){
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());

        mc.animateTo(center);
        addMarker(center);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }

    }
}