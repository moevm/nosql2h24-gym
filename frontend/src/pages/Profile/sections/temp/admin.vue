<template>
  <h1>Запросы для администратора</h1>
  
  <!-- Кнопки для получения и обновления данных администраторов -->
  <el-row style="gap:15px">
    <el-button @click="getAdminByid">Получить админа по ID</el-button>
    <el-button @click="updateAdminById">Обновить админа по ID</el-button>
    <el-button @click="getAdmins">Получить всех админов</el-button>
  </el-row>
  
  <Spacer></Spacer>
  
  <!-- Кнопки для получения и создания поощрений -->
  <el-row style="gap:15px">
    <el-button @click="getAllPromotions">Получить все поощрения</el-button>
    <el-button @click="createPromotion">Создать поощрение</el-button>
  </el-row>
  
  <Spacer></Spacer>
  
  <!-- Кнопки для получения и создания залов -->
  <el-row style="gap:15px">
    <el-button @click="getRooms">Получить залы</el-button>
    <el-button @click="createRoom">Создать зал</el-button>
  </el-row>
  
  <!-- Отображение таблиц -->
  <Spacer></Spacer>
  
  <!-- Таблица администраторов -->
  <div v-if="currentTable === 'admins'">
    <el-table :data="adminsData" style="width: 100%">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="Имя" prop="name"></el-table-column>
      <el-table-column label="Фамилия" prop="surname"></el-table-column>
      <el-table-column label="Email" prop="email"></el-table-column>
    </el-table>
  </div>
  
  <!-- Таблица поощрений -->
  <div v-if="currentTable === 'promotions'">
    <el-table :data="promotionData" style="width: 100%">
      <el-table-column label="Название" prop="name"></el-table-column>
      <el-table-column label="Описание" prop="description"></el-table-column>
      <el-table-column label="Дата начала" prop="startDate"></el-table-column>
      <el-table-column label="Дата окончания" prop="endDate"></el-table-column>
    </el-table>
  </div>
  
  <!-- Таблица залов -->
  <div v-if="currentTable === 'rooms'">
    <el-table :data="roomsData" style="width: 100%">
      <el-table-column label="Название" prop="name"></el-table-column>
      <el-table-column label="Вместимость" prop="capacity"></el-table-column>
      <el-table-column label="Адрес" prop="location.address"></el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import axiosInstance from "../../../../widgets/axios";
import { ref } from "vue";
import Spacer from "../../../../shared/components/Spacer.vue";

const adminsData = ref(null);
const adminId = "653ef3a8a3e34567bcdf3001";
const promotionData = ref(null);
const roomsData = ref(null);

// Флаг для текущей активной таблицы
const currentTable = ref<'admins' | 'promotions' | 'rooms' | null>(null);

// Функции для получения и обновления администраторов
const getAdminByid = async () => {
  try {
    const response = await axiosInstance.get(`/admins/${adminId}`);
    adminsData.value = [response.data] as any; // Отображаем одного администратора
    currentTable.value = 'admins'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при получении админа:", error);
  }
};

const updateAdminById = async () => {
  try {
    const response = await axiosInstance.put(`/admins/${adminId}`, {
      name: "Иван",
      surname: "Иванов" + Math.ceil(Math.random() * 1000),
      email: "admin@example.com",
    });
    adminsData.value = [response.data] as any; // Отображаем обновленного администратора
    currentTable.value = 'admins'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при обновлении админа:", error);
  }
};

const getAdmins = async () => {
  try {
    const response = await axiosInstance.get(`/admins`);
    adminsData.value = response.data; // Отображаем всех админов
    currentTable.value = 'admins'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при получении админов:", error);
  }
};

// Функции для работы с поощрениями
const getAllPromotions = async () => {
  try {
    const response = await axiosInstance.get(`/promotions`);
    promotionData.value = response.data; // Отображаем все поощрения
    currentTable.value = 'promotions'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при получении всех поощрений:", error);
  }
};

const createPromotion = async () => {
  try {
    const response = await axiosInstance.post(`/promotions`, {
      name: "Бассейн",
      description: "Предложение действует целый месяц!",
      startDate: "2024-11-09T12:20:55.971Z",
      endDate: "2024-12-09T12:20:55.971Z",
      discountPercentage: 10,
      creatorId: adminId,
    });
    promotionData.value = [response.data] as any; // Отображаем только созданное поощрение
    currentTable.value = 'promotions'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при создании поощрения:", error);
  }
};

// Функции для работы с залами
const getRooms = async () => {
  try {
    const response = await axiosInstance.get(`/rooms`);
    roomsData.value = response.data; // Отображаем все залы
    currentTable.value = 'rooms'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при получении залов:", error);
  }
};

const createRoom = async () => {
  try {
    const response = await axiosInstance.post(`/rooms`, {
      name: "Main Gym in Out City!",
      capacity: 500,
      address: "Tverskaya Street, 12",
      number: 1,
    });
    roomsData.value = [response.data] as any; // Отображаем только созданный зал
    currentTable.value = 'rooms'; // Устанавливаем активную таблицу
  } catch (error) {
    console.error("Ошибка при создании зала:", error);
  }
};
</script>

<style scoped lang="scss">

</style>
