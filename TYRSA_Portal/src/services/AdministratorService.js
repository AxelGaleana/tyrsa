import Vue from "vue";
export default {
  getAllAdministrators() {
    return Vue.axios.get("/admins");
  },
  createAdministrator(administrator) {
    return Vue.axios.post("/admins", administrator);
  },
  updateAdministrator(administrator) {
    return Vue.axios.post("/admins", administrator);
  },
  getAllRoles() {
    return Vue.axios.get("/roles");
  }
};
