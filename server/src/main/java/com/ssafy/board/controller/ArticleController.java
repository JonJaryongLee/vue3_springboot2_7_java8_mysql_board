package com.ssafy.board.controller;

import com.ssafy.board.model.Article;
import com.ssafy.board.service.ArticleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.JwtException;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Value("${my.secret.key}")
    private String secretKey;

    private SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * 모든 게시글을 조회합니다.
     *
     * @return 게시글 목록
     */
    @GetMapping
    @ResponseBody
    public List<Article> getAllArticles() {
        return articleService.findArticles();
    }

    /**
     * 주어진 ID에 해당하는 게시글을 조회합니다.
     *
     * @param id 게시글 ID
     * @return ID에 해당하는 게시글
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Article getArticleById(@PathVariable Long id) {
        return articleService.findArticle(id);
    }

    /**
     * 새로운 게시글을 생성하는 메서드입니다.
     *
     * @param jwtToken 사용자의 JWT 토큰입니다. 이 토큰은 사용자의 ID를 파싱하는 데 사용됩니다.
     * @param article 생성할 게시글의 정보를 담고 있는 Article 객체입니다.
     * @return ResponseEntity<String> HTTP 응답 상태와 메시지를 담은 ResponseEntity 객체를 반환합니다.
     *         성공적으로 게시글이 생성되면 상태 코드 201(CREATED)를 반환합니다.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createNewArticle(@RequestHeader("Authorization") String jwtToken, @RequestBody Article article) {
        return processArticle(null, jwtToken, article, "create");
    }

    /**
     * 주어진 ID에 해당하는 게시글을 업데이트하는 메서드입니다.
     *
     * @param id 업데이트할 게시글의 ID입니다.
     * @param jwtToken 사용자의 JWT 토큰입니다. 이 토큰은 사용자의 ID를 파싱하는 데 사용됩니다.
     * @param article 업데이트할 게시글의 정보를 담고 있는 Article 객체입니다.
     * @return ResponseEntity<String> HTTP 응답 상태와 메시지를 담은 ResponseEntity 객체를 반환합니다.
     *         성공적으로 게시글이 업데이트되면 상태 코드 200(OK)를 반환합니다.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> updateArticleById(@PathVariable Long id, @RequestHeader("Authorization") String jwtToken, @RequestBody Article article) {
        return processArticle(id, jwtToken, article, "update");
    }

    /**
     * 주어진 ID에 해당하는 게시글을 삭제하는 메서드입니다.
     *
     * @param id 삭제할 게시글의 ID입니다.
     * @param jwtToken 사용자의 JWT 토큰입니다. 이 토큰은 사용자의 ID를 파싱하는 데 사용됩니다.
     * @return ResponseEntity<String> HTTP 응답 상태와 메시지를 담은 ResponseEntity 객체를 반환합니다.
     *         성공적으로 게시글이 삭제되면 상태 코드 200(OK)를 반환합니다.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteArticleById(@PathVariable Long id, @RequestHeader("Authorization") String jwtToken) {
        return processArticle(id, jwtToken, null, "delete");
    }

    /**
     * 게시글을 생성, 업데이트, 삭제하는 공통 로직을 처리하는 메서드입니다.
     *
     * @param id 게시글의 ID입니다. 생성 작업에서는 null이 될 수 있습니다.
     * @param jwtToken 사용자의 JWT 토큰입니다. 이 토큰은 사용자의 ID를 파싱하는 데 사용됩니다.
     * @param article 생성 또는 업데이트할 게시글의 정보를 담고 있는 Article 객체입니다. 삭제 작업에서는 null이 될 수 있습니다.
     * @param operation 수행할 작업을 나타내는 문자열입니다. "create", "update", "delete" 중 하나입니다.
     * @return ResponseEntity<String> HTTP 응답 상태와 메시지를 담은 ResponseEntity 객체를 반환합니다.
     *         성공적으로 작업이 수행되면 상태 코드 201(CREATED), 200(OK) 중 하나를 반환하고,
     *         게시글을 찾을 수 없는 경우에는 상태 코드 404(NOT_FOUND)와 "게시글을 찾을 수 없습니다" 메시지를 반환하며,
     *         인증 토큰이 유효하지 않은 경우에는 상태 코드 401(UNAUTHORIZED)와 "Invalid token" 메시지를 반환합니다.
     * @throws JwtException JWT 토큰 파싱 과정에서 예외가 발생하면 이를 처리하기 위해 JwtException을 던집니다.
     */
    private ResponseEntity<String> processArticle(Long id, String jwtToken, Article article, String operation) {
        try {
            String userId = parseJwtToken(jwtToken);
            if(userId != null) {
                if(operation.equals("create")) {
                    article.setUserId(userId);
                    articleService.createArticle(article);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    Article existingArticle = findArticle(id);
                    if(existingArticle != null) {
                        if(isUserAuthorized(existingArticle, userId)) {
                            switch(operation) {
                                case "update":
                                    article.setUserId(userId);
                                    article.setArticleNo(id);
                                    article.setHit(0L);
                                    articleService.updateArticle(article);
                                    return new ResponseEntity<>(HttpStatus.OK);
                                case "delete":

                                    articleService.deleteArticle(id);
                                    return new ResponseEntity<>(HttpStatus.OK);
                                default:
                                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid operation");
                            }
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없습니다");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다");
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (JwtException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    /**
     * 주어진 JWT 토큰을 파싱하여 사용자 ID를 반환하는 메서드입니다.
     *
     * @param jwtToken 파싱할 JWT 토큰입니다. "Bearer "로 시작하는 경우, 이 부분은 제거됩니다.
     * @return String 토큰이 유효하면 사용자 ID를 반환하고, 그렇지 않으면 null을 반환합니다.
     */
    private String parseJwtToken(String jwtToken) {
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }
        if(verifyToken(jwtToken)) {
            Jws<Claims> claims = Jwts.parser().setSigningKey(getSecretKeySpec().getEncoded()).parseClaimsJws(jwtToken);
            return claims.getBody().getSubject();
        } else {
            return null;
        }
    }

    /**
     * 주어진 ID에 해당하는 게시글을 찾는 메서드입니다.
     *
     * @param id 찾을 게시글의 ID입니다.
     * @return Article ID에 해당하는 게시글을 반환합니다. 게시글이 존재하지 않는 경우에는 null을 반환합니다.
     */
    private Article findArticle(Long id) {
        return articleService.findArticle(id);
    }

    /**
     * 주어진 사용자가 게시글에 대한 권한이 있는지 확인하는 메서드입니다.
     *
     * @param article 권한을 확인할 게시글입니다.
     * @param userId 권한을 확인할 사용자의 ID입니다.
     * @return boolean 사용자가 게시글에 대한 권한이 있으면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isUserAuthorized(Article article, String userId) {
        if (article == null || !article.getUserId().equals(userId)) {
            return false;
        }
        return true;
    }


    /**
     * 주어진 JWT 토큰이 유효한지 검증하는 메서드입니다.
     *
     * @param jwtToken 검증할 JWT 토큰입니다.
     * @return Boolean 토큰이 유효하면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     * @throws JwtException JWT 토큰 파싱 과정에서 예외가 발생하면 이를 처리하기 위해 JwtException을 던집니다.
     */
    private Boolean verifyToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(getSecretKeySpec().getEncoded()).parseClaimsJws(jwtToken);
            return true;
        } catch (JwtException exception) {
            return false;
        }
    }
}
