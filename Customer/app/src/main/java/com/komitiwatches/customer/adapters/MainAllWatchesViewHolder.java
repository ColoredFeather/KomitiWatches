package com.komitiwatches.customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.komitiwatches.customer.R;

/**
 * Created by anandparmar on 21/01/17.
 */

public class MainAllWatchesViewHolder extends RecyclerView.ViewHolder {
    TextView modelNumber, modelType, price, quantity;
    ImageView watchImage;
    RippleView addButton, minusButton;

    public MainAllWatchesViewHolder(View itemView) {
        super(itemView);
        modelNumber = (TextView) itemView.findViewById(R.id.model_number_tv);
        modelType = (TextView) itemView.findViewById(R.id.model_type_tv);
        price = (TextView) itemView.findViewById(R.id.price_tv);
        quantity = (TextView) itemView.findViewById(R.id.quantity_tv);
        watchImage = (ImageView) itemView.findViewById(R.id.image_layout);
        addButton = (RippleView) itemView.findViewById(R.id.add_button);
        minusButton = (RippleView) itemView.findViewById(R.id.minus_button);
    }
}
