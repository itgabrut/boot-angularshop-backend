package model.PK;

import java.io.Serializable;

/**
 * Created by imedvede on 04.05.2018.
 */
public class BucketPK implements Serializable {

    private int item;

    private int client;

    public BucketPK(){

    }
    public BucketPK(int item, int client) {
        this.item = item;
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BucketPK bucketPK = (BucketPK) o;

        if (item != bucketPK.item) return false;
        return client == bucketPK.client;

    }

    @Override
    public int hashCode() {
        int result = item;
        result = 31 * result + client;
        return result;
    }

    public int getItem() {
        return item;
    }

    public int getClient() {
        return client;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
