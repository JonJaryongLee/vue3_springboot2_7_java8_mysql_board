<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useUserStore } from "@/stores/user";
import { useRouter } from "vue-router";

const auth = useAuthStore();
const user = useUserStore();
const router = useRouter();

const userId = ref("");
const userPwd = ref("");

/**
 * 사용자를 비동기적으로 로그인합니다.
 *
 * @async
 * @function login
 * @returns {Promise<Object>} 로그인 작업의 결과입니다.
 * @throws {Error} 로그인 작업이 실패하면 오류를 발생시킵니다.
 */
async function login() {
  const result = await auth.login({
    userId: userId.value,
    userPwd: userPwd.value,
  });
  alert(result.message);
  if (result.success) {
    user.userId = userId.value;
    router.push({ name: "board" });
  }
}
</script>

<template>
  <div class="container mt-3">
    <h1 class="mb-3">로그인</h1>
    <form @submit.prevent="login" class="mb-3">
      <div class="mb-3">
        <label for="userId" class="form-label">아이디:</label>
        <input id="userId" v-model="userId" type="text" class="form-control" required />
      </div>
      <div class="mb-3">
        <label for="userPwd" class="form-label">비밀번호:</label>
        <input id="userPwd" v-model="userPwd" type="password" class="form-control" required />
      </div>
      <button type="submit" class="btn btn-primary">로그인</button>
    </form>
  </div>
</template>
