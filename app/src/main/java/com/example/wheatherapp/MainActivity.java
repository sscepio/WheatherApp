package com.example.wheatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private final List<Item> items = new ArrayList<>();
   private final RecyclerView.Adapter adapter = new ItemAdapter(this.items);
    private static final String IDCITY = "IDCITY";
    private static String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
        Button home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent= new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    };

    public void Add(View view) {
        EditText editText = findViewById(R.id.enterCity);
        items.add(new Item(editText.getText().toString()));
        editText.setText("");
        adapter.notifyItemInserted(items.size()-1);

    }

    public void WhatherInfo(View view) {
        Intent intent= new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
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
}