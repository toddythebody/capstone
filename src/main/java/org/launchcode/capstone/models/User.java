package org.launchcode.capstone.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Pattern(regexp = "^[\\S]{5,30}$", message = "No spaces/5-30 characters")
    private String name;
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^[\\S]{5,}$", message = "No spaces/5-30 characters")
    private String password;

    public User() { }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
