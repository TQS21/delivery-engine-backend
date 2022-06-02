package ua.tqs21.deliveryengine.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "order_status")
    private OrderStatus status;

    @Column(name="creation_ts")
    @CreationTimestamp
    private Date timestamp;

    @Column(name = "delivery_ts")
    private Date delivery_timestamp;

    @ManyToOne
    @JoinColumn(name="rider_id")
    private Rider courier;

    @ManyToOne
    @JoinColumn(name="service_id")
    private Service shop;


    public Order() {}

    public Order(OrderStatus status, Date timestamp, Date delivery_timestamp, Rider courier, Service shop) {
        this.status = status;
        this.timestamp = timestamp;
        this.delivery_timestamp = delivery_timestamp;
        this.courier = courier;
        this.shop = shop;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return this.status;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date ts) {
        this.timestamp = ts;
    }

    public Date getDelivery_timestamp() {
        return this.delivery_timestamp;
    }

    public void setDelivery_timestamp(Date ts) {
        this.delivery_timestamp = ts;
    }

    public Rider getCourier() {
        return this.courier;
    }

    public void setCourier(Rider courier) {
        this.courier = courier;
    }

    public Service getShop() {
        return this.shop;
    }
    
    public void setShop(Service shop) {
        this.shop = shop;
    }
}