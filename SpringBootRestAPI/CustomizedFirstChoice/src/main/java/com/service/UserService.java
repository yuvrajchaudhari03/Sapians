package com.service;


import com.entities.Role;
import com.models.GiveAuthorityModel;
import com.repository.RoleRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.entities.User;
import com.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService
{
	@Autowired
	private UserRepository userrepo;

	@Autowired
	private RoleRepository roleRepo;

	//register
	public User registerUser(User user) 
	{
		// TODO Auto-generated method stub
		userrepo.save(user);
		if(user.getRoles()==null || user.getRoles().size()==0){
			Role userRole = new Role();
			userRole.setName("USER");
			userRole.setUser(user);
			user.addRole(userRole);
			roleRepo.save(userRole);
		}
		return user;
	}

	public User getUserById(int id)
	{
		// TODO Auto-generated method stub
		return userrepo.findById(id).get();
	}

	//login
	public User loginUser(User user) {
		// TODO Auto-generated method stub
		
		User user1=userrepo.findByEmail(user.getU_email(),user.getU_password());
		
		if(user1!=null && user1.getU_email().equals(user.getU_email())&& user1.getU_password().equals(user.getU_password()))
			return user1 ;
		else
			return null;
	}


	//update
	public User addWalletMoney(User user) {
		// TODO Auto-generated method stub
		User existinguser;
		existinguser=userrepo.findById(user.getU_id()).orElse(null);
		if(existinguser != null) {
			float existingWallet = existinguser.getWallet() > 0 ? existinguser.getWallet() : 0;
			existinguser.setWallet(existingWallet + user.getWallet());
		}
		return userrepo.save(existinguser);
	}



	//update
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		User existinguser;
		existinguser=userrepo.findById(user.getU_id()).orElse(null);
		if(existinguser != null) {
			existinguser.setU_fname(user.getU_fname());
			existinguser.setU_lname(user.getU_lname());
			existinguser.setU_phone(user.getU_phone());
			existinguser.setU_email(user.getU_email());
			existinguser.setU_password(user.getU_password());
			existinguser.setU_address(user.getU_address());
		}
		
		return userrepo.save(existinguser);
	}


	public boolean deleteUser(int u_id) {
		// TODO Auto-generated method stub
		
		userrepo.deleteById(u_id);
		return true;
		
	}


	public User singleUser(int u_id) {
		// TODO Auto-generated method stub
		return userrepo.findById(u_id).orElse(null);
	}


	public java.util.List<User> allUser() {
		// TODO Auto-generated method stub
		return userrepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userrepo.findByEmailAddress(email);
		if(user == null)
			throw new UsernameNotFoundException(email + " not found.");
		return new org.springframework.security.core.userdetails.User(user.getU_email(), user.getU_password(), getGrantedAuthority(user));
	}

	private Collection<GrantedAuthority> getGrantedAuthority(User user){
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for(Role role: user.getRoles()){
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	public boolean giveAuthority(GiveAuthorityModel giveAuthorityModel) throws Exception {
		User user = userrepo.getById(giveAuthorityModel.getAuthorityTo());
		for (Role role : user.getRoles())
			if(role.getName().equalsIgnoreCase(giveAuthorityModel.getAuthority()))
				throw new Exception("User already has the Authority.");

		Role role = new Role();
		role.setUser(user);
		role.setName(giveAuthorityModel.getAuthority());

		user.addRole(role);
		userrepo.save(user);
		return true;
	}

}

