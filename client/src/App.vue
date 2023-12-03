<script setup>
import { RouterLink, RouterView } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useUserStore } from "@/stores/user";

const auth = useAuthStore();
const user = useUserStore();

/**
 * 사용자를 로그아웃합니다.
 *
 * @function logout
 */
function logout() {
  user.userId = null;
  auth.logout();
}
</script>

<template>
  <header class="p-3 bg-dark text-white">
    <nav class="navbar navbar-expand-lg navbar-dark">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <template v-if="!auth.token">
          <li class="nav-item">
            <RouterLink class="nav-link" :to="{ name: 'login' }">로그인</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" :to="{ name: 'signup' }">회원가입</RouterLink>
          </li>
        </template>
        <li class="nav-item">
          <RouterLink class="nav-link" :to="{ name: 'board' }">게시판</RouterLink>
        </li>
      </ul>
    </nav>
    <p class="mt-2" v-if="user.userId">{{ user.userId }}님 환영합니다.</p>
    <button class="btn btn-light" v-if="auth.token" @click="logout">Logout</button>
  </header>
  <main class="container mt-3">
    <RouterView />
  </main>
</template>

<style scoped></style>
