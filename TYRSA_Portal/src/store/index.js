import Vue from "vue";
import Vuex from "vuex";
import router from "@/router";

Vue.use(Vuex);
export default new Vuex.Store({
  state: {
    color: localStorage.getItem("color") || "primary",
    status: "",
    loginerror: "",
    error: "",
    user: JSON.parse(localStorage.getItem("user") || "{}")
  },
  mutations: {
    auth_request(state) {
      state.status = "loading";
    },
    auth_success(state, user) {
      state.status = "success";
      state.user = JSON.parse(user);
      state.loginerror = "";
    },
    auth_error(state, error) {
      state.status = "error";
      state.loginerror = error;
      state.user = "{}";
    },
    ERROR(state, error) {
      state.error = error;
    },
    logout(state) {
      state.status = "";
      state.user = "{}";
    }
  },
  actions: {
    login({ commit }, user) {
      return new Promise(resolve => {
        commit("auth_request");
        Vue.axios
          .post("/auth/login", user)
          .then(resp => {
            let userJSON = resp.data;
            localStorage.setItem("user", JSON.stringify(userJSON));
            commit("auth_success", JSON.stringify(userJSON));
            resolve(resp);
          })
          .catch(err => {
            if (err.response.data) {
              const error = err.response.data.error;
              commit("auth_error", error);
              localStorage.removeItem("user");
            }
          });
      });
    },
    tokenerror({ commit }, error) {
      return new Promise(resolve => {
        localStorage.removeItem("user");
        commit("auth_error", error);

        delete Vue.axios.defaults.headers.common["Authorization"];

        // Redirigir a login
        router.push({ name: "Login" });

        resolve();
      });
    },
    error({ commit }, error) {
      return new Promise(resolve => {
        commit("ERROR", error);
        resolve();
      });
    },
    logout({ commit }) {
      return new Promise(resolve => {
        commit("logout");
        localStorage.removeItem("user");

        delete Vue.axios.defaults.headers.common["Authorization"];
        
        resolve();
      });
    }
  },
  getters: {
    isLoggedIn: state => !!state.user.token,
    authStatus: state => state.status,
    showError: state => state.loginerror,
    getUser: state => state.user
  },
  modules: {}
});
