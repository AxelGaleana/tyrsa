import Vue from "vue";
export default {
  //logout(id) {
  logout() {
    //const url = "/auth/logout/" + id;
    const url = "/auth/logout";
    return Vue.axios.post(url);
  }
};
