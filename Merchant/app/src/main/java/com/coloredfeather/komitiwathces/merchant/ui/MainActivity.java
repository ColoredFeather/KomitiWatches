package com.coloredfeather.komitiwathces.merchant.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.coloredfeather.komitiwathces.merchant.R;
import com.coloredfeather.komitiwathces.merchant.Utils.AppUtils;
import com.coloredfeather.komitiwathces.merchant.Utils.Preferences;
import com.coloredfeather.komitiwathces.merchant.network.APIManager;
import com.coloredfeather.komitiwathces.merchant.network.Utils.APIConstants;
import com.coloredfeather.komitiwathces.merchant.network.Utils.TxnCompletionEvent;
import com.coloredfeather.komitiwathces.merchant.network.requests.AddNewItemRequest;
import com.coloredfeather.komitiwathces.merchant.network.responses.GenericResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.coloredfeather.komitiwathces.merchant.R.id.take_picture_text_view;

public class MainActivity extends BaseActivity implements APIManager.APICallback {

    @BindView(R.id.model_number_edit_view)
    EditText modelNumber;
    @BindView(R.id.model_number_error)
    TextView modelNumberError;

    @BindView(R.id.model_type_edit_view)
    Spinner modelType;
    @BindView(R.id.model_type_error)
    TextView modelRTypeError;

    @BindView(R.id.model_subtype_edit_view)
    Spinner modelSubType;
    @BindView(R.id.model_subtype_error)
    TextView modelSubTypeError;

    @BindView(R.id.amount_edit_view)
    EditText amount;
    @BindView(R.id.amount_error)
    TextView amountError;

    @BindView(R.id.selected_image_view)
    ImageView selectedImageView;
    @BindView(R.id.take_piucture_layout)
    LinearLayout takePictureLayout;
    @BindView(take_picture_text_view)
    TextView takePictureTextView;
    @BindView(R.id.take_picture_image_view)
    ImageView takePictureImageView;

    private String photoAsString;
    private CompositeSubscription subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.take_piucture_layout, R.id.selected_image_view})
    protected void takePictureOnClick() {
        EasyImage.openChooserWithGallery(this, "take picture of the product", 0);
    }

    @OnClick(R.id.submit_button)
    protected void submitButtonOnClick() {
        modelNumberError.setVisibility(View.INVISIBLE);
        amountError.setVisibility(View.INVISIBLE);
        takePictureTextView.setText("click here to take a photo*");
        takePictureTextView.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        if (modelNumber.getText().length() != 0) {
            if (amount.getText().length() != 0) {
                if (selectedImageView.getVisibility() == View.VISIBLE) {
                    createAddNewItemRequest();
                } else {
                    takePictureTextView.setText("Please take a picture of your product*");
                    takePictureTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
            } else {
                amountError.setVisibility(View.VISIBLE);
            }
        } else {
            modelNumberError.setVisibility(View.VISIBLE);
        }
    }

    private void createAddNewItemRequest() {
        showProgress("adding new product...");

        AddNewItemRequest request = new AddNewItemRequest();
        request.setpCode(Preferences.getPartnerCode(this));
        request.setItemCode(modelNumber.getText().toString());
        request.setType(modelType.getSelectedItem().toString());
        request.setSubType(modelSubType.getSelectedItem().toString());
        request.setPrice(amount.getText().toString());
        request.setPhoto(photoAsString);
        request.setAddedBy(Preferences.getMobileNumber(this));

        Subscription subscription = APIManager.getAPIManager(this).addNewItem(request, this);
        subscriptions = new CompositeSubscription();
        subscriptions.add(subscription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("MainActivity", e.getMessage());
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) {
                        photoFile.delete();
                    }
                }
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFile);
            }
        });
    }

    private void onPhotosReturned(File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        photoAsString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        Picasso.with(this).load(imageFile).into(selectedImageView);
        selectedImageView.setVisibility(View.VISIBLE);
        takePictureLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(TxnCompletionEvent event) {
        String typeString = event.getUrlType();
        if (!TextUtils.isEmpty(typeString)) {
            if (typeString.endsWith(APIManager.ADD_NEW_ITEM)) {
                GenericResponse genericResponse;
                hideProgress();
                switch (event.getTxnStatus()) {
                    case TxnCompletionEvent.SUCCESS:
                        GenericResponse response = new Gson().fromJson(event.getResponseString(), GenericResponse.class);
                        AppUtils.showMessage(this, response.getResponseStatus().getMessage(), APIConstants.RESULT_SUCCESS);
                        resetActivity();
                        break;
                    case TxnCompletionEvent.FAILURE_WITH_NO_RESPONSE:
                    case TxnCompletionEvent.WARN:
                        AppUtils.showMessage(this, event.getFailureMsg(), APIConstants.RESULT_FAIL);
                        break;
                    case TxnCompletionEvent.FAILURE_WITH_RESPONSE:
                        genericResponse = new Gson().fromJson(event.getResponseString(), GenericResponse.class);
                        AppUtils.showMessage(this, genericResponse.getResponseStatus().getMessage(), APIConstants.RESULT_FAIL);
                        break;
                }
            }
        }
        onStopSubscriptions();
    }

    private void resetActivity() {
        modelNumber.setText("");
        amount.setText("");
        selectedImageView.setVisibility(View.GONE);
        takePictureLayout.setVisibility(View.VISIBLE);
        modelNumber.requestFocus();
    }

    @Override
    public void onError(Throwable throwable) {
        AppUtils.showMessage(this, throwable.getMessage(), APIConstants.RESULT_FAIL);
        onStopSubscriptions();
    }

    private void onStopSubscriptions() {
        subscriptions.unsubscribe();
        hideProgress();
    }
}
