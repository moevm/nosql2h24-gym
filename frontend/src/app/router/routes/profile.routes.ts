import ProfilePage from '../../../pages/Profile/ProfilePage.vue';
// import MyRecordsSection from "../../../pages/Profile/sections/MyRecordsSection.vue";
// import TrainersAndSections from "../../../pages/Profile/sections/TrainersAndSections.vue";
// import AbonementSection from "../../../pages/Profile/sections/AbonementSection.vue";
// import BonusesSection from "../../../pages/Profile/sections/BonusesSection.vue";

import admin from '../../../pages/Profile/sections/temp/admin.vue'
import trainer from '../../../pages/Profile/sections/temp/trainer.vue'
import client from '../../../pages/Profile/sections/temp/client.vue'

export default [
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage,
    meta: { requiresAuth: true }, // Защищенный маршрут
    children: [
      {
        path: 'admin',
        name: 'Admin',
        component: admin,
        meta: { requiresAuth: true },
      },
      {
        path: 'trainer',
        name: 'Trainer',
        component: trainer,
        meta: { requiresAuth: true },
      },
      {
        path: 'client',
        name: 'Client',
        component: client,
        meta: { requiresAuth: true },
      },
      // {
      //   path: 'my-records',
      //   name: 'MyRecords',
      //   component: MyRecordsSection,
      //   meta: { requiresAuth: true },
      // },
      // {
      //   path: 'trainers-and-sections',
      //   name: 'TrainersAndSections',
      //   component: TrainersAndSections,
      //   meta: { requiresAuth: true },
      // },
      // {
      //   path: 'my-abonements',
      //   name: 'MyAbonements',
      //   component: AbonementSection,
      //   meta: { requiresAuth: true },
      // },
      // {
      //   path: 'my-bonuses',
      //   name: 'MyBonuses',
      //   component: BonusesSection,
      //   meta: { requiresAuth: true },
      // },
    ],
  },
];