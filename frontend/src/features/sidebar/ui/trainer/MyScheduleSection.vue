<template>
  <div style="margin-top: 20px;">
    <el-container style="align-items: center; gap: 15px; padding: 15px 0; justify-content: space-between">
      <h1>Мои тренировки</h1>
      <el-button style="margin-top: 20px;" type="primary" @click="handleModalOpen">Добавить тренировку</el-button>
    </el-container>
    <spacer></spacer>

    <!-- Фильтрация и сортировка -->
    <el-button v-if="filteredSortedTrainings.length > 0" @click="handleOpenFilter">
      {{ isFilterOpened ? 'Закрыть фильтрацию и поиск' : 'Открыть фильтрацию и поиск' }}
    </el-button>
    <spacer></spacer>

    <el-row
      v-if="isFilterOpened"
      style="margin-bottom: 20px; gap: 15px;"
      type="flex"
      justify="start"
    >
      <!-- Фильтр по секциям -->
      <el-col :span="6">
        <el-select
          v-model="filterSections"
          placeholder="Фильтровать по секциям"
          style="width: 100%;"
          multiple
          clearable
        >
          <el-option
            v-for="section in uniqueSections"
            :key="section"
            :label="section"
            :value="section"
          />
        </el-select>
      </el-col>

      <!-- Фильтр по статусу -->
      <el-col :span="6">
        <el-select
          v-model="filterStatus"
          placeholder="Фильтровать по статусу"
          style="width: 100%;"
          multiple
          clearable
        >
          <el-option label="AWAIT" value="AWAIT" />
          <el-option label="PROGRESS" value="PROGRESS" />
          <el-option label="FINISH" value="FINISH" />
          <!-- Добавьте другие статусы по необходимости -->
        </el-select>
      </el-col>

      <!-- Фильтр по диапазону дат и времени -->
      <el-col :span="8">
        <el-date-picker
          v-model="filterTimeRange"
          type="datetimerange"
          range-separator="до"
          start-placeholder="Начало"
          end-placeholder="Конец"
          format="DD/MM/YYYY HH:mm"
          value-format="DD/MM/YYYY HH:mm"
          :clearable="true"
          style="width: 100%;"
          placeholder="Фильтровать по дате и времени"
        />
      </el-col>

      <!-- Сортировка -->
      <el-col :span="4">
        <el-select
          v-model="sortBy"
          placeholder="Сортировать по"
          style="width: 100%;"
          clearable
        >
          <el-option label="Тренеру" value="trainer" />
          <el-option label="Время начала" value="startTime" />
          <el-option label="Время окончания" value="endTime" />
          <el-option label="Дата начала" value="startTime" />
          <el-option label="Дата окончания" value="endTime" />
          <!-- Добавьте другие поля сортировки по необходимости -->
        </el-select>
      </el-col>

      <el-col :span="4">
        <el-select
          v-model="sortOrder"
          placeholder="Порядок"
          style="width: 100%;"
          clearable
        >
          <el-option label="По возрастанию" value="asc" />
          <el-option label="По убыванию" value="desc" />
        </el-select>
      </el-col>

      <!-- Кнопка сброса фильтров -->
      <el-col :span="4">
        <el-button type="primary" @click="resetFilters">Сбросить</el-button>
      </el-col>
    </el-row>
    <spacer></spacer>

    <!-- Карточки записей -->
    <el-container direction="vertical" style="gap: 15px">
      <el-card
        v-if="filteredSortedTrainings.length > 0"
        v-for="training in filteredSortedTrainings"
        :key="training.id"
        :body-style="{
          display:'grid',
          gridTemplateColumns: '1fr auto',
        }"
      >
        <div>
          <p><strong>Секция:</strong> {{ training.section.name }}</p>
          <p><strong>Комната:</strong> {{ training.room.name }} (Вместимость: {{ training.room.capacity }})</p>
          <p>
            <strong>Время:</strong> {{ formatDate(training.startTime) }} {{ formatTime(training.startTime) }} - {{ formatTime(training.endTime) }}
          </p>
          <p><strong>Доступные места:</strong> {{ training.availableSlots }}</p>
        </div>
        <el-container direction="vertical" style="gap: 15px;  ">
          <el-button style="margin-left:0" type="danger" @click="handleDeleteTraining(training)">Удалить</el-button>
          <el-button style="margin-left:0" type="primary" @click="handleEditTraining(training)">
            Редактировать
          </el-button>
          <el-button style="margin-left:0" type="primary" @click="handleWatchClientsOnTraining(training)">
            Просмотреть клиентов
          </el-button>
        </el-container>
      </el-card>
      <el-text v-else>Запланированных тренировок нет</el-text>
    </el-container>
    <spacer/>

    <el-dialog :model-value="isAddTrainModalOpen" @close="handleModalClose" title="Добавить тренировку">
      <el-form :model="newTraining" label-width="120px">
        <el-form-item label="Секция" label-position="top" required>
          <el-select v-model="newTraining.section" placeholder="Выберите секцию" clearable>
            <el-option
              v-for="section in trainerSections"
              :key="section"
              :label="section"
              :value="section"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Комната" label-position="top" required>
          <el-select v-model="newTraining.roomId" placeholder="Выберите комнату" clearable>
            <el-option
              v-for="room in rooms"
              :key="room.id"
              :label="room.name"
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Начало" label-position="top" required>
          <el-date-picker
            v-model="newTraining.startTime"
            type="datetime"
            format="DD/MM/YYYY HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            :disabled-date="disabledDate"
            @change="onStartTimeChange"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="Количество часов" label-position="top" required>
          <el-select
            v-model="newTraining.hours"
            placeholder="Выберите количество часов"
            :disabled="!newTraining.startTime"
            clearable
          >
            <el-option v-for="n in 9" :key="n" :label="n" :value="n" />
          </el-select>
        </el-form-item>
        <el-form-item label="Доступные места" label-position="top" required>
          <el-input
            v-model.number="newTraining.availableSlots"
            type="number"
            placeholder="Введите количество доступных мест"
            min="0"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleModalClose">Отмена</el-button>
        <el-button type="primary" @click="addTraining" :disabled="!isTrainingFormValid">{{ isEditMode ? 'Отредактировать' : 'Добавить' }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :model-value="isClientsModalOpen" @close="handleClientsModalClose" title="Клиенты на тренировке">
      <el-container direction="vertical" style="gap: 15px;">
        <el-row style="gap: 15px;">
          <el-col>
            <el-input
              v-model="searchName"
              placeholder="Поиск по имени"
              clearable
            />
          </el-col>
          <el-col>
            <el-input
              v-model="searchEmail"
              placeholder="Поиск по email"
              clearable
            />
          </el-col>
          <el-col>
            <el-input
              v-model="searchPhone"
              placeholder="Поиск по телефону"
              clearable
            />
          </el-col>
        </el-row>
        <el-card
          v-for="client in filteredClients"
          :key="client.id"
          :body-style="{
          display:'grid',
          gridTemplateColumns: '1fr',
        }"
        >
          <div>
            <p><strong>Имя:</strong> {{ client.details.name }} {{ client.details.surname }}</p>
            <p><strong>Email:</strong> {{ client.details.email }}</p>
            <p><strong>Телефон:</strong> {{ client.details.phoneNumber }}</p>
          </div>
        </el-card>
        <el-button type="primary" @click="handleClientsModalClose">Закрыть</el-button>
      </el-container>
      <span v-if="clients.length === 0">Еще никто не записался...</span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { IUserData } from '@/widgets/authController/index.ts';
