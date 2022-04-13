package com.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity

public class User {

	@Id
	@GeneratedValue
	private int u_id;
	
	private String u_fname;
	private String u_lname;
	private String u_phone;
	private String u_address;
	private String u_email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String u_password;
	private float wallet=2000f;
	private boolean status=false;

	/*This is Bidirectional Approch: i.e. In User, will have filed named roles. And in Role, we will have field named User */
	/*mappedBy - Tell hibernate which table ownes the relationship    */
//	@JsonIgnore
	@OneToMany(mappedBy = "user",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<>();

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	


	public User(int u_id, String u_fname, String u_lname, String u_phone, String u_address, String u_email,
			String u_password, float wallet) {
		super();
		this.u_id = u_id;
		this.u_fname = u_fname;
		this.u_lname = u_lname;
		this.u_phone = u_phone;
		this.u_address = u_address;
		this.u_email = u_email;
		this.u_password = u_password;
		this.wallet = wallet;
	}


	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getU_phone() {
		return u_phone;
	}

	public void setU_phone(String u_phone) {
		this.u_phone = u_phone;
	}

	public String getU_address() {
		return u_address;
	}

	public void setU_address(String u_address) {
		this.u_address = u_address;
	}

	public String getU_email() {
		return u_email;
	}

	public void setU_email(String u_email) {
		this.u_email = u_email;
	}

	public String getU_password() {
		return u_password;
	}

	public void setU_password(String u_password) {
		this.u_password = new BCryptPasswordEncoder().encode(u_password);
	}

	@Override
	public String toString() {
		return "User [u_id=" + u_id + ", u_fname=" + u_fname + ", u_lname=" + u_lname + ", u_phone=" + u_phone
				+ ", u_address=" + u_address + ", u_email=" + u_email + ", u_password=" + u_password + ", wallet="
				+ wallet + "]";
	}

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}



	public String getU_lname() {
		return u_lname;
	}



	public void setU_lname(String u_lname) {
		this.u_lname = u_lname;
	}



	public String getU_fname() {
		return u_fname;
	}



	public void setU_fname(String u_fname) {
		this.u_fname = u_fname;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if(this.roles == null)
			this.roles = new ArrayList<>();
		this.roles.add(role);
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
