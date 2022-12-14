package com.techelevator.dao;

import com.techelevator.model.Potluck;
import com.techelevator.model.exceptions.CategoryNotFoundException;
import com.techelevator.model.exceptions.PotluckNotFoundException;
import com.techelevator.model.exceptions.UserNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPotluckDao implements PotluckDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPotluckDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Potluck> getAllPlucks() {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place, pluck_banner " +
                "FROM pluck";
        SqlRowSet allPlucks = jdbcTemplate.queryForRowSet(sql);
        List<Potluck> pluckList = new ArrayList<>();

        while (allPlucks.next()) {
            pluckList.add(mapRowToPluck(allPlucks));
        }
        return pluckList;
    }

    @Override
    public Potluck getPluckById(int pluckId) {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place, pluck_banner " +
                "FROM pluck WHERE pluck_id = ?";

        SqlRowSet pluckById = jdbcTemplate.queryForRowSet(sql, pluckId);

        for (Potluck pluck : getAllPlucks()) {
            if (pluck.getPluckId() == pluckId) {
                return pluck;
            }
        }
        throw new PotluckNotFoundException();
    }

    @Override
    public List<Potluck> getAllPlucksByUser(int userId) {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck " +
                "JOIN pluck_user AS pu ON pu.pluck_id = pluck.pluck_id " +
                "WHERE pu.user_id = ?";

        SqlRowSet plucksByUser = jdbcTemplate.queryForRowSet(sql);
        List<Potluck> pluckList = new ArrayList<>();

        try {
            while (plucksByUser.next()) {
                pluckList.add(mapRowToPluck(plucksByUser));
            }
            return pluckList;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Integer createPluck(String pluckName, LocalDateTime pluckTime, String pluckPlace, String pluckDescription, String pluckImageURL) {
        String sql = "INSERT INTO pluck (pluck_name, pluck_date_time, pluck_place, pluck_description, pluck_banner) " +
                "VALUES (?,?,?,?,?) " +
                "RETURNING pluck_id";
        return jdbcTemplate.queryForObject(sql,Integer.class,pluckName,pluckTime,pluckPlace,pluckDescription,pluckImageURL);
    }

    @Override
    public Boolean updatePluck(int pluckId, String pluckName, String pluckDescription, LocalDateTime pluckTime, String pluckPlace) {
        String sql = "UPDATE pluck SET pluck_name = ?, pluck_description = ?, pluck_date_time = ?, pluck_place = ? "  +
                "WHERE pluck_id = ?";
        if (jdbcTemplate.update(sql, pluckName, pluckDescription, pluckTime, pluckPlace, pluckId) == 1) {
            return true;
        }
        throw new PotluckNotFoundException();

    }



    @Override
    public Potluck getPluckByName(String pluckName) {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck WHERE pluck_name = ?";

        SqlRowSet pluckByName = jdbcTemplate.queryForRowSet(sql, pluckName);

        if (pluckByName.next()) {
            return (mapRowToPluck(pluckByName));
        } else {
            throw new PotluckNotFoundException();
        }

    }

    @Override /*todo: in controller, this will have to happen AFTER dish is added to dish table in order to get dishId.
    todo: Possibly better to put in Dish controller since all these values are in Dish object*/
    public Boolean addDish(int dishId, int pluckId, int cat_id, int user_id, String dish_name) {
        String sql = "INSERT INTO pluck_dish (dish_id, pluck_id, cat_id, user_id, dish_name) VALUES (?,?,?,?,?)";
        try {
            if (jdbcTemplate.update(sql, dishId, pluckId, cat_id, user_id, dish_name) == 1) {
                return true;
            }
            System.err.println("Couldn't update potluck " + pluckId);
            throw new PotluckNotFoundException();
        } catch (CategoryNotFoundException c){
            throw new CategoryNotFoundException();
        } catch (UserNotFoundException u){
            throw new UserNotFoundException();
        }
    }

    @Override
    public Boolean addCat(int pluckId, int catId, int limit) {
        String sql = "INSERT INTO pluck_cat (pluck_id, cat_id, cat_limit) VALUES (?,?,?)";
        try {
            if (jdbcTemplate.update(sql, pluckId, catId, limit) == 1) {
                return true;
            }
            System.err.println("Couldn't update potluck " + pluckId);
            throw new PotluckNotFoundException();
        } catch (CategoryNotFoundException c){
            throw new CategoryNotFoundException();
        }
    }


    private Potluck mapRowToPluck(SqlRowSet pluckRowSet) {
        Potluck pluck = new Potluck();
        pluck.setPluckId(pluckRowSet.getInt("pluck_id"));
        pluck.setPluckName(pluckRowSet.getString("pluck_name"));
        pluck.setPluckDescription(pluckRowSet.getString("pluck_description"));
        pluck.setPluckImageURL(pluckRowSet.getString("pluck_banner"));
        if (pluckRowSet.getTimestamp("pluck_date_time") != null) {
            pluck.setPluckTime(pluckRowSet.getTimestamp("pluck_date_time").toLocalDateTime());
        } else {
            pluck.setPluckTime(null);
        }
        pluck.setPluckPlace(pluckRowSet.getString("pluck_place"));
        return pluck;
    }


}
