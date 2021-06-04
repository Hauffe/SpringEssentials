package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping("/about")
    public String hello() {
        log.info("'hello' called");
        return "Service for Spring Essentials";
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        log.info("'findAll' called");
        return Optional.of(service.findAll())
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"{id}"})
    public ResponseEntity<User> findById(@PathVariable long id){
        log.info("'findById' method called with Id= {} ", id);
        return Optional.ofNullable(service.findById(id))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        log.info("'create' method called with user= {} ", user);
        return Optional.ofNullable(service.create(user))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping(value="{id}")
    public ResponseEntity<User> update(@PathVariable("id") long id,
                       @RequestBody User user) {
        log.info("'update' method called with book= {} and id= {}", user, id);
        return Optional.ofNullable(service.update(id, user))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path ={"{id}"})
    public ResponseEntity<Long> delete(@PathVariable long id) {
        log.info("'delete' method called with user id= {} ", id);
        return Optional.ofNullable(service.delete(id))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

}
