package com.komitiwatches.customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.komitiwatches.customer.adapters.MainAllWatchesAdapter;
import com.komitiwatches.customer.models.WatchEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RippleView filterButton;
    private RippleView clearButton;
    private MainAllWatchesAdapter adapter;

    private static final int CALL_REQUEST_CODE = 524112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        recycleView = (RecyclerView) findViewById(R.id.main_recycle_view);
        filterButton = (RippleView) findViewById(R.id.filter_button);
        clearButton = (RippleView) findViewById(R.id.clear_button);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);
        adapter = new MainAllWatchesAdapter(entities);
        recycleView.setAdapter(adapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+919805343831"));
                    startActivity(intent);
                }
                return true;
            case R.id.menu_item_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Refused", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
