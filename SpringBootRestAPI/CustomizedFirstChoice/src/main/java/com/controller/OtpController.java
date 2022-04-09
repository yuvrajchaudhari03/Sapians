package com.controller;

import com.constants.SecurityConstants;
import com.entities.User;
import com.service.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class OtpController {

    @Autowired
    public OtpService otpService;

    @Autowired
    public EmailService emailService;

    @Autowired
    public UserService userService;

    @Autowired
    public LoginService loginService;

    @GetMapping("/generateOtp/{id}")
    public ResponseEntity generateOTP(@PathVariable String id) throws MessagingException {
        String username = null;
        if(id.endsWith(".com")){
            try {
                username = userService.loadUserByUsername(id).getUsername();
            }
            catch (Exception ex){
                System.out.println("Its a new User");
            }
        }
        else{
            return new ResponseEntity("Only email verification is supported", HttpStatus.FORBIDDEN);
        }
        int otp = otpService.generateOTP(id);
        EmailTemplate template = new EmailTemplate("SendOtp.html");
        Map<String, String> replacements = new HashMap<>();
        replacements.put("user", id);
        replacements.put("otpnum", String.valueOf(otp));
        String message = template.getTemplate(replacements);

        emailService.sendOtpMessage(id, "OTP - Sapien", message);

        return new ResponseEntity("OTP Send Succesfully", HttpStatus.OK);
    }

    @RequestMapping(value ="/validateOtp/{id}", method = RequestMethod.GET)
    public ResponseEntity validateOtp(@PathVariable String id , @RequestParam("otpnum") int otpnum){
        if(otpnum >= 0){
            UserDetails userDetails = null;
            if(id.endsWith(".com")){
                try{
                    userDetails = userService.loadUserByUsername(id);
                }
                catch (Exception ex){
                    userDetails = null;
                }
            }
            else{
                return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
            }

            int serverOtp = otpService.getOtp(id);
            if(serverOtp > 0){
                if(otpnum == serverOtp){
                    otpService.clearOTP(id);
                    String jwt=null;
                    if(userDetails != null){
                        jwt= loginService.login(userDetails);
                    }
                    else{
                        User user = new User();
                        user.setU_email(id);
                        userService.registerUser(user);
                        return new ResponseEntity(user.getU_id(), HttpStatus.OK);
                    }

                    if(jwt != null)
                        return ResponseEntity.ok().header(SecurityConstants.JWT_HEADER, jwt).body("Entered OTP is valid");
                    else
                        return new ResponseEntity("Unexpected Error while generating JWT", HttpStatus.NOT_ACCEPTABLE);
                }
                else {
                    return new ResponseEntity("Entered Otp is not Valid", HttpStatus.NOT_ACCEPTABLE);
                }
            }else {
                return new ResponseEntity("Enter valid OTP", HttpStatus.NOT_ACCEPTABLE);
            }
        }else {
            return new ResponseEntity("Please Enter OTP correctly....!!!", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
