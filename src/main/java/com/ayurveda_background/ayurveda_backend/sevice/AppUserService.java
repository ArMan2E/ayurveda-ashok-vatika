package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepo appUserRepo;

    //encoding the user data before saving to Database
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    //save and encode password
    public ResponseEntity<AppUser> insertNewUser(AppUser appUser){

        Optional<AppUser> checkUser=appUserRepo.findByUsername(appUser.getUsername());
        if(checkUser.isEmpty()){
            appUser.setRoles("USER");
            appUser.setPassword(encoder.encode(appUser.getPassword()));
            appUserRepo.insert(appUser);
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(appUser, HttpStatus.CONFLICT);
    }
    //only save the user not encode password
    public void insertUser(AppUser appUser){
        appUserRepo.save(appUser);
    }

// findByusername as it 'username' is same in repository not userName of UserName
    public AppUser findByUserName(String username){
        return appUserRepo.findByUsername(username).get();
    }


    public List<AppUser> getAllUsers() {
        return appUserRepo.findAll();
    }

}
