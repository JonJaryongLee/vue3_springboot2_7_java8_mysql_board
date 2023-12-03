package com.ssafy.board.mapper;

import com.ssafy.board.model.Article;
import com.ssafy.board.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private MemberMapper memberMapper;

    private Article testArticle;
    private Member testMember;

    @BeforeEach
    public void setup() {
        testMember = new Member();
        testMember.setUserId("leeja7986");
        testMember.setUserName("김철수");
        testMember.setUserPwd("123123");
        testMember.setEmailId("chulsoo");
        testMember.setEmailDomain("naver.com");
        testMember.setJoinDate(LocalDateTime.now());
        memberMapper.insert(testMember);

        testArticle = new Article();
        testArticle.setUserId(testMember.getUserId()); // Use the userId of the testMember
        testArticle.setSubject("제목2");
        testArticle.setContent("내용2");
        testArticle.setHit(0L);
        testArticle.setRegisterTime(LocalDateTime.now());
        articleMapper.insert(testArticle);
    }

    @AfterEach
    public void cleanup() {
        List<Article> articles = articleMapper.findAll();
        for (Article article : articles) {
            if (article.getUserId().equals(testMember.getUserId())) {
                articleMapper.delete(article.getArticleNo());
            }
        }
        memberMapper.delete(testMember.getUserId());
    }

    @Test
    public void testFindAll() {
        // when
        List<Article> articles = articleMapper.findAll();

        // then
        Assertions.assertThat(articles).isNotNull();
    }

    @Test
    public void testFindById() {
        // given
        Long id = testArticle.getArticleNo();

        // when
        Article article = articleMapper.findById(id);

        // then
        Assertions.assertThat(article).isNotNull();
        Assertions.assertThat(article.getArticleNo()).isEqualTo(id);
    }

    @Test
    public void testFindByUserId() {
        // given
        String userId = testMember.getUserId();

        // when
        List<Article> articles = articleMapper.findByUserId(userId);

        // then
        Assertions.assertThat(articles).isNotNull();
        Assertions.assertThat(articles).isNotEmpty();
        Assertions.assertThat(articles.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    public void testInsert() {
        // given
        Article article = new Article();
        article.setUserId(testMember.getUserId()); // Use the userId of the testMember
        article.setSubject("제목3");
        article.setContent("내용3");
        article.setHit(0L);
        article.setRegisterTime(LocalDateTime.now());

        // when
        articleMapper.insert(article);

        // then
        Article insertedArticle = articleMapper.findById(article.getArticleNo());
        Assertions.assertThat(insertedArticle).isNotNull();
        Assertions.assertThat(insertedArticle.getSubject()).isEqualTo("제목3");

        // cleanup
        articleMapper.delete(article.getArticleNo());
    }

    @Test
    public void testDelete() {
        // given
        Long id = testArticle.getArticleNo();

        // when
        articleMapper.delete(id);

        // then
        Article deletedArticle = articleMapper.findById(id);
        Assertions.assertThat(deletedArticle).isNull();
    }

    @Test
    public void testUpdate() {
        // given
        Article article = articleMapper.findById(testArticle.getArticleNo());
        article.setSubject("제목 수정");

        // when
        articleMapper.update(article);

        // then
        Article updatedArticle = articleMapper.findById(article.getArticleNo());
        Assertions.assertThat(updatedArticle).isNotNull();
        Assertions.assertThat(updatedArticle.getSubject()).isEqualTo("제목 수정");

        // cleanup
        article.setSubject(testArticle.getSubject());
        articleMapper.update(article);
    }
}
