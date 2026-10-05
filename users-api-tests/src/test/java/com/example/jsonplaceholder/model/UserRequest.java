package com.example.jsonplaceholder.model;

/** Minimal payload used to create and update users. */
public record UserRequest(String name, String username, String email) {
}
