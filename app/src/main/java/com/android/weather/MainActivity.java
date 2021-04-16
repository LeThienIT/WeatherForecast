package com.android.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String API_KEY = "acbae9c57a24663635f3918fd4e8f0c7";

    EditText edtProvince;
    Button btnOK, btnNextDay;
    TextView txtProvince, txtNational, txtTemper,
            txtTemperState, txtCloud,txtHumidity,
            txtWind,txtCurenTemper;
    ImageView imgWeatherIcon;

    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        btnOK.setOnClickListener(this);
        btnNextDay.setOnClickListener(this);
        if(city == ""){
            getJSonWeather("hanoi");
        }else{
            getJSonWeather(city);
        }
    }

    public void getJSonWeather(final String city){
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String icon = weatherObject.getString("icon");
                            String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";
                            Picasso.get().load(urlIcon).into(imgWeatherIcon);
                            String temperState = weatherObject.getString("main");
                            txtTemperState.setText(temperState);
                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");
                            txtTemper.setText(temp+"ºC");
                            String humidity = main.getString("humidity");
                            txtHumidity.setText(humidity+"%");
                            JSONObject wind = response.getJSONObject("wind");
                            String speed = wind.getString("speed");
                            txtWind.setText(speed+"m/s");
                            JSONObject clouds = response.getJSONObject("clouds");
                            String all = clouds.getString("all");
                            txtCloud.setText(all+"%");
                            String sNgay = response.getString("dt");
                            long lNgay = Long.parseLong(sNgay); // chuyển đổi ngày
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(lNgay*1000);
                            String currentTime = dateFormat.format(date); // ngày giờ cập nhật lấy dữ liệu
                            txtCurenTemper.setText(currentTime);
                            String name = response.getString("name");
                            txtProvince.setText("Tên thành phố "+name);
                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");
                            txtNational.setText("Quốc gia "+country);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố " + city, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void mapping() {
        edtProvince = findViewById(R.id.edtProvince);
        btnOK = findViewById(R.id.btnOK);
        btnNextDay = findViewById(R.id.btnNextDay);
        txtProvince = findViewById(R.id.txtProvince);
        txtNational = findViewById(R.id.txtNational);
        txtTemper = findViewById(R.id.txtTemper);
        txtTemperState = findViewById(R.id.txtTemperState);
        txtCloud = findViewById(R.id.txtCloud);
        txtWind = findViewById(R.id.txtWind);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtCurenTemper = findViewById(R.id.txtCurenTemper);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOK:
                city = edtProvince.getText().toString().trim();
                if(city.equals("")){
                    city = "hanoi";
                }
                getJSonWeather(city);
                break;
        }
    }
}