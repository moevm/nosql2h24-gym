import { createApp } from 'vue'
import App from './app/App.vue'
import router from './app/router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './shared/assets/css/reset.css';
import { createPinia } from "pinia";

const app = createApp(App);
const pinia = createPinia()

app
	.use(pinia)
  .use(router)
  .use(ElementPlus)

app.mount('#app');
