package com.coloredfeather.komitiwatches.customer.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coloredfeather.komitiwatches.customer.models.ItemEntity;
import com.coloredfeather.komitiwatches.customer.ui.CartActivity;
import com.coloredfeather.komitiwatches.customer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.coloredfeather.komitiwatches.customer.models.ItemEntity.addOrUpdateItem;

/**
 * Created by anandparmar on 26/01/17.
 */

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsViewHolder> {
    private List<ItemEntity> cartItems;
    private CartActivity activity;

    public CartItemsAdapter(CartActivity activity, List<ItemEntity> cartItems) {
        this.cartItems = cartItems;
        this.activity = activity;
    }

    @Override
    public CartItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_recycle_view_item, parent, false);
        return new CartItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemsViewHolder holder, int position) {
        final ItemEntity inCartItem = cartItems.get(position);
        Picasso.with(activity).load(inCartItem.getPhoto()).placeholder(R.drawable.item_background).into(holder.itemImage);
        holder.modelNumber.setText(inCartItem.getItemCode());
        holder.quantity.setText(String.valueOf(inCartItem.getQuantity()));

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String quantity = holder.quantity.getText().toString();
                if("".equals(quantity)) {
                    ItemEntity.updateItemQuantity(inCartItem, 0);
                } else {
                    ItemEntity.updateItemQuantity(inCartItem, Integer.valueOf(quantity));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
