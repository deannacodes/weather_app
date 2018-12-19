package j.edu.wasp;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Family {

    protected static ArrayList<FamilyMember> family;
    private int familySize = 0;
    Family() {
        family = new ArrayList<>();
        LatLng susieCord = new LatLng(30.2672, 97.7431);
        family.add(new FamilyMember("Arthur", "Morgan", susieCord, "Austin", "USA"));
        familySize++;
        LatLng norbCord = new LatLng(59.9139, 10.7522);
        family.add(new FamilyMember("Ulfric", "Stormcloak", norbCord, "Oslo", "Norway"));
        familySize ++;
        LatLng grandCord = new LatLng(43.6532, 79.3832);
        family.add(new FamilyMember("Knives", "Chau", grandCord, "Toronto", "Canada"));
        familySize++;
        LatLng londCord = new LatLng(51.51334, -0.08901);
        family.add(new FamilyMember("Farrokh", "Bulsara", londCord, "London", "Great Britain"));
        familySize++;
        LatLng jpCord = new LatLng(35.6895, 139.6917);
        family.add(new FamilyMember("Hidetoshi", "Hasagawa", jpCord, "Tokyo", "Japan"));
        familySize++;
        LatLng auCord = new LatLng(33.8688, 151.2093);
        family.add(new FamilyMember("James", "Howlett", auCord, "Sydney", "Australia"));
        familySize++;

    }

    public static ArrayList<FamilyMember> getFamily() {
        return family;
    }

    public void add(String fName, String lName, LatLng coord, String city, String country) {
        family.add(new FamilyMember(fName, lName, coord, city, country));
        familySize++;
    }

    public FamilyMember getFamilyMember(String fName, String lName) {
        for(FamilyMember fam: family) {
            if(fam.getFName().equals(fName) && fam.getlName().equals(lName))
                return fam;
        }
        return null;
    }

    public FamilyMember getFamilyMember(int index) {
        return family.get(index);
    }


    public int size() {
        return this.familySize;
    }


    public MarkerOptions[] displayFamily() {
        MarkerOptions[] markers = new MarkerOptions[family.size()];
        int index = 0;
        for(FamilyMember fam: family) {
            if(fam != null) {
                LatLng coordinates = fam.getCoordinates();
                MarkerOptions markerOptions
                        = new MarkerOptions().position(coordinates).title(fam.getCity());
                markers[index] = markerOptions;
                index++;
            }
        }
        return markers;
    }

}
