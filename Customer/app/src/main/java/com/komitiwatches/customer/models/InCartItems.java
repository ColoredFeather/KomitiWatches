package com.komitiwatches.customer.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by anandparmar on 25/01/17.
 */

public class InCartItems extends RealmObject {
    private WatchEntity watchEntity;
    private int quantity;
    private String pathToStore;

    public InCartItems() {
    }

    public InCartItems(WatchEntity watchEntity, int quantity) {
        this.watchEntity = watchEntity;
        this.quantity = quantity;
        this.pathToStore = "";
    }

    public InCartItems(WatchEntity watchEntity, int quantity, String pathToStore) {
        this.watchEntity = watchEntity;
        this.quantity = quantity;
        this.pathToStore = pathToStore;
    }

    public WatchEntity getWatchEntity() {
        return watchEntity;
    }

    public void setWatchEntity(WatchEntity watchEntity) {
        this.watchEntity = watchEntity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPathToStore() {
        return pathToStore;
    }

    public void setPathToStore(String pathToStore) {
        this.pathToStore = pathToStore;
    }

    public static RealmResults<InCartItems> getAll(){
        return Realm.getDefaultInstance().where(InCartItems.class).findAll();
    }

    public static long countInCartItems(){
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(InCartItems.class).notEqualTo("quantity", 0).count();
        return count;
    }

    public static RealmResults<InCartItems> getInCartItems(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(InCartItems.class).notEqualTo("quantity", 0).findAll();
    }



    public static void clearInCartItems(){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<InCartItems> results = realm.where(InCartItems.class).findAll();
                for(InCartItems inCartItem: results){
                    inCartItem.setQuantity(0);
                }
            }
        });
    }

    public static void addOrUpdateItem(final WatchEntity watchEntity, final int quantity){
        Realm realm = Realm.getDefaultInstance();
        final InCartItems inCartItems = realm.where(InCartItems.class)
                                    .equalTo("watchEntity.modelNumber", watchEntity.getModelNumber()).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(inCartItems != null){
                    inCartItems.setQuantity(quantity);
                }
            }
        });
    }

    public static String getMsgForSelectedItem(){
        String result = "";
        RealmResults<InCartItems> inCartItemses = Realm.getDefaultInstance().where(InCartItems.class).notEqualTo("quantity", 0).findAll();
        for(InCartItems inCartItem: inCartItemses){
            result = result + inCartItem.getWatchEntity().getModelNumber() + " -> " + inCartItem.getQuantity() + "\n";
        }
        return result;
    }

    public static RealmResults<InCartItems> getFilteredItems(String modelType, int price){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<InCartItems> results = null;
        if("".equals(modelType)){
            results = realm.where(InCartItems.class).lessThanOrEqualTo("watchEntity.prize", price).findAll();
        } else if(price == 0){
            results = realm.where(InCartItems.class).equalTo("watchEntity.modelType", modelType).findAll();
        } else {
            results = realm.where(InCartItems.class).equalTo("watchEntity.modelType", modelType).lessThanOrEqualTo("watchEntity.prize", price).findAll();
        }
        return results;
    }
}
