package com.coloredfeather.komitiwatches.customer.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coloredfeather.komitiwatches.customer.models.ItemEntity;
import com.coloredfeather.komitiwatches.customer.network.APIManager;
import com.coloredfeather.komitiwatches.customer.network.Utils.APIConstants;
import com.coloredfeather.komitiwatches.customer.network.Utils.TxnCompletionEvent;
import com.coloredfeather.komitiwatches.customer.network.common.GenericResponse;
import com.coloredfeather.komitiwatches.customer.network.request.GetAllItemsRequest;
import com.coloredfeather.komitiwatches.customer.network.response.GetAllItemsResponce;
import com.coloredfeather.komitiwatches.customer.utils.AppConstance;
import com.coloredfeather.komitiwatches.customer.R;
import com.coloredfeather.komitiwatches.customer.adapters.MainAllWatchesAdapter;
import com.coloredfeather.komitiwatches.customer.utils.AppUtils;
import com.coloredfeather.komitiwatches.customer.utils.Preferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.coloredfeather.komitiwatches.customer.utils.AppConstance.CALL_REQUEST_CODE;

public class MainActivity extends BaseActivity implements MainAllWatchesAdapter.ChangeCartNumber, APIManager.APICallback {

    @BindView(R.id.main_swipe_view)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.main_recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.no_item_text_view)
    TextView noItemTextView;

    private StaggeredGridLayoutManager layoutManager;
    private MainAllWatchesAdapter adapter;
    private MenuItem cartMenu;
    private int selectedTypePosition = 0;
    private int selectedSubTypePosition = 0;
    private String selectedType = null;
    private String selectedSubType = null;
    private boolean isLoadingMore = false;

    private CompositeSubscription subscriptions;
    private static final String ITEMS_LIMIT = "50";
    private List<GetAllItemsResponce.ItemsInResponse> allItemsList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        noItemTextView.setVisibility(View.GONE);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setHasFixedSize(false);
        adapter = new MainAllWatchesAdapter(this, allItemsList);
        recycleView.setAdapter(adapter);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totleItems = layoutManager.getItemCount();
                int[] lastVisibleItem = layoutManager.findLastVisibleItemPositions(null);

                int max = (lastVisibleItem[0] >= lastVisibleItem[1] ? lastVisibleItem[0] : lastVisibleItem[1]);
                if(!isLoadingMore && totleItems <= (max + 4)){
                    isLoadingMore = true;
                    createGetAllItemsRequest(selectedType, selectedSubType, allItemsList.size());
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                createGetAllItemsRequest(selectedType, selectedSubType, 0);
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        createGetAllItemsRequest(selectedType, selectedSubType, 0);
    }

    private void resetView() {
        allItemsList = new ArrayList<>();
        adapter.updateList(allItemsList);
    }

    private void addItems(List<GetAllItemsResponce.ItemsInResponse> items){
        if(items != null){
            allItemsList.addAll(items);
        }
    }

    private void setAdapterAndShow(){
        if(allItemsList == null || allItemsList.size() == 0){
            noItemTextView.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.GONE);
        } else {
            noItemTextView.setVisibility(View.GONE);
            recycleView.setVisibility(View.VISIBLE);
            adapter.updateList(allItemsList);
        }
    }

    public void createGetAllItemsRequest(String type, String subType, int offset){
        GetAllItemsRequest getAllItemsRequest = new GetAllItemsRequest();

        getAllItemsRequest.setpCode(Preferences.getPartnerCode(this));
        if(type != null && !type.equalsIgnoreCase("ALL")) {
            getAllItemsRequest.setType(type);
        }
        if(subType != null && !subType.equalsIgnoreCase("ALL")){
            getAllItemsRequest.setSubType(subType);
        }
        getAllItemsRequest.setOffset(String.valueOf(offset));
        getAllItemsRequest.setLimit(ITEMS_LIMIT);

        Subscription subscription = APIManager.getAPIManager(this).getAllItems(getAllItemsRequest, this);
        subscriptions = new CompositeSubscription();
        subscriptions.add(subscription);
    }

    @OnClick(R.id.filter_button)
    public void filterButtonOnClick() {
        Intent intent = new Intent(getApplication(), FilterActivity.class);
        intent.putExtra(AppConstance.EXTRA_SELECTED_TYPE_POSITION, selectedTypePosition);
        intent.putExtra(AppConstance.EXTRA_SELECTED_SUB_TYPE_POSITION, selectedSubTypePosition);
        startActivityForResult(intent, AppConstance.FILTER_REQUEST_CODE);
    }

    @OnClick(R.id.clear_button)
    public void openConformClearDialog() {
        List<ItemEntity> inCartItems = ItemEntity.getInCartItems();
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setCancelable(false);
        builder.setTitle("Conform Clear");
        if (inCartItems.size() != 0) {
            builder.setMessage("Do you want to clear your selected items from cart?");
            builder.setPositiveButton(AppConstance.CLEAR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ItemEntity.clearInCartItems();
                    handleCartMenuItem();
                    adapter.notifyDataSetChanged();
                    AppUtils.showMessage(MainActivity.this, "All items from cart are deleted.", null);
                    dialog.cancel();
                }
            });
        } else {
            builder.setMessage("No items in your cart.");
        }
        builder.setNegativeButton(AppConstance.DISMISS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        cartMenu = menu.findItem(R.id.menu_item_cart);
        handleCartMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_call:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+919426465187"));
                    startActivity(intent);
                }
                return true;
            case R.id.menu_item_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menu_logout:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                Preferences.logout(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Refused", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstance.FILTER_REQUEST_CODE && data != null) {
            selectedType = data.getStringExtra(AppConstance.EXTRA_MODEL_TYPE);
            selectedSubType = data.getStringExtra(AppConstance.EXTRA_MODEL_SUB_TYPE);
            selectedTypePosition = data.getIntExtra(AppConstance.EXTRA_SELECTED_TYPE_POSITION, 0);
            selectedSubTypePosition = data.getIntExtra(AppConstance.EXTRA_SELECTED_SUB_TYPE_POSITION, 0);

            swipeRefreshLayout.setRefreshing(true);
            resetView();
            createGetAllItemsRequest(selectedType, selectedSubType, 0);
        }
    }

    @Override
    public void onSuccess(TxnCompletionEvent event) {
        String typeString = event.getUrlType();
        if (!TextUtils.isEmpty(typeString)) {
            if (typeString.endsWith(APIManager.GET_ALL_ITEMS)) {
                GenericResponse genericResponse;
                hideProgress();
                switch (event.getTxnStatus()) {
                    case TxnCompletionEvent.SUCCESS:
                        GetAllItemsResponce response = new Gson().fromJson(event.getResponseString(), GetAllItemsResponce.class);
                        if(!isLoadingMore){
                            resetView();
                        }
                        if(response.getItemsList() != null && response.getItemsList().length != 0) {
                            addItems(Arrays.asList(response.getItemsList()));
                        }
                        setAdapterAndShow();
                        break;
                    case TxnCompletionEvent.FAILURE_WITH_NO_RESPONSE:
                    case TxnCompletionEvent.WARN:
                        AppUtils.showMessage(this, event.getFailureMsg(), APIConstants.RESULT_FAIL);
                        setAdapterAndShow();
                        break;
                    case TxnCompletionEvent.FAILURE_WITH_RESPONSE:
                        genericResponse = new Gson().fromJson(event.getResponseString(), GenericResponse.class);
                        AppUtils.showMessage(this, genericResponse.getResponseStatus().getMessage(), APIConstants.RESULT_FAIL);
                        setAdapterAndShow();
                        break;
                }
            }
        }
        onStopSubscriptions();
    }

    @Override
    public void onError(Throwable throwable) {
        onStopSubscriptions();
    }

    private void onStopSubscriptions() {
        subscriptions.unsubscribe();
        hideProgress();

        isLoadingMore = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    public void handleCartMenuItem() {
        long count = ItemEntity.countInCartItems();
        if (cartMenu != null) {
            RelativeLayout cartWithNumberLayout = (RelativeLayout) cartMenu.getActionView();

            ImageView cartImage = (ImageView) cartWithNumberLayout.findViewById(R.id.cart_image_view);
            TextView itemsInCartNumber = (TextView) cartWithNumberLayout.findViewById(R.id.item_in_cart_count);
            cartImage.setVisibility(View.VISIBLE);

            if (count > 0) {
                itemsInCartNumber.setVisibility(View.VISIBLE);
                if (count < 10) {
                    itemsInCartNumber.setText(" " + count + " ");
                } else if (count > 10 && count < 100) {
                    itemsInCartNumber.setText(" " + count);
                } else {
                    itemsInCartNumber.setText("99+");
                }
            } else {
                itemsInCartNumber.setVisibility(View.GONE);
            }

            cartWithNumberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                }
            });
        }
    }
}
