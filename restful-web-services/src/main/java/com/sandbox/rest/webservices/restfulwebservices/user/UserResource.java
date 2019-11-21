package com.sandbox.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }


    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        final User user = service.findOne(id);
        if(user == null){
            throw new UserNotFoundException("Id-"+id);
        }
        return user;
    }

    //input : user name, birthday json
    //output : CREATED
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = service.save(user);

        //  /user/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/users/{id}")
    public void deleteById(@PathVariable int id) {
        final User user = service.deleteById(id);

        if(user == null){
            throw new UserNotFoundException("Id-"+id);
        }
    }
}
