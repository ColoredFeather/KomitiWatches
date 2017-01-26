package com.komitiwatches.customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.komitiwatches.customer.R;

/**
 * Created by anandparmar on 26/01/17.
 */

public class CartItemsViewHolder extends RecyclerView.ViewHolder {
    ImageView itemImage;
    TextView modelNumber;
    EditText quantity;

    public CartItemsViewHolder(View itemView) {
        super(itemView);
        itemImage = (ImageView) itemView.findViewById(R.id.rv_item_image_view);
        modelNumber = (TextView) itemView.findViewById(R.id.rv_model_number);
        quantity = (EditText) itemView.findViewById(R.id.rv_quantity);
    }
}
