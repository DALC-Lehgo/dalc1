package com.dalc.one.oauth.service.social;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.User;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.oauth.data.KakaoProfile;
import com.dalc.one.oauth.data.KakaoLoginResponse;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Component
public class KakaoOauth implements SocialOauth {
	@Value("${KAKAO-URL}")
    private String KAKAO_URL;
    @Value("${KAKAO-CLIENT}")
    private String KAKAO_CLIENT_ID;
    @Value("${KAKAO-KEY}")
    private String KAKAO_CLIENT_KEY;
    @Value("${ADMIN-KEY}")
    private String adminkey;
    @Value("${KAKAO-CALLBACK}")
    private String KAKAO_CALLBACK_URL;
    
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
        params.put("client_id", KAKAO_CLIENT_ID);
        params.put("redirect_uri", KAKAO_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return KAKAO_URL + "/oauth/authorize" + "?" + parameterString;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String requestAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_KEY);
        params.add("redirect_uri", KAKAO_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String,Object>> kakaoTokenRequest = new HttpEntity<>(params,headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> apiResponseJson = restTemplate.exchange(
            		KAKAO_URL + "/oauth/token",
            		HttpMethod.POST,
            		kakaoTokenRequest,
            		String.class);
            
            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            KakaoLoginResponse kakaoLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<KakaoLoginResponse>() {});
            
            /* 사용자 정보 가져오기 */
            // Http Header 설정
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization", "Bearer " + kakaoLoginResponse.getAccessToken());
    		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest2 = new HttpEntity<>(headers2);
            ResponseEntity<String> apiResponseJson2 = restTemplate.exchange(
            		"https://kapi.kakao.com/v2/user/me", 
            		HttpMethod.POST,
            		kakaoTokenRequest2,
            		String.class
            		);            
            String resultJson = apiResponseJson2.getBody();
            String token = "";

            ObjectMapper objectMapper2 = new ObjectMapper();
            KakaoProfile profile = null;
            if(resultJson != null) {
            	try {
            	    profile = objectMapper2.readValue(resultJson, KakaoProfile.class);
            	} catch (JsonProcessingException e) {
            	    e.printStackTrace();
            	}

            	// 사용자 정보로 Lehgo 가입
                try {
                	if(profile.getKakao_account().getGender() != null) {
                		if(profile.getKakao_account().getGender().equals("female")) {
                			profile.getKakao_account().setGender("0");
                    	}
                    	else if(profile.getKakao_account().getGender().equals("male")) {
                    		profile.getKakao_account().setGender("1");
                    	}
                	}
                	else {
                		profile.getKakao_account().setGender("");
                	}
                	
                	if(profile.getKakao_account().getAge_range() != null) {
                		profile.getKakao_account().setAge_range(profile.getKakao_account().getAge_range().substring(0,2));
                	}
                	else {
                		profile.getKakao_account().setAge_range("0");
                	}
                
                	User user = new User(profile.getKakao_account().getEmail(), profile.getProperties().getNickname(), 
                			adminkey + profile.getKakao_account().getEmail(), profile.getKakao_account().getEmail(), 
                			profile.getProperties().getNickname(), profile.getKakao_account().getGender(), 
                			profile.getKakao_account().getAge_range(), "KAKAO", " ", "ROLE_USER");
                	
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
