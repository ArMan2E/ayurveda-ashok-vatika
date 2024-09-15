package com.ayurveda_background.ayurveda_backend.sevice;

import com.ayurveda_background.ayurveda_backend.entity.AppUser;
import com.ayurveda_background.ayurveda_backend.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=appUserRepo.findByUsername(username).get();

                //.orElseThrow(()->new UsernameNotFoundException("Username not found "));

        if(appUser!=null){
            System.out.println(appUser.getUsername());
            return User.builder()
                    .username(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles(appUser.getRoles())
                    .build();
        }
        throw new UsernameNotFoundException("Username not found");
    }
}
