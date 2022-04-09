package com.repository;

import com.entities.Role;
import com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
