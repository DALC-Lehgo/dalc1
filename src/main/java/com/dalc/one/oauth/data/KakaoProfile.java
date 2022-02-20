package com.dalc.one.oauth.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KakaoProfile {
    public int id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter
    @Setter
    @Data
    public class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }

    @Getter
    @Setter
    @Data
    public class KakaoAccount {
    	public String age_range;
    	public String name;
        public Profile profile;
        public Boolean profile_nickname_needs_agreement;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public String gender;
        public Boolean has_age_range;
        public Boolean age_range_needs_agreement;
        public Boolean has_birthday;
        public Boolean birthday_needs_agreement;
        public Boolean has_gender;
        public Boolean gender_needs_agreement;

        @Data
        public class Profile {
            public String nickname;
        }
    }
}