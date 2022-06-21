package ua.tqs21.deliveryengine.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "riders")
public class Rider {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "courier")
    private Set<Order> deliveries;

    public Rider() {}

    public Rider(User user, Set<Order> deliveries) {
        this.user = user;
        this.deliveries = deliveries;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Order> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(Set<Order> deliveries) {
        this.deliveries = deliveries;
    }
}
