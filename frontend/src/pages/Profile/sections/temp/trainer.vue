<template>
  <h1>Запросы для тренера</h1>
  
  <!-- Кнопки для получения тренеров, добавления тренировок и абонементов -->
  <el-row style="gap:15px">
    <el-button @click="fetchTrainers">Получить всех тренеров</el-button>
    <el-button @click="createTraining">Добавить тренировку</el-button>
  </el-row>
  <Spacer></Spacer>
  
  <el-row style="gap:15px">
    <el-button @click="getTraining">Получить тренировку по ID</el-button>
    <el-button @click="getAllTrainings">Получить все тренировки</el-button>
  </el-row>
  <Spacer></Spacer>
  
  <el-button @click="createAbonement">Создать абонемент по идентификатору клиента.</el-button>
  <Spacer></Spacer>
  
  <el-row style="gap:15px">
    <el-button @click="getRooms">Получить залы</el-button>
  </el-row>
  
  <!-- Отображение таблиц -->
  <Spacer></Spacer>
  
  <!-- Таблица тренеров -->
  <div v-if="currentTable === 'trainersData' && trainersData && trainersData.length">
    <el-table :data="trainersData" style="width: 100%">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="Имя" prop="name"></el-table-column>
      <el-table-column label="Фамилия" prop="surname"></el-table-column>
      <el-table-column label="Специализация" prop="sections"></el-table-column>
    </el-table>
  </div>
  
  <Spacer></Spacer>
  
  <!-- Таблица тренировок -->
  <div v-if="currentTable === 'trainingData' && trainingData && trainingData.length">
    <el-table :data="trainingData" style="width: 100%">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="Время начала" prop="startTime"></el-table-column>
      <el-table-column label="Время окончания" prop="endTime"></el-table-column>
      <el-table-column label="Секция" prop="section.name"></el-table-column>
      <el-table-column label="Зал" prop="room.name"></el-table-column>
    </el-table>
  </div>
  
  <Spacer></Spacer>
  
  <!-- Таблица залов -->
  <div v-if="currentTable === 'roomsData' && roomsData && roomsData.length">
    <el-table :data="roomsData" style="width: 100%">
      <el-table-column label="Название" prop="name"></el-table-column>
      <el-table-column label="Вместимость" prop="capacity"></el-table-column>
      <el-table-column label="Адрес" prop="location.address"></el-table-column>
    </el-table>
  </div>
  
  <Spacer></Spacer>
  
  <!-- Таблица абонементов -->
  <div v-if="currentTable === 'abonementData' && abonementData && abonementData.length">
    <el-table :data="abonementData" style="width: 100%">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="Создан " prop="createdDate"></el-table-column>
      <el-table-column label="Комментарий" prop="comment"></el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axiosInstance from "../../../../widgets/axios";
import Spacer from "../../../../shared/components/Spacer.vue";

const currentTable = ref();

// Переменные для данных
const trainersData = ref<any[]>([]); // Массив для данных тренеров
const trainingData = ref<any[]>([]); // Массив для данных тренировок
const roomsData = ref<any[]>([]); // Массив для данных залов
const abonementData = ref<any[]>([]); // Массив для данных абонементов

// ID для теста
const trainerId = "653ef3a8a3e34567bcdf2001"; // Тренер
const trainingId = "653ef3a8a3e34567bcdf3001"; // Тренировка
const clientId = "653ef3a8a3e34567bcdf1001"; // Клиент

// Функция для получения всех тренеров
const fetchTrainers = async () => {
  try {
    const response = await axiosInstance.get('/trainers');
    trainersData.value = response.data;
    currentTable.value = 'trainersData';
  } catch (error) {
    console.error("Ошибка при получении данных тренеров:", error);
  }
};

// Функция для создания тренировки
const createTraining = async () => {
  try {
    const response = await axiosInstance.post(`/trainers/${trainerId}/trainings`, {
      "startTime": "2024-11-10T21:36:57.216Z",
      "endTime": "2024-11-10T21:36:57.216Z",
      "availableSlots": 10,
      "section": "Плавание",
      "room": {
        id: "653ef3a8a3e34567bcdf5001",
        name: "Main Gym",
        capacity: 100
      }
    });
    trainingData.value = [response.data]; // Обновляем данные тренировок
    currentTable.value = 'trainingData';
  } catch (error) {
    console.error("Ошибка при добавлении тренировки:", error);
  }
};

// Функция для получения тренировки по ID
const getTraining = async () => {
  try {
    const response = await axiosInstance.get(`/trainings/${trainingId}`);
    trainingData.value = [response.data]; // Обновляем данные по одной тренировке
    currentTable.value = 'trainingData';
  } catch (error) {
    console.error("Ошибка при получении тренировки:", error);
  }
};

// Функция для получения всех тренировок
const getAllTrainings = async () => {
  try {
    const response = await axiosInstance.get(`/trainings`);
    trainingData.value = response.data; // Загружаем все тренировки
    currentTable.value = 'trainingData';
  } catch (error) {
    console.error("Ошибка при получении списка тренировок:", error);
  }
};

// Функция для создания абонемента
const createAbonement = async () => {
  try {
    const response = await axiosInstance.post(`/subscriptions/client/${clientId}`, {
      "duration": "52",
      "price": "300.14",
    });
    abonementData.value = [response.data]; // Обновляем данные абонемента
    currentTable.value = 'abonementData';
  } catch (error) {
    console.error("Ошибка при создании абонемента", error);
  }
};

// Функция для получения информации о залах
const getRooms = async () => {
  try {
    const response = await axiosInstance.get(`/rooms`);
    roomsData.value = response.data; // Загружаем информацию о залах
    currentTable.value = 'roomsData';
  } catch (error) {
    console.error("Ошибка при получении залов:", error);
  }
};
</script>

<style scoped lang="scss">

</style>
