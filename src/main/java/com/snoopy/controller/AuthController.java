package com.snoopy.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snoopy.dto.LoginRequest;
import com.snoopy.dto.MailResponse;
import com.snoopy.dto.RegisterRequest;
import com.snoopy.entity.Erole;
import com.snoopy.entity.Role;
import com.snoopy.entity.User;
import com.snoopy.repository.RoleRepository;
import com.snoopy.repository.UserRepository;
import com.snoopy.security.JwtUtil;
import com.snoopy.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@PostMapping("/register.htm")
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

	@RequestMapping(value = "/testmail.htm", method = RequestMethod.POST)
	public MailResponse testMail(@RequestParam String email) {

		Map<String, Object> model = new HashMap<>();

		model.put("username", "Prashant");
		model.put("message", "This is a test email from Snoopy system using FreeMarker template.");
		model.put("company", "Snoopy Application");
		model.put("year", "2026");

		return emailService.sendTestMailWithTemplate(email, model);
	}

	@PostMapping("/resetPassMail.htm")
	public MailResponse forgotPassword(@RequestParam String email) {

		User user = userRepository.findByEmail(email);

		MailResponse response = new MailResponse();

		if (user == null) {
			response.setStatus(false);
			response.setMessage("Email not registered");
			return response;
		}

		String token = UUID.randomUUID().toString();

		user.setResetToken(token);
		user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));

		userRepository.save(user);

		String resetLink = "http://localhost:9090/resetpassword.htm?token=" + token;

		Map<String, Object> model = new HashMap<>();
		model.put("username", user.getUsername());
		model.put("company", "Snoopy Application");
		model.put("year", "2026");
		model.put("link", resetLink);
		model.put("expiry", "30");

		return emailService.sendForgotPasswordMail(email, model);

	}

	@PostMapping("/updatepassword.htm")
	public Map<String, Object> updatePassword(
	        @RequestParam String password,
	        @RequestParam String token) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        User user = userRepository.findByResetToken(token);

	        if (user == null) {

	            response.put("status", false);
	            response.put("message", "Invalid reset link");

	            return response;
	        }

	        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

	            response.put("status", false);
	            response.put("message", "Reset link expired");

	            return response;
	        }

	        String encryptedPassword = passwordEncoder.encode(password);

	        user.setPassword(encryptedPassword);

	        user.setResetToken(null);
	        user.setResetTokenExpiry(null);

	        userRepository.save(user);

	        response.put("status", true);
	        response.put("message", "Password updated successfully");

	    } catch (Exception e) {

	        e.printStackTrace();

	        response.put("status", false);
	        response.put("message", "Something went wrong");
	    }

	    return response;
	}
}