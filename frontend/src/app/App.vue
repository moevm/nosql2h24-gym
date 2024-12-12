<template>
  <div>
    <!-- Кнопка переключения темы -->
    <el-button @click="toggleTheme" style="position: fixed; left: 15px; bottom: 60px; z-index: 9999;">
      Переключить тему
    </el-button>

    <router-view /> <!-- Отображение контента маршрутов -->
  </div>
</template>

<script setup lang="ts">
// Импортируем стили тёмной темы Element Plus
import 'element-plus/theme-chalk/dark/css-vars.css';
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const isDark = ref(false);

const applyTheme = (dark: boolean) => {
  if (dark) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
};

const toggleTheme = () => {
  isDark.value = !isDark.value;
  applyTheme(isDark.value);
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light');
};

onMounted(() => {
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'light') {
    isDark.value = false;
  } else {
    // По умолчанию темная
    isDark.value = true;
  }
  applyTheme(isDark.value);
});
</script>

<style scoped lang="scss">
* {
  font-size: 14px;
}
</style>
