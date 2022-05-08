package com.constants;

import com.entities.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Utilities {

    @Autowired
    private UserRepository userRepository;

    public Integer getCurrentUserId(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailAddress(email);
        return user.getU_id();
    }
}
