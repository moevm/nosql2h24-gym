<template>
  <div v-loading="isLoading" >
    <el-container v-if="!userInfoSectionClicked" direction="vertical" class="user-cards" style="gap: 15px;">

      <!-- Кнопка для открытия/закрытия фильтров -->
      <el-button @click="toggleFilters">
        {{ isFilterOpened ? 'Закрыть фильтрацию и поиск' : 'Открыть фильтрацию и поиск' }}
      </el-button>
      <!-- Фильтрация и сортировка -->
      <el-row v-if="isFilterOpened" style="margin-bottom: 20px; gap: 15px;" type="flex" justify="start">
        <!-- Фильтр по имени -->
        <el-col :span="6">
          <el-input v-model="filters.name" placeholder="Фильтр по имени" clearable />
        </el-col>

        <!-- Фильтр по фамилии -->
        <el-col :span="6">
          <el-input v-model="filters.surname" placeholder="Фильтр по фамилии" clearable />
        </el-col>

        <!-- Фильтр по Email -->
        <el-col :span="6">
          <el-input v-model="filters.email" placeholder="Фильтр по Email" clearable />
        </el-col>

        <!-- Фильтр по телефону -->
        <el-col :span="6">
          <el-input v-model="filters.phoneNumber" placeholder="Фильтр по телефону" clearable />
        </el-col>

        <!-- Фильтр по ролям -->
        <el-col :span="6">
          <el-select v-model="filters.roles" placeholder="Фильтр по ролям" style="width: 100%;" multiple clearable>
            <el-option label="Администратор" value="ROLE_ADMIN" />
            <el-option label="Тренер" value="ROLE_TRAINER" />
            <el-option label="Клиент" value="ROLE_USER" />
            <!-- Добавьте другие роли по необходимости -->
          </el-select>
        </el-col>

        <!-- Фильтр по полу -->
        <el-col :span="6">
          <el-select v-model="filters.gender" placeholder="Фильтр по полу" style="width: 100%;" clearable>
            <el-option label="Мужчина" value="MALE" />
            <el-option label="Женщина" value="FEMALE" />
            <el-option label="Не указано" value="" />
          </el-select>
        </el-col>

        <el-col>
          <el-date-picker
            v-model="filters.createdAt"
            type="daterange"
            range-separator="до"
            start-placeholder="Начало"
            end-placeholder="Конец"
            format="DD.MM.YYYY"
            clearable
          />
        </el-col>

        <!-- Фильтр по комментариям -->
        <el-col :span="6">
          <el-input v-model="filters.comment" placeholder="Фильтр по комментариям" clearable />
        </el-col>

        <!-- Сортировка по полям -->
        <el-col :span="6">
          <el-select v-model="sortBy" placeholder="Сортировать по" style="width: 100%;">
            <el-option label="Имя" value="name" />
            <el-option label="Фамилия" value="surname" />
            <el-option label="Email" value="email" />
            <el-option label="Телефон" value="phoneNumber" />
            <el-option label="Роль" value="roles" />
            <el-option label="Пол" value="gender" />
            <el-option label="Комментарий" value="comment" />
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="sortOrder" placeholder="Порядок" style="width: 100%;">
            <el-option label="По возрастанию" value="asc" />
            <el-option label="По убыванию" value="desc" />
          </el-select>
        </el-col>

        <!-- Кнопка сброса фильтров -->
        <el-col :span="6">
          <el-button type="primary" @click="resetFilters">Сбросить фильтры</el-button>
        </el-col>
      </el-row>
      <spacer></spacer>

      <!-- Карточки пользователей -->
      <el-card v-for="user in filteredSortedUsers" :key="user.id" shadow="hover">
        <el-container style="display: grid; grid-template-columns: 1fr auto">
          <div>
            <h3>{{ user.name }} {{ user.surname }}</h3>
            <spacer></spacer>
            <p>Email: {{ user.email }}</p>
            <spacer></spacer>
            <p>Телефон: {{ user.phoneNumber }}</p>
            <spacer></spacer>
            <p>Роль: {{ user.roles.join(', ') }}</p>
            <spacer></spacer>
            <p>Дата регистрации: {{ new Date(user.createdAt).toLocaleDateString('ru-RU') }}</p>
            <spacer></spacer>
            <p v-if="user.comment">Комментарий: {{ user.comment }}</p>
            <spacer></spacer>
            <el-tag v-if="user.gender === 'MALE'" type="success">Мужчина</el-tag>
            <el-tag v-else-if="user.gender === 'FEMALE'" type="warning">Женщина</el-tag>
            <el-tag v-else type="info">Не указано</el-tag>
          </div>
          <el-container direction="vertical" style="gap: 15px;">
            <el-button type="primary" @click="handleInfoSectionCLick(user)">Подробнее</el-button>
            <el-button type="danger">Удалить</el-button>
          </el-container>
        </el-container>
      </el-card>
    </el-container>
    <UserInfoSection v-else :role="clickedUser.roles[0]" :user-id="clickedUser.id" @close="handleCloseUserSection"/>
  </div>
