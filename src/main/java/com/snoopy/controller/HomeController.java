package com.snoopy.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.snoopy.entity.User;
import com.snoopy.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("login.htm")
	public String login(HttpServletRequest request, HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.clearContext();
		return "login";
	}
	
	@GetMapping("logout.htm")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		SecurityContextHolder.clearContext();

		return "redirect:/login.htm";
	}

	@GetMapping("forgotpassword.htm")
	public String forgotpassword() {
		return "forgot-password";
	}

	@GetMapping("signup.htm")
	public String signup() {
		return "signup";
	}

	@GetMapping("error_400.htm")
	public String error_400() {
		return "error_400";
	}

	@GetMapping("error_401.htm")
	public String error_401() {
		return "error_401";
	}

	@GetMapping("error_404.htm")
	public String error_404() {
		return "error_404";
	}

	@GetMapping("error_500.htm")
	public String error_500() {
		return "error_500";
	}

	@GetMapping("resetpassword.htm")
	public String resetpassword(@RequestParam String token, Model model) {
		
		 User user = userRepository.findByResetToken(token);

		    // Invalid token
		    if (user == null) {
		        return "error_404";
		    }

		    // Token expired
		    if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
		        return "error_401";
		    }

		    // Pass token to HTML page
		    model.addAttribute("token", token);
		return "reset-password";
	}

	// Common method used to load pages.
	private ModelAndView getPage(String pageName, Authentication authentication, HttpServletRequest request,
			Model model) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return new ModelAndView("login");
		}

		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("username") != null) {
			model.addAttribute("username", session.getAttribute("username"));
		} else {
			model.addAttribute("username", "User");
		}

		return new ModelAndView(pageName);
	}

	// Profile page
	@RequestMapping(value = "profile.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView profile(Authentication authentication, HttpServletRequest request, Model model) {

		return getPage("profile", authentication, request, model);
	}

	// Dashboard page
	@RequestMapping(value = "dashboard.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard(Authentication authentication, HttpServletRequest request, Model model) {

		return getPage("dashboard", authentication, request, model);
	}

}