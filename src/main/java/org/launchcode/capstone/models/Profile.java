package org.launchcode.capstone.models;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.File;
import java.nio.ByteOrder;

@Entity
public class Profile {

    @Id
    @GeneratedValue
    private int id;

    @Lob
    private byte[] pic;

    @Size(max = 1000, message = "1000 characters max")
    private String about;

    @OneToOne
    private User user;

    public Profile() {}

    public Profile(byte[] pic, String about) {
        this.pic = pic;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
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
