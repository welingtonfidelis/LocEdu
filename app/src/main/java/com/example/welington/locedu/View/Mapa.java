package com.example.welington.locedu.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.example.welington.locedu.Controller.VetorHelper;
import com.example.welington.locedu.Model.Local;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.welington.locedu.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFrag; //mapa
    private GoogleMap map;
    private LocationRequest mLocationRequest; //Configura o pedido de localização
    private Marker markerLocation; //marcador da posicao atual
    private Polyline polyline;
    private Location location, locationDest;
    private LatLng latLngUser, latLngDest;
    private CameraPosition positionCamera;
    private CameraUpdate updateCamera;
    private FusedLocationProviderClient mLocationServices;
    private LocationCallback mLocationCallback;
    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        final TextView distancia = findViewById(R.id.tv_distancia);
        locationDest = new Location("");
        locationDest.setLatitude(local.getLatitude());
        locationDest.setLongitude(local.getLongitude());

        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);

        mapFrag = SupportMapFragment.newInstance(options);
        mapFrag.getMapAsync(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.llMap, mapFrag);
        ft.commit();


        mLocationServices = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                location = locationResult.getLastLocation();

                latLngUser = new LatLng(location.getLatitude(), location.getLongitude());

                positionCamera = new CameraPosition.Builder().target(latLngUser).zoom(18).build();
                updateCamera = CameraUpdateFactory.newCameraPosition(positionCamera);

                if (map != null) {
                    if (markerLocation != null){
                        markerLocation.setPosition(latLngUser);
                        distancia.setText(String.valueOf(location.distanceTo(locationDest)));
                        checaDistancia(location);

                    }else{
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("Você");
                        markerOptions.icon(VetorHelper.bitmapDescriptorFromVector(Mapa.this, R.drawable.ic_marker_radius));
                        markerOptions.position(latLngUser);

                        markerLocation = map.addMarker(markerOptions);
                    }

                    map.moveCamera(updateCamera);
                }
            }
        };

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Colocando marcador na localizacao do usuario
        latLngDest = new LatLng(local.getLatitude(), local.getLongitude());
        MarkerOptions marcador = new MarkerOptions();
        marcador.title("Destino");
        marcador.snippet(local.getNomeLocal());
        marcador.position(latLngDest);
        map.addMarker(marcador);

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            finish();
        }

        mLocationServices.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        //colocando a polilinha

        if(polyline == null){

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(new LatLng(-20.7146686,-46.6277484), new LatLng(-20.7144947,-46.6286372));
            polylineOptions.color(Color.BLUE);

            polyline = map.addPolyline(polylineOptions);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocationServices.removeLocationUpdates(mLocationCallback);
    }

    public void checaDistancia(Location locationUser){
        if(location.distanceTo(locationDest) <= 5){
            mLocationServices.removeLocationUpdates(mLocationCallback);//parando de buscar localização do usuario
            Intent it = new Intent(getBaseContext(), Feedback.class);
            startActivity(it);
        }
    }
}
