<template>
  <el-container class="main" style="height:100vh">
    <el-header style="border-bottom:2px solid white; padding: 15px 0; height: auto">
      <el-row class="is-justify-space-between">
        <el-col :span="4" style="padding: 0 15px">
          <img style="height: 100%; width:150px" src="../../shared/assets/logo.png" alt="Logo">
        </el-col>
        <el-col :span="12" style="text-align: center; align-content: center;">
          <el-row>
            <el-col :span="6">
              <el-link :underline="false" class="white-text">На главную</el-link>
            </el-col>
            <el-col :span="6">
              <el-link :underline="false" class="white-text">Тренеры</el-link>
            </el-col>
            <el-col :span="6">
              <el-link :underline="false" class="white-text">Отзывы</el-link>
            </el-col>
            <el-col :span="6">
              <el-link :underline="false" class="white-text">Контакты</el-link>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="4" style="text-align: center">
          <el-button @click="handleLoginButton">Войти</el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-container style="text-align: center; margin-top: 15px">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">TRAINER (операции, связанные с тренерами)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="fetchTrainers">Получить всех тренеров</el-button>
          <el-button @click="createTraining">Добавить тренировку</el-button>
          <el-button @click="updateTrainer">Обновить тренера</el-button>
          <el-button @click="deleteTrainer">Удалить тренера</el-button>
          <el-text v-if="trainersData" style="color:white; background: rgba(0,0,0, .5); font-size: 18px">Данные тренеры:
            <ul>
              <li v-for="data of trainersData">{{ data }}</li>
            </ul>
          </el-text>
        </el-row>
      </el-space>
    </el-container>
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">TRAINER (операции, связанные с тренерами)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="createAbonement">Создать абонемент по идентификатору клиента.</el-button>
        </el-row>
      </el-space>
    </el-container>
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">TRAINING (Операции, связанные с тренировками)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="getTraining">Получить тренировку по ID</el-button>
          <el-button @click="updateTraining">Обновить тренировку по ID</el-button>
          <el-button @click="deleteTraining">Удалить тренировку по ID</el-button>
          <el-button @click="registerClientForTraining">Записать клиента на тренировку</el-button>
          <el-button @click="getAllTrainings">Получить все тренировки</el-button>
          <el-button @click="getTrainingClients">Получить клиентов тренировки</el-button>
          <el-text v-if="trainingData" style="color:white; background: rgba(0,0,0, .5); font-size: 18px">Данные тренировки:
            <ul>
              <li v-for="data of trainingData">{{ data }}</li>
            </ul>
          </el-text>
        </el-row>
      </el-space>
    </el-container>
    
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">PROMOTION (Операции, связанные с поощрениями)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="getPromotionById">Получить поощрение по ID</el-button>
          <el-button @click="updatePromotion">Обновить поощрение по ID</el-button>
          <el-button @click="deletePromotion">Удалить поощрение по ID</el-button>
          <el-button @click="getAllPromotions">Получить все поощрения</el-button>
          <el-button @click="createPromotion">Создать поощрение</el-button>
          <el-text size="large" v-if="promotionData">Данные поощрения: {{ promotionData }}</el-text>
        </el-row>
      </el-space>
    </el-container>
    
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">CLIENTS (Операции, связанные с клиентами)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="getClientById">Получить клиента по ID</el-button>
          <el-button @click="updateClientById">Обновить клиента по ID</el-button>
          <el-button @click="deleteClientById">Удалить клиента по ID</el-button>
          <el-button @click="getClients">Получить всех клиентов</el-button>
          <el-text v-if="clientsData" style="color:white; background: rgba(0,0,0, .5); font-size: 18px">Данные клиентов:
            <ul>
              <li v-for="data of clientsData">{{ data }}</li>
            </ul>
          </el-text>
        </el-row>
      </el-space>
    </el-container>
    
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">ROOMS (Операции, связанные с ЗАЛАМИ)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="getRooms">Получить залы</el-button>
          <el-button @click="createRoom">Создать зал</el-button>
          <el-button @click="deleteRoom">Удалить зал по ID</el-button>
          <el-text v-if="roomsData" style="color:white; background: rgba(0,0,0, .5); font-size: 18px">Данные залов:
            <ul>
              <li v-for="data of roomsData">{{ data }}</li>
            </ul>
          </el-text>
        </el-row>
      </el-space>
    </el-container>
    
    <el-container style="text-align: center">
      <el-header height="40px" style="text-transform:uppercase; font-size: 24px">ADMIN CONTROLLER (Операции, связанные с АДМИНАМИ)</el-header>
      <el-space direction="vertical">
        <el-row style="gap:15px">
          <el-button @click="getAdminByid">Получить админа по ID</el-button>
          <el-button @click="updateAdminById">Обновить админа по ID</el-button>
          <el-button @click="deleteAdminById">Удалить админа по ID</el-button>
          <el-button @click="getAdmins">Получить всех админов</el-button>
          <el-text v-if="adminsData" style="color:white; background: rgba(0,0,0, .5); font-size: 18px">Данные админов:
            <ul>
              <li v-for="data of adminsData">{{ data }}</li>
            </ul>
          </el-text>
        </el-row>
      </el-space>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import router from '../../app/router';
