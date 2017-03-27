package com.coloredfeather.komitiwatches.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.coloredfeather.komitiwatches.customer.R;
import com.coloredfeather.komitiwatches.customer.adapters.CartItemsAdapter;
import com.coloredfeather.komitiwatches.customer.models.ItemEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    @BindView(R.id.cart_items_recycle_view) RecyclerView cartItems;
    @BindView(R.id.place_your_order_button) RippleView placeOrderButton;
    @BindView(R.id.no_item_text_view) TextView noItemTextView;

    private CartItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (ItemEntity.countInCartItems() != 0) {
            cartItems.setVisibility(View.VISIBLE);
            placeOrderButton.setVisibility(View.VISIBLE);
            noItemTextView.setVisibility(View.GONE);
        } else {
            cartItems.setVisibility(View.GONE);
            placeOrderButton.setVisibility(View.GONE);
            noItemTextView.setVisibility(View.VISIBLE);
        }

        List<ItemEntity> inCartItems = ItemEntity.getInCartItems();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cartItems.setLayoutManager(linearLayoutManager);
        adapter = new CartItemsAdapter(this, inCartItems);
        cartItems.setAdapter(adapter);
    }

    @OnClick(R.id.place_your_order_button)
    public void placeOrderButtonOnClick(){
        String order = ItemEntity.getMsgForSelectedItem();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, order);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
