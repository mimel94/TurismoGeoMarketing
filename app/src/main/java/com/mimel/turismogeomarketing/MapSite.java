package com.mimel.turismogeomarketing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mimel.turismogeomarketing.modelos.Places;

import java.util.Map;

public class MapSite extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    String latitud;
    String longitud;
    GoogleMap mapGoogle;
    MapView mapGoogleView;

    DatabaseReference refSite;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;

    Button btnSave;
    RatingBar ratingBarPlace;

    Places place = new Places();
    //View myView;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_site);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSite = database.getReference(FirebaseReferences.PLACES);

        Bundle placeSend = getIntent().getExtras();
        btnSave = (Button)findViewById(R.id.savePlaceBtn);
        ratingBarPlace = (RatingBar)findViewById(R.id.ratingBar);

        ratingBarPlace.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                place.setScore(ratingBar.getRating());
            }
        });



       if(placeSend != null){
            place = (Places) placeSend.getSerializable("place");
        }

        mapGoogleView = (MapView) findViewById(R.id.setMap);
        if (mapGoogleView != null) {
            mapGoogleView.onCreate(null);
            mapGoogleView.onResume();
            mapGoogleView.getMapAsync((OnMapReadyCallback) this);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(place.getLongitud() == null)){
                    refSite.child(mAuth.getCurrentUser().getUid()).push().setValue(place);
                    Toast.makeText(getApplicationContext(), "Sitio creado!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), PrincipalUser.class);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), "Debe escoger la ubicación!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mapGoogle = googleMap;

        UiSettings uiSettings = mapGoogle.getUiSettings();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapGoogle.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }

        uiSettings.isZoomControlsEnabled();
        uiSettings.isZoomGesturesEnabled();
        uiSettings.setMyLocationButtonEnabled(true);


        mapGoogle.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapGoogle.clear();
                Marker site = mapGoogle.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true));
                double latitud = latLng.latitude;
                double longitud = latLng.longitude;
                setLocationLatLng(longitud, latitud);


            }
        });


    }

    /*@Override
    public boolean onMarkerClick(Marker marker) {
        Marker site = mapGoogle.addMarker(new MarkerOptions()
                .position(marker.getPosition())
                .draggable(true));
        return false;
    }*/

    public void setLocationLatLng(double longitud, double latitud) {
        this.latitud =  String.valueOf(latitud);
        this.longitud = String.valueOf(longitud);
        place.setLocation(this.latitud, this.longitud);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mapGoogle.clear();
        Marker site = mapGoogle.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
        double latitud = latLng.latitude;
        double longitud = latLng.longitude;
        setLocationLatLng(longitud, latitud);

    }
}
