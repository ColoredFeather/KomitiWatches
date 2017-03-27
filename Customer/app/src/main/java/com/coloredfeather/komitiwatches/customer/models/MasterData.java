package com.coloredfeather.komitiwatches.customer.models;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by anandparmar on 17/03/17.
 */

public class MasterData extends RealmObject {

    private String type;
    private String subType;
    private String code;
    private String value;
    private String pCode;

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

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public MasterData() {
    }

    public MasterData(String type, String subType, String code, String value, String pCode) {
        this.type = type;
        this.subType = subType;
        this.code = code;
        this.value = value;
        this.pCode = pCode;
    }

    public static void save(final MasterData masterData){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(masterData);
            }
        });
    }

    public static List<MasterData> getMasterDataByPCodeAndTypeAndSubType(String pCode, String type, String subType){
        return Realm.getDefaultInstance().where(MasterData.class).equalTo("pCode", pCode).equalTo("type", type).equalTo("subType", subType).findAll();
    }
}
