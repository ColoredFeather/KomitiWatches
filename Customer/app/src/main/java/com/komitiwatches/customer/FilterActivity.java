package com.komitiwatches.customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.andexert.library.RippleView;

import static com.komitiwatches.customer.AppConstance.FILTER_REQUEST_CODE;

public class FilterActivity extends AppCompatActivity {

    private Spinner modelTypeSpinner;
    private RippleView filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews() {
        modelTypeSpinner = (Spinner) findViewById(R.id.spinner_model_type);
        filterButton = (RippleView) findViewById(R.id.filter_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("modelType", modelTypeSpinner.getSelectedItem().toString());
                setResult(AppConstance.FILTER_REQUEST_CODE, intent);
                finish();
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
