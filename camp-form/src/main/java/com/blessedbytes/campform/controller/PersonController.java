package com.blessedbytes.campform.controller;

//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blessedbytes.campform.Person;

@RestController
@RequestMapping("/person")

public class PersonController {

    @GetMapping("/")
    public String getAll(){
        return "bayboom";
    }

    @GetMapping("/{requestedId}")
    public Person getPerson(@PathVariable int requestedId) {
        Person person = new Person();
        person.setName("rodrigo");
        return person;
     }
}
