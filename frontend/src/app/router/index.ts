import { createRouter, createWebHistory } from 'vue-router';
import loginRoutes from './routes/login.routes';
import homeRoutes from './routes/home.routes';
import profileRoutes from './routes/profile.routes';

const routes = [
  ...loginRoutes,
  ...homeRoutes,
  ...profileRoutes,
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _, next) => {
  const token = localStorage.getItem('authToken'); // Получаем токен из localStorage
  
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;