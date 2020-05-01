package com.techie.ssjwt.controller;

import com.techie.ssjwt.model.AuthenticationRequest;
import com.techie.ssjwt.model.UserInfo;
import com.techie.ssjwt.service.JwtUserDetailsService;
import com.techie.ssjwt.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtAuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @GetMapping("/helloUser/{username}")
    public String helloUser(@PathVariable("username") String userName) {
        return "Hello " + userName + " to JWT World!";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        //After succesfull authentication call userdetails service by passing userName
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtUtil.generateToken(userDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.add("AuthorizationToken", token);
        return new ResponseEntity("Authentication Successfull", headers, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody UserInfo userInfo) {
        UserInfo userInfoFromDB = userDetailsService.saveUser(userInfo);
        return ResponseEntity.ok("User "+userInfoFromDB.getUsername()+" has registered successfully");
    }

}
