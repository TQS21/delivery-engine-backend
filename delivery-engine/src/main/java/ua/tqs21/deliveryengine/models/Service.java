package ua.tqs21.deliveryengine.models;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private ServiceOwner user;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "lon"))
    })
    private Address address;
    
    @OneToMany(mappedBy = "shop")
    private Set<Order> deliveries;

    public Service() {}

    public Service(String name, ServiceOwner user, Address address, Set<Order> deliveries) {
        this.name = name;
        this.user = user;
        this.address = address;
        this.deliveries = deliveries;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceOwner getUser() {
        return this.user;
    }

    public void setUser(ServiceOwner user) {
        this.user = user;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address a) {
        this.address = a;
    }

    public Set<Order> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(Set<Order> deliveries) {
        this.deliveries = deliveries;
    }

}
