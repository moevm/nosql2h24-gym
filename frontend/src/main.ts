import { createApp } from 'vue'
import App from './app/App.vue'
import router from './app/router';
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import 'element-plus/theme-chalk/dark/css-vars.css'
import Ru from 'element-plus/es/locale/lang/ru'
import 'dayjs/locale/ru'

import './shared/assets/css/reset.css';
import { createPinia } from "pinia";

const app = createApp(App);
const pinia = createPinia()

app
	.use(pinia)
  .use(router)
  .use(ElementPlus, {
		locale: Ru,
	})

app.mount('#app');
