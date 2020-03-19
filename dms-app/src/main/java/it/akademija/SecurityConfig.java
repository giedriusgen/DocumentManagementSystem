package it.akademija;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import it.akademija.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityEntryPoint securityEntryPoint;
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http.authorizeRequests()
				// UI without security
				.antMatchers("/", "/swagger-ui.html", "/login*").permitAll()
				// all /api/ are safe, accessed only when user is logged in
				.antMatchers( "/api/**").authenticated().and().formLogin().loginPage("/login").permitAll()
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						response.setHeader("Access-Control-Allow-Credentials", "true");
						response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
						response.setHeader("Content-Type", "application/json;charset=UTF-8");
						response.getWriter().print("{\"isAdmin\": \"" + userService
								.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).isAdmin()
								 + "\"}");
					}
				})
				.failureHandler(new SimpleUrlAuthenticationFailureHandler()).loginPage("/login").permitAll().and()
				.logout().permitAll()
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						response.setHeader("Access-Control-Allow-Credentials", "true");
						response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
						response.setHeader("Content-Type", "application/json;charset=UTF-8");
					}
				}).and().csrf().disable() 
				.exceptionHandling().authenticationEntryPoint(securityEntryPoint).and().headers().frameOptions()
				.disable(); 

	}
}
