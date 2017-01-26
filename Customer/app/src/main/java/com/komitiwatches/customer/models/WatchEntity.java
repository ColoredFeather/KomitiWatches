package com.komitiwatches.customer.models;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anandparmar on 21/01/17.
 */

public class WatchEntity extends RealmObject{

    @PrimaryKey
    private String modelNumber;

    private String modelType;
    private Boolean available;
    private String url;

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void createDefaultEntries(){
        Realm realm = Realm.getDefaultInstance();
        if(realm.where(WatchEntity.class).count() != 4) {
            realm.beginTransaction();
            WatchEntity watch1 = realm.createObject(WatchEntity.class, "#KW001");
            watch1.setModelType("Sports-Gents");
            watch1.setAvailable(true);
            watch1.setUrl("http://abc.com");

            InCartItems inCartItem1 = new InCartItems(watch1, 0);
            realm.copyToRealm(inCartItem1);

            WatchEntity watch2 = realm.createObject(WatchEntity.class, "#KW002");
            watch2.setModelType("Sports-Ladies");
            watch2.setAvailable(true);
            watch2.setUrl("http://efg.com");

            InCartItems inCartItem2 = new InCartItems(watch2, 0);
            realm.copyToRealm(inCartItem2);

            WatchEntity watch3 = realm.createObject(WatchEntity.class, "#KW003");
            watch3.setModelType("China-Gents");
            watch3.setAvailable(true);
            watch3.setUrl("http://abc.com");

            InCartItems inCartItem3 = new InCartItems(watch3, 0);
            realm.copyToRealm(inCartItem3);

            WatchEntity watch4 = realm.createObject(WatchEntity.class, "#KW004");
            watch4.setModelType("China-Ladies");
            watch4.setAvailable(true);
            watch4.setUrl("http://efg.com");

            InCartItems inCartItem4 = new InCartItems(watch4, 0);
            realm.copyToRealm(inCartItem4);

            realm.commitTransaction();
        }
    }

    public static RealmResults<WatchEntity> getAll(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(WatchEntity.class).findAll();
    }
}
