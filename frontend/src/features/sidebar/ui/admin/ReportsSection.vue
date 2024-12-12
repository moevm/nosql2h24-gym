<template>
  <div>
    <h2>Статистика</h2>

    <el-container style="padding-top:5px; gap:10px;">
      <el-button @click="changeTab('abonements')" :type="currentTab === 'abonements' ? 'primary' : ''">
        Статистика по абонементам
      </el-button>
      <el-button @click="changeTab('trainings')" :type="currentTab === 'trainings' ? 'primary' : ''">
        Статистика по занятиям
      </el-button>
    </el-container>

    <!-- Вкладка статистики по абонементам -->
    <el-container v-if="currentTab === 'abonements'" style="margin-top: 20px; gap: 15px;" direction="vertical">
      <el-row style="gap: 10px;" type="flex" align="middle">
        <!-- Фильтр по клиенту -->
        <el-col :span="6">
          <el-select v-model="clientId" placeholder="Выберите клиента" clearable filterable>
            <el-option
              v-for="client in clients"
              :key="client.id"
              :label="client.name + ' ' + client.surname"
              :value="client.id"
            />
          </el-select>
        </el-col>

        <!-- Выбор периода дат и времени -->
        <el-col :span="8">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            start-placeholder="Дата начала"
            end-placeholder="Дата окончания"
            format="DD-MM-YYYY HH:mm:ss"
            value-format="DD-MM-YYYY HH:mm:ss"
            clearable
            style="width: 100%;"
            @clear="handleDateRangeClear"
          />
        </el-col>

        <!-- Статус -->
        <el-col :span="4">
          <el-select v-model="status" placeholder="Статус" clearable>
            <el-option label="Все" value=""></el-option>
            <el-option label="Активен" value="ACTIVE"></el-option>
            <el-option label="Заморожен" value="FREEZE"></el-option>
            <el-option label="Неактивен" value="INACTIVE"></el-option>
          </el-select>
        </el-col>

        <!-- Кнопка обновления -->
        <el-col :span="4">
          <el-button type="primary" @click="fetchDataAbonements" :disabled="!isValidDateRange">Обновить</el-button>
        </el-col>
      </el-row>

      <!-- Сообщение об ошибке при неверном периоде -->
      <p v-if="!isValidDateRange" style="color:red">Дата начала не может быть больше даты окончания.</p>

      <Spacer></Spacer>

      <el-table :data="subscriptions" style="width:100%">
        <el-table-column prop="id" label="ID Абонемента" width="200" sortable/>
        <el-table-column prop="clientName" label="Клиент" sortable/>
        <el-table-column prop="startDate" label="Дата начала" sortable/>
        <el-table-column prop="endDate" label="Дата окончания" sortable/>
        <el-table-column prop="status" label="Статус" sortable/>
      </el-table>

      <el-pagination
        style="margin-top: 10px;"
        background
        :page-size="size"
        :total="total"
        :current-page="page + 1"
        @current-change="handlePageChangeAbonements"
        @size-change="handleSizeChangeAbonements"
        :page-sizes="[5,10,20,50]"
        layout="total, sizes, prev, pager, next"
      ></el-pagination>

      <Spacer></Spacer>
    </el-container>

    <!-- Вкладка статистики по занятиям -->
    <el-container v-else-if="currentTab === 'trainings'" style="margin-top: 20px; gap: 15px;" direction="vertical">
      <el-row style="gap: 10px;" type="flex" align="middle">
        <!-- Фильтр по типу тренировки -->
        <el-col :span="6">
          <el-select v-model="trainingType" placeholder="Тип тренировки" clearable>
            <el-option label="Все" value=""></el-option>
            <el-option label="Йога" value="Йога"></el-option>
            <el-option label="Бокс" value="Бокс"></el-option>
            <el-option label="Атлетика" value="Атлетика"></el-option>
            <el-option label="Бег" value="Бег"></el-option>
            <el-option label="Бодибилдинг" value="Бодибилдинг"></el-option>
          </el-select>
        </el-col>

        <!-- Выбор периода дат и времени -->
        <el-col :span="8">
          <el-date-picker
            v-model="dateRangeTrainings"
            type="datetimerange"
            start-placeholder="Дата начала"
            end-placeholder="Дата окончания"
            format="DD-MM-YYYY HH:mm:ss"
            value-format="DD-MM-YYYY HH:mm:ss"
            clearable
            style="width: 100%;"
            @clear="handleDateRangeClearTrainings"
          />
        </el-col>

        <!-- Кнопка обновления -->
        <el-col :span="4">
          <el-button type="primary" @click="fetchDataTrainings" :disabled="!isValidDateRangeTrainings">Обновить</el-button>
        </el-col>
      </el-row>

      <!-- Сообщение об ошибке при неверном периоде -->
      <p v-if="!isValidDateRangeTrainings" style="color:red">Дата начала не может быть больше даты окончания.</p>

      <Spacer></Spacer>
      <el-table :data="trainingsData" style="width:100%">
        <el-table-column prop="sectionName" label="Тип тренировки" sortable/>
        <el-table-column prop="trainingCount" label="Количество проведенных" sortable/>
        <el-table-column prop="clientCount" label="Количество клиентов" sortable/>
        <el-table-column prop="loadPercentage" label="Загруженность" sortable/>
      </el-table>

      <el-pagination
        style="margin-top: 10px;"
        background
        :page-size="sizeTrainings"
        :total="totalTrainings"
        :current-page="pageTrainings + 1"
        @current-change="handlePageChangeTrainings"
        @size-change="handleSizeChangeTrainings"
        :page-sizes="[5,10,20,50]"
        layout="total, sizes, prev, pager, next"
      ></el-pagination>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import axiosInstance from '@/widgets/axios/index.ts';
