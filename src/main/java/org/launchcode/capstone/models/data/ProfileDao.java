package org.launchcode.capstone.models.data;

import org.launchcode.capstone.models.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileDao extends CrudRepository<Profile, Integer> {
}
