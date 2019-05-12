package org.launchcode.capstone.models.data;

public interface CustomRepo<User, String> {
    public User findByName(String name);
}
