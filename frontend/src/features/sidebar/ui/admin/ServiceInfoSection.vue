<template>
  <el-container direction="vertical" style="gap: 15px; padding: 20px;">

    <!-- Кнопка для открытия/закрытия фильтров -->
    <el-button @click="toggleFilters">
      {{ isFilterOpened ? 'Закрыть фильтрацию и поиск' : 'Открыть фильтрацию и поиск' }}
    </el-button>
    <spacer></spacer>

    <!-- Фильтры и сортировка -->
    <el-row v-if="isFilterOpened" style="margin-bottom: 20px; gap: 15px;" type="flex" justify="start">
      <el-col :span="6">
        <el-input v-model="filters.name" placeholder="Фильтр по названию" clearable />
      </el-col>
      <el-col :span="6">
        <el-input
            v-model="filters.minCapacity"
            placeholder="Больше чем"
            clearable
            type="number"
        />
      </el-col>

      <el-col :span="6">
        <el-input
            v-model="filters.maxCapacity"
            placeholder="Меньше чем"
            clearable
            type="number"
        />
      </el-col>
      <el-col :span="6">
        <el-input v-model="filters.address" placeholder="Фильтр по адресу" clearable />
      </el-col>
      <el-col :span="6">
        <el-select v-model="filters.workingDays" placeholder="Фильтр по рабочим дням" multiple clearable>
          <el-option label="Понедельник" value="ПН" />
          <el-option label="Вторник" value="ВТ" />
          <el-option label="Среда" value="СР" />
          <el-option label="Четверг" value="ЧТ" />
          <el-option label="Пятница" value="ПТ" />
          <el-option label="Суббота" value="СБ" />
          <el-option label="Воскресенье" value="ВС" />
        </el-select>
      </el-col>

      <el-col :span="6">
        <el-select v-model="filters.trainers" placeholder="Фильтр по тренерам" multiple clearable>
          <el-option
            v-for="trainer in availableTrainers"
            :key="trainer.id"
            :label="trainer.name + ' ' + trainer.surname"
            :value="trainer.id"
          />
        </el-select>
      </el-col>

      <el-col :span="6">
        <el-select v-model="filters.sections" placeholder="Фильтр по секциям" multiple clearable>
          <el-option
            v-for="section in allTrainerSections"
            :key="section"
            :label="section"
            :value="section"
          />
        </el-select>
      </el-col>

      <el-col :span="6">
        <el-select v-model="sortBy" placeholder="Сортировать по" style="width: 100%;" clearable>
          <el-option label="Название" value="name" />
          <el-option label="Емкость" value="capacity" />
          <el-option label="Адрес" value="address" />
          <el-option label="Рабочие дни" value="workingDays" />
          <el-option label="Часы открытия" value="openingTime" />
          <el-option label="Часы закрытия" value="closingTime" />
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select v-model="sortOrder" placeholder="Порядок" style="width: 100%;" clearable>
          <el-option label="По возрастанию" value="asc" />
          <el-option label="По убыванию" value="desc" />
        </el-select>
      </el-col>

      <el-col :span="6">
        <el-button type="primary" @click="resetFilters">Сбросить</el-button>
      </el-col>
    </el-row>
    <spacer></spacer>

    <!-- Таблица с залами -->
    <el-table
      :data="displayedRooms"
      highlight-current-row
      style="width: 100%;cursor:pointer;"
      @row-click="handleRowClick"
    >
      <el-table-column label="Название" prop="name" sortable/>
      <el-table-column label="Емкость" prop="capacity" sortable/>
      <el-table-column label="Адрес" prop="address" sortable/>
      <el-table-column label="Рабочие дни" prop="workingDays" sortable/>
      <el-table-column label="Часы открытия" prop="openingTime" sortable/>
      <el-table-column label="Часы закрытия" prop="closingTime" sortable/>
      <el-table-column label="Тренеры" prop="trainers" sortable/>
      <el-table-column label="Секции" prop="sections" sortable/>
    </el-table>

    <!-- Модальное окно для просмотра/редактирования зала -->
    <el-dialog
      :model-value="isEditDialogVisible"
      title="Просмотр/Редактирование зала"
      width="50%"
      @close="isEditDialogVisible = false"
    >
      <el-form :model="selectedRoom" label-width="120px">
        <el-form-item label="Название">
          <el-input v-model="selectedRoom.name" :disabled="!editingMode" />
        </el-form-item>
        <el-form-item label="Емкость">
          <el-input v-model.number="selectedRoom.capacity" type="number" min="0" :disabled="!editingMode"/>
        </el-form-item>
        <el-form-item label="Адрес">
          <el-input v-model="selectedRoom.location.address" :disabled="!editingMode"/>
        </el-form-item>
        <el-form-item label="Номер">
          <el-input v-model.number="selectedRoom.location.number" type="number" min="0" :disabled="!editingMode"/>
        </el-form-item>
        <el-form-item label="Рабочие дни">
          <el-select v-model="selectedRoom.workingDays" multiple placeholder="Выберите рабочие дни" clearable :disabled="!editingMode">
            <el-option label="Понедельник" value="ПН" />
            <el-option label="Вторник" value="ВТ" />
            <el-option label="Среда" value="СР" />
            <el-option label="Четверг" value="ЧТ" />
            <el-option label="Пятница" value="ПТ" />
            <el-option label="Суббота" value="СБ" />
            <el-option label="Воскресенье" value="ВС" />
          </el-select>
        </el-form-item>
        <el-form-item label="Часы открытия">
          <el-time-picker
            v-model="selectedRoom.openingTime"
            placeholder="Выберите время"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            clearable
            :disabled="!editingMode"
          />
        </el-form-item>
        <el-form-item label="Часы закрытия">
          <el-time-picker
            v-model="selectedRoom.closingTime"
            placeholder="Выберите время"
            format="HH:mm:ss"
            value-format="HH:mm:ss"
            clearable
            :disabled="!editingMode"
          />
        </el-form-item>
        <el-form-item label="Id тренеров">
          <el-select v-model="selectedRoom.trainers" multiple placeholder="Выберите тренеров" clearable :disabled="!editingMode">
            <el-option
              v-for="trainer in availableTrainers"
              :key="trainer.id"
              :label="trainer.name + ' ' + trainer.surname"
              :value="trainer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Секции">
          <el-select v-model="selectedRoom.sections" multiple placeholder="Секции рассчитываются автоматически" clearable :disabled="true">
            <el-option
              v-for="section in allTrainerSections"
              :key="section"
              :label="section"
              :value="section"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">Закрыть</el-button>
        <el-button v-if="!editingMode" @click="startEditing">Редактировать</el-button>
        <template v-else>
          <el-button @click="cancelEditing">Отменить</el-button>
          <el-button type="primary" @click="saveRoom">Сохранить</el-button>
        </template>
      </div>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import axiosInstance from '@/widgets/axios/index';
