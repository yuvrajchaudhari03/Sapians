package com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.entities.Role;
import com.models.GiveAuthorityModel;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.entities.User;
import com.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController
{

	@Autowired
	 private UserService userservice;
	 
	
	@PostMapping("/vendor/{emailOrPhone}")
	public ResponseEntity registerVendor(@PathVariable String emailOrPhone) throws Exception {
		GiveAuthorityModel giveAuthorityModel = new GiveAuthorityModel();
		giveAuthorityModel.setAuthority(Role.VENDOR);
		giveAuthorityModel.setAuthorityToAsEmail(emailOrPhone);
		userservice.giveAuthority(giveAuthorityModel);
		return ResponseEntity.ok().build();
	}

	@PostMapping
	public User registerUser(@RequestBody User user) {
		Role userRole = new Role();
		userRole.setName(Role.USER);
		userRole.setUser(user);
		user.setRoles(new ArrayList<Role>(Collections.singleton(userRole)));
		return userservice.registerUser(user);
	}

	/*TODO For below two endpoints add auth as Admin bcz, Normal user can use my_profile endpoint instead of this*/
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") int id) {
		return userservice.getUser(id);
	}


	@GetMapping("/{email}")
	public User getUser(@PathVariable("email") String email) {
		return userservice.getUser(email);
	}


	@PostMapping("/login")
	public ResponseEntity login(){
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity(currentUser + " Logged In", HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity updateCurrentUser(@RequestBody User user) {
		User updatedUser = null;
		updatedUser = userservice.updateUser(user);
		return new ResponseEntity(updatedUser, HttpStatus.OK);
	}

	@PostMapping("/addMoney/{paymentDone}")
	public User addMoneyToUserWallet(@PathVariable Integer paymentDone){
		return userservice.addWalletMoney(paymentDone);
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

	@GetMapping("/getallvendor")
	public List<User> allVendorUser()
	{
		return userservice.allVendor();
	}

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
		return new ResponseEntity(userservice.loadUserByEmailOrPhone(currentUser), HttpStatus.OK);
	}

	@PatchMapping("/approve/{u_id}/{status}")
	public User approvevendor(@PathVariable("u_id") int v_id, @PathVariable("status") Boolean v_status)
	{
		return userservice.approveVendor(v_id, v_status);
	}
}
