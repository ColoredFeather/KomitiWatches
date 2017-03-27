package com.coloredfeather.komitiwatches.customer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.andexert.library.RippleView;
import com.coloredfeather.komitiwatches.customer.utils.AppConstance;
import com.coloredfeather.komitiwatches.customer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.mode;

public class FilterActivity extends BaseActivity {

    @BindView(R.id.spinner_model_type) Spinner modelTypeSpinner;
    @BindView(R.id.spinner_model_sub_type) Spinner modelSubTypeSpinner;

    private int selectedType;
    private int selectedSubType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        selectedType = getIntent().getIntExtra(AppConstance.EXTRA_SELECTED_TYPE_POSITION, 0);
        selectedSubType = getIntent().getIntExtra(AppConstance.EXTRA_SELECTED_SUB_TYPE_POSITION, 0);

        modelTypeSpinner.setSelection(selectedType);
        modelSubTypeSpinner.setSelection(selectedSubType);
    }

    @OnClick(R.id.filter_button)
    public void filterButtonOnClick() {
        Intent intent = new Intent();
        intent.putExtra(AppConstance.EXTRA_MODEL_TYPE, modelTypeSpinner.getSelectedItem().toString());
        intent.putExtra(AppConstance.EXTRA_SELECTED_TYPE_POSITION, modelTypeSpinner.getSelectedItemPosition());
        intent.putExtra(AppConstance.EXTRA_MODEL_SUB_TYPE, modelSubTypeSpinner.getSelectedItem().toString());
        intent.putExtra(AppConstance.EXTRA_SELECTED_SUB_TYPE_POSITION, modelSubTypeSpinner.getSelectedItemPosition());
        setResult(AppConstance.FILTER_REQUEST_CODE, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
