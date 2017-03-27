package com.coloredfeather.komitiwatches.customer.network.response;

import com.coloredfeather.komitiwatches.customer.models.ItemEntity;
import com.coloredfeather.komitiwatches.customer.network.common.GenericResponse;

import java.util.List;

/**
 * Created by anandparmar on 20/02/17.
 */

public class GetAllItemsResponce extends GenericResponse {

    private ItemsInResponse[] itemsList;

    public ItemsInResponse[] getItemsList() {
        return itemsList;
    }

    public void setItemsList(ItemsInResponse[] itemsList) {
        this.itemsList = itemsList;
    }

    public class ItemsInResponse {
        private String pCode;
        private String itemCode;
        private String type;
        private String subType;
        private String price;
        private String photo;
        private String discription;

        public String getpCode() {
            return pCode;
        }

        public void setpCode(String pCode) {
            this.pCode = pCode;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }
    }
}
