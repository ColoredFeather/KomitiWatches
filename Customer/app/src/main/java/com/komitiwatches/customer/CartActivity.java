package com.komitiwatches.customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.komitiwatches.customer.adapters.CartItemsAdapter;
import com.komitiwatches.customer.adapters.MainAllWatchesAdapter;
import com.komitiwatches.customer.models.InCartItems;

import org.w3c.dom.Text;

import io.realm.RealmResults;

import static android.os.Build.VERSION_CODES.M;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartItems;
    private RippleView placeOrderButton;
    private TextView noItemTextView;

    private CartItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        cartItems = (RecyclerView) findViewById(R.id.cart_items_recycle_view);
        placeOrderButton = (RippleView) findViewById(R.id.place_your_order_button);
        noItemTextView = (TextView) findViewById(R.id.no_item_text_view);
        if(InCartItems.countInCartItems()!=0) {
            cartItems.setVisibility(View.VISIBLE);
            placeOrderButton.setVisibility(View.VISIBLE);
            noItemTextView.setVisibility(View.GONE);
        } else {
            cartItems.setVisibility(View.GONE);
            placeOrderButton.setVisibility(View.GONE);
            noItemTextView.setVisibility(View.VISIBLE);
        }

        RealmResults<InCartItems> inCartItemses = InCartItems.getInCartItems();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartItems.setLayoutManager(linearLayoutManager);
        adapter = new CartItemsAdapter(this, inCartItemses);
        cartItems.setAdapter(adapter);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String order = InCartItems.getMsgForSelectedItem();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.putExtra(Intent.EXTRA_TEXT, order);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
