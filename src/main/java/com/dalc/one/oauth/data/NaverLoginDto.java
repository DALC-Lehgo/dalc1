package com.dalc.one.oauth.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NaverLoginDto {
    public String resultcode;
    public String message;
    public Response response;

    @Getter
    @Setter
    @Data
    public class Response {
        public String id;
        public String nickname;
        public String name;
        public String email;
        public String gender;
        public String age;
    }

}