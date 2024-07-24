import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import request from "@/utils/request";

import VueLazyload from 'vue-lazyload'

import './assets/icon-font/iconfont.css'
Vue.config.productionTip = false;

Vue.use(VueLazyload)

Vue.config.productionTip = false
Vue.use(ElementUI, { size: 'small' });

Vue.prototype.$request=request
Vue.prototype.$baseUrl=process.env.VUE_APP_BASEURL

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
