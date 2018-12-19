package j.edu.wasp;

public class Directions {

    private String directions;
    private String orgin;
    private String destination;
    private String url
            = "https://maps.googleapis.com/maps/api/directions/json?";

    // https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal

    Directions() {

    }

    Directions(String orgin, String destination){
        this.orgin = orgin;
        this.destination = destination;
    }

    public String getOrgin() {
        return orgin;
    }

    public void setOrgin(String orgin) {
        this.orgin = orgin;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private boolean valid(String place) {
        if(place.length() > 0 && place != "" && place.trim().length() > 0) {
            return true;
        }
        return false;
    }

    public String getUrl() {
        if(valid(this.orgin) && valid(this.destination)) {
            url += "orgin=" + this.orgin
                    + "&destination="
                    + this.destination;
            return url;
        }
        return null;
    }


}
