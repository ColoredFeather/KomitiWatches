package com.coloredfeather.komitiwatches.customer.models;

import android.content.ClipData;

import com.coloredfeather.komitiwatches.customer.network.response.GetAllItemsResponce;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anandparmar on 21/01/17.
 */

public class ItemEntity extends RealmObject {

    @PrimaryKey
    private String itemCode;
    private String pCode;
    private String type;
    private String subType;
    private String photo;
    private String price;
    private String discriptions;
    private int quantity;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscriptions() {
        return discriptions;
    }

    public void setDiscriptions(String discriptions) {
        this.discriptions = discriptions;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static List<ItemEntity> getAll() {
        return Realm.getDefaultInstance().where(ItemEntity.class).findAll();
    }

    public static long countInCartItems() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ItemEntity.class).count();
    }

    public static List<ItemEntity> getInCartItems() {
        return Realm.getDefaultInstance().where(ItemEntity.class).findAll();
    }

    public static void clearInCartItems() {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ItemEntity> results = realm.where(ItemEntity.class).findAll();
                results.deleteAllFromRealm();
            }
        });
    }

    public static void addOrUpdateItem(final GetAllItemsResponce.ItemsInResponse itemsInResponse, final int quantity) {
        Realm realm = Realm.getDefaultInstance();
        final ItemEntity item = getByItemCode(itemsInResponse.getItemCode());
        if (item == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ItemEntity itemEntity = realm.createObject(ItemEntity.class, itemsInResponse.getItemCode());
                    itemEntity.setpCode(itemsInResponse.getpCode());
                    itemEntity.setType(itemsInResponse.getType());
                    itemEntity.setSubType(itemsInResponse.getSubType());
                    itemEntity.setPrice(itemsInResponse.getPrice());
                    itemEntity.setPhoto(itemsInResponse.getPhoto());
                    itemEntity.setDiscriptions(itemsInResponse.getDiscription());
                    itemEntity.setQuantity(quantity);
                }
            });
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ItemEntity itemEntity = getByItemCode(itemsInResponse.getItemCode());
                    itemEntity.setpCode(itemsInResponse.getpCode());
                    itemEntity.setType(itemsInResponse.getType());
                    itemEntity.setSubType(itemsInResponse.getSubType());
                    itemEntity.setPrice(itemsInResponse.getPrice());
                    itemEntity.setPhoto(itemsInResponse.getPhoto());
                    itemEntity.setDiscriptions(itemsInResponse.getDiscription());
                    itemEntity.setQuantity(quantity);
                }
            });
        }
    }

    public static void updateItemQuantity(final ItemEntity item, final int quantity) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemEntity itemEntity = getByItemCode(item.getItemCode());
                itemEntity.setQuantity(quantity);
            }
        });
    }

    public static String getMsgForSelectedItem() {
        String result = "";
        List<ItemEntity> itemEntities = Realm.getDefaultInstance().where(ItemEntity.class).findAll();
        for (ItemEntity item : itemEntities) {
            result = result + item.getItemCode() + " -> " + item.getQuantity() + "\n";
        }
        return result;
    }

    public static List<ItemEntity> getFilteredItems(String type, String subType) {
        return Realm.getDefaultInstance().where(ItemEntity.class).equalTo("type", type).equalTo("subType", subType).findAll();
    }

    public static ItemEntity getByItemCode(String itemCode) {
        return Realm.getDefaultInstance().where(ItemEntity.class).equalTo("itemCode", itemCode).findFirst();
    }
}
