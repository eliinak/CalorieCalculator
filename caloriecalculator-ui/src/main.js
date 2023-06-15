import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import * as axios from 'axios'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { DropdownPlugin, TablePlugin, BFormGroup, BDropdown, BFormCheckbox, BButton, BFormInput, BTable, BCard, BFormRadioGroup, BFormTextarea, BImg, BIcon, BPagination, BInputGroupAppend, BDropdownItemButton, BFormSelect, CardPlugin, BInputGroup, BButtonGroup, BFormFile, BCardImg, BTbody, BTr, BTd, BImgLazy, BTableSimple } from 'bootstrap-vue'
import VeeValidate from 'vee-validate'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
  faHome,
  faUser,
  faUserPlus,
  faSignInAlt,
  faSignOutAlt
} from '@fortawesome/free-solid-svg-icons'
Vue.component('font-awesome-icon', FontAwesomeIcon)

library.add(faHome, faUser, faUserPlus, faSignInAlt, faSignOutAlt)
Vue.use(VeeValidate, { fieldsBagName: 'formFields' })
Vue.use(CardPlugin)
// Vue.use(VeeValidate)
Vue.component('b-form-input', BFormInput)
Vue.component('b-form-group', BFormGroup)
Vue.component('b-button', BButton)
Vue.component('b-form-checkbox', BFormCheckbox)
Vue.component('b-dropdown', BDropdown)
Vue.component('b-table', BTable)
Vue.component('b-card', BCard)
Vue.component('b-form-radio-group', BFormRadioGroup)
Vue.component('b-form-textarea', BFormTextarea)
Vue.component('b-img', BImg)
Vue.component('b-icon', BIcon)
Vue.component('b-pagination', BPagination)
Vue.component('b-input-group-append', BInputGroupAppend)
Vue.component('b-dropdown-item-button', BDropdownItemButton)
Vue.component('b-form-select', BFormSelect)
Vue.component('b-input-group', BInputGroup)
Vue.component('b-button-group', BButtonGroup)
Vue.component('b-form-file', BFormFile)
Vue.component('b-card-img', BCardImg)
Vue.component('b-tbody', BTbody)
Vue.component('b-tr', BTr)
Vue.component('b-td', BTd)
Vue.component('b-img-lazy', BImgLazy)
Vue.component('b-table-simple', BTableSimple)
Vue.use(DropdownPlugin)
Vue.use(TablePlugin)
// Vue.use(Chart)

Vue.prototype.$axios = axios
Vue.config.productionTip = false

new Vue({
  router,
  store,
  axios,
  render: h => h(App)
}).$mount('#app')
