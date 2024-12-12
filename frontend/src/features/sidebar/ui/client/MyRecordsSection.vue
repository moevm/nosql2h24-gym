<template>
  <el-container direction="vertical" >
    <h2>Мои записи</h2>
    <spacer></spacer>

    <!-- Фильтрация и сортировка -->
    <el-button v-if="filteredSortedTrainings.length > 0" @click="handleOpenFilter">{{ `${isFilterOpened ? 'Закрыть' : 'Открыть'} фильтрацию и поиск` }}</el-button>
    <el-row v-if="isFilterOpened" style="margin-bottom: 20px; gap: 15px; " >
      <el-col >
        <el-input v-model="filterTrainer" placeholder="Поиск по тренеру" clearable />
      </el-col>

      <el-col>
        <el-select v-model="filterSections" placeholder="Фильтровать по секциям" style="width: 100%;" multiple clearable>
          <el-option v-for="section in uniqueSections" :key="section" :label="section" :value="section" />
        </el-select>
      </el-col>

      <el-col >
        <el-date-picker
          v-model="filterTimeRange"
          type="datetimerange"
          start-placeholder="Начало"
          end-placeholder="Конец"
          range-separator="-"
          format="DD/MM/YYYY HH:mm"
          value-format="DD/MM/YYYY HH:mm"
          :clearable="true"
          placeholder="Фильтровать по времени"
        />
      </el-col>

      <el-col>
        <el-select v-model="sortBy" placeholder="Сортировать по" style="width: 100%;" clearable>
          <el-option label="Тренеру" value="trainer" />
          <el-option label="Время" value="time" />
        </el-select>
      </el-col>

      <el-col>
        <el-select v-model="sortOrder" placeholder="Порядок" style="width: 100%;" clearable>
          <el-option label="По возрастанию" value="asc" />
          <el-option label="По убыванию" value="desc" />
        </el-select>
      </el-col>

      <el-col>
        <el-button type="primary" @click="resetFilters">Сбросить</el-button>
      </el-col>
    </el-row>
    <spacer></spacer>
    <!-- Карточки записей -->
    <el-card
      v-if="filteredSortedTrainings.length > 0"
      v-for="training in filteredSortedTrainings"
      :key="training.id"
      class="training-card"
    >
      <el-container direction="vertical" class="training-info" >
        <p>
          <el-text type="primary" size="large" style="font-weight: 700">{{ training.trainer.name }} {{ training.trainer.surname }}</el-text>
        </p>
        <p>
          <el-text type="primary">Секция:</el-text>
          {{ training.section.name }}
        </p>
        <p>
          <el-text type="primary">Время: </el-text>
          <el-text>{{ formatDate(training.startTime) }} </el-text>
          {{ formatTime(training.startTime) }} - {{ formatTime(training.endTime) }}
        </p>
        <p>
          <el-text type="primary">Зал:</el-text>
          {{ training.room.name }} (Вместимость: {{ training.room.capacity }})
        </p>
      </el-container>
    </el-card>
    <el-text v-else>Нет записей...</el-text>
  </el-container>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { ref, computed, onMounted } from 'vue';
import axiosInstance from '@/widgets/axios/index';
import { useAuthStore } from '@/widgets/store/auth/index';

const isFilterOpened = ref(false);
const handleOpenFilter = () => {
  isFilterOpened.value = !isFilterOpened.value;
  resetFilters();
}

interface Trainer {
  id: string;
  name: string;
  surname: string;
  gender: string | null;
  qualification: string | null;
  hourlyRate: number | null;
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
  section: Section;
  startTime: string;
  endTime: string;
  duration: number;
}

const trainings = ref<Training[]>([]);
const filterTrainer = ref('');
const filterSections = ref<string[]>([]);
const filterTimeRange = ref<[string, string] | null>(null);
const sortBy = ref('trainer');
const sortOrder = ref('asc');

const resetFilters = () => {
  filterTrainer.value = '';
  filterSections.value = [];
  filterTimeRange.value = null;
  sortBy.value = 'trainer';
  sortOrder.value = 'asc';
};

const uniqueSections = computed(() => {
  const sections = new Set<string>();
  trainings.value.forEach(training => {
    if (training.section) {
      sections.add(training.section.name);
    }
  });
  return Array.from(sections);
});

const formatTime = (time: string) => {
  const date = new Date(time);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

const formatDate = (time: string) => {
  const date = new Date(time);
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return `${day}.${month}.${year}`;
};

const authStore = useAuthStore();
onMounted(() => {
  authStore.loadUserDataFromToken();
});

const userInfo = computed(() => authStore.user);

onMounted(() => {
  axiosInstance
    .get(`/clients/${userInfo.value?.id}/trainings`)
    .then(res => {
      trainings.value = res.data;
    });
});

const convertToTimestamp = (dateString: string) => {
  const [day, month, year, hour, minute] = dateString.split(/[\s/:]+/);
  const formattedDate = new Date(`${year}-${month}-${day}T${hour}:${minute}:00`);
  return formattedDate.getTime();
};

const filteredSortedTrainings = computed(() => {
  let filtered = trainings.value;

  if (filterTrainer.value) {
    filtered = filtered.filter(training =>
      `${training.trainer.name} ${training.trainer.surname}`.toLowerCase().includes(filterTrainer.value.toLowerCase())
    );
  }

  if (filterSections.value.length) {
    filtered = filtered.filter(training => filterSections.value.includes(training.section.name));
  }

  if (filterTimeRange.value) {
    const [startRange, endRange] = filterTimeRange.value.map(convertToTimestamp);

    filtered = filtered.filter(training => {
      const trainingStartTimestamp = new Date(training.startTime).getTime();
      const trainingEndTimestamp = new Date(training.endTime).getTime();

      return (trainingStartTimestamp >= startRange && trainingEndTimestamp <= endRange);
    });
  }

  if (sortBy.value === 'trainer') {
    filtered.sort((a, b) => {
      const aTrainer = `${a.trainer.name} ${a.trainer.surname}`;
      const bTrainer = `${b.trainer.name} ${b.trainer.surname}`;
      return sortOrder.value === 'asc' ? aTrainer.localeCompare(bTrainer) : bTrainer.localeCompare(aTrainer);
    });
  } else if (sortBy.value === 'time') {
    filtered.sort((a, b) => {
      const aTime = new Date(a.startTime).getTime();
      const bTime = new Date(b.startTime).getTime();
      return sortOrder.value === 'asc' ? aTime - bTime : bTime - aTime;
    });
  }

  return filtered;
});
</script>

<style scoped>
.training-card {
  margin-bottom: 15px;
  border-radius: 5px;
}

.training-info p {
  margin: 0 0 10px;
}
</style>
