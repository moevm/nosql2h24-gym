<template>
  <el-dialog
    @close="emits('close')"
    :model-value="isOpened"
    style="background: #00254D; color: white"
  >
    <template #title>
      <el-text :type="'primary'" :size="'large'">
        <strong>Запись на тренировку</strong>
      </el-text>
    </template>
    <el-text v-if="trainings[0]" :size="'large'">
      <el-text :type="'primary'" :size="'large'">
        <strong>Тренер {{ trainings[0].trainer.name }} {{ trainings[0].trainer.surname }}</strong>
      </el-text>
      <br>
      <el-text :size="'default'">
        Зал {{ trainings[0].room.name }}
      </el-text>
    </el-text>
    <spacer />
    <el-card
      v-if="trainings.length > 0"
      v-for="training in trainings"
      :key="training.id"
      class="training-card"
      :body-style="{ background: '#00254D' }"
    >
      <div class="training-info">
        <el-text><strong>Доступных мест:</strong> {{ training.availableSlots }}</el-text>
        <Spacer></Spacer>
        <el-text><strong>Время:</strong> {{ formatTime(training.startTime) }} - {{ formatTime(training.endTime) }}</el-text>
        <Spacer></Spacer>
        <el-text><strong>Специализация:</strong> {{ training.section.name }}</el-text>
        <Spacer></Spacer>
        <el-container v-if="training.availableSlots !== 0">
          <!-- Кнопка подтверждения только для выбранной тренировки -->
          <el-button
            v-if="selectedTrainingId === training.id"
            @click="handleConfirmAssignToTraining(training)"
            type="success"
          >
            Подтвердить
          </el-button>
          <el-button @click="toggleSelect(training.id)" type="primary">
            {{ selectedTrainingId === training.id ? 'Отменить' : 'Выбрать' }}
          </el-button>
        </el-container>
      </div>
    </el-card>
    <el-text v-else>
      Нет тренировок у данного тренера...
    </el-text>
  </el-dialog>
</template>

<script setup lang="ts">
import { ITraining } from '@/features/sidebar/config/trainerConfig';
import Spacer from '@/shared/components/Spacer.vue';
import { IUserData } from '@/widgets/authController/index';
import { useAuthStore } from '@/widgets/store/auth/index';
import { ref, onMounted, computed } from 'vue';
import axiosInstance from '../../../widgets/axios/index';

interface Trainer {
  id: string;
  name: string;
  surname: string;
  gender: string | null;
  qualification: string;
  hourlyRate: number;
}

interface Room {
  id: string;
  name: string;
  capacity: number;
}

interface Section {
  name: string;
}

interface Training {
  id: string;
  trainer: Trainer;
  room: Room;
  availableSlots: number;
  section: Section;
  startTime: string;
  endTime: string;
}

const authStore = useAuthStore();

onMounted(() => {
  authStore.loadUserDataFromToken();
})

// Проверка, если пользователь авторизован
const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

const emits = defineEmits([ 'close' ]);
const selectedTrainingId = ref<string | null>(null); // Хранит id выбранной тренировки

// Функция для обработки подтверждения записи на тренировку
const handleConfirmAssignToTraining = (training: ITraining) => {
  axiosInstance.post(`/trainings/${training.id}/registration/${userInfo.value?.id}`).then(() => {
    alert('Запись успешна');
    emits('close')
  });
}

// Переключение состояния выбора тренировки
const toggleSelect = (trainingId: string) => {
  if (selectedTrainingId.value === trainingId) {
    selectedTrainingId.value = null; // Отменить выбор
  } else {
    selectedTrainingId.value = trainingId; // Выбрать тренировку
  }
};

const props = defineProps({
  trainerId: {
    type: String,
    required: true,
  },
  isOpened: {
    type: Boolean,
    required: true,
  },
});

const trainings = ref<Training[]>([]);

const formatTime = (time: string) => {
  const date = new Date(time);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

onMounted(() => {
  axiosInstance
    .get(`/trainers/${props.trainerId}/trainings`)
    .then((res) => {
      trainings.value = res.data;
    });
});
</script>

<style scoped>
.training-card {
  margin-bottom: 15px;
}

.training-info p {
  margin: 0 0 5px;
}

span {
  color: white;
}
</style>
