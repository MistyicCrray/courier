import Vue from 'vue'
import App from './App'

import courierIndex from './pages/courier-index/courier-index.vue'
Vue.component('courierIndex', courierIndex)

import contract from './pages/contract/contract.vue'
Vue.component('contract', contract)

import cuCustom from './colorui/components/cu-custom.vue'
Vue.component('cu-custom', cuCustom)

import addCourier from './pages/courier-index/add-courier.vue'
Vue.component('addCourier', addCourier)

import user from './pages/user/user.vue'
Vue.component('user', user)
Vue.config.productionTip = false

import moment from 'moment' //导入文件
Vue.prototype.$moment = moment; //赋值使用

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
