package com.komitiwatches.customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.komitiwatches.customer.MainActivity;
import com.komitiwatches.customer.R;
import com.komitiwatches.customer.models.InCartItems;
import com.komitiwatches.customer.models.WatchEntity;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by anandparmar on 21/01/17.
 */

public class MainAllWatchesAdapter extends RecyclerView.Adapter<MainAllWatchesViewHolder> {
    private RealmResults<InCartItems> list;
    private MainActivity activity;

    public MainAllWatchesAdapter(MainActivity activity, RealmResults<InCartItems> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public MainAllWatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);
        return new MainAllWatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainAllWatchesViewHolder holder, int position) {
        final InCartItems inCartItem = list.get(position);
        holder.modelNumber.setText(inCartItem.getWatchEntity().getModelNumber());
        holder.modelType.setText(inCartItem.getWatchEntity().getModelType());
        if(position==0) {
            holder.watchImage.setImageResource(R.drawable.a_1);
        } else if(position==1) {
            holder.watchImage.setImageResource(R.drawable.a_2);
        } else if(position==2) {
            holder.watchImage.setImageResource(R.drawable.a_3);
        } else {
            holder.watchImage.setImageResource(R.drawable.a_4);
        }
        holder.quantity.setText(String.valueOf(inCartItem.getQuantity()));

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(tempQuantity+5));
                InCartItems.addOrUpdateItem(inCartItem.getWatchEntity(), tempQuantity+5);
                activity.handleCartMenuItem();
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                if(tempQuantity != 0) {
                    holder.quantity.setText(String.valueOf(tempQuantity - 5));
                    InCartItems.addOrUpdateItem(inCartItem.getWatchEntity(), tempQuantity - 5);
                    activity.handleCartMenuItem();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ChangeCartNumber{
        void handleCartMenuItem();
    }

    public void updateList(RealmResults<InCartItems> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
