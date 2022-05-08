package com.service;


import com.entities.Role;
import com.models.GiveAuthorityModel;
import com.repository.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.entities.User;
import com.repository.UserRepository;

import javax.security.auth.login.LoginException;
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
		userrepo.save(user);
		return user;
	}

	public User loginUser(User user) {
		User user1=userrepo.authenticateUser(user.getU_email(),user.getU_password());
		
		if(user1!=null && user1.getU_email().equals(user.getU_email())&& user1.getU_password().equals(user.getU_password()))
			return user1 ;
		else
			return null;
	}

	public User addWalletMoney(User user) {
		User existinguser;
		existinguser=userrepo.findById(user.getU_id()).orElse(null);
		if(existinguser != null) {
			float existingWallet = existinguser.getWallet() > 0 ? existinguser.getWallet() : 0;
			existinguser.setWallet(existingWallet + user.getWallet());
		}
		return userrepo.save(existinguser);
	}
	public User addWalletMoney(Integer paymentDone) {
		String existinguserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User existinguser=userrepo.findByEmailAddress(existinguserEmail);

		if(existinguser != null) {
			float existingWallet = existinguser.getWallet() > 0 ? existinguser.getWallet() : 0;
			existinguser.setWallet(existingWallet + paymentDone);
			return userrepo.save(existinguser);
		}
		return null;
	}


	public boolean deleteUser(int u_id) {
		userrepo.deleteById(u_id);
		return true;
		
	}

	public User getUser(String email) {
		// TODO Auto-generated method stub
		return userrepo.findByEmailAddress(email);
	}

	public User updateUser(User user) throws UsernameNotFoundException {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		if(currentUserEmail == null){
			throw new UsernameNotFoundException("Please Login first...!!!");
		}
		User existing  = this.getUser(currentUserEmail);

		if(user.getU_address() != null)
			existing.setU_address(user.getU_address());
		if(user.getU_fname() != null)
			existing.setU_fname(user.getU_fname());
		if(user.getU_lname() != null)
			existing.setU_lname(user.getU_lname());
		if(user.getU_phone() != null)
			existing.setU_phone(user.getU_phone());
		if(user.getU_password() != null)
			existing.setU_password(user.getU_password());

		return this.userrepo.save(existing);
	}

	public User getUser(int u_id) {
		// TODO Auto-generated method stub
		return userrepo.findById(u_id).orElse(null);
	}


	public java.util.List<User> allUser() {
		// TODO Auto-generated method stub
		return userrepo.findAll();
	}
	public java.util.List<User> allVendor() {
		// TODO Auto-generated method stub
		return userrepo.findAllVendor();
	}


	public User loadUserByEmailOrPhone(String emailOrPhone){
		User user = null;
		if(StringUtils.containsIgnoreCase(emailOrPhone,".co"))
			user = userrepo.findByEmailAddress(emailOrPhone);
		else
			user = userrepo.findByPhoneNumber(emailOrPhone);
		if(user == null)
			throw new UsernameNotFoundException(emailOrPhone + " not found.");
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = null;
		if(StringUtils.containsIgnoreCase(email,".co"))
			user = userrepo.findByEmailAddress(email);
		else
			user = userrepo.findByPhoneNumber(email);
		if(user == null)
			throw new UsernameNotFoundException(email + " not found.");
		return user;
//		return new User(user.getU_fname(), user.getU_lname(), user.getU_phone(), user.getU_address(),
//				user.getU_email(), user.getU_password(), user.getWallet(), getGrantedAuthority(user));
	}

	private Collection<GrantedAuthority> getGrantedAuthority(User user){
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for(Role role: user.getRoles()){
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	public boolean giveAuthority(GiveAuthorityModel giveAuthorityModel) throws Exception {
		User user = null;
		if(StringUtils.isNotEmpty(giveAuthorityModel.getAuthorityToAsEmail()))
			user = userrepo.findByEmailAddress(giveAuthorityModel.getAuthorityToAsEmail());
		else
			user = userrepo.getById(giveAuthorityModel.getAuthorityTo());

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

	public User approveVendor(int v_id, Boolean v_status) {
		User user = userrepo.findById(v_id).orElse(null);
		if(user!= null){
			user.setStatus(v_status);
			return userrepo.save(user);
		}
		return null;
	}


}

