package com.example.jsonplaceholder;

import static com.example.jsonplaceholder.config.ApiSpecs.USERS_PATH;
import static com.example.jsonplaceholder.config.ApiSpecs.usersRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.example.jsonplaceholder.model.UserRequest;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Users CRUD")
class UsersCrudTest {

    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int EXISTING_USER_ID = 1;
    private static final int NON_EXISTING_USER_ID = 999;
    private static final int FIRST_NEW_USER_ID = 11;
    private static final String USER_BY_ID_PATH = USERS_PATH + "/{id}";

    @Test
    @DisplayName("Create: POST /users creates a user")
    void shouldCreateUser() {
        UserRequest newUser = new UserRequest("Jane Doe", "jdoe", "jane.doe@example.com");

        given()
                .spec(usersRequest())
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post(USERS_PATH)
                .then()
                .statusCode(STATUS_CREATED)
                .body("id", equalTo(FIRST_NEW_USER_ID))
                .body("name", equalTo(newUser.name()))
                .body("username", equalTo(newUser.username()))
                .body("email", equalTo(newUser.email()));
    }

    @Test
    @DisplayName("Read: GET /users/{id} returns an existing user")
    void shouldReadExistingUser() {
        given()
                .spec(usersRequest())
                .when()
                .get(USER_BY_ID_PATH, EXISTING_USER_ID)
                .then()
                .statusCode(STATUS_OK)
                .body("id", equalTo(EXISTING_USER_ID));
    }

    @Test
    @DisplayName("Read: GET /users/{id} returns 404 for a non-existing user")
    void shouldReturnNotFoundForNonExistingUser() {
        given()
                .spec(usersRequest())
                .when()
                .get(USER_BY_ID_PATH, NON_EXISTING_USER_ID)
                .then()
                .statusCode(STATUS_NOT_FOUND);
    }

    @Test
    @DisplayName("Update: PUT /users/{id} replaces a user")
    void shouldUpdateUserWithPut() {
        UserRequest updatedUser = new UserRequest("John Smith", "jsmith", "john.smith@example.com");

        given()
                .spec(usersRequest())
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .put(USER_BY_ID_PATH, EXISTING_USER_ID)
                .then()
                .statusCode(STATUS_OK)
                .body("id", equalTo(EXISTING_USER_ID))
                .body("name", equalTo(updatedUser.name()))
                .body("email", equalTo(updatedUser.email()));
    }

    @Test
    @DisplayName("Update: PATCH /users/{id} changes only the given fields")
    void shouldUpdateUserPartiallyWithPatch() {
        String newName = "Patched Name";

        given()
                .spec(usersRequest())
                .contentType(ContentType.JSON)
                .body(Map.of("name", newName))
                .when()
                .patch(USER_BY_ID_PATH, EXISTING_USER_ID)
                .then()
                .statusCode(STATUS_OK)
                .body("id", equalTo(EXISTING_USER_ID))
                .body("name", equalTo(newName));
    }

    @Test
    @DisplayName("Delete: DELETE /users/{id} removes a user")
    void shouldDeleteUser() {
        given()
                .spec(usersRequest())
                .when()
                .delete(USER_BY_ID_PATH, EXISTING_USER_ID)
                .then()
                .statusCode(STATUS_OK);
    }
}
