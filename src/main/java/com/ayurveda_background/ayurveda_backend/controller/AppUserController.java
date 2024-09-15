package com.ayurveda_background.ayurveda_backend.controller;

import com.ayurveda_background.ayurveda_backend.Util.JwtUtil;
import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.sevice.AppUserService;
import com.ayurveda_background.ayurveda_backend.sevice.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/user")
public class AppUserController {

    @Autowired
    AppUserService appUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;


/*
*
    *   Signup JSON: Provides only the essential information needed to create a user.
        Update JSON: Includes a list of notes or other
    * references that can be added to the user after the initial creation.
*
* */


    /**
     *
     * ALWAYS SAVE ROLE AS
     * USER NEVER ROLE_USER OR ROLE_SOMETHING
     * */



    @PostMapping("/signup")
    public ResponseEntity<AppUser> signUp(@RequestBody AppUser appUser){
        //newUser inserted with updated data

        ResponseEntity<AppUser> saveUser= appUserService.insertNewUser(appUser);
        if(saveUser.getStatusCode().isError()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(appUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saveUser.getBody());
    }

    @GetMapping("/users")
    public List<AppUser> getAllUser(){
        return appUserService.getAllUsers();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody AppUser appUser){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        AppUser oldUser=appUserService.findByUserName(username);
        if(oldUser !=null){
            // Update only the fields that are provided in the request body
            if (appUser.getUsername() != null && !appUser.getUsername().isEmpty()) {
                oldUser.setUsername(appUser.getUsername());
            }

            if (appUser.getPassword() != null && !appUser.getPassword().isEmpty()) {
                oldUser.setPassword(appUser.getPassword()); // Consider encoding password here
            }

            if (appUser.getEmail() != null && !appUser.getEmail().isEmpty()) {
                oldUser.setEmail(appUser.getEmail());
            }
            //encoding pass and username to update user
            appUserService.insertUser(oldUser);
        return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody AppUser appUser){

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword())
            );
            UserDetails userDetails= userDetailsService.loadUserByUsername(appUser.getUsername());
            String jwt=jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error("Exception occurred while createAuthToken ",e);
            return new ResponseEntity<>("Incorrect Username or password",HttpStatus.BAD_REQUEST);        }
    }
}

