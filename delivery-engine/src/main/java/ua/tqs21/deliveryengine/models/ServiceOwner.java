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

@Entity
@Table(name = "service_owners")
public class ServiceOwner {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "user")
    private Set<Service> services;

    public ServiceOwner() {}

    public ServiceOwner(User user, Set<Service> services) {
        this.user = user;
        this.services = services;
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

    public Set<Service> getServices() {
        return this.services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
}
