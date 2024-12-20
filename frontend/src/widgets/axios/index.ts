import axios from 'axios';
import { useAuthStore } from "../store/auth";

const baseURL = 'http://localhost:8080';


const axiosInstance = axios.create({
  baseURL, // Указываем базовый URL для всех запросов
  headers: {
    'Content-Type': 'application/json', // Заголовок по умолчанию
    'Access-Control-Allow-Credentials':true
  },
});

// Интерсептор для добавления токена в заголовки запросов
axiosInstance.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore();
    const token = authStore.getAuthToken(); // Получаем токен из localStorage или другого места
    if (token) {
      // Добавляем токен в заголовки запроса
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;