import axiosInstance from "../../widgets/axios";

const trainersData = ref(null);
const trainerId = 1; // Используем тестовый ID для примера

// Переход на страницу входа
const handleLoginButton = () => {
  router.push('/login');
};

// Получить всех тренеров
const fetchTrainers = async () => {
  try {
    const response = await axiosInstance.get('/trainers');
    trainersData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении данных тренеров:", error);
  }
};

// Добавить тренировку тренеру
const createTraining = async () => {
  try {
    const response = await axiosInstance.post(`/trainers/${trainerId}/trainings`, {
      "startTime": "2024-11-10T21:36:57.216Z",
      "endTime": "2024-11-10T21:36:57.216Z",
      "availableSlots": 10,
      "section": "Плавание",
      "roomId": "1"
    });
    alert("Тренировка добавлена:", response.data);
  } catch (error) {
    console.error("Ошибка при добавлении тренировки:", error);
  }
};

// Обновить данные тренера
const updateTrainer = async () => {
  try {
    const response = await axiosInstance.put(`/trainers/${trainerId}`, {
      "name": "Иван",
      "surname": "Иванов",
      "email": "example@example.com",
      "phoneNumber": "+79998887766",
      "experience": 2,
      "hourlyRate": 500,
      "specialization": "string",
      "sections": "['Name1', 'Name2']"
    });
    alert("Данные тренера обновлены:", response.data);
  } catch (error) {
    console.error("Ошибка при обновлении данных тренера:", error);
  }
};

// Удалить тренера
const deleteTrainer = async () => {
  try {
    await axiosInstance.delete(`/trainers/${trainerId}`);
    alert("Тренер удалён.");
  } catch (error) {
    console.error("Ошибка при удалении тренера:", error);
  }
};

const clientId = 1; // Пример ID клиента
const createAbonement = async () => {
  try {
    await axiosInstance.post(`/subscriptions/client/${clientId}`);
  } catch (error) {
    console.error("Ошибка при создании абонемента", error);
  }
}

const trainingData = ref(null);
const trainingId = 1; // Пример ID тренировки

// Получить тренировку по её идентификатору
const getTraining = async () => {
  try {
    const response = await axiosInstance.get(`/trainings/${trainingId}`);
    trainingData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении тренировки:", error);
  }
};

// Обновить тренировку по её идентификатору
const updateTraining = async () => {
  try {
    const response = await axiosInstance.put(`/trainings/${trainingId}`, {
      "startTime": "2024-11-10T21:36:04.328Z",
      "endTime": "2024-11-10T21:36:04.328Z",
      "availableSlots": 10,
      "section": "Плавание",
      "roomId": "1"
    });
    alert("Тренировка обновлена:", response.data);
  } catch (error) {
    console.error("Ошибка при обновлении тренировки:", error);
  }
};

// Удалить тренировку по её идентификатору
const deleteTraining = async () => {
  try {
    await axiosInstance.delete(`/trainings/${trainingId}`);
    alert("Тренировка удалена.");
  } catch (error) {
    console.error("Ошибка при удалении тренировки:", error);
  }
};

// Записать клиента на тренировку
const registerClientForTraining = async () => {
  try {
    const response = await axiosInstance.post(`/trainings/${trainingId}/registration/${clientId}`);
    alert("Клиент записан на тренировку:", response.data);
  } catch (error) {
    console.error("Ошибка при записи клиента на тренировку:", error);
  }
};

// Получить список всех тренировок
const getAllTrainings = async () => {
  try {
    const response = await axiosInstance.get(`/trainings`);
    trainingData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении списка тренировок:", error);
  }
};

// Получить клиентов записанных на тренировку по её идентификатору
const getTrainingClients = async () => {
  try {
    const response = await axiosInstance.get(`/trainings/${trainingId}/registration`);
    alert("Клиенты тренировки:", response.data);
  } catch (error) {
    console.error("Ошибка при получении клиентов тренировки:", error);
  }
};

