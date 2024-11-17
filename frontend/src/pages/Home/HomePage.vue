<template>
  <el-container class="main">
    <el-header class="main__header">
      <el-row class="is-justify-space-between">
        <el-col :span="4" style="padding: 0 15px">
          <img style="height: 100%; width:150px" src="../../shared/assets/logo.png" alt="Logo">
        </el-col>
        <el-col :span="12" style="text-align: center; align-content: center;">
          <el-row>
            <el-col :span="8">
              <el-link :underline="false" class="white-text" @click="scrollToSection('main')">На главную</el-link>
            </el-col>
            <el-col :span="8">
              <el-link :underline="false" class="white-text" @click="scrollToSection('third')">Тренеры</el-link>
            </el-col>
            <el-col :span="8">
              <el-link :underline="false" class="white-text" @click="scrollToSection('fourth')">Отзывы</el-link>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="4" style="text-align: center">
          <el-button @click="handleLoginButton">{{ !!userInfo ? "В личный кабинет" : "Войти" }}</el-button>
        </el-col>
      </el-row>
    </el-header>
    
    <!-- Секции -->
    <el-main id="main" class="main__section first">
      <div style="padding: 100px">
        <h1 style="font-size: 80px">READY TO TRAIN <br> <span style="color:#3700FF">YOUR BODY</span></h1>
        <Spacer></Spacer>
        <p style="font-size: 18px">Gym training is а structured and disciplined approach to physical exercise that <br>
          focuses on strength, endurance and overall fitness improvement.</p>
        <Spacer size="40"></Spacer>
        <el-button @click="handleLoginButton" type="primary" plain style="font-size: 18px">LETS JOIN NOW</el-button>
      </div>
    </el-main>
    
    <el-main id="second" class="main__section second">
      <img src="../../shared/assets/images/ExperiencePage.jpg" alt="Experience" />
    </el-main>
    <el-main id="third" class="main__section third">
      <img src="../../shared/assets/images/TrainersPage.jpg" alt="Trainers" />
    </el-main>
    <el-main id="fourth" class="main__section fourth">
      <img src="../../shared/assets/images/ReviewersPage.jpg" alt="Reviewers" />
    </el-main>
    
    <el-footer class="main__footer">
      <el-row justify="space-between">
        <span>Privacy | Terms and condition</span>
        <span>2024 All rights reserved. IronGym</span>
      </el-row>
    </el-footer>
  </el-container>
</template>
<script lang="ts" setup>
import router from '../../app/router';
import Spacer from "../../shared/components/Spacer.vue";
import { useAuthStore } from "../../widgets/store/auth";
import { onMounted, computed } from "vue";

const handleLoginButton = () => {
  router.push('/login');
};

const scrollToSection = (sectionId: string) => {
  const section = document.getElementById(sectionId);
  if (section) {
    section.scrollIntoView({
      behavior: 'smooth', // Плавная прокрутка
      block: 'start',     // Начало секции
    });
  }
};

const authStore = useAuthStore();

onMounted(() => {
  authStore.loadUserDataFromToken();
})

// Проверка, если пользователь авторизован
const userInfo = computed(() => {
  return authStore.user;
});
</script>

<style scoped lang="scss">
.main {
  
  &__header {
    border-bottom:2px solid white;
    padding: 15px 0;
    height: 80px;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
  }
  
  &__section {
    padding: 0;
  }
  
  &__section.first {
    background: url('../../shared/assets/images/Ka4okBG.png') center center / cover no-repeat;
    padding-top: 80px;
    height: 100vh;
  }
  
  &__footer {
    padding: 20px;
  }
}

img {
  pointer-events: none;
}

.white-text {
  color: white;
}
</style>
