<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import axios from "axios";

const router = useRouter();
const auth = useAuthStore();

const URL = import.meta.env.VITE_APP_SERVER_URL;
const subject = ref("");
const content = ref("");

if (!auth.token) {
  alert("로그인이 필요합니다.");
  router.push({ name: "login" });
}

/**
 * 지정된 URL로 POST 요청을 보내 새 게시물을 비동기적으로 생성합니다.
 * 성공하면 알림을 표시하고 게시판으로 이동합니다.
 *
 * @async
 * @function
 * @throws {Error} 게시물 생성 중 오류가 발생하면 오류를 콘솔에 기록합니다.
 */
async function create() {
  try {
    await axios.post(
      `${URL}/articles`,
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
    alert("글 작성 성공");
    router.push({ name: "board" });
  } catch (error) {
    console.error(error);
  }
}
</script>

<template>
  <div class="container mt-3">
    <h1 class="mb-3">글 작성</h1>
    <form @submit.prevent="create" class="mb-3">
      <div class="mb-3">
        <label for="subject" class="form-label">제목:</label>
        <input id="subject" v-model="subject" type="text" class="form-control" required />
      </div>
      <div class="mb-3">
        <label for="content" class="form-label">내용:</label>
        <textarea id="content" v-model="content" class="form-control" rows="5" required></textarea>
      </div>
      <button type="submit" class="btn btn-primary">작성하기</button>
    </form>
  </div>
</template>
