<script setup>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useUserStore } from "@/stores/user";
import axios from "axios";

const router = useRouter();
const route = useRoute();
const auth = useAuthStore();
const user = useUserStore();

const URL = import.meta.env.VITE_APP_SERVER_URL;
const subject = ref("");
const content = ref("");

if (!auth.token) {
  alert("로그인이 필요합니다.");
  router.push({ name: "login" });
}

/**
 * 지정된 URL로 게시물을 비동기적으로 업데이트합니다.
 * 성공하면 알림을 표시하고 상세 페이지로 이동합니다.
 *
 * @async
 * @function
 * @throws {Error} 게시물 업데이트 중 오류가 발생하면 오류를 콘솔에 기록합니다.
 */
async function update() {
  try {
    await axios.put(
      `${URL}/articles/${route.params.id}`,
      {
        subject: subject.value,
        content: content.value,
      },
      {
        headers: {
          Authorization: `Bearer ${auth.token}`,
        },
      }
    );
    alert("글 수정 성공");
    router.push({ name: "detail", params: { id: route.params.id } });
  } catch (error) {
    console.error(error);
  }
}

/**
 * 지정된 URL에서 게시물을 비동기적으로 가져옵니다.
 * 사용자가 게시물의 작성자가 아닌 경우, 알림을 표시하고 게시판으로 이동합니다.
 *
 * @async
 * @function
 * @throws {Error} 게시물을 가져오는 중 오류가 발생하면 오류를 콘솔에 기록하고, 알림을 표시한 후 게시판으로 이동합니다.
 */
async function getArticle() {
  try {
    const response = await axios.get(`${URL}/articles/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${auth.token}`,
      },
    });
    if (user.userId !== response.data.userId) {
      alert("권한이 없습니다.");
      router.push({ name: "board" });
    }
    subject.value = response.data.subject;
    content.value = response.data.content;
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
    <h1 class="mb-3">글 수정</h1>
    <form @submit.prevent="update" class="mb-3">
      <div class="mb-3">
        <label for="subject" class="form-label">제목:</label>
        <input id="subject" v-model="subject" type="text" class="form-control" required />
      </div>
      <div class="mb-3">
        <label for="content" class="form-label">내용:</label>
        <textarea id="content" v-model="content" class="form-control" rows="5" required></textarea>
      </div>
      <button type="submit" class="btn btn-primary">수정하기</button>
    </form>
  </div>
</template>
