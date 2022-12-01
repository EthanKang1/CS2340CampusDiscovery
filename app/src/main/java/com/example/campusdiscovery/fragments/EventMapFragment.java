package com.example.campusdiscovery.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.activities.ViewEventActivity;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.EventListViewModel;
import com.example.campusdiscovery.models.UserMapViewModel;
import com.google.gson.Gson;

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
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    double CULCx = 33.7746527;
    double CULCy = -84.3964040;
    double SCx = 33.7736952;
    double SCy = -84.3982591;

    private Gson gson = new Gson();

    private EventListViewModel eventListViewModel;
    private UserMapViewModel userMapViewModel;
    private Map<Marker, Event> markerList;

    private final ActivityResultLauncher<Intent> eventActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Event newEvent = gson.fromJson(data.getStringExtra("currentEvent"), Event.class);
                        String action = data.getStringExtra("action");
                        String RSVPList = data.getStringExtra("RSVPList");
                        int eventPosition = data.getIntExtra("eventPosition", -1);

                        eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
                            item.set(eventPosition, newEvent);
//                            updateListener.notifyUpdate();
//                            eventsAdapter.notifyDataSetChanged();
//                            loadEventPage();
                        });

                    }
                }
            }
    );

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

        // ethan map stuff
        this.eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        this.userMapViewModel = new ViewModelProvider(requireActivity()).get(UserMapViewModel.class);
        this.markerList = new HashMap<Marker, Event>();

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
        mc.setZoom(17);
        GeoPoint startPoint = new GeoPoint(lat, lon);
        mc.animateTo(startPoint);

//        Marker culcmark = new Marker(osm);
//        GeoPoint CULC = new GeoPoint(CULCx, CULCy);
//        culcmark.setPosition(CULC);
//        culcmark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        //culcmark.setIcon(getResources().getDrawable(R.drawable.loc));
//        osm.getOverlays().add(culcmark);
//
//        Marker scmark = new Marker(osm);
//        GeoPoint SC = new GeoPoint(SCx, SCy);
//        scmark.setPosition(SC);
//        scmark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        //scmark.setIcon(getResources().getDrawable(R.drawable.loc));
//        osm.getOverlays().add(scmark);

        this.updateMarkers();

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

    public void updateMarkers() {
        this.markerList.clear();

        this.eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
            for (Event currentEvent : item) {


                String[] coordinates = currentEvent.getLocation().split(",", 2);
                double coordLat = Double.parseDouble(coordinates[0]);
                double coordLong = Double.parseDouble(coordinates[1]);

                // create marker
                Marker currentMarker = new Marker(osm);
                currentMarker.setPosition(new GeoPoint(coordLat, coordLong));
                currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                currentMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker arg0, MapView arg1) {
                        userMapViewModel.getSelectedItem().observe(requireActivity(), item -> {
                            //Your stuff
                            // This isn't a great implementation, but it works
                            Event thisEvent = markerList.get(arg0);
                            String currentEventJson = gson.toJson(thisEvent);
                            String userMapJson = gson.toJson(item);

                            Intent intent = new Intent(getActivity(), ViewEventActivity.class);
                            intent.putExtra("currentEvent", currentEventJson);
                            intent.putExtra("userMap", userMapJson);

                            eventActivityResultLauncher.launch(intent);
                        });
                        return true;
                    }

                });

                this.markerList.put(currentMarker, currentEvent);
            }
        });

        for (Marker currentMarker : this.markerList.keySet()) {
            osm.getOverlays().add(currentMarker);
        }

    }
}