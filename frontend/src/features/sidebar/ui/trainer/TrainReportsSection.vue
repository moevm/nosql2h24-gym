<template>
  <div>
    <h1>Статистика тренировок</h1>
    <spacer></spacer>

    <!-- Фильтры -->
    <el-row type="flex" justify="start" align="middle" style="gap: 10px;">
      <el-col>
        <el-input
            v-model.number="filters.aboveClients"
            placeholder="Фильтр по количеству клиентов (≥)"
            type="number"
            clearable
        />
      </el-col>

      <el-col>
        <el-input
            v-model.number="filters.aboveProfit"
            placeholder="Фильтр по прибыли (≥)"
            type="number"
            clearable
        />
      </el-col>

      <el-col>
        <el-date-picker
            v-model="filters.dataRanges"
            type="datetimerange"
            start-placeholder="Начало"
            end-placeholder="Конец"
            range-separator="-"
            format="DD/MM/YYYY HH:mm"
            value-format="YYYY-MM-DDTHH:mm:ssZ"
            :clearable="true"
            placeholder="Фильтровать по времени"
        />
      </el-col>

      <el-col>
        <el-button type="primary" @click="fetchStatistics" :disabled="!isValidDateRange">
          Применить фильтры
        </el-button>
      </el-col>
    </el-row>

    <spacer></spacer>

    <!-- Сообщение об ошибке при неверном периоде -->
    <p v-if="!isValidDateRange" style="color:red">Дата начала не может быть больше даты окончания.</p>

    <spacer></spacer>

    <!-- Таблица -->
    <el-table :data="statistics" style="width: 100%" >
      <el-table-column prop="date" label="Дата" width="150" sortable/>
      <el-table-column prop="time" label="Время" width="120" sortable/>
      <el-table-column prop="clientCount" label="Количество клиентов" width="180" sortable/>
      <el-table-column prop="profit" label="Прибыль" sortable />
    </el-table>

    <!-- Пагинация -->
    <el-pagination
        style="margin-top: 20px; text-align: right;"
        background
        :page-size="pageSize"
        :current-page="currentPage"
        :total="total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
  </div>
</template>


<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { IUserData } from '@/widgets/authController/index.ts';
import axiosInstance from '@/widgets/axios/index.ts';
import { useAuthStore } from '@/widgets/store/auth/index.ts';
import { onMounted, computed, ref, watch } from 'vue';
import dayjs from 'dayjs';
import { ElNotification } from 'element-plus';

const authStore = useAuthStore();
const statistics = ref<any[]>([]);

// Фильтры для поиска
const filters = ref({
  aboveClients: null as number | null,
  aboveProfit: null as number | null,
  dataRanges: [] as [string, string] | []
});

// Пагинация
const currentPage = ref<number>(1); // Номера страниц начинаются с 1
const pageSize = ref<number>(10);
const total = ref<number>(0);

// Валидация диапазона дат
const isValidDateRange = computed(() => {
  if (!filters.value.dataRanges || filters.value.dataRanges.length !== 2) {
    return true;
  }
  const start = dayjs(filters.value.dataRanges[0]);
  const end = dayjs(filters.value.dataRanges[1]);
  return start.isSameOrBefore(end);
});

// Информация о пользователе
const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

// Загрузка данных при монтировании
onMounted(() => {
  authStore.loadUserDataFromToken();
  fetchStatistics();
});

// Функция для запроса статистики с серверной пагинацией
const fetchStatistics = () => {
  if (!isValidDateRange.value) {
    ElNotification.error({
      title: 'Ошибка',
      message: 'Дата начала не может быть больше даты окончания.',
      duration: 5000,
    });
    return;
  }

  // Подготовка данных для запроса
  const requestBody: any = {
    page: currentPage.value - 1, // Сервер ожидает нумерацию с 0
    size: pageSize.value,
    ...(filters.value.aboveProfit !== null && { aboveProfit: filters.value.aboveProfit }),
    ...(filters.value.aboveClients !== null && { aboveClients: filters.value.aboveClients }),
  };

  if (filters.value.dataRanges && filters.value.dataRanges.length === 2) {
    requestBody.dateRangeFrom = dayjs(filters.value.dataRanges[0]).toISOString();
    requestBody.dateRangeTo = dayjs(filters.value.dataRanges[1]).toISOString();
  }

  axiosInstance.post(`/trainers/${userInfo.value?.id}/statistics/trainings`, requestBody)
      .then((res) => {
        // Предполагается, что API возвращает данные в формате:
        // { count: number, data: Array<{ date: string, time: string, clientCount: number, profit: number }> }
        const data = res.data;
        total.value = data.count;
        statistics.value = data.data;
      })
      .catch((error) => {
        console.error('Ошибка при загрузке статистики:', error);
        ElNotification.error({
          title: 'Ошибка',
          message: 'Не удалось загрузить статистику тренировок.',
          duration: 5000,
        });
      });
};

// Обработка изменения страницы
const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchStatistics();
};

// Обработка изменения размера страницы
const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1; // Сброс на первую страницу
  fetchStatistics();
};

// Функция для преобразования даты в метку времени (не требуется, так как используем ISO)
const convertToTimestamp = (dateString: string) => {
  const date = new Date(dateString);
  return date.getTime();
};

// Валидация данных при изменении фильтров
watch(filters, () => {
  currentPage.value = 1; // Сброс на первую страницу при изменении фильтров
}, { deep: true });
</script>


<style scoped lang="scss">
</style>
