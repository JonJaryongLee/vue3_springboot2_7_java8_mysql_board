package com.ssafy.board.service;

import com.ssafy.board.mapper.ArticleMapper;
import com.ssafy.board.mapper.MemberMapper;
import com.ssafy.board.model.Article;
import com.ssafy.board.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class ArticleServiceTest {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    private Member member;
    private Article article;

    @BeforeEach
    public void setUp() {
        // given
        member = new Member();
        member.setUserId("user1");
        member.setUserPwd("samplePwd");
        member.setUserName("testname");
        member.setJoinDate(LocalDateTime.now());
        memberMapper.insert(member);

        article = new Article();
        article.setUserId("user1");
        article.setSubject("test subject");
        article.setContent("test content");
        article.setHit(11L);
        article.setRegisterTime(LocalDateTime.now());
        articleMapper.insert(article);
    }

    @AfterEach
    public void tearDown() {
        List<Article> articles = articleMapper.findAll();
        for (Article article : articles) {
            if (article.getUserId().equals(member.getUserId())) {
                articleMapper.delete(article.getArticleNo());
            }
        }
        memberMapper.delete(member.getUserId());
    }

    @Test
    public void testFindArticles() {
        // given
        List<Article> articles = articleService.findArticles();

        // then
        Assertions.assertThat(articles).isNotNull();
        Assertions.assertThat(articles.size()).isGreaterThan(0);
    }

    @Test
    public void testFindArticle() {
        // given
        Long articleNo = article.getArticleNo();

        // when
        Article foundArticle = articleService.findArticle(articleNo);

        // then
        Assertions.assertThat(foundArticle).isNotNull();
        Assertions.assertThat(foundArticle.getArticleNo()).isEqualTo(articleNo);
    }

    @Test
    public void testCreateArticle() {
        // given
        Article newArticle = new Article();
        newArticle.setUserId("user1");
        newArticle.setSubject("new subject");
        newArticle.setContent("new content");

        // when
        articleService.createArticle(newArticle);

        // then
        Article createdArticle = articleService.findArticle(newArticle.getArticleNo());
        Assertions.assertThat(createdArticle).isNotNull();
        Assertions.assertThat(createdArticle.getUserId()).isEqualTo(newArticle.getUserId());
    }

    @Test
    public void testDeleteArticle() {
        // given
        Long articleNo = article.getArticleNo();

        // when
        articleService.deleteArticle(articleNo);

        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            articleService.findArticle(articleNo);
        });
        String expectedMessage = "Article does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testUpdateArticle() {
        // given
        String updatedContent = "updated content";
        article.setContent(updatedContent);

        // when
        articleService.updateArticle(article);

        // then
        Article updatedArticle = articleService.findArticle(article.getArticleNo());
        Assertions.assertThat(updatedArticle).isNotNull();
        Assertions.assertThat(updatedArticle.getContent()).isEqualTo(updatedContent);
    }

    @Test
    public void testVerifyArticleExistsException() {
        // when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            articleService.findArticle(99L);
        });

        // then
        String expectedMessage = "Article does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testVerifyArticleNotNullException() {
        // given
        Article nullArticle = null;

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            articleService.createArticle(nullArticle);
        });

        // then
        String expectedMessage = "Article is null";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testVerifyEmptySubjectException() {
        // given
        Article emptyFieldsArticle = new Article();
        emptyFieldsArticle.setSubject("   ");
        emptyFieldsArticle.setContent("dasdfa");

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            articleService.createArticle(emptyFieldsArticle);
        });

        // then
        String expectedMessage = "Subject cannot be empty";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testVerifyEmptyContentException() {
        // given
        Article emptyFieldsArticle = new Article();
        emptyFieldsArticle.setSubject("dfasdf");
        emptyFieldsArticle.setContent("");

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            articleService.createArticle(emptyFieldsArticle);
        });

        // then
        String expectedMessage = "Content cannot be empty";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}