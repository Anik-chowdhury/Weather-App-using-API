package com.example.weatherapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue,requestQueue1;
    private String searching_location = "Chittagong";

    TextView tvPlace,tvWeekday,tvStatus,tvTemp,tvWind,tvHumidity;
    ImageView ivDetailImage;
    Button btnaddlocation;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 84 && resultCode == RESULT_OK) {
            searching_location = data.getStringExtra("value");
            JSONparse(searching_location);
            JSONforcast(searching_location);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // seek permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this,"Internet access granted",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"Need network access",Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
        //

        JSONparse(searching_location);
        JSONforcast(searching_location);

        btnaddlocation = findViewById(R.id.btnaddloc);
        btnaddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add.class);
                startActivityForResult(intent, 84);
            }
        });
    }

    private void JSONforcast(String getlocation) {

        String url = "https://api.weatherapi.com/v1/forecast.json?key=e351211cd8194f28972145134200306&q=" + getlocation + "&days=10";

        requestQueue1 = Volley.newRequestQueue(this);
        final ArrayList<Weather> weatherinfo = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("forecast");
                    JSONArray forecastday = jsonObject.getJSONArray("forecastday");
                    int forecastdaylength = forecastday.length();
                    if (forecastdaylength > 3) forecastdaylength = 3;
                    for (int i=0; i<forecastdaylength; i++) {
                        JSONObject detailobject = forecastday.getJSONObject(i);
                        String date = detailobject.getString("date");
                        JSONObject daycondition = detailobject.getJSONObject("day");

                        double avgtemp = daycondition.getDouble("avgtemp_c");
                        double maxwind_mph = daycondition.getDouble("maxwind_mph");
                        double temp_humidity = daycondition.getDouble("avghumidity");
                        temp_humidity = Math.floor(temp_humidity);
                        int avghumidity = (int) temp_humidity;
                        int daychanceofrain = daycondition.getInt("daily_chance_of_rain");

                        JSONObject textobject = daycondition.getJSONObject("condition");
                        String text = textobject.getString("text");
                        String icon = textobject.getString("icon");
                        weatherinfo.add(new Weather(date, text, icon, "#", avgtemp, maxwind_mph, 0, avghumidity, daychanceofrain));
                    }
                    changeForcastUI(weatherinfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error loading forcast data", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue1.add(jsonObjectRequest);
    }

    private void changeForcastUI(ArrayList<Weather> weatherinfo) {
        ListView listView = (ListView) findViewById(R.id.listView);
        WeatherAdapter wadapter = new WeatherAdapter(this,R.layout.weather_list,weatherinfo);
        listView.setAdapter(wadapter);
    }

    private void JSONparse(String getlocation) {

        String url = "https://api.weatherapi.com/v1/current.json?key=e351211cd8194f28972145134200306&q="+getlocation;

        requestQueue = Volley.newRequestQueue(this);
        final ArrayList<Weather> weatherinfo = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject dateobject = response.getJSONObject("location");
                    String localtime = dateobject.getString("localtime");

                    localtime = localtime.substring(0,10);

                    JSONObject jsonObject = response.getJSONObject("current");
                    Double temp = jsonObject.getDouble("temp_c");
                    int isday = jsonObject.getInt("is_day");

                    JSONObject conditionobject = jsonObject.getJSONObject("condition");
                    String text = conditionobject.getString("text");
                    String icon = conditionobject.getString("icon");

                    double wind_mph = jsonObject.getDouble("wind_mph");
                    int wind_degree = jsonObject.getInt("wind_degree");
                    String wind_dir = jsonObject.getString("wind_dir");
                    int humidity = jsonObject.getInt("humidity");

                    weatherinfo.add(new Weather(localtime, text, icon, wind_dir, temp, wind_mph, isday, humidity, 0));
                    changeMainUI(weatherinfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error Loading city",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void changeMainUI(ArrayList<Weather> weatherinfo) {
        tvPlace = findViewById(R.id.tvPlace);
        tvWeekday = findViewById(R.id.tvWeekday);
        tvStatus = findViewById(R.id.tvStatus);
        tvTemp = findViewById(R.id.tvTemp);
        tvWind = findViewById(R.id.tvWind);
        tvHumidity = findViewById(R.id.tvHumidity);
        ivDetailImage = findViewById(R.id.ivDetailImage);

        Weather todaysWeahter = weatherinfo.get(0);

        tvPlace.setText(searching_location);
        tvWeekday.setText(todaysWeahter.getTime());
        tvTemp.setText(String.valueOf(todaysWeahter.getTemp())+"° C");
        String windinfo = String.valueOf(todaysWeahter.getWindmph())+" mph";
        if(todaysWeahter.getWinddir() != "#") windinfo = windinfo + " \n" + todaysWeahter.getWinddir();
        tvWind.setText(windinfo);
        tvHumidity.setText(String.valueOf(todaysWeahter.getHumidity())+"%");
        String weather_status = todaysWeahter.getText();
        tvStatus.setText(weather_status);

        int isday = todaysWeahter.getIsday();
        if(isday == 1) {
            if (weather_status.indexOf("Cloud") != -1 || weather_status.indexOf("cloud") != -1) {
                ivDetailImage.setImageResource(R.drawable.cloudy);
            }
            else if(weather_status.indexOf("Rain") != -1 || weather_status.indexOf("rain") != -1) {
                ivDetailImage.setImageResource(R.drawable.rainy);
            }
            else if(weather_status.indexOf("Haze") != -1 || weather_status.indexOf("haze") != -1) {
                ivDetailImage.setImageResource(R.drawable.hazeinday);
            }
            else {
                ivDetailImage.setImageResource(R.drawable.sunnyday);
            }
        }
        else {
            if (weather_status.indexOf("Cloud") != -1 || weather_status.indexOf("cloud") != -1) {
                ivDetailImage.setImageResource(R.drawable.cloudynight);
            }
            else if(weather_status.indexOf("Rain") != -1 || weather_status.indexOf("rain") != -1) {
                ivDetailImage.setImageResource(R.drawable.rainy);
            }
            else if(weather_status.indexOf("Haze") != -1 || weather_status.indexOf("haze") != -1) {
                ivDetailImage.setImageResource(R.drawable.hazeinnight);
            }
            else {
                ivDetailImage.setImageResource(R.drawable.clearnight);
            }
        }
    }
}
