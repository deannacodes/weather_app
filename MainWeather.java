package j.edu.wasp;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainWeather extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    TextView textViewCity;
    TextView textViewTemperature;
    TextView textViewTemperatureDesc;
    TextView family;
    LinearLayout forecastList;
    ImageView backgroundImage;
    private LatLng familyMember = null;
    private final String CHANNEL_ID = "channel1";
    private final int NOTIFICATION_ID = 001;
    private NotificationManagerCompat notificationManager;
    String mytemp;
    String weatherDesc;
    String famName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weather);

        createNotificationChannels();
        notificationManager = NotificationManagerCompat.from(this);


        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewTemperatureDesc = (TextView) findViewById(R.id.textViewTemperatureDesc);
        forecastList = (LinearLayout) findViewById(R.id.forecastList);
        backgroundImage = (ImageView) findViewById(R.id.weatherBackgroundImageView);

        family = (TextView) findViewById(R.id.family);

        Bundle bundle = null;

        if(getIntent().getExtras() != null) {

            bundle = getIntent().getExtras();
            if(bundle.get("coord") != null) {
                familyMember = (LatLng) bundle.get("coord");
            }

            System.out.println("WEATHER ACTIVITY");
            if(bundle.get("id") != null) {

                long id = (long) bundle.get("id");

                for(FamilyMember fam: MapsActivity.family.getFamily()) {
                    if(fam.getId() == id) {
                        String name = fam.getFName() + " " + fam.getlName();
                        getFamName(name);
                        family.setText(name);
                        break;
                    }
                }
            }

        }

        if(familyMember == null) {
            getFamName("Kanye");
        }

        String[] latAndLong = getLoc();
        String latitude = latAndLong[0];
        String longitude = latAndLong[1];

	String apiKey = "KEY"; //key removed for public display on GitHub

        String urlSingleDay = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=imperial";
        String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&cnt=10&appid=" + apiKey + "&units=imperial";//"https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&cnt=10&appid=" + apiKey + "&units=imperial";

        System.out.println(urlForecast);
        initDayWeather(urlSingleDay);
        initForecast(urlForecast);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_TAG");
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //we might not need this block of code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment(),"FRAG_TAG").commit();
        } else if (id == R.id.action_notify) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment(),"FRAG_TAG").commit();
        }
        else if (id == R.id.nav_maps) {
            Intent intent = new Intent(MainWeather.this, MapsActivity
                    .class);
            // startActivityFromChild(); This might help
            startActivity(intent);
        }
        else if (id == R.id.nav_images) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_TAG");
            if(fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        else if (id == R.id.nav_settings) {
            Toast.makeText(this,"Settings", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_send) {
            Toast.makeText(this,"Send", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=Join me on WASP&body=Download the app and let's connect!");
            intent.setData(data);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String[] getLoc() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Check for proper perms
        }

        Location lastKnownLocation = new Location("");
        lastKnownLocation.setLatitude(34.067016);
        lastKnownLocation.setLongitude(-118.166514);
        if (locationManager.getLastKnownLocation(locationProvider) != null) {
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        }

        if(familyMember != null) {
            System.out.println("MEMBER HAS A VALUE");
            lastKnownLocation.setLatitude(familyMember.latitude);
            lastKnownLocation.setLongitude(familyMember.longitude);
        }


        String latitude = Location.convert(lastKnownLocation.getLatitude(), Location.FORMAT_DEGREES);
        String longitude = Location.convert(lastKnownLocation.getLongitude(), Location.FORMAT_DEGREES);

        String[] latAndLong = {latitude, longitude};

        return latAndLong;
    }

    public void initDayWeather(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject mainWeather = response.getJSONObject("main");
                    String temperature = mainWeather.getString("temp");
                    String[] tempRounder = temperature.split("\\.");
                    temperature = tempRounder[0];
                    theTemp(temperature);
                    textViewTemperature.setText(temperature + "° F");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    String description = weatherArray.getJSONObject(0).getString("description");
                    wDesc(description);
                    textViewTemperatureDesc.setText(description);

                    setBackgroundImage(description);

                    String cityLocation = response.getString("name");
                    if (cityLocation == "Belvedere") {
                        cityLocation = "Cal State LA";
                    }
                    textViewCity.setText(cityLocation);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jsonObjectRequest);
    }

    public void initForecast(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray forecastArray = response.getJSONArray("list");
                    Calendar c = Calendar.getInstance();
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                    for ( int i = 0 ; i < forecastArray.length() ; i++ ) {

                        String hiTemp = forecastArray.getJSONObject(i).getJSONObject("main").getString("temp");
                        String[] tempRounder = hiTemp.split("\\.");
                        hiTemp = tempRounder[0];
                        String tempDesc = forecastArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");


                        //TextView day = new TextView(getBaseContext());
                        TextView weather = new TextView(getBaseContext());

                        //day.setText(getDayOfWeek(i));
                        weather.setText(getDayOfWeek(i + dayOfWeek) + ": \t\t" + hiTemp + " // " + tempDesc);
                        weather.setPadding(0, 5, 0, 5);

                        forecastList.addView(weather);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jsonObjectRequest);
    }

    public String getDayOfWeek(int today) {
        int findDate = today % 7;
        String day = "Sunday";

        switch (findDate) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 0:
                day = "Saturday";
                break;
        }

        return day;
    }

    public void setBackgroundImage(String desc) {
        if (desc.contains("sun") || desc.contains("clear")) {
            backgroundImage.setImageResource(R.drawable.sunny);
        } else if (desc.contains("cloud") || desc.contains("haze") || desc.contains("fog")) {
            backgroundImage.setImageResource(R.drawable.cloudy);
        } else if (desc.contains("rain") || desc.contains("hail")) {
            backgroundImage.setImageResource(R.drawable.rain);
        } else if (desc.contains("snow")) {
            backgroundImage.setImageResource(R.drawable.snow);
        }
    }
