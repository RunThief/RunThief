package com.example.runthief;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

    static final LatLng SEOUL = new LatLng(37.724, 126.7167);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(SEOUL)
                .title("Seoul")/*.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))*/);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

}
