import "@mdi/font/css/materialdesignicons.css";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import XLSX from "xlsx";
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import axios from "axios";
import VueAxios from "vue-axios";

import VCalendar from 'v-calendar';

// Use v-calendar & v-date-picker components
Vue.use(VCalendar, {
  componentPrefix: 'vc',  // Use <vc-calendar /> instead of <v-calendar />
});

axios.defaults.baseURL = process.env.VUE_APP_HUB_API_BASEURL;

Vue.config.productionTip = false;
Vue.use(VueAxios, axios);

Vue.mixin({
  methods: {
    exportToExcel(selected, name) {
      // export json to Worksheet of Excel
      // only array possible
      let selectedWS = XLSX.utils.json_to_sheet(selected);
      // A workbook is the name given to an Excel file
      let wb = XLSX.utils.book_new(); // make Workbook of Excel
      // add Worksheet to Workbook
      // Workbook contains one or more worksheets
      XLSX.utils.book_append_sheet(wb, selectedWS, name); // sheetAName is name of Worksheet
      // export Excel file
      XLSX.writeFile(wb, name + ".xlsx"); // name of the file is 'book.xlsx'
    },
    exportToHtml(selected, name) {
      // export json to Worksheet of Excel
      // only array possible
      let selectedWS = XLSX.utils.json_to_sheet(selected);
      // A workbook is the name given to an Excel file
      let wb = XLSX.utils.book_new(); // make Workbook of Excel
      // add Worksheet to Workbook
      // Workbook contains one or more worksheets
      XLSX.utils.book_append_sheet(wb, selectedWS, name); // sheetAName is name of Worksheet
      // export Excel file
      XLSX.writeFile(wb, name + ".html"); // name of the file is 'book.xlsx'
    }
  }
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
