package org.generictech.userauth.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Config class to configure web security
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private TokenAuthFilter tokenAuthFilter;
	
	public SecurityConfig(TokenAuthFilter tokenAuthFilter) {
		this.tokenAuthFilter = tokenAuthFilter;
	}
	
	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.mvcMatchers(HttpMethod.POST, "/login")
			.mvcMatchers(HttpMethod.POST, "/systemuser");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.cors().disable()
			.httpBasic().disable()
			.formLogin().disable()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers(HttpMethod.POST, "/systemuser").permitAll()
				.anyRequest().authenticated()
				.and()
			.addFilterAt(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
}