/*
    public void viewMap(View view)
    {
        Intent intent = new Intent(MainWeather.this, MapsActivity
                .class);
        startActivity(intent);
    }
*/


    public void viewAddFamily(View view)
    {
        Intent intent = new Intent(MainWeather.this, addfamily.class);
        startActivity(intent);
    }

/*
    public boolean displayNotification(MenuItem item) {

        //method for Android 8.0 and above users
        //createNotificationChannel();

        Intent landingIntent = new Intent(this, MainWeather.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);

        //Picture for the notification
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_menu_maps);
        builder.setContentTitle("Bring an Umbrella");
        builder.setContentText("There's a 70% chance of rain today.");
        builder.setLargeIcon(picture);
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(picture)
                .bigLargeIcon(null));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true);
        builder.setContentIntent(landingPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        return true;
    }
*/
    private void createNotificationChannels(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel ",
                    NotificationManager.IMPORTANCE_HIGH

            );
            channel1.setDescription("This is channel 1");



            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);


        }
    }

    public  void theTemp(String temper){
        mytemp = temper;

    }
    public  void wDesc(String description){
        weatherDesc = description;

    }

    public  void getFamName(String fam){
        famName = fam;

    }


    public void sendOnChannel1(MenuItem item) {

        Random rand = new Random();


        Bitmap picture = getNotificationImage();
        String notificationTitle = getNotificationTitle();
        String notificationMessage = getNotificationMessage();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_full)
                    .setLargeIcon(picture)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture).bigLargeIcon(null))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationMessage)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
        notificationManager.notify(rand.nextInt(100000) + 1, notification);


    }

    public Bitmap getNotificationImage() {
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert_none);
        if (weatherDesc.contains("cloud") || weatherDesc.contains("haze") || weatherDesc.contains("fog")) {
            picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert_cloudy);
        } else if (weatherDesc.contains("rain") || weatherDesc.contains("hail")) {
            picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert_rain);
        } else if (weatherDesc.contains("snow")) {
            picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert_snow);
        } else if (Integer.parseInt(mytemp) > 90) {
            picture = BitmapFactory.decodeResource(getResources(), R.drawable.alert_heat);
        }

        return picture;

    }

    public String getNotificationTitle() {
        String title = "Today's Weather for " + famName;
        return title;

    }

    public String getNotificationMessage() {
        String message = mytemp + "°F // " + weatherDesc;
        if (weatherDesc.contains("cloud") || weatherDesc.contains("haze") || weatherDesc.contains("fog")) {
            message = mytemp + "°F // " + weatherDesc;
        } else if (weatherDesc.contains("rain") || weatherDesc.contains("hail")) {
            message = mytemp + "°F // " + weatherDesc + ". Don't forget your umbrella.";
        } else if (weatherDesc.contains("snow")) {
            message = mytemp + "°F // " + weatherDesc + ". Use caution when driving.";
        } else if (Integer.parseInt(mytemp) > 70) {
            message = mytemp + "°F // " + weatherDesc +  ". Be cautious of heat stroke.";
        }

        return message;

    }

    public void refreshFragment(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_TAG");
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

}

