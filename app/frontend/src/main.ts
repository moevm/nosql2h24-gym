import { createApp } from 'vue'
import App from './app/App.vue'
import router from './app/router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './shared/assets/css/reset.css';

const app = createApp(App);

app
  .use(router)
  .use(ElementPlus)

app.mount('#app');
