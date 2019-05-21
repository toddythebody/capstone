package org.launchcode.capstone.models;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


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
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Item> items = new ArrayList<>();
    @ManyToMany
    private List<User> friends = new ArrayList<>();
    @ManyToMany(mappedBy = "friends")
    private List<User> friendOf = new ArrayList<>();

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

    public List<Item> getItems() {
        return items;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<User> getFriendOf() {
        return friendOf;
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }
}
