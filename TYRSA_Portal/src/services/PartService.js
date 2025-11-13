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

    return Vue.axios.put(`/parts/${part.numeroParte}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
  getPart(numeroParte) {
    return Vue.axios.get("/parts/" + numeroParte);
  },
  getPartLog(numeroParte){
    return Vue.axios.get("/parts/log/" + numeroParte);
  }
};
