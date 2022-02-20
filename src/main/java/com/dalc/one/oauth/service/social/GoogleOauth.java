package com.dalc.one.oauth.service.social;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.dalc.one.ExceptionEnum;
import com.dalc.one.domain.User;
import com.dalc.one.jwt.JwtTokenProvider;
import com.dalc.one.oauth.data.GoogleLoginDto;
import com.dalc.one.oauth.data.GoogleLoginRequest;
import com.dalc.one.oauth.data.GoogleLoginResponse;
import com.dalc.one.service.LehgoFacade;
import com.dalc.one.user.UserService;

@Component
public class GoogleOauth implements SocialOauth {
	@Value("${GOOGLE-URL}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${GOOGLE-CLIENT}")
    private String GOOGLE_CLIENT_ID;
    @Value("${GOOGLE-CALLBACK}")
    private String GOOGLE_CALLBACK_URL;
    @Value("${GOOGLE-KEY}")
    private String GOOGLE_KEY;
    private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://oauth2.googleapis.com";
    @Value("${GOOGLE-SCOPE}")
    private String scopes;
    @Value("${ADMIN-KEY}")
    private String adminkey;

    public String getScopeUrl() {
      return scopes.replaceAll(",", "%20");
    }
    
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
        params.put("scope", getScopeUrl());
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

	@SuppressWarnings("deprecation")
	@Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
		GoogleLoginRequest requestParams = GoogleLoginRequest.builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_KEY)
                .code(code)
                .redirectUri(GOOGLE_CALLBACK_URL)
                .grantType("authorization_code")
                .build();

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL + "/token", httpRequestEntity, String.class);
            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {});

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_SNS_TOKEN_BASE_URL + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();
            String resultJson = restTemplate.getForObject(requestUrl, String.class);
            String token = "";
            if(resultJson != null) {
                GoogleLoginDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});
                
                // 사용자 정보로 Lehgo 가입
                try {
                	User user = new User(userInfoDto.getEmail(), userInfoDto.getName(), adminkey + userInfoDto.getEmail(), userInfoDto.getEmail(), 
	                		userInfoDto.getName(), "0", "20", "GOOGLE", "", "ROLE_USER");
                	
                	//id가 없으면 가입
                	if (lehgo.checkUserId(user.getId()) <= 0) {
                		userService.signUp(user);
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
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e) {
        }
        return null;
	}
}
