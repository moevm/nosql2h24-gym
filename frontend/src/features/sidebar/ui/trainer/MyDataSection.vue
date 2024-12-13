<template>
  <div v-if="trainerInfo">
    <el-card>
      <el-form :model="trainerInfo" :disabled="!editMode" label-width="120px">
        <el-form-item label="Имя" label-position="top">
          <el-input v-model="trainerInfo.name" />
        </el-form-item>
        <el-form-item label="Фамилия" label-position="top">
          <el-input v-model="trainerInfo.surname" />
        </el-form-item>
        <el-form-item label="Email" label-position="top">
          <el-input v-model="trainerInfo.email" />
        </el-form-item>
        <el-form-item label="Номер телефона" label-position="top">
          <el-input v-model="trainerInfo.phoneNumber" />
        </el-form-item>
        <el-form-item label="Комментарий" label-position="top">
          <el-input v-model="trainerInfo.comment" />
        </el-form-item>
        <el-form-item label="Ставка (час)" label-position="top">
          <el-input-number v-model="trainerInfo.trainerInfo.hourlyRate" :min="250" :step="250" />
        </el-form-item>
        <el-form-item label="Квалификация" label-position="top">
          <el-select v-model="trainerInfo.trainerInfo.qualification" placeholder="Выберите квалификацию">
            <el-option v-for="qualification in qualifications" :key="qualification" :label="qualification" :value="qualification" />
          </el-select>
        </el-form-item>

        <el-form-item :label="`Секции${editMode ? '(для ввода значения нужно нажать enter)' : ''}`" label-position="top">
          <el-tag
            v-for="(section, index) in trainerInfo.trainerInfo.sections"
            :key="section"
            :closable="editMode"
            @close="removeSection(index)"
          >
            {{ section }}
          </el-tag>
          <el-input
            v-if="editMode"
            v-model="newSection"
            placeholder="Добавить секцию"
            size="small"
            @keyup.enter="addSection"
          />
        </el-form-item>
      </el-form>
      <div style="margin-top: 15px; text-align: right">
        <el-button v-if="!editMode" type="primary" @click="toggleEdit">Редактировать</el-button>
        <el-button v-else type="danger" @click="cancelEdit">Отменить</el-button>
        <el-button v-if="editMode" type="success" @click="saveChanges">Сохранить</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { IUserData } from '@/widgets/authController/index.ts';
import { useAuthStore } from '@/widgets/store/auth/index.ts';
import { ref, onMounted, computed } from 'vue';
import axiosInstance from '@/widgets/axios/index';

const trainerInfo = ref<any | null>(null);
const editMode = ref(false);
const originalTrainerInfo = ref<any | null>(null);
const newSection = ref('');

const qualifications = ref([
  '1 разряд',
  '2 разряд',
  '3 разряд',
  'Кандидат в мастера спорта',
  'Мастер спорта',
  'Заслуженный мастер спорта'
]);

const toggleEdit = () => {
  editMode.value = true;
  originalTrainerInfo.value = JSON.parse(JSON.stringify(trainerInfo.value)); // Копия данных
};

const cancelEdit = () => {
  editMode.value = false;
  trainerInfo.value = JSON.parse(JSON.stringify(originalTrainerInfo.value)); // Откат изменений
};

const saveChanges = () => {
  axiosInstance.put(`/trainers/${trainerInfo.value?.id}`, trainerInfo.value).then(() => {
    editMode.value = false;
  });
};

const addSection = () => {
  if (newSection.value.trim() && !trainerInfo.value?.trainerInfo.sections.includes(newSection.value.trim())) {
    trainerInfo.value?.trainerInfo.sections.push(newSection.value.trim());
    newSection.value = '';
  }
};

const removeSection = (index: number) => {
  trainerInfo.value?.trainerInfo.sections.splice(index, 1);
};

const authStore = useAuthStore();

onMounted(() => {
  authStore.loadUserDataFromToken();
});

// Проверка, если пользователь авторизован
const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

onMounted(() => {
  axiosInstance.get(`/trainers/${userInfo.value?.id}`).then((res) => {
    trainerInfo.value = res.data;
  });
});
</script>
