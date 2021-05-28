package com.example.springEssentials.Integration;

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
class UserIntegrationTest {

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
		ResponseEntity responseEntity = testRestTemplate
				.withBasicAuth("user", "password")
				.postForEntity("/users", users.get(0), User.class);
		User user = (User) responseEntity.getBody();

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
		ResponseEntity userListResponse =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity("/users", List.class);
		List<User> usersRetrieved = (List<User>) userListResponse.getBody();

		//Assert
		assertNotNull(usersRetrieved);
		assertEquals(2, usersRetrieved.size());
	}

	@Test
	@Order(3)
	void getOneUser() throws JsonProcessingException {
		//Arrange
		List<User> usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
										.withBasicAuth("user", "password")
										.getForEntity("/users", String.class)
										.getBody(), User[].class));
		String url = "/users/"+usersRetrieved.get(0).getId();

		//Act
		ResponseEntity userRetrievedResponse =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(url, User.class);
		User userRetrieved = (User) userRetrievedResponse.getBody();

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
		List<User> usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
								.withBasicAuth("user", "password")
								.getForEntity("/users", String.class)
								.getBody(), User[].class));
		String putUrl = "/users/"+usersRetrieved.get(1).getId();
		String getUrl = "/users/"+usersRetrieved.get(1).getId();
		usersRetrieved.get(1).setName("Margaret");

		//Act
		testRestTemplate
				.withBasicAuth("user", "password")
				.put(putUrl, usersRetrieved.get(1));
		ResponseEntity userRetrievedEntity =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(getUrl, User.class);
		User userUpdated = (User) userRetrievedEntity.getBody();

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
		List<User> usersRetrieved =
				Arrays.asList(objectMapper
						.readValue(testRestTemplate
								.withBasicAuth("user", "password")
								.getForEntity("/users", String.class)
								.getBody(), User[].class));
		String deleteUrlUser1 = "/users/"+usersRetrieved.get(0).getId();
		String deleteUrlUser2 = "/users/"+usersRetrieved.get(1).getId();
		String getAllUrl = "/users/";

		//Act
		testRestTemplate
				.withBasicAuth("user", "password")
				.delete(deleteUrlUser1);
		testRestTemplate
				.withBasicAuth("user", "password")
				.delete(deleteUrlUser2);
		ResponseEntity userRetrievedEntity =
				testRestTemplate
						.withBasicAuth("user", "password")
						.getForEntity(getAllUrl, List.class);
		usersRetrieved  = (List<User>) userRetrievedEntity.getBody();

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
