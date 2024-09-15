package com.ayurveda_background.ayurveda_backend.config;

import com.ayurveda_background.ayurveda_backend.Filter.JwtFilter;
import com.ayurveda_background.ayurveda_backend.Util.JwtUtil;
import com.ayurveda_background.ayurveda_backend.sevice.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.naming.AuthenticationException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //cors takes a customizer c,c.configurationSource(...) takes parameter the created bean
        //corsConfigurationSource()
        httpSecurity.cors(c->c.configurationSource(corsConfigurationSource()));
        httpSecurity
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/user/note/**").authenticated()
                        .requestMatchers("/user/bookmark/**").authenticated()
                        .requestMatchers("/plants/**").permitAll()
                        .requestMatchers("/user/signup").permitAll())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(Customizer.withDefaults());

        //note necessary if a bean of AuthenticationProvider with
        //implementation created already
        //use daoAuthprovider
        //httpSecurity.authenticationProvider(authProvider());
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  // Strength 12
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
    }

    //
    /*
    * By providing this bean, you're telling Spring Security to use DaoAuthenticationProvider as the primary mechanism for
    * authenticating users. This means Spring now knows how to find user details (via UserDetailsService) and verify
    *  passwords (via PasswordEncoder).
    * */
    //VERY IMPORTANT TO PROVIDE A AUTH PROVIDER
    @Bean
    public AuthenticationProvider authProvider(){
        var  daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return daoAuthenticationProvider;
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
        //Header is imp it is wildcard for now
        corsConfig.setAllowedHeaders(List.of("*"));
        //corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);


        return source;
    }
}
