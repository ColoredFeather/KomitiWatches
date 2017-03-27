package com.coloredfeather.komitiwatches.customer.network.request;

import java.io.Serializable;

/**
 * Created by anandparmar on 18/03/17.
 */

public class GetMasterDataRequest implements Serializable {

    private String pCode;
    private String type;
    private String subType;
    private String sinceTime;

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

    public String getSinceTime() {
        return sinceTime;
    }

    public void setSinceTime(String sinceTime) {
        this.sinceTime = sinceTime;
    }
}
