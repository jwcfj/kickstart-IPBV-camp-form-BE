package com.blessedbytes.campform;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.blessedbytes.campform.models.Person;

@Controller
public class IndexController {

	/*@RequestMapping(value="/", method=RequestMethod.GET)
	public String get(){
		//codigo que acessa csv
		return "get";
	}*/

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Person get() {
        Person data = new Person();
        return data;
    }

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String post(@RequestBody Person jsonRequest) throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(jsonRequest);
        System.out.println(json);
		return "post";
	}

	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable String id){

		System.out.println("ID recebido: " + id);

		return "delete";
	}
}