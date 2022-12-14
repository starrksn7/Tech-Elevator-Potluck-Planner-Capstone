package com.techelevator.dao;

import com.techelevator.model.Potluck;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface PotluckDao {

    List<Potluck> getAllPlucks();

    List<Potluck> getAllPlucksByUser(int userId);

    Integer createPluck(String pluckName, LocalDateTime pluckTime, String pluckPlace, String pluckDescription, String pluckImageURL);

    Boolean updatePluck(int pluckId, String pluckName, String pluckDescription, LocalDateTime pluckTime, String pluckPlace);

    Potluck getPluckById(int pluckId);

    Potluck getPluckByName(String pluckName);

    Boolean addDish(int dishId, int pluckId, int cat_id, int user_id, String dish_name);

    Boolean addCat(int pluckId, int catId, int limit);

}
