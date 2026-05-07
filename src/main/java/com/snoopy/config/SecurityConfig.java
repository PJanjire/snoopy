package com.snoopy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.snoopy.security.JwtFilter;
import com.snoopy.security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	private static final String[] AUTH_WHITELIST = { "/login.htm", "/signup.htm", "/register.htm","/error_***.htm",
			"/forgotpassword.htm", "/signin.htm", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
			"/swagger-resources/**", "/webjars/**", "/bower_components*/**", "/dist*/**", "/chart.js*/**", "/img*/**",
			"/js*/**", "/cypher*/**", "/encryption*/**" };

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

				.authenticationProvider(authenticationProvider())

				.authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**")
						.hasAnyRole("USER", "ADMIN").anyRequest().authenticated())

				.formLogin(form -> form.disable())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login.htm?logout=true")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID").permitAll())

				.exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> res.sendRedirect("/login.htm")))
		
		 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}
}