import Spacer from '@/shared/components/Spacer.vue';
import { ElNotification } from 'element-plus';
import { ref, onMounted, computed } from 'vue';
import dayjs from 'dayjs';

const currentTab = ref<'abonements' | 'trainings' | null>(null);

const changeTab = (tab: 'abonements' | 'trainings') => {
  currentTab.value = tab;
  localStorage.setItem('currentTab', tab);
  if (tab === 'abonements') {
    fetchDataAbonements();
  } else if (tab === 'trainings') {
    fetchDataTrainings();
  }
};

const savedTab = localStorage.getItem('currentTab');
if (savedTab === 'trainings') {
  currentTab.value = 'trainings';
} else {
  currentTab.value = 'abonements';
}

// -------------------- Статистика по абонементам --------------------
const clientId = ref<string | null>(null);
const dateRange = ref<(string | null)[]>([
  dayjs().startOf('year').format("DD-MM-YYYY 00:00:00"),
  dayjs().endOf('year').format("DD-MM-YYYY 23:59:59")
]);
const status = ref<string>('');
const page = ref<number>(0);
const size = ref<number>(10);
const subscriptions = ref<any[]>([]);
const total = ref<number>(0);
const clients = ref<any[]>([]);

const isValidDateRange = computed(() => {
  if (!dateRange.value || !dateRange.value[0] || !dateRange.value[1]) {
    return true;
  }
  const start = dayjs(dateRange.value[0], 'DD-MM-YYYY HH:mm:ss');
  const end = dayjs(dateRange.value[1], 'DD-MM-YYYY HH:mm:ss');
  return start.isSameOrBefore(end);
});

const requestParams = computed(() => {
  let formattedStart = '';
  let formattedEnd = '';
  if (dateRange.value && dateRange.value[0] && dateRange.value[1]) {
    formattedStart = dayjs(dateRange.value[0], 'DD-MM-YYYY HH:mm:ss').format('YYYY-MM-DD');
    formattedEnd = dayjs(dateRange.value[1], 'DD-MM-YYYY HH:mm:ss').format('YYYY-MM-DD');
  }

  const params: any = {
    page: page.value,
    size: size.value,
    startDate: formattedStart,
    endDate: formattedEnd
  };

  if (clientId.value) {
    params.clientId = clientId.value;
  }

  if (status.value) {
    params.status = status.value;
  }

  return params;
});

const fetchDataAbonements = () => {
  if (!isValidDateRange.value) {
    return;
  }

  axiosInstance.get('/admins/statistics/subscriptions', { params: requestParams.value })
    .then(res => {
      const data = res.data;
      total.value = data.count;

      subscriptions.value = data.subscriptions.map((item: any) => {
        return {
          id: item.client.id,
          clientName: item.client.name + ' ' + item.client.surname,
          startDate: item.startDate ? item.startDate : 'Не указано',
          endDate: item.endDate ? item.endDate : 'Не указано',
          status: item.status
        }
      });
    })
    .catch(error => {
      ElNotification.error({
        title: 'Ошибка',
        message: 'Ошибка при загрузке статистики абонементов',
        duration: 5000,
      });
    });
};

