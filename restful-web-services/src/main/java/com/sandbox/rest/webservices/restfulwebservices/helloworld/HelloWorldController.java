package com.sandbox.rest.webservices.restfulwebservices.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    //GET
    //URI - /hello-world
    //method - "Hello World!"

    //@RequestMapping(method = RequestMethod.GET, path = "/hello-world")

    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
}
