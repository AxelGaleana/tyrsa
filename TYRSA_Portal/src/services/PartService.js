import Vue from "vue";
export default {
  getAllParts() {
    return Vue.axios.get("/parts");
  },
  createPart(part, imageFile) {
    const formData = new FormData();

    const partBlob = new Blob([JSON.stringify(part)], {
      type: 'application/json'
    });

    formData.append('part', partBlob);

    if (imageFile) {
      formData.append('image', imageFile);
    }

    return Vue.axios.post("/parts", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  updatePart(part, imageFile) {
    const formData = new FormData();

    // Convertir el objeto `part` a JSON y agregarlo como Blob
    const partBlob = new Blob([JSON.stringify(part)], {
      type: 'application/json'
    });
    formData.append('part', partBlob);

    // Agregar imagen si se proporciona
    if (imageFile) {
      formData.append('image', imageFile);
    }

    return Vue.axios.put(`/parts/${part.id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  getPart(id) {
    return Vue.axios.get("/parts/" + id);
  },
  getPartLog(rootPartId){
    return Vue.axios.get("/parts/log/" + rootPartId);
  },
  approvePartUpdate(logId){
    return Vue.axios.put("/parts/log/approve/" + logId);
  },
  denyPartUpdate(logId){
    return Vue.axios.put("/parts/log/deny/" + logId);
  }
};
