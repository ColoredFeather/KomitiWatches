package com.coloredfeather.komitiwathces.merchant.network.Utils;

import android.os.Bundle;

/**
 * Created by anandparmar on 08/03/17.
 */

public class TxnCompletionEvent implements Cloneable {

    public static final int SUCCESS = 1;
    public static final int FAILURE_WITH_RESPONSE = 2;
    public static final int FAILURE_WITH_NO_RESPONSE = 3;
    public static final int WARN = 4;

    private String successMsg = "Success";
    private String failureMsg = "Operation Failed";
    private String request;
    private String response;
    private int code;
    private int txnStatus;
    private String url;
    private Bundle bundle;
    private String urlType;
    private boolean isNetworkError;
    private String stanValueOnTimeOutOccurred;
    private String eventType = "TxnEvent";

    /**
     * @return the wasEventASuccess
     */
    public TxnCompletionEvent setTxnStatus(int txnStatus) {
        this.txnStatus = txnStatus;
        return this;
    }

    public int getTxnStatus() {
        return txnStatus;
    }

    /**
     * @return the successMsg
     */
    public String getSuccessMsg() {
        return successMsg;
    }

    /**
     * @param successMsg the successMsg to set
     */
    public TxnCompletionEvent setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
        return this;
    }

    /**
     * @return the failureMsg
     */
    public String getFailureMsg() {
        return failureMsg;
    }

    /**
     * @param failureMsg the failureMsg to set
     */
    public TxnCompletionEvent setFailureMsg(String failureMsg) {
        this.failureMsg = failureMsg;
        return this;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public TxnCompletionEvent setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * @return the response
     */
    public String getResponseString() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public TxnCompletionEvent setResponseString(String response) {
        this.response = response;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public TxnCompletionEvent setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public String getRequest() {
        return request;
    }

    public TxnCompletionEvent setRequest(String request) {
        this.request = request;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public TxnCompletionEvent setUrl(String url) {
        if (url != null) {
            this.url = url;
            int index = url.lastIndexOf("/");
            String type = url.substring(index + 1);
            setUrlType(type);
        }
        return this;
    }

    public String getUrlType() {
        return urlType;
    }

    public TxnCompletionEvent setUrlType(String urlType) {
        this.urlType = urlType;
        return this;
    }

    public TxnCompletionEvent setNetworkError(boolean networkError) {
        isNetworkError = networkError;
        return this;
    }

    public boolean isNetworkError() {
        return isNetworkError;

    }

    public String getStanValueOnTimeOutOccurred() {
        return stanValueOnTimeOutOccurred;
    }

    public TxnCompletionEvent setStanValueOnTimeOutOccurred(String stanValueOnTimeOutOccurred) {
        this.stanValueOnTimeOutOccurred = stanValueOnTimeOutOccurred;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public TxnCompletionEvent setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    @Override
    protected TxnCompletionEvent clone() throws CloneNotSupportedException {
        TxnCompletionEvent clone = new TxnCompletionEvent();
        clone.bundle = getBundle();
        clone.successMsg = successMsg;
        clone.failureMsg = failureMsg;
        clone.request = request;
        clone.response = response;
        clone.txnStatus = txnStatus;
        clone.url = url;
        clone.urlType = urlType;
        return clone;
    }

    public String getTransactionStatusVerbose() {
        String status = APIConstants.RESULT_FAIL;
        if (txnStatus == 1) {
            status = APIConstants.RESULT_SUCCESS;
        } else if (txnStatus == 4) {
            status = APIConstants.RESULT_WARN;
        }
        return status;
    }

}
