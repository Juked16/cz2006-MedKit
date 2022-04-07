package com.example.medkit2006.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.boundary.SearchUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilDetailUI extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facil_detail);
        //Instantiation of map object, get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchUI.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.medfacil_name);
        textView.setText(message);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set the map coordinates to Kyoto Japan.
        LatLng sing = new LatLng(1.3340416323795148, 103.84970722221946);
        googleMap.addMarker(new MarkerOptions()
                .position(sing)
                .title("Raffles"));
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sing));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic
        googleMap.setTrafficEnabled(true);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}