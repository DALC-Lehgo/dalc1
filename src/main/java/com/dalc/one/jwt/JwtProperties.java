package com.dalc.one.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private final static String issuer = "lehgo";
	private final static String secretKey = "DalcLehgo";
	private final static String tokenPrefix = "bearer ";

	public static String getIssuer() {
		return issuer;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static String getTokenPrefix() {
		return tokenPrefix;
	}
}