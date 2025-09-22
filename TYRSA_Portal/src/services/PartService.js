import Vue from "vue";
export default {
  getAllParts() {
    return Vue.axios.get("/parts");
  },
  createPart(part) {
    return Vue.axios.post("/parts", part);
  },
  updateUser(part) {
    return Vue.axios.put("/user/" + part.numeroParte, part);
  },
  getPart(numeroParte) {
    return Vue.axios.get("/user/" + numeroParte);
  },
};
