package com.sandbox.rest.webservices.restfulwebservices.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }


    @GetMapping("/users/{id}")
    @ApiOperation(value = "Finds Users by id",
            notes = "Also returns a link to retrieve all users with rel - all-users")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        final User user = service.findOne(id);
        if(user == null){
            throw new UserNotFoundException("Id-"+id);
        }

        EntityModel<User> model = new EntityModel<>(user);

        Link findOneLink = linkTo(methodOn(this.getClass()).retrieveUser(id)).withSelfRel();
        Link linkToAll = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");

        model.add(findOneLink).add(linkToAll);
        return model;
    }

    //input : user name, birthday json
    //output : CREATED
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
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
