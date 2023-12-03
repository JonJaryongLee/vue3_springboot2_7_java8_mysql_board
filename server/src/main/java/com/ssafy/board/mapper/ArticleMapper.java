package com.ssafy.board.mapper;

import com.ssafy.board.model.Article;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("SELECT a.*, m.userName FROM Article a INNER JOIN Member m ON a.userId = m.userId")
    List<Article> findAll();

    @Select("SELECT a.*, m.userName FROM Article a INNER JOIN Member m ON a.userId = m.userId WHERE a.articleNo = #{articleNo}")
    Article findById(@Param("articleNo") Long articleNo);

    @Select("SELECT a.*, m.userName FROM Article a INNER JOIN Member m ON a.userId = m.userId WHERE a.userId = #{userId}")
    List<Article> findByUserId(@Param("userId") String userId);

    @Insert("INSERT INTO Article(userId, subject, content, hit, registerTime) VALUES(#{userId}, #{subject}, #{content}, #{hit}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyProperty = "articleNo")
    void insert(Article article);

    @Delete("DELETE FROM Article WHERE articleNo = #{articleNo}")
    void delete(@Param("articleNo") Long articleNo);

    @Update("UPDATE Article SET userId = #{userId}, subject = #{subject}, content = #{content}, hit = #{hit}, registerTime = #{registerTime} WHERE articleNo = #{articleNo}")
    void update(Article article);
}
