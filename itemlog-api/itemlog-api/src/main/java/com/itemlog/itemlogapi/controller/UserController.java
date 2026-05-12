package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.entity.User;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller for user-related endpoints.
// IMPORTANT: This controller exposes all users and should not be left public
// in a deployed production API.
//@RestController
//@RequestMapping("/users")
//public class UserController {

    // Repository used to read user records from the database.
  //  private final UserRepository repo;

    // Constructor injection allows Spring to provide the user repository.
    //public UserController(UserRepository repo) {
      //  this.repo = repo;
    //}

    // Handles GET /users.
    // This returns every user in the database.
    //
    // SECURITY NOTE:
    // This endpoint should be removed, disabled, or restricted to an admin role.
    // In the current project, it was likely useful for testing,
    // but it is not suitable for a public deployed backend.
    //@GetMapping
    //public List<User> getAllUsers() {
      //  return repo.findAll();
    //}
//}
