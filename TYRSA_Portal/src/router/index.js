import Vue from "vue";
import VueRouter from "vue-router";
import VueMask from 'v-mask'
import AdminsPage from "../components/Users.vue";
import Cliente from "../components/Clientes.vue"
import Material from "../components/Materiales.vue"
import Parte from "../components/Parte.vue";
import Industrializacion from "../components/Industrializacion.vue";
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
  },
  {
    path: "/clientes",
    name: "Clientes",
    component: Cliente,
    beforeEnter: (to, from, next) => {
      if (!!store.getters.getUser.role && 
          store.getters.getUser.role === "ROLE_ADMIN")
      {
        next();
      } else {
        next("/");
      }
    }
  },
  {
    path: "/materiales",
    name: "Materiales",
    component: Material,
    beforeEnter: (to, from, next) => {
      if (!!store.getters.getUser.role && 
          store.getters.getUser.role === "ROLE_ADMIN")
      {
        next();
      } else {
        next("/");
      }
    }
  },
  {
    path: "/parte/:id",
    name: "Parte",
    component: Parte
  },
  {
    path: "/industrializacion",
    name: "Industrializacion",
    component: Industrializacion
  },
];

const router = new VueRouter({
  routes
});

export default router;
