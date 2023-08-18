package com.blessedbytes.campform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        // Preencha o objeto com os dados
        return data;
    }

	@RequestMapping(value="/", method=RequestMethod.POST)
	public String post(){
		return "post";
	}

	@RequestMapping(value="/", method=RequestMethod.DELETE)
	public String delete(){
		return "delete";
	}
}