package com.example.springEssentials.service;

import com.example.springEssentials.dao.UserRepository;
import com.example.springEssentials.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public Iterable<User> findAll(){
        return repository.findAll();
    }

    public User findById(Long id){
            return repository.findById(id)
                    .orElse(null);
    }

    public User create(User user){
        return repository.save(user);
    }

    public Long delete(Long id){
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return id;
                }).orElse(null);
    }


    public User update(long id, User user) {
            return repository.findById(id)
                    .map(record -> {
                        record.setName(user.getName());
                        record.setEmail(user.getEmail());
                        User userUpdated = repository.save(record);
                        return userUpdated;
                    }).orElse(null);
    }
}
