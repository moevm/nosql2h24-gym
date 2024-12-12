import MoneyReportsSection from '../../../features/sidebar/ui/trainer/MoneyReportsSection.vue';
import MyDataSection from '../../../features/sidebar/ui/trainer/MyDataSection.vue';
import MyScheduleSection from '../../../features/sidebar/ui/trainer/MyScheduleSection.vue';
import TrainReportsSection from '../../../features/sidebar/ui/trainer/TrainReportsSection.vue';

export interface ITrainerForClient {
  id: string;
  name: string;
  surname: string;
  password: string;
  email: string;
  phoneNumber: string;
  experience: number | null;
  hourlyRate: number | null;
  specialization: string | null;
  sections: string[];
  gender: "FEMALE" | "MALE" | "OTHER"; // Adjust if more gender options are needed
  birthday: string; // ISO date string
  createdDate: string; // ISO date string
  updatedDate: string; // ISO date string
}

export interface ITraining {
  id: string;
  trainer: {
    id: string;
    name: string;
    surname: string;
    gender: string | null;
    qualification: string;
    hourlyRate: number;
  };
  section: {
    name: string;
  };
  room: {
    id: string;
    name: string;
    capacity: number;
  };
  startTime: string; // ISO date string
  endTime: string; // ISO date string
  availableSlots: number;
  hasFreeRegistration: boolean;
  status: string | null;
  clients: {
    id: string;
    name: string;
    surname: string;
    gender: string | null;
    loyaltyPoints: number;
    registrationDate: string | null; // ISO date string
  }[];
  createdAt: string; // ISO date string
  updatedAt: string; // ISO date string
}

export const trainerRoutes = [
  {
    path: 'my-data',
    name: 'Мои данные',
    component: MyDataSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'my-schedule',
    name: 'Мое расписание',
    component: MyScheduleSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'money-reports',
    name: 'Отчеты по прибыли',
    component: MoneyReportsSection,
    meta: { requiresAuth: true },
  },
  {
    path: 'train-reports',
    name: 'Отчеты по тренировкам',
    component: TrainReportsSection,
    meta: { requiresAuth: true },
  },
]

export const trainerSections = trainerRoutes.map(item => item.name)
