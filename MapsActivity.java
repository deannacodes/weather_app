package j.edu.wasp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener, GoogleMap.OnMarkerClickListener {

    protected static GoogleMap mMap;
    protected static Family family = new Family();
    protected static ArrayList<FamilyMember> tempFam
            = Family.getFamily();
    protected ImageButton addFamilyBtn;
    protected Switch viewDisplay;
    protected ImageButton editFamilyBtn;
    private int count = 0;
    protected boolean normal= true;
    protected boolean hybrid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        addFamilyBtn = (ImageButton) findViewById(R.id.addFamily);
        addFamilyBtn.setOnClickListener(this);
        editFamilyBtn = (ImageButton) findViewById(R.id.editFamily);
        editFamilyBtn.setOnClickListener(this);
        viewDisplay = (Switch) findViewById(R.id.viewDisplay);
        viewDisplay.setOnClickListener(this);

        if (mMap != null) {
            mMap.clear();
            MapsActivity.displayFamilyonMap();
        }

    }

    public Family family() {
        return this.family;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        displayFamilyonMap();

        mMap.setOnMarkerClickListener(this);

        Directions directions = new Directions();
        directions.setOrgin("Cudahy");
        directions.setDestination("Las Vegas");

    }

    public GoogleMap getMap() {
        return this.mMap;
    }


    public static void displayFamilyonMap() {
        MarkerOptions[] markers = family.displayFamily();
        int index = 0;
        for(MarkerOptions marker: markers) {
            FamilyMember member = family.getFamilyMember(index);
            mMap.addMarker(marker.position(member.getCoordinates()).title(member.getFName() + " " + member.getlName())).hideInfoWindow();
            index++;
        }

    }

    public void addFamilyMember() {
       Intent addFamily = new Intent(this, addfamily.class);

        startActivity(addFamily);
    }

    public void addFamilyMemberToMap(FamilyMember familyMember) {
        MarkerOptions m = new MarkerOptions();
       // mMap.addMarker()
    }


    @Override
    public void onClick(View clicked) {
        if(clicked.getId() == addFamilyBtn.getId()) {
            addFamilyMember();
        } else if(clicked.getId() == viewDisplay.getId()) {
            count++;
            if(count % 2 == 0) {
                normal = hybrid;
            } else {
                normal = true;
            }
            if(normal) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                viewDisplay.setText("Hybrid");
                viewDisplay.setTextColor(Color.BLUE);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                viewDisplay.setTextColor(Color.BLACK);
                viewDisplay.setText("Normal");
            }
        } else if(clicked.getId() == editFamilyBtn.getId()) {
            Intent editFamIntent = new Intent(this, editFamilyActivity.class);
            startActivity(editFamIntent);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, MainWeather.class);
        LatLng coordinates = (LatLng) marker.getPosition();
        intent.putExtra("coord", coordinates);
        System.out.println("COORD: " + coordinates);

        for(FamilyMember fam: family().getFamily()) {
            System.out.println("SEARCHING: " + fam.getCoordinates());
            if(fam.getCoordinates().latitude == coordinates.latitude &&
                    fam.getCoordinates().longitude == coordinates.longitude) {
                System.out.println("FOUND: " + fam.getFName());
                intent.putExtra("id", fam.getId());
                break;
            }

        }

       // intent.putExtra("family", );
        startActivity(intent);
        finish();
        return false;
    }
}