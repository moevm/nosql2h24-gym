<template>
  <el-button @click="emits('close')">
    Назад
  </el-button>
  <spacer></spacer>
  <div v-if="userInfo">
    <el-card>
      <el-form :model="userInfo" :disabled="!editMode" label-width="120px">
        <el-form-item label="Имя" label-position="top">
          <el-input v-model="userInfo.name" />
        </el-form-item>
        <el-form-item label="Фамилия" label-position="top">
          <el-input v-model="userInfo.surname" />
        </el-form-item>
        <el-form-item label="Email" label-position="top">
          <el-input v-model="userInfo.email" />
        </el-form-item>
        <el-form-item label="Пароль" label-position="top">
          <el-input v-model="userInfo.password" :type="editMode ? 'text' : 'password'" />
        </el-form-item>
        <el-form-item label="Дата регистрации" label-position="top">
          <el-date-picker v-model="userInfo.createdDate" disabled readonly format="DD-MM-YYYY"/>
        </el-form-item>
        <el-form-item label="Пол" label-position="top">
          <el-select v-model="userInfo.gender" placeholder="Выберите пол">
            <el-option label="Женский" value="FEMALE" />
            <el-option label="Мужской" value="MALE" />
          </el-select>
        </el-form-item>
        <el-form-item label="Дата рождения" label-position="top">
          <el-date-picker v-model="userInfo.birthday" type="date" placeholder="Выберите дату" format="DD-MM-YYYY"/>
        </el-form-item>

        <template v-if="props.role === 'ROLE_TRAINER'">
          <el-form-item label="Комментарий" label-position="top">
            <el-input v-model="userInfo.comment" />
          </el-form-item>
          <el-form-item label="Номер телефона" label-position="top">
            <el-input v-model="userInfo.phoneNumber" />
          </el-form-item>
          <el-form-item label="Ставка (час)" label-position="top">
            <el-input v-model="userInfo.trainerInfo.hourlyRate" type="number" />
          </el-form-item>
          <el-form-item label="Квалификация" label-position="top">
            <el-input v-model="userInfo.trainerInfo.qualification" />
          </el-form-item>
          <el-form-item label="Секции" label-position="top">
            <el-tag
              v-for="(section, index) in userInfo.trainerInfo.sections"
              :key="index"
              closable
              @close="removeSection(index)"
            >
              {{ JSON.parse(section).name }}
            </el-tag>
            <el-input
              v-model="newSection"
              placeholder="Добавить секцию"
              size="small"
              @keyup.enter="addSection"
            />
          </el-form-item>
        </template>

        <template v-if="props.role === 'ROLE_USER'">
          <h3>Информация об абонементе</h3>
          <el-card v-for="subscription in userInfo.clientInfo.subscriptions" :key="subscription.startDate" >
            <p>
              <el-text type="primary">Статус:</el-text>
              <span :style="{ color: subscription.status === 'ACTIVE' ? 'green' : 'blue', fontWeight: 700 }">{{ subscription.status }}</span>
            </p>
            <p>
              <el-text type="primary">Дата начала:</el-text> {{ formatDate(subscription.startDate) }}
            </p>
            <p>
              <el-text type="primary">Дата окончания:</el-text> {{ formatDate(subscription.endDate) }}
            </p>
            <p>
              <el-text type="primary">Оставшиеся дни:</el-text> {{ subscription.restDays }}
            </p>
            <div style="display: flex; gap: 10px; margin-top: 15px">
              <el-button @click="openExtendForm(subscription)" type="primary">
                Продлить
              </el-button>
              <el-button @click="toggleFreeze" type="danger">
                Заморозить
              </el-button>
            </div>
            <!-- Форма продления -->
            <el-card
              class="card-dark"
              v-if="showExtendForm"
              @close="closeExtendForm"
            >
              <h3>Продление абонемента</h3>
              <el-form :model="extendForm">
                <el-form-item label="Количество дней" :label-position="'top'">
                  <el-input v-model="extendForm.days" type="number"/>
                </el-form-item>
                <el-form-item label="Цена" :label-position="'top'">
                  <el-input v-model="extendForm.price" type="number"/>
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
                Вы уверены что хотите заморозить абонемент?
              </el-text>
              <Spacer></Spacer>

              <el-container style="justify-content:space-between">
                <el-button @click="submitFreeze" type="danger">
                  Заморозить
                </el-button>
                <el-button type="primary">
                  Отменить
                </el-button>
              </el-container>
            </el-dialog>
          </el-card>
        </template>
      </el-form>

      <div style="margin-top: 15px; text-align: right">
        <el-button v-if="!editMode" type="primary" @click="toggleEdit">Редактировать</el-button>
        <el-button v-else type="danger" @click="cancelEdit">Отменить</el-button>
        <el-button v-if="editMode" type="success" @click="saveChanges">Сохранить</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { ref, onMounted } from 'vue';
