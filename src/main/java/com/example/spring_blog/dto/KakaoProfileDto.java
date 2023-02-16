package com.example.spring_blog.dto;

import lombok.Data;

@Data
public class KakaoProfileDto {

    public Long id;
    public String connected_at;
    public Properties properties;
    public Kakao_account kakao_account;

    @Data
    public class Properties {

        public String nickname;

    }

    @Data
    public class Kakao_account {

        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public class Profile {

            public String nickname;

        }
    }
}