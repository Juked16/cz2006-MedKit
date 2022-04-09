package com.example.medkit2006.boundary;

import static com.example.medkit2006.MainActivity.facilityMgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.boundary.SearchUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilityDetailUI extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facil_detail);
        //Instantiation of map object, get a handle to the fragment and register the callback.
        try{
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch(Exception e){
            Log.d("Map","Map instantiation unsuccessful.");
        }
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchUI.EXTRA_MESSAGE);
        Log.d("Received MF Name", message);
        facilityMgr.getFacilityDetails(message, facility -> {
                    TextView textView = findViewById(R.id.medfacil_name);
                    textView.setText(facility.getName());
                    textView = findViewById(R.id.medfacil_descr);
                    textView.setText(facility.getDescription());
                    if(facility.getLatitude() != 0.0 || facility.getLongitude() != 0.0){
                    LatLng pos = new LatLng(facility.getLatitude(), facility.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .position(pos)
                            .title(facility.getName()));
                    map.moveCamera(CameraUpdateFactory.newLatLng(pos));}
                },
                e -> { Log.d("Received Medical Facility List Unsuccessful", e.toString().trim()); });
        // Capture the layout's TextView and set the string as its text
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng sing = new LatLng(1.287953, 103.851784);
        googleMap.addMarker(new MarkerOptions()
                .position(sing)
                .title("Downtown Core"));
        // Move the camera to the map coordinates and zoom in closer.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sing));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        // Display traffic
        googleMap.setTrafficEnabled(true);
    }

    public void goBack(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}