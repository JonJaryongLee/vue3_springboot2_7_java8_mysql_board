package com.ssafy.board.mapper;

import com.ssafy.board.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    private Member testMember;

    @BeforeEach
    public void setup() {
        testMember = new Member();
        testMember.setUserId("jony123");
        testMember.setUserName("김철수");
        testMember.setUserPwd("123123");
//        testMember.setEmailId("chulsoo");
//        testMember.setEmailDomain("naver.com");
        testMember.setJoinDate(LocalDateTime.now());
        memberMapper.insert(testMember);
    }

    @AfterEach
    public void cleanup() {
        memberMapper.delete(testMember.getUserId());
    }

    @Test
    public void testFindAll() {
        // when
        List<Member> members = memberMapper.findAll();

        // then
        Assertions.assertThat(members).isNotNull();
    }

    @Test
    public void testFindById() {
        // given
        String id = testMember.getUserId();

        // when
        Member member = memberMapper.findById(id);

        // then
        Assertions.assertThat(member).isNotNull();
        Assertions.assertThat(member.getUserId()).isEqualTo(id);
    }

    @Test
    public void testFindByUserName() {
        // given
        String userName = testMember.getUserName();

        // when
        Member member = memberMapper.findByUserName(userName);

        // then
        Assertions.assertThat(member).isNotNull();
        Assertions.assertThat(member.getUserName()).isEqualTo(userName);
    }

    @Test
    public void testGetUserPwd() {
        // given
        String userName = testMember.getUserName();

        // when
        String userPwd = memberMapper.getUserPwd(userName);

        // then
        Assertions.assertThat(userPwd).isNotNull();
    }

    @Test
    public void testInsert() {
        // given
        Member member = new Member();
        member.setUserId("jony456");
        member.setUserName("박영희");
        member.setUserPwd("123123");
//        member.setEmailId("younghee");
//        member.setEmailDomain("naver.com");
        member.setJoinDate(LocalDateTime.now());

        // when
        memberMapper.insert(member);

        // then
        Member insertedMember = memberMapper.findById(member.getUserId());
        Assertions.assertThat(insertedMember).isNotNull();
        Assertions.assertThat(insertedMember.getUserName()).isEqualTo("박영희");

        // cleanup
        memberMapper.delete(member.getUserId());
    }

    @Test
    public void testDelete() {
        // given
        String id = testMember.getUserId();

        // when
        memberMapper.delete(id);

        // then
        Member deletedMember = memberMapper.findById(id);
        Assertions.assertThat(deletedMember).isNull();

        // cleanup
        memberMapper.insert(testMember);
    }

    @Test
    public void testUpdate() {
        // given
        Member member = memberMapper.findById(testMember.getUserId());
        member.setUserName("박영희");

        // when
        memberMapper.update(member);

        // then
        Member updatedMember = memberMapper.findById(member.getUserId());
        Assertions.assertThat(updatedMember).isNotNull();
        Assertions.assertThat(updatedMember.getUserName()).isEqualTo("박영희");

        // cleanup
        member.setUserName(testMember.getUserName());
        memberMapper.update(member);
    }
}
