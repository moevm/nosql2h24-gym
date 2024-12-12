<template>
  <div>
    <h1>Статистика тренировок</h1>
    <el-table :data="statistics" style="width: 100%" v-if="statistics">
      <el-table-column prop="date" label="Дата" width="150" sortable/>
      <el-table-column prop="time" label="Время" width="120" sortable/>
      <el-table-column prop="clientCount" label="Количество клиентов" width="180" sortable/>
      <el-table-column prop="profit" label="Прибыль" sortable />
    </el-table>

    <el-text v-else>Статистика отсутствует</el-text>
  </div>
</template>

<script setup lang="ts">
import { IUserData } from '@/widgets/authController/index.ts';
import axiosInstance from '@/widgets/axios/index.ts';
import { useAuthStore } from '@/widgets/store/auth/index.ts';
import { ElTableColumn } from 'element-plus';
import { onMounted, computed, ref } from 'vue';

const authStore = useAuthStore();
const statistics = ref<any[]>([]);

onMounted(() => {
  authStore.loadUserDataFromToken();
  axiosInstance.get(`/trainers/${userInfo.value?.id}/statistics/trainings`).then((res) => {
    statistics.value = res.data;
  });
});

const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

</script>

<style scoped lang="scss">
</style>