import axiosInstance from '@/widgets/axios/index.ts';
import { formatDate } from '@/shared/utils/formatDate.ts';

const props = defineProps({
  userId: {
    type: String,
    required: true,
  },
  role: {
    type: String,
    required: true,
  },
});

const extendForm = ref({
  days: null,
  price: null
});

const emits = defineEmits(['close'])

const submitExtend = () => {
  axiosInstance
    .put(`/subscriptions/${ props.userId }/extend`, {
      duration: extendForm.value.days,
      price: extendForm.value.price
    })
    .then(() => {
      alert('Абонемент продлен')
      closeExtendForm();
      refreshUserInfo();
    });
};

const refreshUserInfo = () => {
  const endpointMap = {
    ROLE_ADMIN: `/admins/${props.userId}`,
    ROLE_TRAINER: `/trainers/${props.userId}`,
    ROLE_USER: `/clients/${props.userId}`,
  };

  axiosInstance.get(endpointMap[props.role]).then((res) => {
    userInfo.value = res.data;
  });
};

const userInfo = ref<any | null>(null);
const editMode = ref(false);
const originalUserInfo = ref<any | null>(null);
const newSection = ref('');
const showExtendForm = ref(false);

const toggleEdit = () => {
  editMode.value = true;
  originalUserInfo.value = JSON.parse(JSON.stringify(userInfo.value));
};

const cancelEdit = () => {
  editMode.value = false;
  userInfo.value = JSON.parse(JSON.stringify(originalUserInfo.value));
};

const saveChanges = () => {
  const endpointMap = {
    ROLE_ADMIN: `/admins/${props.userId}`,
    ROLE_TRAINER: `/trainers/${props.userId}`,
    ROLE_USER: `/clients/${props.userId}`,
  };

  axiosInstance.put(endpointMap[props.role], userInfo.value).then(() => {
    editMode.value = false;
  });
};

const addSection = () => {
  if (newSection.value.trim()) {
    userInfo.value.trainerInfo.sections.push(JSON.stringify({ name: newSection.value.trim() }));
    newSection.value = '';
  }
};

const removeSection = (index: number) => {
  userInfo.value.trainerInfo.sections.splice(index, 1);
};

const openExtendForm = (subscription: any) => {
  showExtendForm.value = true;
};

const closeExtendForm = () => {
  showExtendForm.value = false;
};

const isFreezeModalOpen = ref(false);

const toggleFreeze = () => {
  isFreezeModalOpen.value = !isFreezeModalOpen.value;
};

const submitFreeze = () => {
  axiosInstance.put(`/subscriptions/${props.userId}/freeze`).then(() => {
    alert('Абонемент заморожен');
    refreshUserInfo();
    toggleFreeze();
  });
}

onMounted(() => {
  const endpointMap = {
    ROLE_ADMIN: `/admins/${props.userId}`,
    ROLE_TRAINER: `/trainers/${props.userId}`,
    ROLE_USER: `/clients/${props.userId}`,
  };

  axiosInstance.get(endpointMap[props.role]).then((res) => {
    userInfo.value = res.data;
  });
});
</script>

<style scoped lang="scss"></style>
