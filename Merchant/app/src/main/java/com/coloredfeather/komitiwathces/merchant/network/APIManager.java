package com.coloredfeather.komitiwathces.merchant.network;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.coloredfeather.komitiwathces.merchant.network.Utils.APIConstants;
import com.coloredfeather.komitiwathces.merchant.network.Utils.TxnCompletionEvent;
import com.coloredfeather.komitiwathces.merchant.network.requests.AddNewItemRequest;
import com.coloredfeather.komitiwathces.merchant.network.responses.AddNewItemResponse;
import com.coloredfeather.komitiwathces.merchant.network.responses.GenericResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by anandparmar on 07/03/17.
 */

public class APIManager {

    public static final String ADD_NEW_ITEM = "addNewItem";

    private APIs api;
    private static APIManager apiManager;
    private APICallback apiCallback;

    public static final int CONNECTION_TIMEOUT_IN_MINS = 25;
    public static final int READ_TIMEOUT_IN_MINS = 25;
    public static final int WRITE_TIMEOUT_IN_MINS = 25;
    private static boolean enableLogs = true;

    public static APIManager getAPIManager(Context context){
        if(apiManager == null){
            apiManager = new APIManager(context);
        }
        return apiManager;
    }

    private APIManager(Context context){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();
        logging2.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .writeTimeout(WRITE_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false);

        if (enableLogs) {
            okHttpClientBuilder.addInterceptor(logging);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.43.219:8080/api/1.0/")
                .client(okHttpClientBuilder.build())
                .build();

        api = retrofit.create(APIs.class);
    }

    public Subscription addNewItem(AddNewItemRequest addNewItemRequest, APICallback callback){
        apiCallback = callback;
        return api.addNewItem(addNewItemRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNewSubscriber());
    }

    public Subscriber getNewSubscriber() {
        return new Subscriber<Response<GenericResponse>>(){

            @Override
            public void onCompleted() {
                this.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                apiCallback.onError(e);
            }

            @Override
            public void onNext(Response<GenericResponse> response) {
                Request request = response.raw().request();
                HttpUrl httpUrl = request.url();
                String url = httpUrl.toString();
                Headers headers = response.headers();
                String requestString = APIManager.bodyToString(request.body());
                TxnCompletionEvent transactionCompletionEvent = APIManager.getTransactionCompletionEvent(response.body().toString(), requestString, url, headers);
                apiCallback.onSuccess(transactionCompletionEvent);

            }
        };
    }

    public interface APICallback {
        void onSuccess(TxnCompletionEvent event);
        void onError(Throwable throwable);
    }

    public static  TxnCompletionEvent getTransactionCompletionEvent(String responseString, String requestString, String url, okhttp3.Headers headers) {
        TxnCompletionEvent txnCompletionEvent = new TxnCompletionEvent();
        txnCompletionEvent.setUrl(url != null ? url : null);
        txnCompletionEvent.setRequest(requestString);

        GenericResponse genericResponse = null;
        if (!TextUtils.isEmpty(responseString)) {
            genericResponse = new Gson().fromJson(responseString, GenericResponse.class);
        }

        if (genericResponse == null) {
            txnCompletionEvent.setTxnStatus(TxnCompletionEvent.FAILURE_WITH_NO_RESPONSE);
            txnCompletionEvent.setFailureMsg("No response received from server");
        } else {
            if (genericResponse.getResponseStatus().getStatus().equalsIgnoreCase(APIConstants.RESULT_SUCCESS)) {
                txnCompletionEvent.setTxnStatus(TxnCompletionEvent.SUCCESS);
                txnCompletionEvent.setResponseString(responseString);

            } else if (genericResponse.getResponseStatus().getStatus().equalsIgnoreCase(APIConstants.RESULT_WARN)) {
                txnCompletionEvent.setTxnStatus(TxnCompletionEvent.WARN);
                txnCompletionEvent.setResponseString(responseString);
            } else {
                txnCompletionEvent.setFailureMsg(genericResponse.getResponseStatus().getMessage());
                txnCompletionEvent.setTxnStatus(TxnCompletionEvent.FAILURE_WITH_RESPONSE);
                txnCompletionEvent.setResponseString(responseString);
            }
        }
        if (headers != null) {
            Set<String> names = headers.names();
            Bundle bundle = new Bundle();
            for (String key : names) {
                bundle.putString(key, headers.get(key));
            }
            txnCompletionEvent.setBundle(bundle);
        }
        return txnCompletionEvent;
    }

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) copy.writeTo(buffer);
            else return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
