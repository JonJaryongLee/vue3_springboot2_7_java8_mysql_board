package com.ssafy.board.service;

import com.ssafy.board.mapper.ArticleMapper;
import com.ssafy.board.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleMapper articleMapper;

    /**
     * 모든 게시글을 조회합니다.
     * @return 게시글 목록
     */
    public List<Article> findArticles() {
        return articleMapper.findAll();
    }

    /**
     * 주어진 번호에 해당하는 게시글을 조회합니다.
     * @param articleNo 조회할 게시글 번호
     * @return 조회된 게시글
     */
    public Article findArticle(Long articleNo) {
        Article article = articleMapper.findById(articleNo);
        verifyArticleExists(article);
        return article;
    }

    /**
     * 새로운 게시글을 생성합니다.
     * @param article 생성할 게시글
     */
    @Transactional
    public void createArticle(Article article) {
        verifyArticleNotNull(article);
        verifyEmptyFields(article);
        article.setHit(0L);
        article.setRegisterTime(LocalDateTime.now());
        articleMapper.insert(article);
    }

    /**
     * 주어진 번호에 해당하는 게시글을 삭제합니다.
     * @param articleNo 삭제할 게시글 번호
     */
    @Transactional
    public void deleteArticle(Long articleNo) {
        Article article = findArticle(articleNo);
        articleMapper.delete(articleNo);
    }

    /**
     * 게시글을 업데이트합니다.
     * @param article 업데이트할 게시글
     */
    @Transactional
    public void updateArticle(Article article) {
        verifyArticleNotNull(article);
        verifyEmptyFields(article);
        verifyArticleExists(article);
        article.setRegisterTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    /**
     * 주어진 게시글이 존재하는지 검증합니다.
     * @param article 검증할 게시글
     */
    private void verifyArticleExists(Article article) {
        if (article == null) {
            throw new RuntimeException("Article does not exist");
        }
    }

    /**
     * 주어진 게시글이 null이 아닌지 검증합니다.
     * @param article 검증할 게시글
     */
    private void verifyArticleNotNull(Article article) {
        if (article == null) {
            throw new RuntimeException("Article is null");
        }
    }

    /**
     * Article 객체의 subject 필드와 content 필드가 null이 아니거나 비어 있지 않음을 검증합니다.
     *
     * @param article 검증할 Article 객체
     * @throws RuntimeException 만약 Article 객체의 subject 필드나 content 필드가 null이거나 공백 문자만 포함하고 있다면 RuntimeException을 발생시킵니다.
     */
    private void verifyEmptyFields(Article article) {
        if (article.getSubject() == null || article.getSubject().trim().isEmpty()) {
            throw new RuntimeException("Subject cannot be empty");
        } else if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            throw new RuntimeException("Content cannot be empty");
        }
    }
}
