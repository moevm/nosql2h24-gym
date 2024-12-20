<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column
      label="Период"
      prop="period"
      sortable
    />
    <el-table-column
      label="Значение"
      prop="value"
      sortable
    />
  </el-table>
</template>

<script setup lang="ts">
import { IUserData } from '@/widgets/authController/index.ts';
import axiosInstance from '@/widgets/axios/index.ts';
import { useAuthStore } from '@/widgets/store/auth/index.ts';
import { onMounted, computed, ref } from 'vue';
import { ElTable, ElTableColumn } from 'element-plus';

const authStore = useAuthStore();
const statistics = ref<any>(null);
const tableData = ref<any[]>([]);

const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

// Функция для форматирования данных в таблицу
function formatStatistics(data: any) {
  tableData.value = [
    { period: 'Сегодня', value: data.profitStatistics.today },
    { period: 'На этой неделе', value: data.profitStatistics.this_week },
    { period: 'В этом месяце', value: data.profitStatistics.this_month },
    { period: 'В этом году', value: data.previous_months_profit.total || 0 }
  ];
}

onMounted(() => {
  authStore.loadUserDataFromToken();
  axiosInstance.get(`/trainers/${userInfo.value?.id}/statistics/profit`).then(res => {
    statistics.value = res.data;
    formatStatistics(statistics.value);
  });
});
</script>

<style scoped lang="scss">
</style>