const handleDateRangeClear = () => {
  dateRange.value = [
    dayjs().startOf('year').format("DD-MM-YYYY 00:00:00"),
    dayjs().endOf('year').format("DD-MM-YYYY 23:59:59")
  ];
};

const handlePageChangeAbonements = (val: number) => {
  page.value = val - 1;
  fetchDataAbonements();
};

const handleSizeChangeAbonements = (val: number) => {
  size.value = val;
  page.value = 0;
  fetchDataAbonements();
};

// -------------------- Статистика по занятиям --------------------
const trainingType = ref<string>('');
const dateRangeTrainings = ref<(string | null)[]>([
  dayjs().startOf('year').format("DD-MM-YYYY 00:00:00"),
  dayjs().endOf('year').format("DD-MM-YYYY 23:59:59")
]);
const pageTrainings = ref<number>(0);
const sizeTrainings = ref<number>(10);
const trainingsData = ref<any[]>([]);
const totalTrainings = ref<number>(0);

const isValidDateRangeTrainings = computed(() => {
  if (!dateRangeTrainings.value || !dateRangeTrainings.value[0] || !dateRangeTrainings.value[1]) {
    return true;
  }
  const start = dayjs(dateRangeTrainings.value[0], 'DD-MM-YYYY HH:mm:ss');
  const end = dayjs(dateRangeTrainings.value[1], 'DD-MM-YYYY HH:mm:ss');
  return start.isSameOrBefore(end);
});

const requestParamsTrainings = computed(() => {
  let formattedStart = '';
  let formattedEnd = '';
  if (dateRangeTrainings.value && dateRangeTrainings.value[0] && dateRangeTrainings.value[1]) {
    formattedStart = dayjs(dateRangeTrainings.value[0], 'DD-MM-YYYY HH:mm:ss').format('YYYY-MM-DD');
    formattedEnd = dayjs(dateRangeTrainings.value[1], 'DD-MM-YYYY HH:mm:ss').format('YYYY-MM-DD');
  }

  const params: any = {
    page: pageTrainings.value,
    size: sizeTrainings.value,
    startDate: formattedStart,
    endDate: formattedEnd
  };

  if (trainingType.value) {
    params.type = trainingType.value;
  }

  return params;
});

const fetchDataTrainings = () => {
  if (!isValidDateRangeTrainings.value) {
    return;
  }

  axiosInstance.get('/admins/statistics/trainings', { params: requestParamsTrainings.value })
    .then(res => {
      const data = res.data;
      totalTrainings.value = data.count;
      trainingsData.value = data.map((d:any) => {
        return {
          ...d,
          loadPercentage: Math.round(d.loadPercentage) // Округляем до целого числа
        }
      });
    })
    .catch(error => {
      ElNotification.error({
        title: 'Ошибка',
        message: 'Ошибка при загрузке статистики по занятиям',
        duration: 5000,
      });
    });
};

const handleDateRangeClearTrainings = () => {
  dateRangeTrainings.value = [
    dayjs().startOf('year').format("DD-MM-YYYY 00:00:00"),
    dayjs().endOf('year').format("DD-MM-YYYY 23:59:59")
  ];
};

const handlePageChangeTrainings = (val: number) => {
  pageTrainings.value = val - 1;
  fetchDataTrainings();
};

const handleSizeChangeTrainings = (val: number) => {
  sizeTrainings.value = val;
  pageTrainings.value = 0;
  fetchDataTrainings();
};

const loadClients = () => {
  axiosInstance.get('/clients').then(res => {
    clients.value = res.data;
  }).catch(error => {
    console.error('Ошибка при загрузке клиентов', error);
  });
};

onMounted(() => {
  loadClients();
  const savedTab = localStorage.getItem('currentTab');
  if (savedTab === 'trainings') {
    currentTab.value = 'trainings';
    fetchDataTrainings();
  } else {
    currentTab.value = 'abonements';
    fetchDataAbonements();
  }
});
</script>

<style scoped lang="scss">
</style>
