package com.example.springEssentials.controller;

import com.example.springEssentials.model.User;
import com.example.springEssentials.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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
        ResponseEntity responseEntity = this.userController.findAll();
        List<User> response = (List<User>) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(response);
        assertEquals(response, userMock);

    }

    @Test
    void findAllEmpty() {
        //Arrange
        List<User> userMock = new ArrayList<>();
        Mockito.when(userService.findAll()).thenReturn(userMock);

        //Act
        ResponseEntity responseEntity = this.userController.findAll();
        List<User> response = (List<User>) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertTrue(response.isEmpty());
    }

    @Test
    void findById() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(userService.findById(1L)).thenReturn(userMock);

        //Act
        ResponseEntity responseEntity = this.userController.findById(1L);
        User response = (User) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(response);
        assertEquals(response, userMock);
        assertEquals(1L, response.getId());
    }

    @Test
    void findByIdNotFound() {
        //Arrange
        Mockito.when(userService.findById(1L)).thenReturn(null);

        //Act
        ResponseEntity responseEntity = this.userController.findById(1L);

        //Assert
        assertEquals(ResponseEntity.notFound().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    void create() {
        //Arrange
        User userMock = prepareUser();
        Mockito.when(userService.create(userMock))
                .thenReturn(userMock);

        //Act
        ResponseEntity responseEntity = userController.create(userMock);
        User user = (User) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(user);
        assertEquals(user, userMock);
    }

    @Test
    void createEmpty() {
        //Act
        ResponseEntity responseEntity = this.userController.create(null);

        //Assert
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    void update(){
        //Arrange
        User userMock = prepareUser();
        userMock.setName("Margaret");
        Mockito.when(userService.update(userMock.getId(), userMock)).thenReturn(userMock);

        //Act
        ResponseEntity responseEntity = this.userController.update(userMock.getId(), userMock);
        User response = (User) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertEquals(userMock.getId(), response.getId());
        assertEquals("Margaret", response.getName());
        assertNotEquals(response, prepareUser());
    }

    @Test
    void delete() {
        //Arrange
        Mockito.when(userService.delete(1L)).thenReturn(1L);

        //Act
        ResponseEntity responseEntity = this.userController.delete(1L);
        Long deletedId = (Long) responseEntity.getBody();

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(deletedId);
        assertEquals(deletedId, 1L);
    }

    @Test
    void deleteNotFound() {
        //Arrange
        Mockito.when(userService.delete(1L)).thenReturn(null);

        //Act
        ResponseEntity responseEntity = this.userController.delete(1L);
        Long deletedId = (Long) responseEntity.getBody();

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