import { onMounted, ref, computed, watch } from 'vue';
import { ElMessage } from 'element-plus';

const isFilterOpened = ref(false);
const isEditDialogVisible = ref(false);
const selectedRoom = ref<any>(null);
const availableTrainers = ref<any[]>([]);
const editingMode = ref(false);

const filters = ref({
  name: '',
  minCapacity: '',
  maxCapacity: '',
  address: '',
  workingDays: [] as string[], // ПН, ВТ, ...
  openingTime: '',
  closingTime: '',
  trainers: [] as string[],
  sections: [] as string[],
});


const sortBy = ref('name');
const sortOrder = ref('asc');

const rooms = ref<any[]>([]);
const allTrainerSections = ref<string[]>([]);

const toggleFilters = () => {
  isFilterOpened.value = !isFilterOpened.value;
  if (!isFilterOpened.value) {
    resetFilters();
  }
};

const resetFilters = () => {
  filters.value = {
    name: '',
    capacity: '',
    address: '',
    workingDays: [],
    openingTime: '',
    closingTime: '',
    trainers: [],
    sections: [],
  };
  sortBy.value = 'name';
  sortOrder.value = 'asc';
};

onMounted(() => {
  loadRooms();
  loadAvailableTrainers();
});

const loadRooms = () => {
  axiosInstance.get('/rooms').then(res => {
    // workingDays приходит строкой типа "ПН, СР, СБ", преобразуем в массив ["ПН","СР","СБ"]
    rooms.value = res.data.map((room: any) => {
      const wd = room.workingDays ? room.workingDays.split(',').map((d: string) => d.trim()) : [];
      return {
        ...room,
        workingDays: wd
      };
    });
  }).catch(error => {
    console.error('Ошибка при загрузке залов:', error);
    ElMessage.error('Не удалось загрузить залы.');
  });
};

