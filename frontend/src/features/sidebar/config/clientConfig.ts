import AbonementSection from '../../../features/sidebar/ui/client/AbonementSection.vue';
import MyRecordsSection from '../../../features/sidebar/ui/client/MyRecordsSection.vue';
import TrainersAndSections from '../../../features/sidebar/ui/client/TrainersAndSections.vue';


export const clientRoutes = [
  {
    path: 'trainers-and-sections',
    name: 'Тренеры и секции',
    component: TrainersAndSections,
    meta: { requiresAuth: true },
  },
  {
    path: 'my-records',
    name: 'Мои записи',
    component: MyRecordsSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'my-abonements',
    name: 'Абонемент',
    component: AbonementSection,
    meta: { requiresAuth: true },
  },
]

export const clientSections =  clientRoutes.map(item => item.name)
