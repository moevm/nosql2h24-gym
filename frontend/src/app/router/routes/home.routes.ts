import HomePage from '../../../pages/Home/HomePage.vue';

export default [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: true },  // Защищенный маршрут
  },
];