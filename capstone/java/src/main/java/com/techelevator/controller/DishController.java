package com.techelevator.controller;

import com.techelevator.dao.DishDao;
import com.techelevator.dao.RestrictionDao;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/dish")
@CrossOrigin


public class DishController {
    private DishDao dishDao;
    private RestrictionDao restrictionDao;

    private DishController(DishDao dishDao, RestrictionDao restrictionDao) {
        this.dishDao = dishDao;
        this.restrictionDao = restrictionDao;
    }

    @GetMapping(path = "")
    public List<Dish> getAllDishes() {
        return dishDao.getAllDishes();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create")
    public int createDish(@RequestBody @Valid Dish dish) {
        return dishDao.createDish(dish.getDishPluckId(), dish.getDishCatId(), dish.getDishUserId(), dish.getDishName(), dish.getDishDescription(), dish.getServings());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path="/update/{dishId}")
    public boolean updateDish(@RequestBody @Valid Dish dish, @PathVariable int dishId){
        return dishDao.updateDish(dishId, dish.getDishDescription(), dish.getDishName(), dish.getServings(), dish.getUsername());
    }

    @GetMapping(path = "/dish{dishId}")
    public Dish getDishById(@PathVariable int dishId) {
        return dishDao.getDishById(dishId);
    }

    @GetMapping(path = "/user{userId}/pluck{pluckId}")
    public List<Dish> getDishesByUserAndPluck(@PathVariable int userId, @PathVariable int pluckId) {
        return dishDao.getDishesByUserAndPluck(userId, pluckId);
    }

    @GetMapping(path = "/pluck{pluckId}")
    public List<Dish> getDishesByPluckId(@PathVariable int pluckId) {
        return dishDao.getDishesByPluckId(pluckId);
    }

    @GetMapping(path = "/cat{catId}/pluck{pluckId}")
    public List<Dish> getDishesByCatAndPluck(@PathVariable int catId, @PathVariable int pluckId) {
        return dishDao.getDishesByCatAndPluck(catId, pluckId);
    }

    @GetMapping(path = "/cat_of_dish{dishId}")
    public Category getCatByDishId(@PathVariable int dishId) {
        return dishDao.getCategoryByDishId(dishId);
    }

    @GetMapping(path = "/pluck_of_dish{dishId}")
    public Potluck getPluckByDishId(@PathVariable int dishId) {
        return dishDao.getPluckByDishId(dishId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/addRestriction")
    public boolean addRestriction(@RequestBody AddRestrictionToDishDTO newCat) {
        return dishDao.addRestriction(newCat.getDishId(), newCat.getRestrictionId());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/delete/{dishId}")
    public boolean deleteDish(@PathVariable int dishId){return dishDao.deleteDish(dishId);}

    @GetMapping(path = "/dietaryRestrictions")
    public List<Restriction> getAllRestrictions(){
        return restrictionDao.getRestrictionsList();
    }

    @GetMapping(path = "/{dishId}/restrictions")
    public List<Integer> getRestrictionsByDish(@PathVariable int dishId) {
        return dishDao.getDishRestrictionIds(dishId);
    }
    
}
