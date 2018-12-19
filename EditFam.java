package j.edu.wasp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditFam extends AppCompatActivity implements View.OnClickListener {
    private Family family;
    private EditText fName;
    private EditText lName;
    private EditText city;
    private EditText country;
    private Button editBtn;
    private long id = -1;
    private FamilyMember familyMember = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fam);

        mMap = MapsActivity.mMap;

        family = MapsActivity.family;
        Bundle bundle = getIntent().getExtras();

        id = (long) bundle.get("id");

        for(FamilyMember fam: family.getFamily()) {
            if(fam.getId() == id) {
                familyMember = fam;
            }
        }

        editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);

        fName = (EditText) findViewById(R.id.editFName);
        fName.setText(familyMember.getFName());

        lName = (EditText) findViewById(R.id.editLName);
        lName.setText(familyMember.getlName());

        city = (EditText) findViewById(R.id.editCity);
        city.setText(familyMember.getCity());

        country = (EditText) findViewById(R.id.editCountry);
        country.setText(familyMember.getCountry());

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
    public void onClick(View v) {
        if(v.getId() == editBtn.getId()) {
            String firstName = fName.getText().toString();
            String lastName = lName.getText().toString();
            String cityName = city.getText().toString();
            String countryName = country.getText().toString();
            if(validEntry(fName, lName, city, country)) {
                for(FamilyMember fam: family.getFamily()) {
                    if(fam.getId() == id) {
                        fam.setfName(firstName);
                        fam.setlName(lastName);
                        fam.setCity(cityName);
                        fam.setCountry(countryName);

                        LatLng coordinates = getCoordinates(city.getText().toString());
                        fam.setCoordinates(coordinates);

                        mMap.clear();
                        MapsActivity.displayFamilyonMap();
                        finish();
                        break;
                    }
                }

            }
        }
    }
}
