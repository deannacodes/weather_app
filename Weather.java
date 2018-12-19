package j.edu.wasp;

public class Weather {

    protected String city;
    protected String country;
    protected double tempature;

    Weather() {

    }

    Weather(String city, String country, double tempature) {
        this.city = city;
        this.country = country;
        this.tempature = tempature;
    }

    public String getCity() {
        return  this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTempature() {
        return this.tempature;
    }

    public void setTempature(double tempature) {
        this.tempature = tempature;
    }

}
