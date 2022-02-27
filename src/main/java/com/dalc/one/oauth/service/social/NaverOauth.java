package com.dalc.one.oauth.service.social;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.User;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.oauth.data.NaverLoginDto;
import com.dalc.one.oauth.data.NaverLoginResponse;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Component
public class NaverOauth implements SocialOauth {
	@Value("${NAVER-URL}")
    private String NAVER_SNS_BASE_URL;
    @Value("${NAVER-CLIENT}")
    private String NAVER_CLIENT_ID;
    @Value("${NAVER-CALLBACK}")
    private String NAVER_CALLBACK_URL;
    @Value("${NAVER-KEY}")
    private String NAVER_KEY;
    @Value("${ADMIN-KEY}")
    private String adminkey;
    
    private UserService userService;
	private LehgoFacade lehgo;

	@Autowired
	public void setUserDetailServiceImpl(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setFacade(LehgoFacade lehgo) {
		this.lehgo = lehgo;
	}
	
	@Override
	public String getOauthRedirectURL() {
		Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", NAVER_CLIENT_ID);
        params.put("redirect_uri", NAVER_CALLBACK_URL);
        
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        
        return NAVER_SNS_BASE_URL + "/authorize" + "?" + parameterString;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String requestAccessToken(String code) {
		final String state = new BigInteger(130, new SecureRandom()).toString();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_KEY);
        params.add("grant_type", "authorization_code");
        params.add("state", state);
        
        HttpEntity<MultiValueMap<String,Object>> naverTokenRequest = new HttpEntity<>(params,headers);
        RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<String> apiResponseJson = restTemplate.exchange(
				NAVER_SNS_BASE_URL + "/token",
				HttpMethod.POST,
				naverTokenRequest,
				String.class
				);
			
	        // ObjectMapper를 통해 String to Object로 변환
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
	        NaverLoginResponse naverLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<NaverLoginResponse>() {});
	        
	        /* 사용자 정보 가져오기 */
            // Http Header 설정
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization", "Bearer " + naverLoginResponse.getAccessToken());
    		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> naverTokenRequest2 = new HttpEntity<>(headers2);
            ResponseEntity<String> apiResponseJson2 = restTemplate.exchange(
            		"https://openapi.naver.com/v1/nid/me", 
            		HttpMethod.GET,
            		naverTokenRequest2,
            		String.class
            		);            
            String resultJson = apiResponseJson2.getBody();
            
            String token = "";
            ObjectMapper objectMapper2 = new ObjectMapper();
            NaverLoginDto profile = null;
            if(resultJson != null) {
            	try {
            	    profile = objectMapper2.readValue(resultJson, NaverLoginDto.class);
            	} catch (JsonProcessingException e) {
            	    e.printStackTrace();
            	}

            	// 사용자 정보로 Lehgo 가입
                try {
                	if(profile.getResponse().getGender() != null) {
                		if(profile.getResponse().getGender().equals("F")) {
                			profile.getResponse().setGender("0");
                    	}
                    	else if(profile.getResponse().getGender().equals("M")) {
                    		profile.getResponse().setGender("1");
                    	}
                	}
                	else {
                		profile.getResponse().setGender("");
                	}
                	
                	if(profile.getResponse().getAge() != null) {
                		profile.getResponse().setAge(profile.getResponse().getAge().substring(0,2));
                	}
                	else {
                		profile.getResponse().setAge("0");
                	}
                
                	User user = new User(profile.getResponse().getEmail(), profile.getResponse().getName(), 
                			adminkey + profile.getResponse().getEmail(), profile.getResponse().getEmail(), 
                			profile.getResponse().getName(), profile.getResponse().getGender(), 
                			profile.getResponse().getAge(), "NAVER", " ", "ROLE_USER");
                	//id가 없으면 가입
                	if (lehgo.checkUserId(user.getId()) <= 0) {
                		userService.signUpAll(user);
        			}
                	
            		token = JwtTokenProvider.makeJwtToken(userService.loadUserByUsername(user.getId()));            
                }
                catch(NullPointerException e) {
        			throw new ResponseStatusException
        			(ExceptionEnum.EXIST_ID.getStatus(), ExceptionEnum.EXIST_ID.getMessage());
        		}
                return "bearer " + token;
            }
            else {
                throw new Exception("Kakao OAuth failed!");
            }
	            
		 }
            catch (Exception e) {
            	e.printStackTrace();
            }
		return null;
	}

}
