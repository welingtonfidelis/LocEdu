package br.com.welingtonfidelis.locedu.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Helper.VetorHelper;
import br.com.welingtonfidelis.locedu.Model.Evento;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

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
    private Evento evento;
    private TextView distancia, textoAndar, andar;
    private FloatingActionButton focalizaCamera;
    private boolean tipoChamada, tipoMapa = true;
    private Switch trocaMapa;
    private MarkerOptions markerOptions;

    private boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            new AlertDialog.Builder(Mapa.this)
                    .setTitle("GPS DESATIVADO")
                    .setMessage("Por favor, verifique se seu GPS está ativado.")
                    .setPositiveButton("OK", null).show();
        }

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);
        evento = gson.fromJson(getIntent().getStringExtra("EVENTO"), Evento.class);

        Intent it = getIntent();
        tipoChamada = it.getBooleanExtra("TIPOCHAMADA", false);//verifica se a chamada veio da tela inicial ou do mapa com destino

        //Criando e editando toolbar
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SUA LOCALIZAÇÃO");
        getSupportActionBar().setSubtitle("Você está aqui");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_cor));

        distancia = findViewById(R.id.tv_distancia);
        focalizaCamera = findViewById(R.id.fb_focaliza_camera);
        andar = findViewById(R.id.tv_andar_mapa);
        trocaMapa = findViewById(R.id.swt_troca_mapa);

        if(evento != null){//verifica se a chamada veio da lista de eventos
            locationDest = new Location("");
            locationDest.setLatitude(evento.getLatitude());
            locationDest.setLongitude(evento.getLongitude());
            distancia.setVisibility(View.VISIBLE);
            andar.setVisibility(View.VISIBLE);
            andar.setText("Andar: "+  evento.getAndar());
            getSupportActionBar().setSubtitle("Destino: "+evento.getNomeEvento());
        }

        else if(tipoChamada){
            locationDest = new Location("");
            locationDest.setLatitude(local.getLatitude());
            locationDest.setLongitude(local.getLongitude());
            distancia.setVisibility(View.VISIBLE);
            andar.setVisibility(View.VISIBLE);
            andar.setText("Andar: "+  local.getAndar());
            getSupportActionBar().setSubtitle("Destino: "+local.getNomeLocal());
        }


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

                if (tipoChamada) distancia.setText(String.format("%.2f",location.distanceTo(locationDest))+" m até seu destino");
                else if(evento != null) distancia.setText(String.format("%.2f",location.distanceTo(locationDest))+" m até seu destino");

                latLngUser = new LatLng(location.getLatitude(), location.getLongitude());

                positionCamera = new CameraPosition.Builder().target(latLngUser).zoom(19).build();
                updateCamera = CameraUpdateFactory.newCameraPosition(positionCamera);

                if (map != null) {
                    trocaMapa.setVisibility(View.VISIBLE);
                    if (markerLocation != null){
                        markerLocation.setPosition(latLngUser);
                        if(tipoChamada) checaDistancia(location);

                        if(trocaMapa.isChecked() && tipoMapa){
                            map.clear();
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            trocaMapa.setText("Mapa");

                            carregaMarcadorUsuario();
                            carregaMarcadoDestino();

                            tipoMapa = false;
                        }
                        else if(!trocaMapa.isChecked() && !tipoMapa){
                            map.clear();
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            trocaMapa.setText("Satélite");

                            carregaPlanta();
                            carregaMarcadoDestino();
                            carregaMarcadorUsuario();

                            tipoMapa = true;
                        }


                    }else{
                        markerOptions = new MarkerOptions();
                        carregaMarcadorUsuario();
                    }

                }
                if (init) {//verificando se camera ja foi setada para posicao do usuário
                    map.moveCamera(updateCamera);
                    init = false;
                }
            }
        };

        //focar camera na localização do usuário
        focalizaCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location!= null){
                    map.moveCamera(updateCamera);
                }
                else{
                    Toast.makeText(Mapa.this, "Recebendo sinal de GPS, aguarde por favor.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        carregaPlanta();

        carregaMarcadoDestino();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
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

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add();
            polylineOptions.color(Color.BLUE);

            polyline = map.addPolyline(polylineOptions);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_ajuda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent it = new Intent(Mapa.this, Home.class);
                startActivity(it);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ReferencesHelper.getFirebaseAuth().signOut();
        mLocationServices.removeLocationUpdates(mLocationCallback);
    }

    public void checaDistancia(Location locationUser){
        if(location.distanceTo(locationDest) <= 5){
            mLocationServices.removeLocationUpdates(mLocationCallback);//parando de buscar localização do usuario
            Intent it = new Intent(getBaseContext(), Feedback.class);
            startActivity(it);
        }
    }

    public void carregaPlanta(){
        //sobrepor imagem no mapa

        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(-20.714930, -46.628944),
                new LatLng(-20.713806, -46.627306));

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.planta_campus))
                .positionFromBounds(newarkBounds);

        GroundOverlay imageOverlay = map.addGroundOverlay(newarkMap);
    }

    public void carregaMarcadoDestino(){
        //Colocando marcador na localizacao do usuario
        if(tipoChamada){
            latLngDest = new LatLng(local.getLatitude(), local.getLongitude());
            MarkerOptions marcador = new MarkerOptions();
            marcador.title("Destino");
            marcador.snippet(local.getNomeLocal());
            marcador.position(latLngDest);
            map.addMarker(marcador);
        }
        else if(evento != null){
            latLngDest = new LatLng(evento.getLatitude(), evento.getLongitude());
            MarkerOptions marcador = new MarkerOptions();
            marcador.title("Destino");
            marcador.snippet(evento.getNomeEvento());
            marcador.position(latLngDest);
            map.addMarker(marcador);
        }
    }

    public void carregaMarcadorUsuario(){
        markerOptions.title("Você");
        markerOptions.icon(VetorHelper.bitmapDescriptorFromVector(Mapa.this, R.drawable.ic_marker_radius));
        markerOptions.position(latLngUser);

        markerLocation = map.addMarker(markerOptions);
    }
}
