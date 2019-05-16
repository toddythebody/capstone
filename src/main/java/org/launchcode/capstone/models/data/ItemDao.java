package org.launchcode.capstone.models.data;

import org.launchcode.capstone.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemDao extends CrudRepository<Item, Integer> {
}
