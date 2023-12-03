package com.ssafy.board.service;

import com.ssafy.board.mapper.MemberMapper;
import com.ssafy.board.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService  {

    private final MemberMapper memberMapper;

    @Value("${my.secret.key}")
    private String secretKey;

    private SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param member 등록할 회원 정보를 담고 있는 Member 객체입니다.
     * @return 등록된 회원을 위한 JWT 토큰을 반환합니다.
     * @throws RuntimeException 사용자 ID가 이미 존재하는 경우, 사용자 ID 또는 비밀번호가 비어있는 경우 예외를 발생시킵니다.
     */
    @Transactional
    public String signUp(Member member) {
        try {
            validateNewMember(member);
            member.setJoinDate(LocalDateTime.now());
            validateEmptyFields(member);
            String hashedPassword = passwordEncoder.encode(member.getUserPwd());
            member.setUserPwd(hashedPassword);
            memberMapper.insert(member);
            return createToken(member);
        } catch (Exception e) {
            throw new RuntimeException("회원 가입 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 주어진 회원의 사용자 ID와 비밀번호가 비어있지 않은지 검증합니다.
     *
     * @param member 검증할 회원. 이는 등록할 회원 정보를 담고 있는 Member 객체입니다.
     * @throws RuntimeException 사용자 ID, 비밀번호, 유저명이 비어있는 경우 예외를 발생시킵니다.
     */
    private void validateEmptyFields(Member member) {
        if (member.getUserId() == null || member.getUserId().trim().isEmpty()) {
            throw new RuntimeException("User ID cannot be empty");
        } else if (member.getUserPwd() == null || member.getUserPwd().trim().isEmpty()) {
            throw new RuntimeException("User Password cannot be empty");
        } else if (member.getUserName() == null || member.getUserName().trim().isEmpty()) {
            throw new RuntimeException("UserName cannot be empty");
        }
    }

    /**
     * 새로 가입하려는 회원의 유효성을 검증합니다.
     *
     * @param member 검증할 회원. 이는 가입하려는 회원의 정보를 담고 있는 객체입니다.
     * @throws RuntimeException 이미 존재하는 사용자 ID일 경우 발생합니다.
     */
    private void validateNewMember(Member member) {
        Member existingMember = memberMapper.findById(member.getUserId());
        if (existingMember != null) {
            throw new RuntimeException("User ID already exists");
        }
    }

    /**
     * 사용자 이름과 비밀번호를 이용하여 로그인합니다.
     *
     * @param userId 사용자 아이디입니다.
     * @param userPwd 사용자 비밀번호입니다.
     * @return 로그인한 회원을 위한 JWT 토큰을 반환합니다.
     * @throws RuntimeException 사용자 이름 또는 비밀번호가 유효하지 않은 경우 예외를 발생시킵니다.
     */
    public String logIn(String userId, String userPwd) {
        Member member = memberMapper.findById(userId);
        validateMember(member, userPwd);
        return createToken(member);
    }

    /**
     * 주어진 회원의 존재와 비밀번호를 검증합니다.
     *
     * @param member 검증할 회원. 이는 회원의 id를 사용하여 검색된 non-null 객체여야 합니다.
     * @param userPwd 검증할 비밀번호. 이는 사용자가 입력한 비밀번호입니다.
     * @throws RuntimeException 회원이 존재하지 않거나 비밀번호가 일치하지 않을 경우 발생합니다.
     */
    private void validateMember(Member member, String userPwd) {
        if (member == null) {
            throw new RuntimeException("User does not exist");
        } else if (!passwordEncoder.matches(userPwd, member.getUserPwd())) {
            throw new RuntimeException("Invalid password");
        }
    }


    /**
     * 회원을 위한 JWT 토큰을 생성합니다.
     *
     * @param member 토큰을 생성할 회원 정보를 담고 있는 Member 객체입니다.
     * @return 생성된 JWT 토큰을 반환합니다.
     */
    private String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSecretKeySpec())
                .compact();
    }
}
