package com.example.springEssentials.service;

import com.example.springEssentials.dao.UserRepository;
import com.example.springEssentials.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        //Arrange
        List<User> users = prepareList();
        Mockito.when(repository.findAll()).thenReturn(users);

        //Act
        Iterable<User> response = this.userService.findAll();

        //Assert
        assertNotNull(response);
        assertEquals(response, users);
    }

    @Test
    void findById() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(userMock));

        //Act
        User response = this.userService.findById(1L);

        //Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void update(){
        //Arrange
        User oldUser = prepareUser();
        User userMock = prepareUser();
        userMock.setName("Margaret");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(userMock));
        Mockito.when(repository.save(userMock)).thenReturn(userMock);

        //Act
        User response = this.userService.update(1L, userMock);

        //Assert
        assertNotNull(response);
        assertEquals(oldUser.getId(), response.getId());
        assertEquals("Margaret", response.getName());
        assertNotEquals(oldUser, response);
    }

    @Test
    void create() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(repository.save(userMock))
                .thenReturn(userMock);

        //Act
        User user = userService.create(userMock);

        //Assert
        assertNotNull(user);
        assertEquals(userMock, user);
    }

    private List<User> prepareList(){
        List<User> list= new ArrayList<>();
        list.add(new User(1L, "user1", "test1@test.com"));
        list.add(new User(2L, "user2", "test2@test.com"));
        list.add(new User(3L, "user3", "test3@test.com"));
        return list;
    }

    private User prepareUser(){
        User user = new User(1L, "user1", "test@test.com");
        return user;
    }
}