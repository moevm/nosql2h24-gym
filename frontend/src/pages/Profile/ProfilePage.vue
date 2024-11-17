<template>
  <div class="profile">
    <el-button type="danger" @click="logout" style="position: absolute; right: 15px; top: 15px;">Выйти</el-button>
    <div class="profile__sidebar">
      <div class="profile__sidebar-header">
        <img width="30px" height="30px" src="../../shared/assets/icons/anonim.svg" alt="">
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
          :class="{
            'active': activeSectionName === section
          }"
          
          class="profile__sidebar-sections-card"
          style="cursor: pointer"
          @click="changeSection(section)"
        >
          {{ section }}
        </el-card>
      </div>
    </div>
    
    <div class="profile__section">
      <router-view/>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";

import Spacer from "../../shared/components/Spacer.vue";
import { useAuthStore } from "../../widgets/store/auth";
import { getRoleName, IUserData, Roles } from "../../widgets/authController";
import router from "../../app/router";

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

const sidebarSections = computed(() => {
  if (!userInfo.value) return [];
  
  switch(userInfo.value?.roles[0]) {
    case Roles.ROLE_ADMIN:
      return [
        'Admin',
        'Trainer',
        'Client',
      ]
    case Roles.ROLE_TRAINER:
      return [
        'Trainer',
      ]
    case Roles.ROLE_USER:
      return [
        'Client',
      ]
  }
})

const changeSection = (_section: string) => {
  router.push({ name: _section })
}

const activeSectionName = computed(() => {
  return router.currentRoute.value?.name;
})

const logout = () => {
  authStore.logout();
  location.reload();
}

</script>

<style scoped lang="scss">
.profile {
  min-height: 100vh;
  background: #242424;
  
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

.el-card {
  background: #00254D;
  color: white;
  border: 0;
}
</style>