<script setup>
import { ref } from "vue";
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import axios from "axios";

const auth = useAuthStore();

const URL = import.meta.env.VITE_APP_SERVER_URL;

const articles = ref([
  // {
  //   articleNo: 1,
  //   userId: "userId",
  //   subject: "제목",
  //   content: "내용",
  //   hit: 0,
  //   registerTime: "2023-12-03T12:23:58"
  // }
]);

/**
 * 지정된 URL에서 게시글을 비동기적으로 가져와 `articles` ref를 업데이트합니다.
 *
 * @async
 * @function
 * @returns {Promise<void>} 아무것도 반환하지 않습니다.
 * @throws 요청이 실패하면 오류를 발생시킵니다.
 */
async function getArticles() {
  try {
    const response = await axios.get(`${URL}/articles`);
    if (response.status === 200) {
      articles.value = response.data.sort((a, b) => b.articleNo - a.articleNo);
    }
  } catch (error) {
    console.error(error);
  }
}

getArticles();
</script>

<template>
  <div class="container mt-3">
    <h1 class="mb-3">게시판</h1>
    <ul class="list-group" v-if="articles.length">
      <li class="list-group-item" v-for="article in articles" :key="article.articleNo">
        <p class="mb-1"><strong>articleNo:</strong> {{ article.articleNo }}</p>
        <p class="mb-1">
          <RouterLink class="text-decoration-none" :to="{ name: 'detail', params: { id: article.articleNo } }">
            <strong>제목:</strong> {{ article.subject }}
          </RouterLink>
        </p>
        <p class="mb-0"><strong>작성자:</strong> {{ article.userId }}</p>
      </li>
    </ul>
    <p class="mt-3" v-else>작성된 게시글이 없습니다.</p>
    <RouterLink v-if="auth.token" :to="{ name: 'createForm' }" class="btn btn-primary mt-3">글 작성</RouterLink>
  </div>
</template>
