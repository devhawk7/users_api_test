package com.example.jsonplaceholder;

import static com.example.jsonplaceholder.config.ApiSpecs.USERS_PATH;
import static com.example.jsonplaceholder.config.ApiSpecs.usersRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.response.ValidatableResponse;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GET /users")
class UsersGetTest {

    private static final int STATUS_OK = 200;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String EXPECTED_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final int EXPECTED_USERS_COUNT = 10;

    @Test
    @DisplayName("responds with status code 200 OK")
    void shouldReturnStatusCodeOk() {
        getUsers().statusCode(STATUS_OK);
    }

    @Test
    @DisplayName("responds with the expected Content-Type header")
    void shouldReturnJsonContentTypeHeader() {
        getUsers()
                .header(CONTENT_TYPE, notNullValue())
                .header(CONTENT_TYPE, equalTo(EXPECTED_CONTENT_TYPE));
    }

    @Test
    @DisplayName("responds with an array of 10 users")
    void shouldReturnArrayOfTenUsers() {
        Integer[] expectedIds = IntStream.rangeClosed(1, EXPECTED_USERS_COUNT).boxed().toArray(Integer[]::new);

        getUsers()
                .body("$", hasSize(EXPECTED_USERS_COUNT))
                .body("id", containsInAnyOrder(expectedIds))
                .body("name", everyItem(notNullValue()))
                .body("email", everyItem(notNullValue()));
    }

    private ValidatableResponse getUsers() {
        return given()
                .spec(usersRequest())
                .when()
                .get(USERS_PATH)
                .then();
    }
}
