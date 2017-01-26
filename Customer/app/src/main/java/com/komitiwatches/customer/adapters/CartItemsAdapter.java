package com.komitiwatches.customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.komitiwatches.customer.CartActivity;
import com.komitiwatches.customer.R;
import com.komitiwatches.customer.models.InCartItems;

import io.realm.RealmResults;

/**
 * Created by anandparmar on 26/01/17.
 */

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsViewHolder> {
    private RealmResults<InCartItems> cartItems;
    private CartActivity activity;

    public CartItemsAdapter(CartActivity activity, RealmResults<InCartItems> cartItems) {
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
        final InCartItems inCartItem = cartItems.get(position);
        if(position==0) {
            holder.itemImage.setImageResource(R.drawable.a_1);
        } else if(position==1) {
            holder.itemImage.setImageResource(R.drawable.a_2);
        } else if(position==2) {
            holder.itemImage.setImageResource(R.drawable.a_3);
        } else {
            holder.itemImage.setImageResource(R.drawable.a_4);
        }
        holder.modelNumber.setText(inCartItem.getWatchEntity().getModelNumber());
        holder.price.setText(activity.getResources().getString(R.string.amount, inCartItem.getWatchEntity().getPrize()));
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
                    InCartItems.addOrUpdateItem(inCartItem.getWatchEntity(), 0);
                } else {
                    InCartItems.addOrUpdateItem(inCartItem.getWatchEntity(), Integer.valueOf(quantity));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
