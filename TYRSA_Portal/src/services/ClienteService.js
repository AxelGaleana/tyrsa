import Vue from "vue";
export default {
  getAllClientes() {
    return Vue.axios.get("/clientes/getAllClientes");
  },
  getAllClientesActivos() {
    return Vue.axios.get("/clientes/getAllClientesActvos");
  },
  crearCliente(cliente) {
    return Vue.axios.post("/clientes", cliente);
  },
  actualizarCliente(cliente) {
    return Vue.axios.put("/clientes/" + cliente.id, cliente);
  },
  getCliente(id) {
    return Vue.axios.get("/clientes/" + id)
  }
};
