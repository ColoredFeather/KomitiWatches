package com.coloredfeather.komitiwathces.merchant.network;

import com.coloredfeather.komitiwathces.merchant.network.requests.AddNewItemRequest;
import com.coloredfeather.komitiwathces.merchant.network.responses.AddNewItemResponse;


import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by anandparmar on 07/03/17.
 */

public interface APIs {

    @POST(APIManager.ADD_NEW_ITEM)
    Observable<Response<AddNewItemResponse>> addNewItem(@Body AddNewItemRequest addNewItemRequest);
}
