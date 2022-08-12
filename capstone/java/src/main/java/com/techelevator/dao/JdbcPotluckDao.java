package com.techelevator.dao;

import com.techelevator.model.Potluck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPotluckDao implements PotluckDao {
    private final JdbcTemplate jdbcTemplate;



    public JdbcPotluckDao(JdbcTemplate jdbcTemplate, PotluckDao potluckDao) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Potluck> getAllPlucks() {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck";
        SqlRowSet allPlucks = jdbcTemplate.queryForRowSet(sql);
        List<Potluck> pluckList = new ArrayList<>();

        while (allPlucks.next()) {
            pluckList.add(mapRowToPluck(allPlucks));
        }
        return pluckList;
    }

    @Override
    public List<Potluck> getAllPlucksByUser(int userId) {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck " +
                "JOIN pluck_user AS pu ON pu.pluck_id = pluck.pluck_id " +
                "WHERE pu.user_id = ?";

       SqlRowSet plucksByUser = jdbcTemplate.queryForRowSet(sql);
       List<Potluck> pluckList = new ArrayList<>();

       while (plucksByUser.next()){
           pluckList.add(mapRowToPluck(plucksByUser));
       }
       return pluckList;
    }


    @Override
    public Boolean createPluck(String pluckName, LocalDateTime pluckTime, String pluckPlace) {
        String sql = "INSERT INTO pluck (pluck_name, pluck_date_time, pluck_place) values (?,?,?)";
        if (jdbcTemplate.update(sql, pluckName, pluckPlace) == 1) {
            return true;
        }
        System.err.println("Couldn't create new potluck");

        return false;
    }

    @Override
    public Boolean updatePluck(int pluckId, String pluckName, String pluckDescription, LocalDateTime pluckTime, String pluckPlace) {
        String sql = "UPDATE pluck SET pluck_name = ?, pluck_date_time = ?, pluck_place = ? " +
                "WHERE pluck_id = ?";
        if (jdbcTemplate.update(sql, pluckName, pluckDescription, pluckTime, pluckPlace, pluckId) == 1) {
            return true;
        }
        System.err.println("Couldn't update potluck " + pluckId);
        return false;

    }

    @Override
    public Potluck getPluckById(int pluckId) {
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck WHERE pluck_id = ?";
        for (Potluck pluck : getAllPlucks()) {
            if (pluck.getPluckId() == pluckId) {
                return pluck;
            }
        }
        return null;
    }

    @Override
    public Potluck getPluckByName(String pluckName){
        String sql = "SELECT pluck_id, pluck_name, pluck_description, pluck_date_time, pluck_place " +
                "FROM pluck WHERE pluck_name = ?";
        for (Potluck pluck : getAllPlucks()) {
            if (pluck.getPluckName() == pluckName) {
                return pluck;
            }
        }
        return null;

    }

    @Override //todo: in controller, this will have to happen AFTER dish is added to dish table in order to get dishId
    public Boolean addDish(int dishId, int pluckId, int cat_id, int user_id, String dish_name) {
        String sql = "INSERT INTO pluck_dish (dish_id, pluck_id, cat_id, user_id, dish_name) VALUES (?,?,?,?,?)";
        if (jdbcTemplate.update(sql, dishId, pluckId, cat_id, user_id, dish_name) == 1) {
            return true;
        }
        System.err.println("Couldn't update potluck " + pluckId);
        return false;
    }

    @Override
    public Boolean addCat(int pluckId, int catId) {
        String sql = "INSERT INTO pluck_cat (pluck_id, cat_id) VALUES (?,?)";
        if (jdbcTemplate.update(sql, pluckId, catId) == 1){
            return true;
        }
        System.err.println("Couldn't update potluck " + pluckId);
        return false;
    }


    private Potluck mapRowToPluck(SqlRowSet pluckRowSet) {
        Potluck pluck = new Potluck();
        pluck.setPluckId(pluckRowSet.getInt("pluck_id"));
        pluck.setPluckName(pluckRowSet.getString("pluck_name"));
        pluck.setPluckDescription(pluckRowSet.getString("pluck_description"));
        if (pluckRowSet.getTimestamp("pluck_date_time") != null) {
            pluck.setPluckTime(pluckRowSet.getTimestamp("pluck_date_time").toLocalDateTime());
        } else {
            pluck.setPluckTime(null);
        }
        pluck.setPluckPlace(pluckRowSet.getString("pluck_place"));
        return pluck;
    }


}
