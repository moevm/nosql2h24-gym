<template>
  <div>
    <el-button @click="importData" :loading="loading">Импорт данных</el-button>
    <input
      type="file"
      ref="fileInput"
      style="display: none"
      @change="handleFileChange"
      accept=".json"
    />
    <div
      class="drag-drop-area"
      @dragover.prevent
      @drop="handleDrop"
    >
      Перетащите файл JSON сюда
    </div>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axiosInstance from '@/widgets/axios/index.ts';
import { ElLoading, ElNotification } from 'element-plus';  // Импортируем компоненты для индикатора загрузки и уведомлений

const fileInput = ref<HTMLInputElement | null>(null);
let selectedFile: File | null = null;
const errorMessage = ref<string | null>(null);
const loading = ref<boolean>(false); // Состояние загрузки

const importData = () => {
  fileInput.value?.click();  // Открытие диалога выбора файла
};

const handleFileChange = () => {
  if (fileInput.value?.files) {
    selectedFile = fileInput.value.files[0];
    if (selectedFile) {
      validateAndUploadFile(selectedFile);
    }
  }
};

const handleDrop = (event: DragEvent) => {
  event.preventDefault();
  const file = event.dataTransfer?.files[0];
  if (file) {
    validateAndUploadFile(file);
  }
};

const validateAndUploadFile = (file: File) => {
  // Проверяем MIME-тип на json
  // В некоторых браузерах type может быть пустым, поэтому можно также проверить расширение
  if (file.type !== 'application/json' && !file.name.endsWith('.json')) {
    errorMessage.value = 'Пожалуйста, выберите файл в формате JSON.';
    return;
  }

  errorMessage.value = null; // Очистить сообщение об ошибке, если файл валиден
  uploadFile(file);
};

const uploadFile = async (file: File) => {
  loading.value = true;  // Включаем индикатор загрузки

  const formData = new FormData();
  formData.append('file', file);

  const loadingInstance = ElLoading.service({
    target: document.body, // Индикатор будет накладываться на весь экран
    text: 'Загрузка файла...', // Текст на индикаторе
  });

  try {
    const response = await axiosInstance.post('/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    console.log('Файл успешно загружен:', response.data);
    loading.value = false;
    loadingInstance.close();  // Закрываем индикатор загрузки
    ElNotification.success({
      title: 'Успех',
      message: 'Данные успешно импортированы!',
      duration: 3000,
    });
  } catch (error) {
    console.error('Ошибка при загрузке файла:', error);
    loading.value = false;
    loadingInstance.close();  // Закрываем индикатор загрузки

    // Выводим уведомление об ошибке через ElNotification
    ElNotification.error({
      title: 'Ошибка',
      message: 'Произошла ошибка при загрузке файла. Пожалуйста, попробуйте снова.',
      duration: 5000, // Уведомление будет отображаться 5 секунд
    });
  }
};
</script>

<style scoped lang="scss">
.drag-drop-area {
  width: 100%;
  padding: 20px;
  border: 2px dashed #007bff;
  border-radius: 5px;
  text-align: center;
  cursor: pointer;
  margin-top: 10px;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
