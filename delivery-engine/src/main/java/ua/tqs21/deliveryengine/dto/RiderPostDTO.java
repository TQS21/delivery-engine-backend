package ua.tqs21.deliveryengine.dto;

import java.util.Date;

public class RiderPostDTO {
    private String email;
    private String photo;
    private Date birthdate;
    private String password;

    public RiderPostDTO() {}

    public RiderPostDTO(String email, String photo, Date bday, String password) {
        this.email = email;
        this.photo = photo;
        this.birthdate = bday;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getDate() {
        return this.birthdate;
    }

    public void setDate(Date date) {
        this.birthdate = date;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
