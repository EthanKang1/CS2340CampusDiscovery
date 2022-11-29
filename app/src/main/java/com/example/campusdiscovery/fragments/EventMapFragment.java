package com.example.campusdiscovery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusdiscovery.R;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import android.app.Activity;
import android.os.Bundle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventMapFragment extends Fragment {

    public EventMapFragment() {
        // Required empty public constructor
    }

    public class OsmdroidDemoMap extends Activity {
        private MapView         mMapView;
        private MapController   mMapController;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.osm_main);
            mMapView = (MapView) findViewById(R.id.mapview);
            mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
            mMapView.setBuiltInZoomControls(true);
            mMapController = (MapController) mMapView.getController();
            mMapController.setZoom(13);
            GeoPoint gPt = new GeoPoint(51500000, -150000);
            mMapController.setCenter(gPt);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventMapFragment newInstance() {
        EventMapFragment fragment = new EventMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_map, container, false);



        // Inflate the layout for this fragment
        return view;
    }
}