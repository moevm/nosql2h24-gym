<template>
  <div v-loading="isLoading">
    <el-container v-if="!userInfoSectionClicked" direction="vertical" class="user-cards" style="gap: 15px;">
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
            <el-button type="danger" @click="handleUserDelete(user)">Удалить</el-button>
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
import { ElNotification } from 'element-plus';

const isLoading = ref(true);
const userInfoSectionClicked = ref(false);
const clickedUser = ref(null);
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
  }).finally(() => {
    isLoading.value = false;
  });
});

// Фильтрация и сортировка
const filteredSortedUsers = computed(() => {
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

// Функция для удаления пользователя
const handleUserDelete = (user) => {
  if (user.roles.includes('ROLE_USER')) {
    // Для пользователей с ролью ROLE_USER проверяем статус абонемента
    axiosInstance.get(`/clients/${user.id}/subscription`).then((res) => {
      const subscription = res.data;
      if (subscription.status === 'active') {
        ElNotification({
          title: 'Ошибка',
          message: 'У этого пользователя есть активный абонемент. Удаление невозможно.',
          type: 'error'
        });
      } else {
        deleteUser(user);
      }
    }).catch((error) => {
      ElNotification({
        title: 'Ошибка',
        message: error.response.data?.errors || 'Неизвестная ошибка',
        type: 'error'
      });
    });
  } else {
    deleteUser(user);
  }
};

// Функция для отправки запроса на удаление пользователя
const deleteUser = (user) => {
  let deleteUrl = '';

  if (user.roles.includes('ROLE_USER')) {
    deleteUrl = `/clients/${user.id}`;
  } else if (user.roles.includes('ROLE_TRAINER')) {
    deleteUrl = `/trainers/${user.id}`;
  } else if (user.roles.includes('ROLE_ADMIN')) {
    deleteUrl = `/admins/${user.id}`;
  }

  axiosInstance.delete(deleteUrl).then(() => {
    ElNotification({
      title: 'Успех',
      message: `Пользователь ${user.name} ${user.surname} успешно удалён.`,
      type: 'success'
    });
    // Обновить список пользователей
    users.value = users.value.filter(u => u.id !== user.id);
  }).catch((error) => {
    ElNotification({
      title: 'Ошибка',
      message: error.response.data?.errors || 'Неизвестная ошибка',
      type: 'error'
    });
  });
};
</script>
