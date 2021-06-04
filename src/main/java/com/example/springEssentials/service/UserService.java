package com.example.springEssentials.service;

import com.example.springEssentials.dao.UserRepository;
import com.example.springEssentials.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public List<User> findAll(){
        return (List<User>) repository.findAll();
    }

    public User findById(Long id){
        var user = repository.findById(id).orElse(null);

        if(user == null)
            log.warn("Can't findById with id {} from repository ", id);

        return user;
    }

    public User create(User user){
        User userCreated = null;
        String message = testObject(user);

        if(!"".equals(message))
            log.error("Can't save {} in repository because {}", user, message);
        else{
            userCreated = repository.save(user);
        }
        return userCreated;
    }

    public User update(long id, User user) {
        var userToUpdate = repository.findById(id)
                .map(record -> {
                    record.setName(user.getName());
                    record.setEmail(user.getEmail());
                    User userUpdated = repository.save(record);
                    return userUpdated;
                }).orElse(null);


        if(userToUpdate == null)
            log.error("Can't update user id={} to user={} in repository", id, user);

            return userToUpdate;
    }

    public Long delete(Long id){
        var idDeleted = repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return id;
                }).orElse(null);


        if(idDeleted == null)
            log.warn("Can't findById with id {} from repository ", id);

        return idDeleted;
    }

    private String testObject(User user){
        String message = "";
        if(user.getName() == null){
            message += "Name is null ";
        }
        if(user.getEmail() == null){
            message += "Email is null ";
        }
        return message;
    }
}
