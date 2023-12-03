package com.ssafy.board.service;

import com.ssafy.board.mapper.MemberMapper;
import com.ssafy.board.model.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setUserId("testUser");
        member.setUserPwd("testPassword");
        member.setUserName("Test User");
    }

    @AfterEach
    public void tearDown() {
        memberMapper.delete(member.getUserId());
    }

    @Test
    public void signUpTest() {
        // given
        String userId = member.getUserId();

        // when
        String token = memberService.signUp(member);

        // then
        assertThat(token).isNotNull();
        assertThat(memberMapper.findById(userId)).isNotNull();
    }

    @Test
    public void logInTest() {
        // given
        memberService.signUp(member);

        // when
        String token = memberService.logIn("testUser", "testPassword");

        // then
        assertThat(token).isNotNull();
    }

    @Test
    public void signUpFailTest() {
        // given
        memberService.signUp(member);

        // when
        Exception exception = null;
        try {
            memberService.signUp(member);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User ID already exists");
    }

    @Test
    public void logInNonExistentUserTest() {
        // given
        String nonExistentUserId = "nonExistentUser";
        String anyPassword = "anyPassword";

        // when
        Exception exception = null;
        try {
            memberService.logIn(nonExistentUserId, anyPassword);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("User does not exist");
    }


    @Test
    public void logInInvalidPasswordTest() {
        // given
        String existentUserId = "existentUser";
        String validPassword = "validPassword";
        String invalidPassword = "invalidPassword";

        Member member = new Member();
        member.setUserId(existentUserId);
        member.setUserPwd(validPassword);
        member.setUserName("Test User");
        memberService.signUp(member);

        // when
        Exception exception = null;
        try {
            memberService.logIn(existentUserId, invalidPassword);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid password");
    }

    @Test
    public void logInWithEmptyPasswordTest() {
        // given
        String existentUserId = "existentUser";
        String emptyPassword = "    ";

        Member member = new Member();
        member.setUserId(existentUserId);
        member.setUserPwd("validPassword");
        member.setUserName("Test User");
        memberService.signUp(member);

        // when
        Exception exception = null;
        try {
            memberService.logIn(existentUserId, emptyPassword);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid password");
    }

    @Test
    public void signUpWithEmptyUserIdTest() {
        // given
        String emptyUserId = "    ";
        String validPassword = "validPassword";

        Member member = new Member();
        member.setUserId(emptyUserId);
        member.setUserPwd(validPassword);
        member.setUserName("Test User");

        // when
        Exception exception = null;
        try {
            memberService.signUp(member);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User ID cannot be empty");
    }

    @Test
    public void signUpWithEmptyUserNameTest() {
        // given
        Member member = new Member();
        member.setUserId("jony123");
        member.setUserPwd("validPassword");
        member.setUserName("   ");

        // when
        Exception exception = null;
        try {
            memberService.signUp(member);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: UserName cannot be empty");
    }

    @Test
    public void signUpWithEmptyPasswordTest() {
        // given
        String validUserId = "validUser";
        String emptyPassword = "";

        Member member = new Member();
        member.setUserId(validUserId);
        member.setUserPwd(emptyPassword);
        member.setUserName("Test User");

        // when
        Exception exception = null;
        try {
            memberService.signUp(member);
        } catch (RuntimeException e) {
            exception = e;
        }

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User Password cannot be empty");
    }

}
