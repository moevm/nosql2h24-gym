<template>
  <el-container class="login">
    <el-main style="display: flex; justify-content:center; align-items: center">
      <el-space direction="vertical">
        <el-text size="large" style="color: white; font-weight: 700">
          {{ isRegisterMode ? 'РЕГИСТРАЦИЯ' : 'ВХОД' }}
        </el-text>
        
        <el-form>
          <el-form-item label="Почта" label-position="top">
            <el-input v-model="formData.email" style="width: 200px" />
          </el-form-item>
          
          <el-form-item label="Пароль" label-position="top">
            <el-input v-model="formData.password" style="width: 200px" type="password" />
          </el-form-item>
          
          <!-- Поля для регистрации -->
          <template v-if="isRegisterMode">
            <el-form-item label="Имя" label-position="top">
              <el-input v-model="formData.name" style="width: 200px" />
            </el-form-item>
            
            <el-form-item label="Фамилия" label-position="top">
              <el-input v-model="formData.surname" style="width: 200px" />
            </el-form-item>
            
            <el-form-item label="Телефон" label-position="top">
              <el-input v-model="formData.phoneNumber" style="width: 200px" />
            </el-form-item>
          </template>
          
          <el-button @click="handleSubmit" type="primary">
            {{ isRegisterMode ? 'Зарегистрироваться' : 'Войти' }}
          </el-button>
          
          <el-form-item>
            <el-link @click="toggleMode">
              {{ isRegisterMode ? 'Уже зарегистрированы?' : 'Не зарегистрированы?' }}
            </el-link>
          </el-form-item>
        </el-form>
      </el-space>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import axiosInstance from "../../widgets/axios";
import router from "../../app/router";
import { useAuthStore } from "../../widgets/store/auth";

const authStore = useAuthStore();

const formData = reactive({
  name: 'Иван',
  surname: 'Иванов',
  password: '',
  email: '',
  phoneNumber: '+79998887766',
});

const isRegisterMode = ref(false);

const toggleMode = () => {
  isRegisterMode.value = !isRegisterMode.value;
  formData.name = 'Иван';
  formData.surname = 'Иванов';
  formData.phoneNumber = '+79998887766';
};

const handleSubmit = async () => {
  try {
    if (isRegisterMode.value) {
      // Режим регистрации
      await axiosInstance.post('/register', {
        name: formData.name,
        surname: formData.surname,
        password: formData.password,
        email: formData.email,
        phoneNumber: formData.phoneNumber,
      });
      
      await router.push('/login');
    } else {
      // Режим входа
      await axiosInstance.post('/login', {
        password: formData.password,
        email: formData.email,
      }).then(res => {
        const tokens: {
          accessToken: string,
          refreshToken: string,
        } = res.data;
        
        authStore.setAuthToken(tokens.accessToken)
      });
      
      await router.push('/profile');
    }
    
    // Переход на главную после успешного запроса
  } catch (error) {
    console.error("Ошибка при отправке формы:", error);
  }
};
</script>

<style scoped>
.login {
  height: 100vh;
}
</style>
