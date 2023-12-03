import { ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useAuthStore = defineStore("auth", () => {
  const token = ref(null);
  const URL = import.meta.env.VITE_APP_SERVER_URL;

  /**
   * 로그인을 위한 비동기 함수입니다.
   * @async
   * @function login
   * @param {Object} userLoginObj - 로그인 정보를 담은 객체입니다.
   * @param {string} userLoginObj.userId - 사용자의 아이디입니다.
   * @param {string} userLoginObj.userPwd - 사용자의 비밀번호입니다.
   * @returns {Promise<Object>} 로그인 요청을 보내고, 응답을 받아 토큰을 설정합니다. 요청이 실패하면 에러 메시지를 반환합니다.
   */
  async function login(userLoginObj) {
    try {
      const response = await axios.post(`${URL}/members/login`, userLoginObj);
      token.value = response.data;
      return { success: true, message: "로그인에 성공했습니다." };
    } catch (error) {
      return { success: false, message: error.response.data.message };
    }
  }

  /**
   * 회원 가입을 위한 비동기 함수입니다.
   * @async
   * @function signup
   * @param {Object} userSignupObj - 회원 가입 정보를 담은 객체입니다.
   * @param {string} userSignupObj.userId - 사용자의 아이디입니다.
   * @param {string} userSignupObj.userPwd - 사용자의 비밀번호입니다.
   * @param {string} userSignupObj.userName - 사용자의 이름입니다.
   * @returns {Promise<Object>} 회원 가입 요청을 보내고, 응답을 받아 토큰을 설정합니다. 요청이 실패하면 에러 메시지를 반환합니다.
   */
  async function signup(userSignupObj) {
    try {
      const response = await axios.post(`${URL}/members/signup`, userSignupObj);
      token.value = response.data;
      return { success: true, message: "회원가입에 성공했습니다." };
    } catch (error) {
      return { success: false, message: error.response.data.message };
    }
  }

  /**
   * 로그아웃을 처리하는 함수입니다.
   * @function logout
   * @returns {Object} 저장된 토큰을 지우고, 로그아웃 성공 메시지를 반환합니다.
   */
  function logout() {
    token.value = null;
    return { success: true, message: "로그아웃에 성공했습니다." };
  }

  return { token, login, signup, logout };
});
