<template>
  <div>
    <h2>Информация об абонементе</h2>
    <spacer></spacer>
    <el-card
      v-if="userAbonementInfo"
    >
      <p>
        <el-text type="primary" >Статус: </el-text>
        <span :style="{
          color: getStatusColor(userAbonementInfo.status),
          fontWeight: 700
        }">{{ userAbonementInfo.status }}</span>
      </p>
      <spacer/>
      <p>
        <el-text type="primary" >Дата начала:</el-text> {{ formatDate(userAbonementInfo.startDate) }}
      </p>
      <spacer/>
      <p>
        <el-text type="primary" >Дата окончания:</el-text> {{ formatDate(userAbonementInfo.endDate) }}
      </p>
      <spacer/>
      <p>
        <el-text type="primary" >Оставшиеся дни:</el-text> {{ userAbonementInfo.restDays }}
      </p>
      <spacer/>
      <p>
        <el-text type="primary" >Длительность:</el-text> {{ userAbonementInfo.duration }} дней
      </p>

      <div style="display: flex; gap: 10px; margin-top: 15px">
        <el-button @click="openExtendForm" type="primary">
          Продлить
        </el-button>
        <el-button @click="toggleFreeze" :type="'danger'">
          {{ userAbonementInfo.status === 'FREEZE' ? 'Разморозить' : 'Заморозить' }}
        </el-button>
      </div>
      <spacer></spacer>
      <!-- Форма продления -->
      <el-card
        class="card-dark"
        v-if="showExtendForm"
        @close="closeExtendForm"
      >
        <h3>Продление абонемента</h3>
        <el-form :model="extendForm">
          <el-form-item label="Выберите длительность" :label-position="'top'">
            <el-select v-model="extendForm.duration" placeholder="Выберите срок" @change="calculatePrice">
              <el-option label="1 неделя" value="7"></el-option>
              <el-option label="1 месяц" value="30"></el-option>
              <el-option label="3 месяца" value="90"></el-option>
              <el-option label="6 месяцев" value="180"></el-option>
              <el-option label="1 год" value="365"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Цена" :label-position="'top'">
            <el-input v-model="extendForm.price" type="number" :disabled="true" readonly />
          </el-form-item>
        </el-form>
        <div style="text-align: right; margin-top: 15px">
          <el-button @click="closeExtendForm">Отменить</el-button>
          <el-button type="primary" @click="submitExtend">Отправить</el-button>
        </div>
      </el-card>

      <spacer></spacer>

      <!-- Форма заморозки -->
      <el-dialog
        :model-value="isFreezeModalOpen"
      >
        <el-text>
          {{ userAbonementInfo.status === 'FREEZE' ? 'Вы уверены что хотите разморозить абонемент?' : 'Вы уверены что хотите заморозить абонемент?' }}
        </el-text>
        <Spacer></Spacer>

        <el-container style="justify-content:space-between">
          <el-button @click="submitFreeze" type="danger">
            {{ userAbonementInfo.status === 'FREEZE' ? 'Разморозить' : 'Заморозить' }}
          </el-button>
          <el-button type="primary">
            Отменить
          </el-button>
        </el-container>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { formatDate } from '@/shared/utils/formatDate.ts';
import { IUserData } from '@/widgets/authController/index.ts';
import axiosInstance from '@/widgets/axios/index';
import { useAuthStore } from '@/widgets/store/auth/index';
import { ref, onMounted, computed } from 'vue';
import { ElNotification } from 'element-plus'; // Импортируем ElNotification

interface UserAbonementInfo {
  duration: number;
  startDate: string;
  endDate: string;
  clientId: string;
  restDays: number;
  status: string;
}

const authStore = useAuthStore();

onMounted(() => {
  authStore.loadUserDataFromToken();
})

// Проверка, если пользователь авторизован
const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

const userAbonementInfo = ref<UserAbonementInfo>();
const showExtendForm = ref(false);

const extendForm = ref({
  duration: null,
  price: null
});

const openExtendForm = () => {
  showExtendForm.value = true;
};

const closeExtendForm = () => {
  showExtendForm.value = false;
  extendForm.value = { duration: null, price: null };
};

const isFreezeModalOpen = ref(false);

const toggleFreeze = () => {
  isFreezeModalOpen.value = !isFreezeModalOpen.value;
};

// Функция для расчета цены на основе выбранной длительности
const calculatePrice = () => {
  const basePricePerMonth = 1500;
  const daysInMonth = 30;

  if (extendForm.value.duration) {
    const months = extendForm.value.duration / daysInMonth;
    extendForm.value.price = basePricePerMonth * months;
  }
};

const submitExtend = () => {
  axiosInstance
    .put(`/subscriptions/${userInfo.value?.id}/extend`, {
      duration: extendForm.value.duration,
      price: extendForm.value.price
    })
    .then(() => {
      ElNotification({
        title: 'Уведомление',
        message: 'Абонемент продлен',
        type: 'success',
        duration: 3000
      });
      closeExtendForm();
      // Обновляем данные абонемента
      fetchUserAbonementInfo();
    });
};

const submitFreeze = () => {
  axiosInstance
    .put(`/subscriptions/${userInfo.value?.id}/freeze`)
    .then((res) => {
      if (res.data.status === 'FREEZE') {
        ElNotification({
          title: 'Уведомление',
          message: 'Абонемент успешно заморожен',
          type: 'success',
          duration: 3000
        });
      } else {
        ElNotification({
          title: 'Уведомление',
          message: 'Абонемент успешно разморожен',
          type: 'success',
          duration: 3000
        });
      }

      isFreezeModalOpen.value = false;
      // Обновляем данные абонемента
      fetchUserAbonementInfo();
    });
};

const isLoading = ref(false)

const fetchUserAbonementInfo = () => {
  axiosInstance
    .get(`/clients/${userInfo.value?.id}/subscription`)
    .then((res) => {
      userAbonementInfo.value = res.data;
    })
    .catch((error) => {
      ElNotification({
        title: 'Ошибка',
        message: 'Не удалось загрузить данные абонемента',
        type: 'error',
        duration: 3000
      });
    });
};

onMounted(() => {
  fetchUserAbonementInfo();
});

// Функция для получения цвета в зависимости от статуса
const getStatusColor = (status: string) => {
  switch (status) {
    case 'ACTIVE':
      return 'green';
    case 'INACTIVE':
      return 'orange';
    case 'FREEZE':
      return 'blue';
    default:
      return 'gray';
  }
};
</script>

<style scoped lang="scss">
</style>
