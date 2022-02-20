package com.dalc.one.oauth.data;

import lombok.Data;
import lombok.NoArgsConstructor;

//일회성 토큰을 받은 후, 해당 일회성 토큰을 가지고 AccessToken을 얻기 위한 Request VO
//lombok 사용

@Data
@NoArgsConstructor
public class NaverLoginResponse {
    private String accessToken; 
    private String expiresIn;   // Access Token의 남은 수명
    private String refreshToken;    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
    private String error;
    private String tokenType;
    private String errorDescription;
}