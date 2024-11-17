<template>
  <h1>Запросы для клиента</h1>
  
  <!-- Кнопки для получения, обновления и списка клиентов -->
  <el-row style="gap:15px">
    <el-button @click="getClientById">Получить клиента по ID</el-button>
    <el-button @click="updateClientById">Обновить клиента по ID</el-button>
    <el-button @click="getClients">Получить всех клиентов</el-button>
  </el-row>
  
  <!-- Отображение таблицы с клиентами -->
  <Spacer></Spacer>
  <div v-if="clientsData && clientsData.length">
    <el-table :data="clientsData" style="width: 100%">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="Имя" prop="name"></el-table-column>
      <el-table-column label="Фамилия" prop="surname"></el-table-column>
      <el-table-column label="Email" prop="email"></el-table-column>
      <el-table-column label="Телефон" prop="phoneNumber"></el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axiosInstance from "../../../../widgets/axios";
import Spacer from "../../../../shared/components/Spacer.vue";

const clientsData = ref<any[]>([]); // Массив для данных клиентов
const clientId = "653ef3a8a3e34567bcdf1001"; // Пример ID клиента

// Функция для получения клиента по ID
const getClientById = async () => {
  try {
    const response = await axiosInstance.get(`/clients/${clientId}`);
    clientsData.value = [response.data]; // Загружаем данные одного клиента в массив
  } catch (error) {
    console.error("Ошибка при получении клиента:", error);
  }
}

// Функция для обновления клиента по ID
const updateClientById = async () => {
  try {
    const response = await axiosInstance.put(`/clients/${clientId}`, {
      "name": "Иван",
      "surname": "Иванов"  + Math.ceil(Math.random() * 1000),
      "email": "client@example.com",
      "phoneNumber": "+79998887766"
    });
    clientsData.value = [response.data]; // Обновляем данные клиента в таблице
  } catch (error) {
    console.error("Ошибка при обновлении клиента:", error);
  }
}

// Функция для получения всех клиентов
const getClients = async () => {
  try {
    const response = await axiosInstance.get(`/clients`);
    clientsData.value = response.data; // Загружаем всех клиентов
  } catch (error) {
    console.error("Ошибка при получении списка клиентов:", error);
  }
}
</script>

<style scoped lang="scss">

</style>
