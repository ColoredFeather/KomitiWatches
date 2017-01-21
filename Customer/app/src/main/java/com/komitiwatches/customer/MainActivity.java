package com.komitiwatches.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.komitiwatches.customer.adapters.MainAllWatchesAdapter;
import com.komitiwatches.customer.models.WatchEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private Button filterButton;
    private Button listButton;
    private Button clearButton;
    private MainAllWatchesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        recycleView = (RecyclerView) findViewById(R.id.main_recycle_view);
        filterButton = (Button) findViewById(R.id.filter_button);
        listButton = (Button) findViewById(R.id.list_button);
        clearButton = (Button) findViewById(R.id.clear_button);

        WatchEntity watch1 = new WatchEntity();
        watch1.setModelNumber("#KW001");
        watch1.setModelType("Sports-Gents");
        watch1.setPrize("545");
        watch1.setAvailable(true);
        watch1.setUrl("http://abc.com");

        WatchEntity watch2 = new WatchEntity();
        watch2.setModelNumber("#KW002");
        watch2.setModelType("Sports-Ladies");
        watch2.setPrize("300");
        watch2.setAvailable(true);
        watch2.setUrl("http://efg.com");

        ArrayList<WatchEntity> entities = new ArrayList<WatchEntity>();
        entities.add(watch1);
        entities.add(watch2);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);
        adapter = new MainAllWatchesAdapter(entities);
        recycleView.setAdapter(adapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
