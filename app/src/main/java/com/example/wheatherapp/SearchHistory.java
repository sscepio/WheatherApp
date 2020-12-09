package com.example.wheatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.example.wheatherapp.Singleton.dataSearch;

public class SearchHistory extends AppCompatActivity {
    private final List<Item> items = new ArrayList<>();
   private final RecyclerView.Adapter adapter = new SearchHistory.ItemAdapter(this.items);
    private static final String IDCITY = "IDCITY";
    private static String city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        for (String i: dataSearch) {
            items.add(new Item(i));
            adapter.notifyItemInserted(items.size()-1);

        }

         RecyclerView recyclerView = findViewById(R.id.recycle1);
         recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        SearchHistory.ItemAdapter adapter = new SearchHistory.ItemAdapter(items);
        recyclerView.setAdapter(adapter);
       }

    Singleton data = new Singleton();
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
            Intent intent= new Intent(SearchHistory.this, Activity2.class);
            intent.putExtra(IDCITY,city);
            startActivity(intent);
        }

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
                Intent intent = new Intent(SearchHistory.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_history:
                Intent intent1 = new Intent(SearchHistory.this,SearchHistory.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}