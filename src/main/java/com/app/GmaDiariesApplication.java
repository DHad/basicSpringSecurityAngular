package com.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GmaDiariesApplication {
	
	@RequestMapping("/resource")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("date", "12/12/1945");
		model.put("image", "Hello World");
		return model;
	}
	

	public static void main(String[] args) {
		SpringApplication.run(GmaDiariesApplication.class, args);
	}
}
