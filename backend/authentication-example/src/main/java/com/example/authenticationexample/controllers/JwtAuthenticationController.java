package com.example.authenticationexample.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.authenticationexample.config.JwtRequestFilter;
import com.example.authenticationexample.config.JwtTokenUtil;
import com.example.authenticationexample.models.JwtRequest;
import com.example.authenticationexample.models.JwtResponse;
import com.example.authenticationexample.service.JwtUserDetailsService;

@RestController
@CrossOrigin("http:localhost:3000")
public class JwtAuthenticationController {
	private Logger log = LoggerFactory.getLogger(JwtAuthenticationController.class);
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		log.info(authenticationRequest.getPassword() + "\t" + authenticationRequest.getUsername() + "\t"
				+ authenticationRequest.getType());
		
		//authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		log.info(authenticationRequest.getUsername() + ":" + authenticationRequest.getType());
		
		String usernameAndType = authenticationRequest.getUsername() + ":" + authenticationRequest.getType();
		log.info(usernameAndType);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(usernameAndType);
		final String token = jwtTokenUtil.generateToken(userDetails, authenticationRequest.getType());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}