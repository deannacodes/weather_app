package j.edu.wasp;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FamilyMember {

    private String fName;
    private String lName;
    private LatLng coordinates;
    private String city;
    private String country;
    private static long count = 0;
    private long id;

    FamilyMember() {

    }

    FamilyMember(String fName, String lName, LatLng coordinates, String city, String country) {
        this.fName = fName;
        this.lName = lName;
        this.coordinates = coordinates;
        this.city = city;
        this.country = country;
        this.id = count++;
    }

    public long getId() {
        return this.id;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getFName() {
        return this.fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getlName() {
        return this.lName;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public LatLng getCoordinates() {
        return this.coordinates;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public String toString() {
        String info = "";
        info = this.lName + ", " + this.fName + " ";
        info += " is okay";
        return info;
    }

}