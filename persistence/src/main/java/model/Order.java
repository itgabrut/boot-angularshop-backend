package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import model.enums_utils.Delivery_status;
import model.enums_utils.Pay_status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by ilya on 20.08.2016.
 * order entity
 */

@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @Column(name = "payway")
    private String payway;

    @Column(name = "delivery")
    private String delivery;

    @DateTimeFormat( pattern = "dd-MM-yyyy")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnore
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private Delivery_status deliveryStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private Pay_status payStatus;
    @JsonIgnore
    @OneToMany(mappedBy = "order" ,fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderForItem> orderForItems;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getPayway() {
        return payway;
    }

    public String getDelivery() {
        return delivery;
    }

    public Client getClient() {
        return client;
    }

    public Delivery_status getDeliveryStatus() {
        return deliveryStatus;
    }

    public Pay_status getPayStatus() {
        return payStatus;
    }

    public List<OrderForItem> getOrderForItems() {
        return orderForItems;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDeliveryStatus(Delivery_status deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setPayStatus(Pay_status payStatus) {
        this.payStatus = payStatus;
    }

    public void setOrderForItems(List<OrderForItem> orderForItems) {
        this.orderForItems = orderForItems;
    }


    public static Order getSimpleOrder(){
        Order order = new Order();
        order.setPayway("credit card");
        order.setPayStatus(Pay_status.WAITING);
        order.setDelivery("airmail");
        order.setDeliveryStatus(Delivery_status.WAITING_FOR_PAYMENT);
        return order;
    }
}
