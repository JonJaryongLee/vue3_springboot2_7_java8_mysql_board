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
const userName = ref("");

/**
 * 새로운 사용자를 비동기적으로 등록합니다.
 *
 * @async
 * @function signup
 * @returns {Promise<Object>} 회원가입 작업의 결과입니다.
 * @throws {Error} 회원가입 작업이 실패하면 오류를 발생시킵니다.
 */
async function signup() {
  // 아이디와 비밀번호의 유효성 검사
  if (!userId.value || userId.value.length < 5) {
    alert("아이디는 5자 이상이어야 합니다.");
    return;
  }

  const passwordRegex = /^(?=.*\d).{8,}$/;
  if (!userPwd.value || !passwordRegex.test(userPwd.value)) {
    alert("비밀번호는 8자 이상이며, 최소 하나의 숫자를 포함해야 합니다.");
    return;
  }

  const result = await auth.signup({
    userId: userId.value,
    userPwd: userPwd.value,
    userName: userName.value,
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
    <h1 class="mb-3">회원가입</h1>
    <form @submit.prevent="signup" class="mb-3">
      <div class="mb-3">
        <label for="userId" class="form-label">아이디:</label>
        <input
          id="userId"
          v-model="userId"
          type="text"
          class="form-control"
          required
        />
      </div>
      <div class="mb-3">
        <label for="userName" class="form-label">닉네임:</label>
        <input
          id="userName"
          v-model="userName"
          type="text"
          class="form-control"
          required
        />
      </div>
      <div class="mb-3">
        <label for="userPwd" class="form-label">비밀번호:</label>
        <input
          id="userPwd"
          v-model="userPwd"
          type="password"
          class="form-control"
          required
        />
      </div>
      <button type="submit" class="btn btn-primary">회원가입</button>
    </form>
  </div>
</template>
