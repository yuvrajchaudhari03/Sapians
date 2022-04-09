package com.controller;

import java.util.List;

import com.models.GiveAuthorityModel;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entities.User;
import com.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController
{

	@Autowired
	 private UserService userservice;
	 
	
	@PostMapping("/adduser")
	public User registerUser(@RequestBody User user) {
		return userservice.registerUser(user);
	
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") int id) {
		return userservice.getUserById(id);
	}

	@PostMapping("/login")
	public ResponseEntity login(){
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity(currentUser + " Logged In", HttpStatus.OK);
	}
	
	@PutMapping("/updateuser")
	public User updateUser(@RequestBody User user)
	{
		return userservice.updateUser(user);
	}//Ok

	@PostMapping("/addMoney")
	public User addMoneyToUserWallet(@RequestBody User user)
	{
		return userservice.addWalletMoney(user);
	}


	@DeleteMapping("/{u_id}")
	public Boolean deleteUser(@PathVariable int u_id)
	{
		boolean value=userservice.deleteUser(u_id);
		if(value==true)
			return true;
		else
			return false;
	}

	@GetMapping("/getalluser")
	public List<User> allUser()
	{
		return userservice.allUser();
	}//Ok

	@PostMapping("/giveAuthority")
	public ResponseEntity giveAuthority(@RequestBody GiveAuthorityModel giveAuthority){
		try {
			userservice.giveAuthority(giveAuthority);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity("Authority Given", HttpStatus.OK);
	}

	@GetMapping("/my_profile")
	public ResponseEntity getCurrentProfile(){
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity(userservice.loadUserByUsername(currentUser), HttpStatus.OK);
	}
	 
}
