package com.nss.usermanagement.role.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {
    @Value("${recaptcha.secret}")
    private String secretKey;
    public boolean verify(String response){
        String url="https://www.google.com/recaptcha/api/siteverify?secret="+secretKey+response;
        RestTemplate restTemplate=new RestTemplate();
       Map<String,Object> responseEntity=restTemplate.getForObject(url,Map.class);
        return responseEntity != null && (Boolean) responseEntity.get("success");
    }
}