const loadAvailableTrainers = () => {
  axiosInstance.get('/trainers').then(res => {
    availableTrainers.value = res.data;
    const sectionsSet = new Set<string>();
    availableTrainers.value.forEach((trainer: any) => {
      if (trainer.sections && Array.isArray(trainer.sections)) {
        trainer.sections.forEach((section: string) => sectionsSet.add(section));
      }
    });
    allTrainerSections.value = Array.from(sectionsSet);
  }).catch(error => {
    console.error('Ошибка при загрузке тренеров:', error);
    ElMessage.error('Не удалось загрузить тренеров.');
  });
};

const handleRowClick = (row: any) => {
  axiosInstance.get(`/rooms/${row.id}`).then(res => {
    const r = res.data;
    const wd = r.workingDays ? r.workingDays.split(',').map((d:string)=> d.trim()) : [];
    selectedRoom.value = {
      ...r,
      workingDays: wd
    };
    isEditDialogVisible.value = true;
    editingMode.value = false;
  }).catch(error => {
    console.error('Ошибка при загрузке данных зала:', error);
    ElMessage.error('Не удалось загрузить данные зала.');
  });
};

const startEditing = () => {
  editingMode.value = true;
};

const cancelEditing = () => {
  axiosInstance.get(`/rooms/${selectedRoom.value.id}`).then(res => {
    const r = res.data;
    const wd = r.workingDays ? r.workingDays.split(',').map((d:string)=> d.trim()) : [];
    selectedRoom.value = {
      ...r,
      workingDays: wd
    };
    editingMode.value = false;
  }).catch(error => {
    console.error('Ошибка при загрузке данных зала:', error);
    ElMessage.error('Не удалось загрузить данные зала.');
  });
};

const closeDialog = () => {
  isEditDialogVisible.value = false;
};

// Пересчет секций при изменении trainers
watch(() => selectedRoom.value?.trainers, (newVal) => {
  if (!editingMode.value) return;
  if (!newVal || newVal.length === 0) {
    selectedRoom.value.sections = [];
    return;
  }

  const selectedTrainersData = availableTrainers.value.filter((t: any) => newVal.includes(t.id));
  const newSections = new Set<string>();
  selectedTrainersData.forEach((t: any) => {
    if (t.sections && Array.isArray(t.sections)) {
      t.sections.forEach((sec: string) => newSections.add(sec));
    }
  });
  selectedRoom.value.sections = Array.from(newSections);
});

const saveRoom = () => {
  const openingTime = extractTime(selectedRoom.value.openingTime);
  const closingTime = extractTime(selectedRoom.value.closingTime);

  const payload = {
    name: selectedRoom.value.name,
    capacity: Number(selectedRoom.value.capacity),
    location: {
      address: selectedRoom.value.location.address,
      number: selectedRoom.value.location.number,
    },
    workingDays: selectedRoom.value.workingDays.join(', '), // При сохранении обратно в строку
    openingTime: openingTime,
    closingTime: closingTime,
    trainers: selectedRoom.value.trainers,
    sections: selectedRoom.value.sections,
  };

  axiosInstance.put(`/rooms/${selectedRoom.value.id}`, payload).then(() => {
    ElMessage.success('Зал успешно обновлен.');
    editingMode.value = false;
    loadRooms();
  }).catch(error => {
    console.error('Ошибка при обновлении зала:', error);
    ElMessage.error('Не удалось обновить зал.');
  });
};

function extractTime(timeValue: any) {
  if (!timeValue) {
    return '00:00:00';
  }
  if (typeof timeValue === 'string') {
    if (timeValue.match(/^\d{2}:\d{2}:\d{2}$/)) {
      return timeValue;
    }
    return '00:00:00';
  }
  if (timeValue instanceof Date) {
    const hh = String(timeValue.getHours()).padStart(2,'0');
    const mm = String(timeValue.getMinutes()).padStart(2,'0');
    const ss = String(timeValue.getSeconds()).padStart(2,'0');
    return `${hh}:${mm}:${ss}`;
  }
  return '00:00:00';
}

