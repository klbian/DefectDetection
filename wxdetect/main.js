import Vue from 'vue'
import App from './App'

import util from './common/util'
import '@/components/interceptor.js';//引入拦截

Vue.config.productionTip = false

App.mpType = 'app'



Vue.prototype.$util = util

const app = new Vue({

    ...App
})
app.$mount()
