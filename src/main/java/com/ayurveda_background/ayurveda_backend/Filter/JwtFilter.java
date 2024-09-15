package com.ayurveda_background.ayurveda_backend.Filter;

import com.ayurveda_background.ayurveda_backend.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizeHeader=request.getHeader("Authorization");
        String username = null,jwt=null;

        if (authorizeHeader!=null && authorizeHeader.startsWith("Bearer ")){
            jwt=authorizeHeader.substring(7);
            //extract username from the request payload jwt
            username=jwtUtil.extractUsername(jwt);
        }

        if(username!=null){
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt)){
                UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
