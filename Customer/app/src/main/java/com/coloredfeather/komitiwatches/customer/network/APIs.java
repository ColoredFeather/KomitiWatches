package com.coloredfeather.komitiwatches.customer.network;

import com.coloredfeather.komitiwatches.customer.network.request.GetAllItemsRequest;
import com.coloredfeather.komitiwatches.customer.network.request.GetMasterDataRequest;
import com.coloredfeather.komitiwatches.customer.network.response.GetAllItemsResponce;
import com.coloredfeather.komitiwatches.customer.network.response.GetMasterDataResponse;
import com.coloredfeather.komitiwatches.customer.network.response.LoginRespnce;
import com.coloredfeather.komitiwatches.customer.network.response.RegistrationResponce;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by anandparmar on 20/02/17.
 */

public interface APIs {

    //api endpoints
    String USER_REGISTRATION = "userRegistration";
    String USER_LOGIN = "userLogin";
    String GET_ALL_ITEMS = "getAllItems";

    //query strings
    String MERCHANT_CODE = "merchantCode";
    String MOBILE_NUMBER = "mobileNumber";
    String CUSTOMER_NAME = "customerName";
    String FUNC_SUBTYPE = "funcSubType";
    String ENTERED_OTP = "enteredOTP";


    String ITEM_LIMIT = "limit";
    String ITEM_OFFSET = "offset";
    String MODEL_TYPE = "modelType";
    String MODEL_SUBTYPE = "modelSubType";


    /*@GET(USER_LOGIN)
    Call<LoginRespnce> userLoginAPI(@Query(MERCHANT_CODE) String merchantCode,
                                    @Query(MOBILE_NUMBER) String mobileNumber,
                                    @Query(CUSTOMER_NAME) String customerName,
                                    @Query(FUNC_SUBTYPE) String funcSubType,
                                    @Query(ENTERED_OTP) String enteredOTP);

    @GET(USER_REGISTRATION)
    Call<RegistrationResponce> userRegistrationAPI(@Query(MERCHANT_CODE) String merchantCode,
                                                   @Query(MOBILE_NUMBER) String mobileNumber,
                                                   @Query(FUNC_SUBTYPE) String funcSubType,
                                                   @Query(ENTERED_OTP) String enteredOTP);

    @GET(GET_ALL_ITEMS)
    Call<GetAllItemsResponce> getAllItemsAPI(@Query(MERCHANT_CODE) String merchantCode,
                                             @Query(ITEM_LIMIT) String limit,
                                             @Query(ITEM_OFFSET) String offset,
                                             @Query(MODEL_TYPE) String modelType,
                                             @Query(MODEL_SUBTYPE) String modelSubType);*/

    @POST(APIManager.GET_ALL_ITEMS)
    Observable<Response<GetAllItemsResponce>> getAllItems(@Body GetAllItemsRequest getAllItemsRequest);

    @POST(APIManager.GET_MASTER_DATA)
    Observable<Response<GetMasterDataResponse>> getMasterData(@Body GetMasterDataRequest getMasterDataRequest);

}
