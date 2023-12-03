import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/HomeView.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/LoginView.vue"),
    },
    {
      path: "/signup",
      name: "signup",
      component: () => import("../views/SignupView.vue"),
    },
    {
      path: "/articles",
      name: "board",
      component: () => import("../views/BoardView.vue"),
    },
    {
      path: "/articles/:id",
      name: "detail",
      component: () => import("../views/DetailView.vue"),
    },
    {
      path: "/articles/create",
      name: "createForm",
      component: () => import("../views/CreateFormView.vue"),
    },
    {
      path: "/articles/update/:id",
      name: "updateForm",
      component: () => import("../views/UpdateFormView.vue"),
    },
  ],
});

export default router;
