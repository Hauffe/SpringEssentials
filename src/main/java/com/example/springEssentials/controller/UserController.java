package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping("/about")
    public String hello() {
        return "Service for Spring Essentials";
    }

    @GetMapping("/findAll")
    public List<User> findAll(){
        return service.findAll();
    }

    @GetMapping(path = {"/findById/{id}"})
    public User findById(@PathVariable long id){
        return service.findById(id);
    }

    @PostMapping("/add")
    public User create(@RequestBody User user){
        return service.create(user);
    }

    @PutMapping(value="/update/{id}")
    public User update(@PathVariable("id") long id,
                                 @RequestBody User user) {
        return service.update(id, user);
    }

    @DeleteMapping(path ={"/delete/{id}"})
    public Long delete(@PathVariable long id) {
        return service.delete(id);
    }
}
