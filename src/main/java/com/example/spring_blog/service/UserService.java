package com.example.spring_blog.service;

import com.example.spring_blog.dto.KakaoProfileDto;
import com.example.spring_blog.dto.OAuthTokenDto;
import com.example.spring_blog.model.RoleType;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//스프링이 컴포넌트 스캔을 통해 빈에 등록
@Service
public class UserService {

    @Value("${values.key}")
    private String secret_server_pwd;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired //DI
    private UserRepository userRepository;

    @Autowired //의존성 주입
    private BCryptPasswordEncoder encode;

    @Transactional
    public void join(User user) {
//        userRepository.findByUsername(user.getUsername()).orElseThrow(()-> {
//            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
//        });
        String rawPassword = user.getPassword();
        String encPassword = encode.encode(rawPassword); //pw 해시화
        user.setRole(RoleType.USER);
        user.setPassword(encPassword);
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        //수정시 영속성 컨텍스트 user 오브젝트를 영속화시키고, 영속화된 user 오브젝트 수정
        //더티 체킹을 이용하는것.
        User persistance = userRepository.findById(user.getId()).orElseThrow(()-> {
            return new IllegalArgumentException("회원 찾기 실패");
        });

        //validation - oauth값이 있으면 pwd랑 email수정 불가 (post공격 방지)
        if(persistance.getOauth()==null || persistance.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encode.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }
        //update 함수 종료시 자동 트랜잭션 커밋
    }

    @Transactional
    public User findMember(String username) {
        //회원이 없을 경우 빈 객체를 리턴
        return userRepository.findByUsername(username).orElseGet(()->new User());
    }

//    @Transactional(readOnly = true) //트랜잭션의 정합성 유지
//    public User login(User user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//    }

    //POST방식으로 key=value 데이터를 요청(카카오쪽으로)
    //->Retrofit2, OkHttp, RestTemplate 등 사용가능
    @Transactional
    public OAuthTokenDto requestKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        //헤더정보 추가를 위한 HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody에 key-value값 추가
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "f6c79072a0eed466ef0cead34c306912");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //오브젝트를 담아 Http POST로 요청하기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", //요청 url
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class //응답받을 타입
        );

        //json으로 오는 응답 받기
        //->Gson, Json Simple, ObjectMapper등 사용가능
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthTokenDto oauthToken;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthTokenDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return oauthToken;
    }

    @Transactional
    public KakaoProfileDto requestKakaoUserProfile(OAuthTokenDto oauthToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfileDto kakaoProfile;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfileDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoProfile;
    }

    @Transactional
    public void setKakaoAutoLogin(KakaoProfileDto kakaoProfile) {
        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .email(kakaoProfile.getKakao_account().getEmail())
                .password(secret_server_pwd)
                .oauth("kakao")
                .build();

        //User 엔티티: username, password, email
        System.out.println("블로그서버 username: "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그서버 email: "+kakaoProfile.getKakao_account().getEmail());
        System.out.println("블로그서버 password: "+ secret_server_pwd);

        User originUser = findMember(kakaoUser.getUsername());

        //가입되지 않은 경우
        if(originUser.getUsername()==null) {
            System.out.println("기존 회원이 아니기에 자동 회원가입 후 로그인을 진행합니다.");
            join(kakaoUser);
        }
        //이미 가입된 경우 로그인처리
        else {
            System.out.println("기존 회원이므로 자동 로그인을 진행합니다. ");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), secret_server_pwd));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
