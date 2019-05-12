package org.launchcode.capstone.models.data;

import org.launchcode.capstone.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
}
