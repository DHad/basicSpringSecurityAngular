package com.app;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@SpringBootApplication
@RestController
public class GmaDiariesApplication {
	
	@RequestMapping(value="/resource", produces = {"application/json", "image/jpeg"})
	public Map<String, Object> home(HttpServletRequest request) {
		
		
		//handle image reading, encoding, writing
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		BufferedImage image = null;
		
		try {
			InputStream is;
			is = new BufferedInputStream(
		          new FileInputStream("src/main/resources/static/images/1941Postcard.jpg"));
			image = ImageIO.read(is);
			ImageIO.write(image, "jpeg", jpegOutputStream);			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		} catch (Exception e) {
			e.getMessage();
		}

		byte[] gmaImageByte = jpegOutputStream.toByteArray();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("date", "12/12/1945");
		model.put("image", gmaImageByte);
		return model;
	}
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic()
			.and().authorizeRequests()
			.antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
			.anyRequest().authenticated().and()
			.logout()
			.and()
			.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
			.csrf().csrfTokenRepository(csrfTokenRepository())
			;
		}
		
		/**TODO
		 * Security will eventually be handled by 
		 * LoadBalancer Sticky Sessions
		 * or AWS ElasticCache
		 * These services should be used to route requests
		 * to the same JVM
		 * Also, Spring Session uses Redis, as does ElastiCache
		 */
		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = 
					new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}
	}
	
	

	public static void main(String[] args) {
		SpringApplication.run(GmaDiariesApplication.class, args);
	}
}


