package ua.tqs21.deliveryengine.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    public Admin() {}

    public Admin(User user) {
        this.user = user;
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
}
