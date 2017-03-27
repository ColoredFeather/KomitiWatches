package com.coloredfeather.komitiwathces.merchant.network.responses;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by anandparmar on 07/03/17.
 */

public class GenericResponse implements Serializable{
    private ResponseStatus responseStatus;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public class ResponseStatus implements Serializable {

        private String message;
        private String status;
        private String code;

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }

        public int getCode() {
            try {
                return Integer.parseInt(code);
            } catch (Exception e) {
                return -1;
            }
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
