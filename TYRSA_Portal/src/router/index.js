import Vue from "vue";
import VueRouter from "vue-router";
import VueMask from 'v-mask'
import AdminsPage from "../components/Users.vue";
import store from "../store/index.js";


Vue.use(VueMask, {
    placeholders: {
    M: /[0-9A-Fa-f]/
  }
});

Vue.use(VueRouter);

const routes = [
  {
    path: "/users",
    name: "Users",
    component: AdminsPage,
    beforeEnter: (to, from, next) => {
      if (!!store.getters.getUser.role && 
          store.getters.getUser.role === "ROLE_ADMIN")
      {
        next();
      } else {
        next("/");
      }
    }
  }
];

const router = new VueRouter({
  routes
});

export default router;
