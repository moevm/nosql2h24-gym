<template>
  <div class="profile">
    <el-button type="danger" @click="logout" style="position: fixed; left: 15px; bottom: 15px;">Выйти</el-button>
    <div class="profile__sidebar">
      <div class="profile__sidebar-header">
        <UserRoleIcon :role="resolvedProfileIcon" />
        <Spacer></Spacer>
        <div class="profile__sidebar-header-info">
          <p style="font-size:18px; word-break: break-word">
            {{ userRolesStr }}
          </p>
          <Spacer></Spacer>
          <p style="font-size: 18px; color: #007BFF; font-weight: bold">
            {{ userEmail }}
          </p>
        </div>
      </div>
      <div class="profile__sidebar-sections">
        <el-card
          v-for="section of sidebarSections"
          :key="section"
          :class="{
            'active': activeSectionName === section
          }"
          class="profile__sidebar-sections-card"
          style="cursor: pointer"
          @click="changeSection(section)"
        >
          {{ section }}
        </el-card>
        <el-card v-if="userInfo?.roles[0] === Roles.ROLE_ADMIN" style="cursor:pointer" @click="onDataExport">
          Экспорт
        </el-card>
        <div v-if="showDownloadButton">
          <el-button type="primary" @click="downloadExportedData">Скачать</el-button>
        </div>
      </div>
    </div>

    <div class="profile__section">
      <router-view/>
    </div>
  </div>
</template>

<script setup lang="ts">
import UserRoleIcon from '@/shared/components/UserRoleIcon.vue';
import axiosInstance from '@/widgets/axios/index.ts';
import { computed, onMounted, ref } from 'vue';

import Spacer from "../../shared/components/Spacer.vue";
import {useAuthStore} from "../../widgets/store/auth";
import {getRoleName, IUserData, Roles} from "../../widgets/authController";
import router from "../../app/router";
import {adminSections} from "../../features/sidebar/config/adminConfig.ts";
import {trainerSections} from "../../features/sidebar/config/trainerConfig.ts";
import {clientSections} from "../../features/sidebar/config/clientConfig.ts";

const authStore = useAuthStore();

onMounted(() => {
  authStore.loadUserDataFromToken();
})

// Проверка, если пользователь авторизован
const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

const userRolesStr = computed(() => {
  if (!userInfo.value) return '';

  return userInfo.value?.roles?.map(item => getRoleName(item)).join(', ')
});

const userEmail = computed(() => {
  if (!userInfo.value) return '';

  return userInfo.value?.email ?? ''
})

const resolvedProfileIcon = ref('client');

const sidebarSections = computed(() => {
  if (!userInfo.value) return [];

  switch (userInfo.value?.roles[0]) {
    case Roles.ROLE_ADMIN:
      resolvedProfileIcon.value = 'admin';
      return adminSections
    case Roles.ROLE_TRAINER:
      resolvedProfileIcon.value = 'trainer';
      return trainerSections
    case Roles.ROLE_USER:
      return clientSections
  }
})

const changeSection = (_section: string) => {
  router.push({name: _section})
}

const activeSectionName = computed(() => {
  return router.currentRoute.value?.name;
})

const logout = () => {
  authStore.logout();
  location.reload();
}

// Данные, которые мы будем экспортировать
const exportedData = ref<any[]>([]);
const showDownloadButton = ref(false);

// Запрос данных для экспорта
const onDataExport = () => {
  showDownloadButton.value = !showDownloadButton.value;
}

// Функция для скачивания в JSON
const downloadExportedData = () => {
  axiosInstance.post('/export')
    .then((res) => {
      exportedData.value = res.data;

      const data = exportedData.value;
      const jsonData = JSON.stringify(data, null, 2); // Красивый формат
      const blob = new Blob([jsonData], { type: 'application/json;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'exported_data.json';
      link.click();

      // После скачивания можно скрыть кнопку, если это нужно
      showDownloadButton.value = false;
    });
}

</script>

<style scoped lang="scss">
.profile {
  min-height: 100vh;

  display: grid;

  grid-template-columns: 300px 1fr;

  &__sidebar {
    border-right: 1px solid seashell;

    display: grid;

    grid-template-rows: 100px 1fr;

    &-sections {
      padding: 15px;
      display: flex;
      flex-direction: column;
      gap: 15px;

      &-card.active {
        background: #007BFF;
      }
    }

    &-header {
      padding: 10px 0 10px 15px;
      border-bottom: 1px solid seashell;
      display: grid;

      grid-template-columns: 30px 20px 1fr;

      img {
        width: 30px;
        height: 30px;
      }
    }
  }


  &__section {
    padding: 15px 0 0 15px;
  }
}
</style>
