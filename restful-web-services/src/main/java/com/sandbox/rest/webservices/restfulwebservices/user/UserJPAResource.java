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
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJPAResource {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/jpa/users/{id}")
    @ApiOperation(value = "Finds Users by id",
            notes = "Also returns a link to retrieve all users with rel - all-users")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        final Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            throw new UserNotFoundException("Id-"+id);

        EntityModel<User> model = new EntityModel<>(user.get());

        Link findOneLink = linkTo(methodOn(this.getClass()).retrieveUser(id)).withSelfRel();
        Link linkToAll = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");

        model.add(findOneLink).add(linkToAll);
        return model;
    }

    //input : user name, birthday json
    //output : CREATED
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        //  /user/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/jpa/users/{id}")
    public void deleteById(@PathVariable int id) {
        userRepository.deleteById(id);
    }


    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrieveAllUsers(@PathVariable int id) {
        final Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent())
            throw new UserNotFoundException("Id-"+id);

        return optionalUser.get().getPosts();
    }
}
