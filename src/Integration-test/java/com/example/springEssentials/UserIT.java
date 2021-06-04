package com.example.springEssentials;

import com.example.springEssentials.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private List<User> users;

	@BeforeEach
	void setUp() {
		System.out.println("BeforeEach called");
		users = prepareList();
	}

	@Test
	@Order(1)
	void createUser() {
		//Act
		var responseEntity = testRestTemplate
				.withBasicAuth("user", "password")
				.postForEntity("/users", users.get(0), User.class);
		var user = responseEntity.getBody();

		//Assert
		assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertNotNull(user);
		assertEquals(users.get(0).getName(), user.getName());
	}

	@Test
	@Order(2)
	void getAllUsers() {
		//Arrange
		testRestTemplate
				.withBasicAuth("user", "password")
				.postForEntity("/users", users.get(1), User.class);

		//Act
		var userListResponse =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity("/users", List.class);
		var usersRetrieved = userListResponse.getBody();

		//Assert
		assertNotNull(usersRetrieved);
		assertEquals(2, usersRetrieved.size());
	}

	@Test
	@Order(3)
	void getOneUser() throws JsonProcessingException {
		//Arrange
		var usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
								.withBasicAuth("user", "password")
								.getForEntity("/users", String.class)
								.getBody(), User[].class));
		var url = "/users/"+usersRetrieved.get(0).getId();

		//Act
		var userRetrievedResponse =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(url, User.class);
		var userRetrieved = userRetrievedResponse.getBody();

		//Assert
		assertEquals(ResponseEntity.ok().build().getStatusCode(), userRetrievedResponse.getStatusCode());
		assertNotNull(userRetrievedResponse.getBody());
		assertNotNull(userRetrieved);
		assertTrue(users.get(0).equals(userRetrieved));
	}

	@Test
	@Order(4)
	void update() throws JsonProcessingException {
		//Arrange
		var usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
								.withBasicAuth("user", "password")
								.getForEntity("/users", String.class)
								.getBody(), User[].class));
		var putUrl = "/users/"+usersRetrieved.get(1).getId();
		var getUrl = "/users/"+usersRetrieved.get(1).getId();
		usersRetrieved.get(1).setName("Margaret");

		//Act
		testRestTemplate
				.withBasicAuth("user", "password")
				.put(putUrl, usersRetrieved.get(1));
		var userRetrievedEntity =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(getUrl, User.class);
		var userUpdated = userRetrievedEntity.getBody();

		//Assert
		assertEquals(ResponseEntity.ok().build().getStatusCode(), userRetrievedEntity.getStatusCode());
		assertNotNull(userRetrievedEntity.getBody());
		assertNotNull(userUpdated);
		assertNotEquals(users.get(1).getName(), userUpdated.getName());
	}

	@Test
	@Order(5)
	void deleteUser() throws JsonProcessingException {
		//Arrange
		var usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
								.withBasicAuth("user", "password")
								.getForEntity("/users", String.class)
								.getBody(), User[].class));
		var deleteUrlUser1 = "/users/"+usersRetrieved.get(0).getId();
		var deleteUrlUser2 = "/users/"+usersRetrieved.get(1).getId();
		var getAllUrl = "/users/";

		//Act
		testRestTemplate
				.withBasicAuth("user", "password")
				.delete(deleteUrlUser1);
		testRestTemplate
				.withBasicAuth("user", "password")
				.delete(deleteUrlUser2);
		var userRetrievedEntity =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(getAllUrl, List.class);
		usersRetrieved  = userRetrievedEntity.getBody();

		//Assert
		assertEquals(ResponseEntity.ok().build().getStatusCode(), userRetrievedEntity.getStatusCode());
		assertTrue(usersRetrieved.isEmpty());
	}


	private List<User> prepareList(){
		List<User> list= new ArrayList<>();
		list.add(new User("user1", "test1@test.com"));
		list.add(new User("user2", "test2@test.com"));
		list.add(new User("user3", "test3@test.com"));
		return list;
	}
}
