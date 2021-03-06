package com.dalc.one.oauth.helper.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.dalc.one.oauth.constants.SocialLoginType;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    @Override
    public SocialLoginType convert(String s) {
        return SocialLoginType.valueOf(s.toUpperCase());
    }
}