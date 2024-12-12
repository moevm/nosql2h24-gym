import { adminRoutes } from '../../../features/sidebar/config/adminConfig.ts';
import { clientRoutes } from '../../../features/sidebar/config/clientConfig.ts';
import { trainerRoutes } from '../../../features/sidebar/config/trainerConfig.ts';

import ProfilePage from '../../../pages/Profile/ProfilePage.vue';

export default [
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage,
    meta: { requiresAuth: true }, // Защищенный маршрут
    children: [...clientRoutes, ...trainerRoutes, ...adminRoutes],
  },
];