import { useAuthStore } from '@/widgets/store/auth/index.ts';
import { ref, onMounted, computed } from 'vue';
import axiosInstance from '@/widgets/axios/index';
import dayjs from 'dayjs';
import { ElNotification } from 'element-plus'; // Импорт ElNotification

// Управление состоянием фильтров и сортировки
const isFilterOpened = ref(false);
const handleOpenFilter = () => {
  isFilterOpened.value = !isFilterOpened.value;
  if (!isFilterOpened.value) {
    resetFilters();
  }
};

// Фильтры
const filterSections = ref<string[]>([]);
const filterStatus = ref<string[]>([]);
const filterTimeRange = ref<[string, string] | null>(null); // Диапазон дат и времени

// Сортировка
const sortBy = ref<string | null>(null);
const sortOrder = ref<string | null>(null);

// Данные тренировок
interface Trainer {
  id: string;
  name: string;
  surname: string;
  gender: string | null;
  qualification: string | null;
  hourlyRate: number | null;
}

interface Room {
  id: string;
  name: string;
  capacity: number;
}

interface Section {
  name: string;
}

interface Training {
  id: string;
  trainer: Trainer;
  room: Room;
  section: Section;
  startTime: string; // ISO string
  endTime: string;   // ISO string
  duration: number;
  status: string;    // AWAIT, PROGRESS, FINISH и т.д.
}

