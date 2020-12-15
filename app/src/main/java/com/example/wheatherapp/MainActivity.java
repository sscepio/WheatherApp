package com.example.wheatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private final List<Item> items = new ArrayList<>();
   private final RecyclerView.Adapter adapter = new ItemAdapter(this.items);
    private static final String IDCITY = "IDCITY";
    private static String city;
    private static SensorManager sensorManager;
    private Sensor tempSensor;
    private Sensor absolutyHumidity;
    private  Boolean isTemperatureSensorAvailable;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private TextView textTemp;
    private String tempInfo;
    private String tempHumidity;
    Singleton data = new Singleton();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this,Services.class));
        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
        items.add(new Item("Москва"));
        items.add(new Item("Санкт-Петербург"));
        items.add(new Item("Екатеринбург"));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Button alert1 = findViewById(R.id.sensor);
        alert1.setOnClickListener(clickAlertDialog1);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.Home:
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.search_history:
                        Intent intent1 = new Intent(MainActivity.this,SearchHistory.class);
                        startActivity(intent1);
                        return true;

                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }


        });
        textTemp = findViewById(R.id.text_temp);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            absolutyHumidity =  sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            isTemperatureSensorAvailable = true;


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Home:
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_history:
                Intent intent1 = new Intent(MainActivity.this,SearchHistory.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Add(View view) {

        EditText editText = findViewById(R.id.enterCity);
        items.add(new Item(editText.getText().toString()));
        data.Data(editText.getText().toString());
        editText.setText("");
        adapter.notifyItemInserted(items.size()-1);


    }

    public void WhatherInfo(View view) {
        Intent intent= new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
    }



    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

                        tempInfo = "Температура на улице: " + event.values[0] + " C";

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };
    SensorEventListener listener1 = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
                    tempHumidity = "Влажность: " + event.values[0] + " %";
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };


    @Override
    protected void onResume() {
        sensorManager.registerListener(listener,tempSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener1,absolutyHumidity,SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

            sensorManager.unregisterListener(listener);

    }

    private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<Item> items;

        private ItemAdapter(List<Item> items) {
            this.items = items;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int index) {
            return new RecyclerView.ViewHolder (
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false)
            )
            {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int index) {
            TextView name = holder.itemView.findViewById(R.id.name);
            name.setText(String.format("%s",this.items.get(index).getCity()));
            city = this.items.get(index).getCity();
            Button next = holder.itemView.findViewById(R.id.Info);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    city = items.get(index).getCity();
                    nextIntent();
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    city =  items.get(index).getCity();
                    nextIntent();

                }
            });
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
        public void nextIntent (){
            Intent intent= new Intent(MainActivity.this, Activity2.class);
            intent.putExtra(IDCITY,city);
            startActivity(intent);
        }
    }
    private View.OnClickListener clickAlertDialog1 = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            // Создаём билдер и передаём контекст приложения
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            // В билдере указываем заголовок окна (можно указывать как ресурс,
            // так и строку)
            builder.setTitle(R.string.exclamation)
                    // Указываем сообщение в окне (также есть вариант со
                    // строковым параметром)
                    .setMessage(tempInfo + "\n" + tempHumidity)
                    // Можно указать и пиктограмму
                    .setIcon(R.mipmap.ic_launcher_round)
                    // Из этого окна нельзя выйти кнопкой Back
                    .setCancelable(false)
                    // Устанавливаем кнопку (название кнопки также можно
                    // задавать строкой)
                    .setPositiveButton(R.string.button,
                            // Ставим слушатель, нажатие будем обрабатывать
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(MainActivity.this, "Кнопка нажата", Toast.LENGTH_SHORT).show();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            Toast.makeText(MainActivity.this, "Диалог открыт", Toast.LENGTH_SHORT).show();
        }
    };

}