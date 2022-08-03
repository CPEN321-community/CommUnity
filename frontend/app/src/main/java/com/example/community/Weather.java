package com.example.community;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Weather extends AppCompatActivity {
    EditText city_input, country_input;
    TextView weather_data;
    //OpenWeatherMap API used for the Surprise weather button functionality
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "5603ddab5ca3086399dbee9a2fba312d";

    //Decimal format says that we are specifying up to 1 decimal place and rounding the number
    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city_input = findViewById(R.id.city);
        country_input = findViewById(R.id.country);
        weather_data = findViewById(R.id.tvResult);
    }

    public void getWeatherData(View view) {
        String tempurl = "";
        String city = city_input.getText().toString().trim();
        String country = country_input.getText().toString().trim();

        //City field is mandatory
        if(city.equals("")){
            weather_data.setText("Cannot have empty city field");
        } else{
            //Country field is optional
            if(country.equals("")){
                tempurl = url + "?q=" + city + "&appid=" + appid;
            } else{
                tempurl = url + "?q=" + city + "," + country + "&appid=" + appid;
            }

            StringRequest stringReq = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    String data_output = "";
                    try{
                        JSONObject jsonResp = new JSONObject(response);
                        JSONArray jsonArr = jsonResp.getJSONArray("weather");
                        JSONObject jsonObjWeather = jsonArr.getJSONObject(0);
                        String description = jsonObjWeather.getString("description");
                        JSONObject jsonObjMain = jsonResp.getJSONObject("main");

                        //Get temperature in kelvins and convert to celsius
                        double temp = jsonObjMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjMain.getDouble("feels_like") - 273.15;

                        JSONObject jsonObjWind = jsonResp.getJSONObject("wind");
                        String wind = jsonObjWind.getString("speed");

                        //Location details
                        JSONObject jsonObjectSys = jsonResp.getJSONObject("sys");
                        String country_name = jsonObjectSys.getString("country");
                        String city_name = jsonResp.getString("name");
                        weather_data.setTextColor(Color.rgb(0, 0, 0));

                        data_output += "Weather in " + city_name + " (" + country_name + ")" + "\n Description: " + description
                                + "\n Temperature: " + df.format(temp) + " °C" + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                + "\n Wind Speed: " + wind + "m/s";

                        weather_data.setText(data_output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringReq);
        }
    }
}
