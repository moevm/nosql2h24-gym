import ExportSection from '../../../features/sidebar/ui/admin/ExportSection.vue';
import ImportSection from '../../../features/sidebar/ui/admin/ImportSection.vue';
import ReportsSection from '../../../features/sidebar/ui/admin/ReportsSection.vue';
import ServiceInfoSection from '../../../features/sidebar/ui/admin/ServiceInfoSection.vue';
import UsersSection from '../../../features/sidebar/ui/admin/UsersSection.vue';

export const adminRoutes = [
  {
    path: 'users',
    name: 'Пользователи',
    component: UsersSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'service-info',
    name: 'Служебная информация',
    component: ServiceInfoSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'reports',
    name: 'Статистика',
    component: ReportsSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'imports',
    name: 'Импорт',
    component: ImportSection,
    meta: { requiresAuth: true },
  },
]

export const adminSections = adminRoutes.map(item => item.name)