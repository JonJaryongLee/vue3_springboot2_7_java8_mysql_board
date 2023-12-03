package com.ssafy.board.mapper;

import com.ssafy.board.model.Member;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM Member")
    List<Member> findAll();

    @Select("SELECT * FROM Member WHERE userId = #{userId}")
    Member findById(@Param("userId") String userId);

    @Select("SELECT * FROM Member WHERE userName = #{userName}")
    Member findByUserName(@Param("userName") String userName);

    @Select("SELECT userPwd FROM Member WHERE userName = #{userName}")
    String getUserPwd(@Param("userName") String userName);

    @Insert("INSERT INTO Member(userId, userName, userPwd, emailId, emailDomain, joinDate) VALUES(#{userId}, #{userName}, #{userPwd}, #{emailId}, #{emailDomain}, #{joinDate})")
    void insert(Member member);

    @Delete("DELETE FROM Member WHERE userId = #{userId}")
    void delete(@Param("userId") String userId);

    @Update("UPDATE Member SET userName = #{userName}, userPwd = #{userPwd}, emailId = #{emailId}, emailDomain = #{emailDomain}, joinDate = #{joinDate} WHERE userId = #{userId}")
    void update(Member member);
}