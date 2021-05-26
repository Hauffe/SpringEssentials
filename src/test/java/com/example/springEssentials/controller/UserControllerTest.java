package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;


    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void hello() {
        //Act
        String hello = this.userController.hello();

        //Assert
        assertEquals(hello, "Service for Spring Essentials");
    }

    @Test
    void findAll() {
        //Arrange
        List<User> userMock = prepareList();
        Mockito.when(userService.findAll()).thenReturn(userMock);

        //Act
        List<User> response = this.userController.findAll();

        //Assert
        assertNotNull(response);
        assertEquals(response, userMock);

    }

    @Test
    void findAllEmpty() {
        //Arrange
        List<User> userMock = new ArrayList<>();
        Mockito.when(userService.findAll()).thenReturn(userMock);

        //Act
        List<User> response = this.userController.findAll();

        //Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void findById() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(userService.findById(1L)).thenReturn(userMock);

        //Act
        User response = this.userController.findById(1L);

        //Assert
        assertNotNull(response);
        assertEquals(response, userMock);
        assertEquals(1L, response.getId());
    }

    @Test
    void findByIdNotFound() {
        //Arrange
        Mockito.when(userService.findById(1L)).thenReturn(null);

        //Act
        User response = this.userController.findById(1L);

        //Assert
        assertNull(response);
    }

    @Test
    void create() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(userService.create(userMock))
                .thenReturn(userMock);

        //Act
        User response = userController.create(userMock);

        //Assert
        assertNotNull(response);
        assertEquals(response, userMock);
    }

    @Test
    void createEmpty() {
        //Act
        User user = userController.create(null);

        //Assert
        assertNull(user);
    }

    @Test
    void update(){
        //Arrange
        User userMock = prepareUser();
        userMock.setName("Margaret");
        Mockito.when(userService.update(userMock.getId(), userMock)).thenReturn(userMock);

        //Act
        User response = this.userController.update(userMock.getId(), userMock);

        //Assert
        assertNotNull(response);
        assertEquals(userMock.getId(), response.getId());
        assertEquals("Margaret", response.getName());
        assertNotEquals(response, prepareUser());
    }

    @Test
    void delete() {
        //Arrange
        Mockito.when(userService.delete(1L)).thenReturn(1L);

        //Act
        Long deletedId = this.userController.delete(1L);

        //Assert
        assertNotNull(deletedId);
        assertEquals(deletedId, 1L);
    }

    @Test
    void deleteNotFound() {
        //Arrange
        Mockito.when(userService.delete(1L)).thenReturn(null);

        //Act
        Long deletedId = this.userController.delete(1L);

        //Assert
        assertNull(deletedId);
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