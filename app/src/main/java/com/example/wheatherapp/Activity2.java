package com.example.wheatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.MalformedURLException;

import java.util.stream.Collectors;


public class Activity2 extends AppCompatActivity {
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static String CITY = "";
    private static final String WEATHER_API_TEXT = ",RU&appid=";
    private static final String WEATHER_API_KEY = "3174fa6f190426fd2b1b0cc5ee920d4d";
    private static final String IDCITY = "IDCITY";

    private static EditText city;
    private static EditText temperature;
    private static EditText pressure;
    private static EditText humidity;
    private static EditText windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Intent intent = getIntent();
        CITY = intent.getStringExtra(IDCITY);
        System.out.println(CITY);
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Home:
                Intent intent = new Intent(Activity2.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_history:
                Intent intent1 = new Intent(Activity2.this,SearchHistory.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init(){
        city = findViewById(R.id.textCity);
        temperature = findViewById(R.id.textTemprature);
        pressure = findViewById(R.id.textPressure);
        humidity = findViewById(R.id.textHumidity);
        windSpeed = findViewById(R.id.textWindspeed);
//        onRefresh();
        startService(new Intent(Activity2.this, Services.class));
        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startService(new Intent(Activity2.this, Services.class));
            }
        });
    }


        public static void onRefresh() {
            try {
                final URL uri = new URL(WEATHER_URL + CITY + WEATHER_API_TEXT + WEATHER_API_KEY);
                final Handler handler = new Handler(); // Запоминаем основной поток
                new Thread(new Runnable() {
                    public void run() {
                        HttpsURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpsURLConnection) uri.openConnection();
                            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                            String result = getLines(in);
                            // преобразование данных запроса в модель
                            Gson gson = new Gson();
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            // Возвращаемся к основному потоку
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    displayWeather(weatherRequest);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "Fail connection", e);
                            e.printStackTrace();
                        } finally {
                            if (null != urlConnection) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                }).start();
            } catch (MalformedURLException e) {
                Log.e(TAG, "Fail URI", e);
                e.printStackTrace();
            }
        }

        private static String getLines(BufferedReader in) {
            return in.lines().collect(Collectors.joining("\n"));
        }

        private static void displayWeather(WeatherRequest weatherRequest){
            city.setText(weatherRequest.getName());
            temperature.setText(String.format("%f2", weatherRequest.getMain().getTemp()));
            pressure.setText(String.format("%d", weatherRequest.getMain().getPressure()));
            humidity.setText(String.format("%d", weatherRequest.getMain().getHumidity()));
            windSpeed.setText(String.format("%d", weatherRequest.getWind().getSpeed()));
        }

}

