package com.repository;

import com.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByPhoneNumber() {
        User user  = userRepository.findByPhoneNumber("8830703113");
        assertSame(user.getU_id(),2);
    }

    @Test
    void findByEmailAddress() {
        User user  = userRepository.findByEmailAddress("cchaudhari1196@gmail.com");
        assertSame(user.getU_id(),2);
    }
}