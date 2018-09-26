package com.example.welington.locedu.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.example.welington.locedu.Helper.ReferencesHelper;
import com.example.welington.locedu.Helper.VetorHelper;
import com.example.welington.locedu.Model.Local;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private TextView distancia, posicaoAtual;
    private FloatingActionButton focalizaCamera;
    private String rotas = "", rotaPoli = "";

    private boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        distancia = findViewById(R.id.tv_distancia);
        posicaoAtual = findViewById(R.id.tv_posicaoAtual);
        focalizaCamera = findViewById(R.id.fb_focaliza_camera);

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

                distancia.setText(String.valueOf(location.distanceTo(locationDest)));

                latLngUser = new LatLng(location.getLatitude(), location.getLongitude());

                posicaoAtual.setText(latLngUser.toString());

                positionCamera = new CameraPosition.Builder().target(latLngUser).zoom(19).build();
                updateCamera = CameraUpdateFactory.newCameraPosition(positionCamera);

                if (map != null) {
                    if (markerLocation != null){
                        markerLocation.setPosition(latLngUser);
                        checaDistancia(location);

                    }else{
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("Você");
                        markerOptions.icon(VetorHelper.bitmapDescriptorFromVector(Mapa.this, R.drawable.ic_marker_radius));
                        markerOptions.position(latLngUser);

                        markerLocation = map.addMarker(markerOptions);
                    }

                }
                if (init) {//verificando se camera ja foi setada para posicao do usuário
                    map.moveCamera(updateCamera);
                    init = false;
                }
            }
        };

        //focar camera la localização do usuário
        focalizaCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(updateCamera);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //map.moveCamera(updateCamera);

        //sobrepor imagem no mapa
        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(-20.715010, -46.628925),
                new LatLng(-20.713806, -46.627306));

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.planta_campus))
                .positionFromBounds(newarkBounds);

        GroundOverlay imageOverlay = map.addGroundOverlay(newarkMap);

        //Colocando marcador na localizacao do usuario
        latLngDest = new LatLng(local.getLatitude(), local.getLongitude());
        MarkerOptions marcador = new MarkerOptions();
        marcador.title("Destino");
        marcador.snippet(local.getNomeLocal());
        marcador.position(latLngDest);
        map.addMarker(marcador);

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            finish();
        }

        mLocationServices.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        //colocando a polilinha

        if(polyline == null){

            /*ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    rotaPoli = dataSnapshot.child("Rotas").child("1").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };*/

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add();
            polylineOptions.color(Color.BLUE);

            polyline = map.addPolyline(polylineOptions);

            //ReferencesHelper.getDatabaseReference().addValueEventListener(valueEventListener);

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
