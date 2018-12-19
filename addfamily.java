package j.edu.wasp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class addfamily extends AppCompatActivity implements View.OnClickListener {

    protected  Family family;
    protected Button addFam;
    protected EditText fName;
    protected EditText lName;
    protected EditText city;
    protected EditText country;
    protected GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfamily);

        family = MapsActivity.family;

        addFam = (Button) findViewById(R.id.addBtn);

        fName = (EditText) findViewById(R.id.fName);
        lName = (EditText) findViewById(R.id.lName);
        city = (EditText) findViewById(R.id.city);
        country = (EditText) findViewById(R.id.country);

        mMap = MapsActivity.mMap;
        addFam.setOnClickListener(this);

    }

    public boolean validEntry(EditText fName, EditText lName, EditText city, EditText country) {
        if(!fName.getText().toString().equals("") && !lName.getText().toString().equals("") && !city.getText().toString().equals("")
                && !country.getText().toString().equals("") && !fName.getText().toString().equals("First Name")
                && !lName.getText().toString().equals("Last Name") && !city.getText().toString().equals("City")
                && !country.getText().toString().equals("Country")) {
            return true;
        }
        return false;
    }

    public LatLng getCoordinates(String city) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> c = null;
        LatLng coord = null;
        try {
            c = geocoder.getFromLocationName(city, 1);
            coord = new LatLng(c.get(0).getLatitude(), c.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coord;
    }

    @Override
    public void onClick(View clicked) {
        if(clicked.getId() == addFam.getId()) {

            if(validEntry(fName, lName, city, country)) {

                LatLng coord = getCoordinates(city.getText().toString());

                family.add(fName.getText().toString(), lName.getText().toString(), coord,
                        city.getText().toString(), country.getText().toString());

                mMap.clear();
                MapsActivity.displayFamilyonMap();
                finish();

            }
        }
    }
}
