package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableOAuth2Client
@RestController
public class ProductServiceController {
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	
	@Autowired
	OAuth2ProtectedResourceDetails details;
	
	@Bean
	@ConfigurationProperties("github.client")
	public AuthorizationCodeResourceDetails githubClient() {
		return new AuthorizationCodeResourceDetails();
	}

	
   private static Map<String, Product> productRepo = new HashMap<>();   
   static {      
      Product honey = new Product();
      honey.setId("1");
      honey.setName("Honey");
      productRepo.put(honey.getId(), honey);      
      Product almond = new Product();
      almond.setId("2");
      almond.setName("Almond");
      productRepo.put(almond.getId(), almond);      
   }
   
   @RequestMapping(value = "/products")
   public ResponseEntity<Object> getProduct() {
	   
	  	
	OAuth2RestTemplate rest = this.oauth2RestTemplate(oauth2ClientContext, details);
	  
      return new ResponseEntity<>(productRepo.values(), HttpStatus.OK);
   }
   
   @Bean
   public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
           OAuth2ProtectedResourceDetails details) {
       return new OAuth2RestTemplate(details, oauth2ClientContext);
   }
}