package com.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.entities.User;

import java.util.List;

@Transactional

@Repository
public interface UserRepository extends JpaRepository<User, Integer> 
{
	@Query(value="select * from User  WHERE u_email=? AND u_password=?",nativeQuery = true)
	User authenticateUser(String u_email, String u_password);

	@Query(value="select * from User  WHERE u_email=?",nativeQuery = true)
    User findByEmailAddress(String email);

	@Query(value="select * from User u INNER JOIN Role r ON u.u_id=r.u_id WHERE r.name='VENDOR'",nativeQuery = true)
	List<User> findAllVendor();

	@Query(value="select * from User  WHERE u_phone	=?",nativeQuery = true)
	User findByPhoneNumber(String phone);
}
