<template>
  <el-container v-loading="isLoading" direction="vertical" class="trainersSections">
    <h2>Тренеры и секции</h2>
    <spacer/>

    <!-- Фильтрация и сортировка -->
    <el-button v-if="filteredSortedTrainers.length > 0" @click="handleOpenFilter">{{ `${isFilterOpened ? 'Закрыть' : 'Открыть'} фильтрацию и поиск` }}</el-button>
    <spacer/>
    <el-container v-if="isFilterOpened" :direction="'vertical'" style="gap: 15px">
      <el-row style="margin-bottom: 20px; gap: 15px;">
        <el-col :span="6">
          <el-input v-model="filters.name" placeholder="Поиск по имени" clearable />
        </el-col>

        <el-col :span="6">
          <el-input v-model="filters.surname" placeholder="Поиск по фамилии" clearable />
        </el-col>

        <el-col :span="6">
          <el-input v-model="filters.qualification" placeholder="Поиск по квалификации" clearable />
        </el-col>

        <el-col :span="6">
          <el-select
            v-model="filters.specialization"
            multiple
            placeholder="Специализация"
            style="width: 100%;"
            clearable
          >
            <el-option :label="'Все'" :value="null" />
            <el-option v-for="section in uniqueSections" :key="section" :label="section" :value="section" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="sortOrder" placeholder="Порядок" style="width: 100%;">
            <el-option label="По возрастанию" value="asc" />
            <el-option label="По убыванию" value="desc" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="sortBy" placeholder="Сортировать по" style="width: 100%;">
            <el-option label="Имя" value="name" />
            <el-option label="Фамилия" value="surname" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="resetFilters">Сбросить фильтры</el-button>
        </el-col>
      </el-row>
    </el-container>

    <!-- Контейнер для карточек тренеров -->
    <el-container v-if="filteredSortedTrainers.length > 0" :direction="'vertical'" style="gap: 15px">
      <el-card
        v-for="trainer of filteredSortedTrainers"
        :key="trainer.id"
        :body-style="{
          display: 'grid',
          gridTemplateColumns: '1fr auto'
        }">
        <div>
          <el-text
            size="large" :type="'primary'" style="font-weight: 700">{{ `${trainer.name} ${trainer.surname}` }}
            <el-button @click="handleTrainerNameClick(trainer)" type="primary" style="margin-left:15px;">Подробнее</el-button>
          </el-text>
          <spacer></spacer>
          <el-text v-if="trainer.sections" :type="'info'">
            Специализация {{ trainer.sections.join(', ') }}
          </el-text>
          <spacer></spacer>
          <el-text :type="'info'">
            Квалификация: {{ trainer.qualification }}
          </el-text>
          <spacer></spacer>
          <el-text :type="'info'">
            Связь {{ trainer.phoneNumber ?? trainer.email }}
          </el-text>
          <spacer></spacer>
        </div>
        <el-button
          @click="handleAssignToTrainerClick(trainer)"
          :type="'primary'"
        >Записаться</el-button>
      </el-card>
    </el-container>
    <el-text v-else>Нет тренеров....</el-text>
  </el-container>

  <assign-to-train-modal
    v-if="trainerIdToAssign"
    :is-opened="isAssignTrainModalOpened"
    :trainer-id="trainerIdToAssign"
    @close="handleAssignTrainModalClose"
  />

  <trainer-info-modal
    :is-opened="isTrainerInfoModalOpened"
    :trainer-data="selectedTrainerData"
    @close="handleTrainerInfoModalClose"
  />
</template>

<script setup lang="ts">
import TrainerInfo from '@/features/sidebar/ui/trainer/components/TrainerInfo.vue';
import TrainerInfoModal from '@/shared/components/modals/TrainerInfoModal.vue';
import { ITrainerForClient } from '../../../../features/sidebar/config/trainerConfig.ts';
import AssignToTrainModal from '../../../../shared/components/modals/AssignToTrainModal.vue';
import Spacer from '../../../../shared/components/Spacer.vue';
import { onMounted, ref, computed } from 'vue';
import axiosInstance from '../../../../widgets/axios/index.ts';

const trainers = ref<ITrainerForClient[]>([]);
const isAssignTrainModalOpened = ref(false);
const isTrainerInfoModalOpened = ref(false);
const selectedTrainerData = ref();
const trainerIdToAssign = ref<string | null>(null);
const isFilterOpened = ref(false);
const isLoading = ref(false);

const filters = ref({
  name: '',
  qualification: '',
  surname: '',
  gender: '',
  birthday: null as string | null,
  specialization: [] as string[], // изменено на массив
});
const sortBy = ref('name');
const sortOrder = ref('asc');

onMounted(async () => {
  isLoading.value = true;
  await axiosInstance.get('/trainers').then(res => {
    trainers.value = res.data as ITrainerForClient[];
  });
  isLoading.value = false;
});

const uniqueSections = computed(() => {
  const sections = new Set<string>();
  trainers.value.forEach(trainer => {
    if (trainer.sections) {
      trainer.sections.forEach(section => sections.add(section));
    }
  });
  return Array.from(sections);
});

const filteredSortedTrainers = computed(() => {
  let filtered = trainers.value.filter(trainer => {
    const nameMatch = filters.value.name
      ? trainer.name.toLowerCase().includes(filters.value.name.toLowerCase())
      : true;
    const surnameMatch = filters.value.surname
      ? trainer.surname.toLowerCase().includes(filters.value.surname.toLowerCase())
      : true;
    const genderMatch = filters.value.gender
      ? trainer.gender.toLowerCase() === filters.value.gender.toLowerCase()
      : true;
    const specializationMatch = filters.value.specialization.length
      ? filters.value.specialization.some(spec => trainer.sections?.includes(spec))
      : true;
    const qualificationMatch = filters.value.qualification
      ? trainer.qualification.toLowerCase().includes(filters.value.qualification.toLowerCase())
      : true;

    return nameMatch && surnameMatch && genderMatch && specializationMatch && qualificationMatch;
  });

  if (sortBy.value) {
    filtered.sort((a, b) => {
      const aValue = a[sortBy.value as keyof ITrainerForClient]?.toString().toLowerCase() || '';
      const bValue = b[sortBy.value as keyof ITrainerForClient]?.toString().toLowerCase() || '';

      if (aValue < bValue) return sortOrder.value === 'asc' ? -1 : 1;
      if (aValue > bValue) return sortOrder.value === 'asc' ? 1 : -1;
      return 0;
    });
  }

  return filtered;
});

const resetFilters = () => {
  filters.value = {
    name: '',
    surname: '',
    gender: '',
    birthday: null,
    specialization: [],
  };
  sortBy.value = 'name';
  sortOrder.value = 'asc';
};

const handleOpenFilter = () => {
  isFilterOpened.value = !isFilterOpened.value;
  resetFilters();
};

const handleAssignToTrainerClick = (trainer: ITrainerForClient) => {
  isAssignTrainModalOpened.value = true;
  trainerIdToAssign.value = trainer.id;
};

const handleAssignTrainModalClose = () => {
  isAssignTrainModalOpened.value = false;
  trainerIdToAssign.value = null;
};

const handleTrainerNameClick = (trainer: ITrainerForClient) => {
  isTrainerInfoModalOpened.value = true;
  selectedTrainerData.value = trainer;
}

const handleTrainerInfoModalClose = () => {
  isTrainerInfoModalOpened.value = false;
  selectedTrainerData.value = null;
};

</script>

<style scoped lang="scss">
/* Кастомные стили для фильтрации и сортировки */
</style>
