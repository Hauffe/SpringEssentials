package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping("/about")
    public String hello() {
        return "Service for Spring Essentials";
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return Optional.of(service.findAll())
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"{id}"})
    public ResponseEntity<User> findById(@PathVariable long id){
        return Optional.ofNullable(service.findById(id))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        return Optional.ofNullable(service.create(user))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping(value="{id}")
    public ResponseEntity<User> update(@PathVariable("id") long id,
                       @RequestBody User user) {
        return Optional.ofNullable(service.update(id, user))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path ={"{id}"})
    public ResponseEntity<Long> delete(@PathVariable long id) {
        return Optional.ofNullable(service.delete(id))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

}
