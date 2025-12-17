import Vue from "vue";
export default {
  getAllMaterials() {
    return Vue.axios.get("/materiales/getAllMaterials");
  },
  getAllMaterialsActivos() {
    return Vue.axios.get("/materiales/getAllMaterialsActvos");
  },
  crearMaterial(material) {
    return Vue.axios.post("/materiales", material);
  },
  actualizarMaterial(material) {
    return Vue.axios.put("/materiales/" + material.id, material);
  },
  getMaterial(id) {
    return Vue.axios.get("/materiales/" + id)
  }
};
