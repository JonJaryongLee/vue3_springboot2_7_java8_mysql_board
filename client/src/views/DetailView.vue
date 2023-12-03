<script setup>
import { ref, computed } from "vue";
import { RouterLink, useRouter, useRoute } from "vue-router";
import { useUserStore } from "@/stores/user";
import { useAuthStore } from "@/stores/auth";
import axios from "axios";
import dayjs from "dayjs";

const router = useRouter();
const route = useRoute();
const user = useUserStore();
const auth = useAuthStore();

const URL = import.meta.env.VITE_APP_SERVER_URL;
const article = ref({
  //   articleNo: 1,
  //   userId: "userId",
  //   subject: "제목",
  //   content: "내용",
  //   hit: 0,
  //   registerTime: "2023-12-03T12:23:58"
});

const parsedDate = computed(() => {
  return dayjs(article.value.registerTime).format("YYYY년 MM월 DD일 HH시 mm분");
});

const isUserArticle = computed(() => {
  if (article.value.userId === user.userId) {
    return true;
  }
  return false;
});

/**
 * 지정된 URL에서 게시물을 비동기적으로 삭제합니다.
 * 성공하면 알림을 표시하고 게시판으로 이동합니다.
 *
 * @async
 * @function
 * @throws {Error} 게시물 삭제 중 오류가 발생하면 오류를 콘솔에 기록합니다.
 */
async function deleteArticle() {
  try {
    await axios.delete(`${URL}/articles/${article.value.articleNo}`, {
      headers: {
        Authorization: `Bearer ${auth.token}`,
      },
    });
    alert("글 삭제 성공");
    router.push({ name: "board" });
  } catch (error) {
    console.error(error);
  }
}

/**
 * 지정된 URL에서 특정 게시글을 비동기적으로 가져와 `article` ref를 업데이트합니다.
 *
 * @async
 * @function
 * @returns {Promise<void>} 아무것도 반환하지 않습니다.
 * @throws 요청이 실패하면 오류를 발생시킵니다. 실패 시 "잘못된 접근입니다."라는 알림을 표시하고 게시판으로 이동합니다.
 */
async function getArticle() {
  try {
    const response = await axios(`${URL}/articles/${route.params.id}`);
    article.value = response.data;
  } catch (error) {
    console.error(error);
    alert("잘못된 접근입니다.");
    router.push({ name: "board" });
  }
}

getArticle();
</script>

<template>
  <div class="container mt-3">
    <h1 class="mb-3">{{ article.subject }}</h1>
    <p><strong>글번호:</strong> {{ article.articleNo }}</p>
    <p><strong>작성자:</strong> {{ article.userId }}</p>
    <p><strong>작성시간:</strong> {{ parsedDate }}</p>
    <p><strong>추천수:</strong> {{ article.hit }}</p>
    <p class="mt-3">{{ article.content }}</p>
    <template v-if="isUserArticle">
      <RouterLink :to="{ name: 'updateForm', params: { id: article.articleNo } }" class="btn btn-primary mt-3">수정</RouterLink>
      <button class="btn btn-danger mt-3 ms-2" @click="deleteArticle">삭제</button>
    </template>
  </div>
</template>
