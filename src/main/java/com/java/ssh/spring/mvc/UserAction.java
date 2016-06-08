package com.java.ssh.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserAction {
	
	@RequestMapping("/addUser_html")
	public String addUser_html(Model model){
		
		return "user/add-user";
	}
	
	@RequestMapping("/addUser")
	@ResponseBody
	public String addUser(
			@RequestParam String username, 
			@RequestParam String password, 
			@RequestParam Integer status
			){
		System.out.println(String.format("[%s %s %d]", username, password, status));
		return "OK";
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public String register(
			@RequestBody String data
			){
		
		return null;
	}

}
