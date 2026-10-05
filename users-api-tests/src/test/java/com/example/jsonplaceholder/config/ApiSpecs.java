package com.example.jsonplaceholder.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Shared request configuration.
 * A new specification is built on every call and no global RestAssured state is modified,
 * so tests that use it are safe to run in parallel.
 */
public final class ApiSpecs {

    public static final String BASE_URI = "https://jsonplaceholder.typicode.com";
    public static final String USERS_PATH = "/users";

    private ApiSpecs() {
    }

    public static RequestSpecification usersRequest() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setAccept(ContentType.JSON)
                .log(LogDetail.URI)
                .build();
    }
}
