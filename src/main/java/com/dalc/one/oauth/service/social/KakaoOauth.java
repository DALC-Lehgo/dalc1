package com.dalc.one.oauth.service.social;

import org.springframework.stereotype.Component;

@Component
public class KakaoOauth implements SocialOauth {

	@Override
	public String getOauthRedirectURL() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String requestAccessToken(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
