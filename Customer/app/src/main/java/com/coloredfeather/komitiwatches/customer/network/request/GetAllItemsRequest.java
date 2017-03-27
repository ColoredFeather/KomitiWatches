package com.coloredfeather.komitiwatches.customer.network.request;

import java.io.Serializable;

import static android.R.attr.x;

/**
 * Created by anandparmar on 17/03/17.
 */

public class GetAllItemsRequest implements Serializable{

    private String pCode;
    private String type;
    private String subType;
    private String offset;
    private String limit;

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
