package com.coloredfeather.komitiwatches.customer.network.response;

import com.coloredfeather.komitiwatches.customer.network.common.GenericResponse;

/**
 * Created by anandparmar on 18/03/17.
 */

public class GetMasterDataResponse extends GenericResponse {

    private MasterData[] masterDataList;

    public MasterData[] getMasterDataList() {
        return masterDataList;
    }

    public void setMasterDataList(MasterData[] masterDataList) {
        this.masterDataList = masterDataList;
    }

    public class MasterData {
        private String pCode;
        private String opCode;
        private String type;
        private String subType;
        private String code;
        private String value;

        public String getpCode() {
            return pCode;
        }

        public void setpCode(String pCode) {
            this.pCode = pCode;
        }

        public String getOpCode() {
            return opCode;
        }

        public void setOpCode(String opCode) {
            this.opCode = opCode;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
