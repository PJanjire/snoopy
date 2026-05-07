package com.snoopy.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snoopy.dto.LoginRequest;
import com.snoopy.dto.RegisterRequest;
import com.snoopy.entity.Erole;
import com.snoopy.entity.Role;
import com.snoopy.entity.User;
import com.snoopy.repository.RoleRepository;
import com.snoopy.repository.UserRepository;
import com.snoopy.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register.htm")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

		Map<String, Object> response = new HashMap<>();
		
		if (userRepository.existsByUsername(request.getUsername())) {
			response.put("status", "error");
			response.put("message", "Username already exists");
			return ResponseEntity.badRequest().body(response);
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			response.put("status", "error");
			response.put("message", "Email already exists");
			return ResponseEntity.badRequest().body(response);
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setMobilenumber(request.getMobilenumber());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setPasswordstatus(1);

		Role role = roleRepository.findByName(Erole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Role not found"));

		Set<Role> roles = new HashSet<>();
		roles.add(role);

		user.setRoles(roles);

		userRepository.save(user);

		response.put("status", "success");
		response.put("message", "User registered successfully");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/signin.htm")
	@ResponseBody
	public Map<String, Object> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
		Map<String, Object> response = new HashMap<>();
				
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			
			String token = jwtUtil.generateToken(userDetails);
			
			HttpSession session = httpRequest.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			session.setAttribute("username", request.getUsername());
			session.setAttribute("JWT_TOKEN", token);
				
			
			response.put("status", "success");
			response.put("goto", "dashboard.htm");
			response.put("username", request.getUsername());
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Invalid username or password");
		}
		return response;
	}

}