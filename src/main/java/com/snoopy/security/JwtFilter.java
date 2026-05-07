package com.snoopy.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String token = null;
	    String username = null;

	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);
	    }

	    if (token == null) {
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            token = (String) session.getAttribute("JWT_TOKEN");
	        }
	    }

	    // ✅ If token found, validate it
	    if (token != null) {
	        try {
	            username = jwtUtil.extractUsername(token);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	                if (jwtUtil.isTokenValid(token, userDetails)) {
	                    UsernamePasswordAuthenticationToken authToken =
	                            new UsernamePasswordAuthenticationToken(
	                                    userDetails, null, userDetails.getAuthorities());
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                } else {
	                	HttpSession expiredSession = request.getSession(false);
	                	if (expiredSession != null) {
	                	    try {
	                	        expiredSession.invalidate();
	                	    } catch (IllegalStateException e) {
	                	    }
	                	}
	                	response.sendRedirect("/login.htm");
	                	return;
	                }
	            }

	        } catch (Exception e) {
	        	 HttpSession invalidSession = request.getSession(false);
	        	    if (invalidSession != null) {
	        	        try {
	        	            invalidSession.invalidate();
	        	        } catch (IllegalStateException ex) {
	        	        }
	        	    }
	        	    response.sendRedirect("/login.htm");
	        	    return;
	        }
	    }

	    filterChain.doFilter(request, response);
	}
}