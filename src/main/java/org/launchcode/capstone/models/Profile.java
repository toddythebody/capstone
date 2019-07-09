package org.launchcode.capstone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@Entity
public class Profile {

    @Id
    @GeneratedValue
    private int id;

    private String pic_location;

    @Size(max = 1000, message = "1000 characters max")
    private String about;

    @OneToOne
    private User user;

    public Profile() {}

    public Profile(String pic_location, String about) {
        this.pic_location = pic_location;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public String getPic_location() {
        return pic_location;
    }

    public void setPic_location(String pic_location) {
        this.pic_location = pic_location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