const displayedRooms = computed(() => {
  // Сначала фильтруем и сортируем оригинальные данные (rooms)
  let filtered = rooms.value.slice();

  // Фильтрация
  if (filters.value.name) {
    filtered = filtered.filter(r => (r.name ?? '').toLowerCase().includes(filters.value.name.toLowerCase()));
  }

  if (filters.value.minCapacity) {
    filtered = filtered.filter(r => r.capacity >= Number(filters.value.minCapacity));
  }

  if (filters.value.maxCapacity) {
    filtered = filtered.filter(r => r.capacity <= Number(filters.value.maxCapacity));
  }

  if (filters.value.address) {
    filtered = filtered.filter(r => {
      const addr = (r.location?.address ?? 'Не указано').toLowerCase();
      const num = r.location?.number != null ? r.location.number : '';
      const fullAddress = (addr + ', ' + num).toLowerCase();
      return fullAddress.includes(filters.value.address.toLowerCase());
    });
  }

  // Фильтрация по рабочим дням
  if (filters.value.workingDays.length > 0) {
    filtered = filtered.filter(r => {
      if (!Array.isArray(r.workingDays) || r.workingDays.length === 0) return false;
      // Проверяем, что все выбранные в фильтре дни присутствуют в массиве r.workingDays
      return filters.value.workingDays.every(dayFilter => r.workingDays.includes(dayFilter));
    });
  }

  // Фильтрация по секциям
  if (filters.value.sections.length > 0) {
    filtered = filtered.filter(r => {
      if (!Array.isArray(r.sections) || r.sections.length === 0) return false;
      return filters.value.sections.every(sec => r.sections.includes(sec));
    });
  }

  // Сортировка
  if (sortBy.value) {
    filtered.sort((a, b) => {
      let aValue: any = '';
      let bValue: any = '';

      switch (sortBy.value) {
        case 'name':
          aValue = (a.name ?? '').toLowerCase(); bValue = (b.name ?? '').toLowerCase();
          break;
        case 'capacity':
          aValue = a.capacity ?? Number.MAX_SAFE_INTEGER;
          bValue = b.capacity ?? Number.MAX_SAFE_INTEGER;
          break;
        case 'address':
          const aAddr = ((a.location?.address ?? '') + ', ' + (a.location?.number ?? '')).toLowerCase();
          const bAddr = ((b.location?.address ?? '') + ', ' + (b.location?.number ?? '')).toLowerCase();
          aValue = aAddr; bValue = bAddr;
          break;
        case 'workingDays':
          // workingDays - массив, для сравнения превращаем в строку
          aValue = Array.isArray(a.workingDays) ? a.workingDays.join(', ').toLowerCase() : '';
          bValue = Array.isArray(b.workingDays) ? b.workingDays.join(', ').toLowerCase() : '';
          break;
        case 'openingTime':
          aValue = a.openingTime ?? '';
          bValue = b.openingTime ?? '';
          break;
        case 'closingTime':
          aValue = a.closingTime ?? '';
          bValue = b.closingTime ?? '';
          break;
        default:
          aValue = '';
          bValue = '';
      }

      if (typeof aValue === 'string') aValue = aValue.toLowerCase();
      if (typeof bValue === 'string') bValue = bValue.toLowerCase();

      if (aValue < bValue) return sortOrder.value === 'asc' ? -1 : 1;
      if (aValue > bValue) return sortOrder.value === 'asc' ? 1 : -1;
      return 0;
    });
  }

  // Преобразуем данные для отображения
  return filtered.map((room: any) => {
    const wd = Array.isArray(room.workingDays) && room.workingDays.length > 0 ? room.workingDays.join(', ') : 'Не указано';
    const opening = room.openingTime || 'Не указано';
    const closing = room.closingTime || 'Не указано';
    const trainerNames = getTrainerNames(room.trainers);
    const sec = Array.isArray(room.sections) && room.sections.length > 0 ? room.sections.join(', ') : 'Не указано';

    return {
      name: room.name ?? 'Не указано',
      capacity: room.capacity ?? 'Не указано',
      address: room.location && room.location.address ? room.location.address : 'Не указано',
      number: room.location && room.location.number != null ? room.location.number : 'Не указано',
      workingDays: wd,
      openingTime: opening,
      closingTime: closing,
      trainers: trainerNames,
      sections: sec,
      id: room.id ?? ''
    };
  });
});

function getTrainerNames(trainerIds: string[]) {
  if(!trainerIds || trainerIds.length===0) return 'Не указано';
  const selected = availableTrainers.value.filter((t:any)=> trainerIds.includes(t.id));
  if (selected.length===0) return 'Не указано';
  return selected.map((t:any)=>t.name+' '+t.surname).join(', ');
}

watch(isEditDialogVisible, (val) => {
  if (!val) {
    editingMode.value = false;
  }
});
</script>

<style scoped lang="scss">
.dialog-footer {
  text-align: right;
}
</style>