const trainings = ref<Training[]>([]);

// Получение уникальных секций для фильтра
const uniqueSections = computed(() => {
  const sections = new Set<string>();
  trainings.value.forEach(training => {
    if (training.section) {
      sections.add(training.section.name);
    }
  });
  return Array.from(sections);
});

// Модальное окно для добавления тренировки
const isAddTrainModalOpen = ref(false);
const handleModalOpen = () => {
  isAddTrainModalOpen.value = true;
};
const handleModalClose = () => {
  isAddTrainModalOpen.value = false;
  isEditMode.value = false;  // Сброс состояния редактирования
  newTraining.value = { section: '', roomId: '', startTime: '', endTime: '', hours: null, availableSlots: 0 };
};

const isClientsModalOpen = ref(false); // Состояние модального окна
const clients = ref<any[]>([]); // Список клиентов

const searchName = ref('');
const searchEmail = ref('');
const searchPhone = ref('');

const filteredClients = computed(() => {
  return clients.value.filter(client => {
    const nameMatches = searchName.value
      ? `${client.details.name} ${client.details.surname}`.toLowerCase().includes(searchName.value.toLowerCase())
      : true;
    const emailMatches = searchEmail.value
      ? client.details.email.toLowerCase().includes(searchEmail.value.toLowerCase())
      : true;
    const phoneMatches = searchPhone.value
      ? client.details.phoneNumber.includes(searchPhone.value)
      : true;

    return nameMatches && emailMatches && phoneMatches;
  });
});


// Функция для открытия модального окна с клиентами
const handleWatchClientsOnTraining = (training: Training) => {
  axiosInstance.get(`/trainings/${training.id}/registration`)
    .then(res => {
      // Сохраняем список клиентов на тренировке
      isClientsModalOpen.value = true; // Открываем модальное окно

      // Массив клиентов из ответа
      const clientIds = res.data;

      // Массив запросов для каждого клиента
      const clientRequests = clientIds.map(client =>
        axiosInstance.get(`/clients/${client.id}`)
          .then(res2 => {
            // Добавляем подробную информацию о клиенте в результат
            return { ...client, details: res2.data };
          })
          .catch(error => {
            console.error(`Ошибка при загрузке данных клиента ${client.id}:`, error);
            return { ...client, details: null }; // Если ошибка, клиент без данных
          })
      );

      // Используем Promise.all для параллельной загрузки всех клиентов
      Promise.all(clientRequests)
        .then(clientsDetails => {
          clients.value = clientsDetails;
        })
        .catch(error => {
          console.error('Ошибка при загрузке подробной информации о клиентах:', error);
          ElNotification({
            title: 'Ошибка',
            message: 'Не удалось загрузить подробную информацию о клиентах.',
            type: 'error',
            duration: 3000,
          });
        });
    })
    .catch(error => {
      ElNotification({
        title: 'Ошибка',
        message: 'Не удалось загрузить список клиентов.',
        type: 'error',
        duration: 3000,
      });
    });
};

// Функция для закрытия модального окна
const handleClientsModalClose = () => {
  isClientsModalOpen.value = false;
  clients.value = []; // Сброс списка клиентов
};

// Новая тренировка
const newTraining = ref({
  section: '',
  roomId: '',
  startTime: '',
  endTime: '',
  hours: null,
  availableSlots: 0,
});

// Данные комнат и секций тренера
const rooms = ref<Room[]>([]);
const trainerInfo = ref<any | null>(null);
const trainerSections = computed<string[]>(() => trainerInfo.value?.trainerInfo.sections || []);

// Ограничение выбора на дату (только текущий день и будущее)
const disabledDate = (date: Date) => {
  return dayjs(date).isBefore(dayjs().startOf('day'));
};

// При изменении времени начала тренировки активируем поле "Количество часов"
const onStartTimeChange = () => {
  if (newTraining.value.startTime) {
    newTraining.value.hours = null; // сбросить значение при изменении времени
  }
};

// Проверка валидности формы для добавления тренировки
const isTrainingFormValid = computed(() => {
  return (
    newTraining.value.section &&
    newTraining.value.roomId &&
    newTraining.value.startTime &&
    newTraining.value.hours !== null &&
    newTraining.value.availableSlots >= 0
  );
});