</template>

<script setup lang="ts">
import UserInfoSection from '@/features/sidebar/ui/admin/sections/UserInfoSection.vue';
import Spacer from '@/shared/components/Spacer.vue';
import axiosInstance from '@/widgets/axios/index.ts';
import { onMounted, ref, computed } from 'vue';

const isLoading = ref(true)
const userInfoSectionClicked = ref(false);
const clickedUser = ref();
const users = ref([]);
const isFilterOpened = ref(false);

// Фильтры для каждого поля
const filters = ref({
  name: '',
  surname: '',
  email: '',
  phoneNumber: '',
  roles: [] as string[],
  gender: '',
  comment: '',
  createdAt: null,
});

// Сортировка
const sortBy = ref('name');
const sortOrder = ref('asc');

const handleInfoSectionCLick = (user) => {
  userInfoSectionClicked.value = true;
  clickedUser.value = user;
};

const handleCloseUserSection = () => {
  userInfoSectionClicked.value = false;
  clickedUser.value = null;
};

// Функция для переключения видимости фильтров
const toggleFilters = () => {
  isFilterOpened.value = !isFilterOpened.value;
  if (!isFilterOpened.value) {
    resetFilters();
  }
};

// Функция сброса фильтров
const resetFilters = () => {
  filters.value = {
    name: '',
    surname: '',
    email: '',
    phoneNumber: '',
    roles: [],
    gender: '',
    comment: ''
  };
  sortBy.value = 'name';
  sortOrder.value = 'asc';
};

// Загрузка данных при монтировании
onMounted(() => {
  isLoading.value = true;
  axiosInstance.get('/admins/users').then(res => {
    users.value = res.data;
  }).catch(error => {
    console.error('Ошибка при загрузке пользователей:', error);
  });

  isLoading.value = false;
});

// Фильтрация и сортировка
const filteredSortedUsers = computed<any>(() => {
  let filtered = users.value;

  // Фильтрация по каждому полю
  if (filters.value.name) {
    filtered = filtered.filter(user =>
      user.name.toLowerCase().includes(filters.value.name.toLowerCase())
    );
  }

  if (filters.value.surname) {
    filtered = filtered.filter(user =>
      user.surname.toLowerCase().includes(filters.value.surname.toLowerCase())
    );
  }

  if (filters.value.email) {
    filtered = filtered.filter(user =>
      user.email.toLowerCase().includes(filters.value.email.toLowerCase())
    );
  }

  if (filters.value.phoneNumber) {
    filtered = filtered.filter(user =>
      user.phoneNumber.toLowerCase().includes(filters.value.phoneNumber.toLowerCase())
    );
  }

  if (filters.value.roles.length > 0) {
    filtered = filtered.filter(user =>
      filters.value.roles.every(role => user.roles.includes(role))
    );
  }

  if (filters.value.gender) {
    filtered = filtered.filter(user =>
      user.gender === filters.value.gender
    );
  }

  if (filters.value.comment) {
    filtered = filtered.filter(user =>
      user.comment && user.comment.toLowerCase().includes(filters.value.comment.toLowerCase())
    );
  }

  if (filters.value.createdAt) {
    const [start, end] = filters.value.createdAt;
    filtered = filtered.filter(user => {
      const createdDate = new Date(user.createdAt);
      return createdDate >= new Date(start) && createdDate <= new Date(end);
    });
  }

  // Сортировка
  if (sortBy.value) {
    filtered = filtered.slice().sort((a, b) => {
      let aValue: string | number = '';
      let bValue: string | number = '';

      switch (sortBy.value) {
        case 'name':
          aValue = a.name.toLowerCase();
          bValue = b.name.toLowerCase();
          break;
        case 'surname':
          aValue = a.surname.toLowerCase();
          bValue = b.surname.toLowerCase();
          break;
        case 'email':
          aValue = a.email.toLowerCase();
          bValue = b.email.toLowerCase();
          break;
        case 'phoneNumber':
          aValue = a.phoneNumber.toLowerCase();
          bValue = b.phoneNumber.toLowerCase();
          break;
        case 'roles':
          aValue = a.roles.join(', ').toLowerCase();
          bValue = b.roles.join(', ').toLowerCase();
          break;
        case 'gender':
          aValue = a.gender ? a.gender.toLowerCase() : '';
          bValue = b.gender ? b.gender.toLowerCase() : '';
          break;
        case 'comment':
          aValue = a.comment ? a.comment.toLowerCase() : '';
          bValue = b.comment ? b.comment.toLowerCase() : '';
          break;
        default:
          aValue = '';
          bValue = '';
      }

      if (aValue < bValue) return sortOrder.value === 'asc' ? -1 : 1;
      if (aValue > bValue) return sortOrder.value === 'asc' ? 1 : -1;
      return 0;
    });
  }

  return filtered;
});
</script>

<style scoped lang="scss">
/* Можно добавить кастомные стили для фильтра и сортировки */
</style>
