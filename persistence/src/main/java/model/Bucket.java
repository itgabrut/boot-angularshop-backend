package model;

import model.PK.BucketPK;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by imedvede on 04.05.2018.
 */
@Entity
@Table( name = "Bucket")
@IdClass(BucketPK.class)
public class Bucket {

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @Id
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    public Item getItem() {
        return item;
    }

    public Client getClient() {
        return client;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