// Форматирование времени для отображения
const formatTime = (time: string) => {
  return dayjs(time).format('HH:mm');
};

// Форматирование даты для отображения
const formatDate = (time: string) => {
  return dayjs(time).format('DD.MM.YYYY');
};

// Определение цвета статуса
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

// Функция сброса фильтров
const resetFilters = () => {
  filterSections.value = [];
  filterStatus.value = [];
  filterTimeRange.value = null;
  sortBy.value = null;
  sortOrder.value = null;
};

// Фильтрация и сортировка тренировок
const filteredSortedTrainings = computed(() => {
  let filtered = trainings.value;

  // Фильтрация по секциям
  if (filterSections.value.length > 0) {
    filtered = filtered.filter(training =>
      filterSections.value.includes(training.section.name)
    );
  }

  // Фильтрация по статусу
  if (filterStatus.value.length > 0) {
    filtered = filtered.filter(training =>
      filterStatus.value.includes(training.status)
    );
  }

  // Фильтрация по диапазону дат и времени
  if (filterTimeRange.value && filterTimeRange.value.length === 2) {
    const [start, end] = filterTimeRange.value;
    const startTimestamp = dayjs(start, 'DD/MM/YYYY HH:mm').valueOf();
    const endTimestamp = dayjs(end, 'DD/MM/YYYY HH:mm').valueOf();

    filtered = filtered.filter(training => {
      const trainingStart = dayjs(training.startTime).valueOf();
      const trainingEnd = dayjs(training.endTime).valueOf();
      return trainingStart >= startTimestamp && trainingEnd <= endTimestamp;
    });
  }

  // Сортировка
  if (sortBy.value) {
    filtered = filtered.slice().sort((a, b) => {
      let compareA: number | string;
      let compareB: number | string;

      switch (sortBy.value) {
        case 'trainer':
          compareA = `${a.trainer.name} ${a.trainer.surname}`.toLowerCase();
          compareB = `${b.trainer.name} ${b.trainer.surname}`.toLowerCase();
          break;
        case 'startTime':
          compareA = dayjs(a.startTime).valueOf();
          compareB = dayjs(b.startTime).valueOf();
          break;
        case 'endTime':
          compareA = dayjs(a.endTime).valueOf();
          compareB = dayjs(b.endTime).valueOf();
          break;
        default:
          compareA = '';
          compareB = '';
      }

      if (compareA < compareB) return sortOrder.value === 'asc' ? -1 : 1;
      if (compareA > compareB) return sortOrder.value === 'asc' ? 1 : -1;
      return 0;
    });
  }

  return filtered;
});

// Загрузка данных при монтировании
const authStore = useAuthStore();

const userInfo = computed<IUserData | null>(() => {
  return authStore.user;
});

const loadTrainings = () => {
  if (userInfo.value?.id) {
    axiosInstance
      .get(`/trainers/${userInfo.value.id}/trainings`)
      .then(res => {
        trainings.value = res.data;
      })
      .catch(error => {
        console.error('Ошибка при загрузке тренировок:', error);
        ElNotification({
          title: 'Ошибка',
          message: 'Не удалось загрузить тренировки.',
          type: 'error',
          duration: 3000,
        });
      });
  }
};

const loadRooms = () => {
  axiosInstance.get('/rooms').then(res => {
    rooms.value = res.data;
  }).catch(error => {
    console.error('Ошибка при загрузке комнат:', error);
    ElNotification({
      title: 'Ошибка',
      message: 'Не удалось загрузить комнаты.',
      type: 'error',
      duration: 3000,
    });
  });
};

const loadTrainerInfo = () => {
  if (userInfo.value?.id) {
    axiosInstance.get(`/trainers/${userInfo.value.id}`).then(res => {
      trainerInfo.value = res.data;
    }).catch(error => {
      console.error('Ошибка при загрузке информации о тренере:', error);
      ElNotification({
        title: 'Ошибка',
        message: 'Не удалось загрузить информацию о тренере.',
        type: 'error',
        duration: 3000,
      });
    });
  }
};