const promotionData = ref(null);
const promotionId = 1; // Пример ID для тестирования

// Получить поощрение по ID
const getPromotionById = async () => {
  try {
    const response = await axiosInstance.get(`/promotions/${promotionId}`);
    promotionData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении поощрения:", error);
  }
};

// Обновить поощрение по ID
const updatePromotion = async () => {
  try {
    const response = await axiosInstance.put(`/promotions/${promotionId}`, {
      "name": "Бассейн",
      "description": "Предложение действует целый месяц!",
      "startDate": "2024-11-09T12:20:55.971Z",
      "endDate": "2024-12-09T12:20:55.971Z",
      "discountPercentage": 10,
      "creatorId": "672e8f509016ff5e3ddaec88"
    });
    alert("Поощрение обновлено:", response.data);
  } catch (error) {
    console.error("Ошибка при обновлении поощрения:", error);
  }
};

// Удалить поощрение по ID
const deletePromotion = async () => {
  try {
    await axiosInstance.delete(`/promotions/${promotionId}`);
    alert("Поощрение удалено.");
  } catch (error) {
    console.error("Ошибка при удалении поощрения:", error);
  }
};

// Получить все поощрения
const getAllPromotions = async () => {
  try {
    const response = await axiosInstance.get(`/promotions`);
    promotionData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении всех поощрений:", error);
  }
};

const createPromotion = async () => {
  try {
    await axiosInstance.post(`/promotions`, {
      "name": "Бассейн",
      "description": "Предложение действует целый месяц!",
      "startDate": "2024-11-09T12:20:55.971Z",
      "endDate": "2024-12-09T12:20:55.971Z",
      "discountPercentage": 10,
      "creatorId": "672e8f509016ff5e3ddaec88"
    })
  } catch (error) {
    console.error("Ошибка при создании поощрения:", error);
  }
}

const clientsData = ref(null);

const getClientById = async () => {
  try {
    const client = await axiosInstance.get(`/clients/${clientId}`);
    alert(client)
  } catch (error) {
    console.error("Ошибка при получении клиента:", error);
  }
}

const updateClientById = async () => {
  try {
    const client = await axiosInstance.put(`/clients/${clientId}`, {
      "name": "Иван",
      "surname": "Иванов",
      "email": "example@example.com",
      "phoneNumber": "+79998887766"
    });
    alert(client)
  } catch (error) {
    console.error("Ошибка при обновлении клиента:", error);
  }
}

const deleteClientById = async() => {
  try {
    await axiosInstance.delete(`/clients/${clientId}`);
  } catch (error) {
    console.error("Ошибка при удалении клиента:", error);
  }
}

const getClients = async () => {
  try {
    const response = await axiosInstance.get(`/clients`);
    clientsData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении клиента:", error);
  }
}

const roomsData = ref(null);
const roomId = 1;

const getRooms = async () => {
  try {
    const response = await axiosInstance.get(`/rooms`);
    roomsData.value = response.data
  } catch (error) {
    console.error("Ошибка при получении залов:", error);
  }
}

const createRoom = async () => {
  try {
    await axiosInstance.post(`/rooms`, {
      "name": "string",
      "capacity": 0,
      "address": "string",
      "number": 0
    });
  } catch (error) {
    console.error("Ошибка при создании зала:", error);
  }
}

const deleteRoom = async () => {
  try {
    await axiosInstance.delete(`/rooms/${roomId}`);
  } catch (error) {
    console.error("Ошибка при удалении зала:", error);
  }
}

const adminsData = ref();
const adminId = 1;

const getAdminByid = async () => {
  try {
    const response = await axiosInstance.get(`/admins/${adminId}`);
    alert(response.data)
  } catch (error) {
    console.error("Ошибка при получении админа:", error);
  }
}

const updateAdminById = async() => {
  try {
    await axiosInstance.put(`/admins/${adminId}`, {
      "name": "Иван",
      "surname": "Иванов",
      "email": "example@example.com"
    });
  } catch (error) {
    console.error("Ошибка при обновлении админа:", error);
  }
}

const deleteAdminById = async () => {
  try {
    await axiosInstance.delete(`/admins/${adminId}`);
  } catch (error) {
    console.error("Ошибка при удалении админа:", error);
  }
}

const getAdmins = async () => {
  try {
    const response = await axiosInstance.get(`/admins}`);
    adminsData.value = response.data;
  } catch (error) {
    console.error("Ошибка при получении админов:", error);
  }
}
</script>

<style scoped lang="scss">
.main {
  background: url('../../shared/assets/Ka4okBG.png') center center / cover no-repeat;
}

.white-text {
  color: white;
}
</style>
