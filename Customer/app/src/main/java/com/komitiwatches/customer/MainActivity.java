package com.komitiwatches.customer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.komitiwatches.customer.adapters.MainAllWatchesAdapter;
import com.komitiwatches.customer.models.InCartItems;
import com.komitiwatches.customer.models.WatchEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;

import static android.R.attr.handle;
import static android.R.attr.mode;
import static com.komitiwatches.customer.AppConstance.CALL_REQUEST_CODE;
import static com.komitiwatches.customer.AppConstance.FILTER_REQUEST_CODE;
import static com.komitiwatches.customer.models.InCartItems.getFilteredItems;

public class MainActivity extends AppCompatActivity implements MainAllWatchesAdapter.ChangeCartNumber{

    private RecyclerView recycleView;
    private RippleView filterButton;
    private RippleView clearButton;
    private TextView noItemTextView;
    private MainAllWatchesAdapter adapter;

    private MenuItem cartMenu;

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
        noItemTextView = (TextView) findViewById(R.id.no_item_text_view);
        noItemTextView.setVisibility(View.GONE);

        WatchEntity.createDefaultEntries();
        RealmResults<InCartItems> entities = InCartItems.getAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setNestedScrollingEnabled(false);
        adapter = new MainAllWatchesAdapter(this, entities);
        recycleView.setAdapter(adapter);

        if(entities.size() ==0 ){
            noItemTextView.setVisibility(View.VISIBLE);
        }

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), FilterActivity.class);
                startActivityForResult(intent, AppConstance.FILTER_REQUEST_CODE);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConformClearDialog();
            }
        });
    }

    private void openConformClearDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setCancelable(false);
        builder.setTitle("Conform Clear");
        builder.setMessage("Are sure to clear your selected items?");
        builder.setNegativeButton(AppConstance.CLEAR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InCartItems.clearInCartItems();
                handleCartMenuItem();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "All items from cart are deleted.", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
        builder.setPositiveButton(AppConstance.DISMISS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        cartMenu = menu.findItem(R.id.menu_item_cart);
        handleCartMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, AppConstance.CALL_REQUEST_CODE);
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+919426465187"));
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
    protected void onResume() {
        super.onResume();
        initViews();
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
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppConstance.FILTER_REQUEST_CODE && data != null){
            String modelType = data.getStringExtra("modelType");
            RealmResults<InCartItems> results = null;
            if(!"All".equalsIgnoreCase(modelType)){
                results = InCartItems.getFilteredItems(modelType);
                adapter.updateList(results);
            } else {
                results = InCartItems.getAll();
                adapter.updateList(results);
            }

            if(results.size() == 0){
                noItemTextView.setVisibility(View.VISIBLE);
            } else {
                noItemTextView.setVisibility(View.GONE);
            }
        }
    }

    public void handleCartMenuItem() {
        long count = InCartItems.countInCartItems();
        if (cartMenu != null){
            RelativeLayout cartWithNumberLayout = (RelativeLayout) cartMenu.getActionView();

            ImageView cartImage = (ImageView) cartWithNumberLayout.findViewById(R.id.cart_image_view);
            TextView itemsInCartNumber = (TextView) cartWithNumberLayout.findViewById(R.id.item_in_cart_count);
            cartImage.setVisibility(View.VISIBLE);

            if(count > 0){
                itemsInCartNumber.setVisibility(View.VISIBLE);
                if (count < 10) {
                    itemsInCartNumber.setText(" " + count + " ");
                } else if (count > 10 && count < 100) {
                    itemsInCartNumber.setText(" " + count);
                } else {
                    itemsInCartNumber.setText("99+");
                }
            } else {
                itemsInCartNumber.setVisibility(View.GONE);
            }

            cartWithNumberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                }
            });
        }
    }
}