// Добавление тренировки
const addTraining = () => {
  if (newTraining.value.hours && newTraining.value.startTime) {
    const startTime = dayjs(newTraining.value.startTime);
    const endTime = startTime.add(newTraining.value.hours, 'hour');
    newTraining.value.endTime = endTime.toISOString();
  }

  const currentTime = dayjs();
  const trainingStartTime = dayjs(newTraining.value.startTime);

  if (trainingStartTime.isBefore(currentTime)) {
    ElNotification({
      title: 'Ошибка',
      message: 'Время начала тренировки не может быть в прошлом. Пожалуйста, выберите другое время.',
      type: 'error',
      duration: 3000,
    });
    return;
  }

  const startTimeWithOffset = dayjs(newTraining.value.startTime).add(3, 'hour').toISOString();
  const endTimeWithOffset = dayjs(newTraining.value.endTime).add(3, 'hour').toISOString();

  const requestData = {
    section: newTraining.value.section,
    roomId: newTraining.value.roomId,
    startTime: startTimeWithOffset,
    endTime: endTimeWithOffset,
    hours: newTraining.value.hours,
    availableSlots: newTraining.value.availableSlots,
  };

  if (isEditMode.value) {
    // Если режим редактирования, отправляем PUT-запрос
    axiosInstance.put(`/trainings/${newTraining.value.trainingId}`, requestData)
      .then(() => {
        ElNotification({
          title: 'Успех',
          message: 'Тренировка успешно обновлена',
          type: 'success',
          duration: 3000,
        });
        isAddTrainModalOpen.value = false;
        isEditMode.value = false;
        loadTrainings(); // Обновляем список тренировок
      })
      .catch(error => {
        console.error('Ошибка при обновлении тренировки:', error);
        ElNotification({
          title: 'Ошибка',
          message: 'Не удалось обновить тренировку. Попробуйте еще раз.',
          type: 'error',
          duration: 3000,
        });
      });
  } else {
    // Если режим добавления, отправляем POST-запрос
    axiosInstance.post(`/trainers/${userInfo.value?.id}/trainings`, requestData)
      .then(() => {
        ElNotification({
          title: 'Успех',
          message: 'Тренировка успешно добавлена',
          type: 'success',
          duration: 3000,
        });
        isAddTrainModalOpen.value = false;
        loadTrainings(); // Обновляем список тренировок
      })
      .catch(error => {
        console.error('Ошибка при добавлении тренировки:', error);
        ElNotification({
          title: 'Ошибка',
          message: error.response.data?.errors || 'Неизвестная ошибка',
          type: 'error',
          duration: 3000,
        });
      });
  }
};

// Удаление тренировки
const handleDeleteTraining = (training: Training) => {
  axiosInstance.delete(`/trainings/${training.id}`)
    .then(() => {
      ElNotification({
        title: 'Успех',
        message: 'Тренировка успешно удалена',
        type: 'success',
        duration: 3000,
      });
      loadTrainings(); // Обновляем список тренировок после удаления
    })
    .catch(error => {
      console.error('Ошибка при удалении тренировки:', error);
      ElNotification({
        title: 'Ошибка',
        message: 'Не удалось удалить тренировку. Попробуйте еще раз.',
        type: 'error',
        duration: 3000,
      });
    });
};

const isEditMode = ref(false); // Новый флаг для режима редактирования

const handleEditTraining = (training: Training) => {
  // Инициализация данных для редактирования
  newTraining.value = {
    trainingId: training.id,
    section: training.section.name,  // Убедитесь, что это только имя секции
    roomId: training.room.id,        // Идентификатор комнаты
    startTime: dayjs(training.startTime).format('YYYY-MM-DD HH:mm:ss'),  // Форматируем время
    endTime: dayjs(training.endTime).format('YYYY-MM-DD HH:mm:ss'),      // Форматируем время окончания
    hours: dayjs(training.endTime).diff(dayjs(training.startTime), 'hour'), // Рассчитываем количество часов
    availableSlots: training.availableSlots  // Количество доступных мест
  };
  isEditMode.value = true; // Включаем режим редактирования
  isAddTrainModalOpen.value = true;
};

// Загрузка данных при монтировании
onMounted(() => {
  authStore.loadUserDataFromToken();
  loadTrainings();
  loadRooms();
  loadTrainerInfo();
});
</script>

<style scoped lang="scss">
.training-card {
  margin-bottom: 15px;
  border-radius: 5px;
}

.training-info p {
  margin: 0 0 10px;
}

.client-card {
  margin-bottom: 15px;
  border-radius: 5px;
}

.client-info p {
  margin: 0 0 10px;
}
</style>
