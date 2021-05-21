package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping("server")
    public String hello() {
        return "Service for Spring Essentials";
    }

    @GetMapping
    public Iterable<User> findAll(){
        return service.findAll();
    }

    @GetMapping(path = {"{id}"})
    public User findById(@PathVariable long id){
        return service.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user){
        return service.create(user);
    }

    @PutMapping(value="/{id}")
    public User update(@PathVariable("id") long id,
                                 @RequestBody User user) {
        return service.update(id, user);
    }

    @DeleteMapping(path ={"{id}"})
    public Long delete(@PathVariable long id) {
        return service.delete(id);
    }
}
