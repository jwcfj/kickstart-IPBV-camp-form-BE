package com.blessedbytes.campform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String get(){
		return "get";
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