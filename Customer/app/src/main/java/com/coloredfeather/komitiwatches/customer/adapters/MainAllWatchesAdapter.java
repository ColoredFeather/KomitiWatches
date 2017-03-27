package com.coloredfeather.komitiwatches.customer.adapters;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coloredfeather.komitiwatches.customer.models.ItemEntity;
import com.coloredfeather.komitiwatches.customer.network.response.GetAllItemsResponce;
import com.coloredfeather.komitiwatches.customer.ui.MainActivity;
import com.coloredfeather.komitiwatches.customer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by anandparmar on 21/01/17.
 */

public class MainAllWatchesAdapter extends RecyclerView.Adapter<MainAllWatchesViewHolder> {
    private List<GetAllItemsResponce.ItemsInResponse> list;
    private MainActivity activity;

    public MainAllWatchesAdapter(MainActivity activity, List<GetAllItemsResponce.ItemsInResponse> list) {
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
        final GetAllItemsResponce.ItemsInResponse item = list.get(position);

        ItemEntity itemEntity = ItemEntity.getByItemCode(item.getItemCode());

        holder.modelNumber.setText(item.getItemCode());
        holder.modelType.setText(item.getType());
        Picasso.with(activity).load(item.getPhoto()).placeholder(R.drawable.item_background).into(holder.watchImage);
        holder.quantity.setText(itemEntity == null ? "0" : String.valueOf(itemEntity.getQuantity()));

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(tempQuantity + 5));
                ItemEntity.addOrUpdateItem(item, tempQuantity + 5);
                activity.handleCartMenuItem();
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempQuantity = Integer.valueOf(holder.quantity.getText().toString());
                if(tempQuantity != 0) {
                    holder.quantity.setText(String.valueOf(tempQuantity - 5));
                    ItemEntity.addOrUpdateItem(item, tempQuantity - 5);
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

    public void updateList(List<GetAllItemsResponce.ItemsInResponse> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
