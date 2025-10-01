import Vue from "vue";
export default {
  getAllParts() {
    return Vue.axios.get("/parts");
  },
  createPart(part) {
    return Vue.axios.post("/parts", part);
  },
  updatePart(part) {
    return Vue.axios.put("/parts/" + part.numeroParte, part);
  },
  getPart(numeroParte) {
    return Vue.axios.get("/parts/" + numeroParte);
  },
};
