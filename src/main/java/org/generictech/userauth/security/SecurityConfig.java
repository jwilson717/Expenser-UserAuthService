package org.generictech.userauth.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
			.cors().and()
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
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		config.setAllowedHeaders(Arrays.asList("Accept", "Authorization", "Content-Type"
				, "Access-Control-Allow-Origin", "Access-Control-Allow-Methods", "Access-Control-Allow-Headers"
				, "Access-Control-Expose-Headers"));
		config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Access-Control-Expose-Headers"
				, "Content-Type", "Authorization", "Accept"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
}
