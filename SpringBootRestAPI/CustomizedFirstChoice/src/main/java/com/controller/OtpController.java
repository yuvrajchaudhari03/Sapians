package com.controller;

import com.constants.SecurityConstants;
import com.entities.Role;
import com.entities.User;
import com.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@Controller
public class OtpController {

    @Autowired
    public GenerateOTPService generateOTPService;

    @Autowired
    public SendMessageService sendEmailMessageService;

    @Autowired
    public SendMessageService sendTextMessageService;

    @Autowired
    public UserService userService;

    @Autowired
    public LoginService loginService;

    @GetMapping("/generateOtp/{id}")
    public ResponseEntity generateOTP(@PathVariable String id) throws MessagingException, IOException {
        String username = null;
        try {
            username = userService.loadUserByUsername(id).getUsername();
        }
        catch (Exception ex){
            System.out.println("Its a new User");
        }

        int otp = generateOTPService.generateOTP(id);
        EmailTemplate template = new EmailTemplate("SendOtp.html");
        Map<String, String> replacements = new HashMap<>();
        replacements.put("user", id);
        replacements.put("otpnum", String.valueOf(otp));
        String message = template.getTemplate(replacements);

        if(StringUtils.containsIgnoreCase(id,".co")){
            sendEmailMessageService.sendOtpMessage(id, "OTP - Sapien", message);
        }
        else {
            sendTextMessageService.sendOtpMessage(id, "OTP - Sapien", message);
        }

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

            int serverOtp = generateOTPService.getOtp(id);
            if(serverOtp > 0){
                if(otpnum == serverOtp){
                    generateOTPService.clearOTP(id);
                    String jwt=null;
                    if(userDetails != null){
                        jwt= loginService.login(userDetails);
                    }
                    else{
                        User user = new User();
                        user.setU_email(id);
                        Role userRole = new Role();
                        userRole.setName(Role.USER);
                        userRole.setUser(user);
                        user.setRoles(new ArrayList<Role>(Collections.singleton(userRole)));
                        userService.registerUser(user);

                        jwt= loginService.loginWithoutPassword(id, user.getRoles());
                        return ResponseEntity.ok().header(SecurityConstants.JWT_HEADER, jwt).body(user.getU_id());
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
