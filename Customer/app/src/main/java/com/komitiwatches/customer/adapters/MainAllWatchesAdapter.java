package com.komitiwatches.customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.komitiwatches.customer.R;
import com.komitiwatches.customer.models.WatchEntity;

import java.util.ArrayList;

/**
 * Created by anandparmar on 21/01/17.
 */

public class MainAllWatchesAdapter extends RecyclerView.Adapter<MainAllWatchesViewHolder> {
    private ArrayList<WatchEntity> list;

    public MainAllWatchesAdapter(ArrayList<WatchEntity> list) {
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
        WatchEntity watchEntity = list.get(position);
        holder.modelNumber.setText(watchEntity.getModelNumber());
        holder.modelType.setText(watchEntity.getModelType());
        holder.price.setText(watchEntity.getPrize());
        holder.quantity.setText("0");

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(tempQuantity+10));
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                if(tempQuantity != 0) {
                    holder.quantity.setText(String.valueOf(tempQuantity - 10));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
