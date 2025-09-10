import Vue from "vue";
export default {
  getAllUsers() {
    return Vue.axios.get("/user/getAllUsers");
  },
  createUser(user) {
    return Vue.axios.post("/user/register", user);
  },
  updateUser(user) {
    return Vue.axios.put("/user/" + user.username, user);
  },
  getAllRoles() {
    return Vue.axios.get("/user/getAllRoles");
  },
  updatePassword(username, passwordRequest) {
    return Vue.axios.put("/user/" + username + "/password", passwordRequest);
  }
};
