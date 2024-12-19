<template>
  <div v-if="trainerData">
    <el-text><strong>Имя:</strong> {{ trainerData.name }}</el-text>
    <spacer></spacer>
    <el-text><strong>Фамилия:</strong> {{ trainerData.surname }}</el-text>
    <spacer></spacer>
    <el-text><strong>Email:</strong> {{ trainerData.email }}</el-text>
    <spacer></spacer>
    <el-text><strong>Номер телефона:</strong> {{ trainerData.phoneNumber }}</el-text>
    <spacer></spacer>
    <el-text><strong>Квалификация:</strong> {{ trainerData.qualification }}</el-text>
    <spacer></spacer>
    <el-text><strong>Часовая ставка:</strong> {{ trainerData.hourlyRate }} руб/час</el-text>
    <spacer></spacer>
    <el-text><strong>Опыт:</strong> {{ trainerData.experience || "Не указан" }}</el-text>
    <spacer></spacer>
    <el-text><strong>Специализации:</strong></el-text>
    <ul style="padding-left: 30px">
      <li v-for="(section, index) in trainerData.sections" :key="index">
        {{ section }}
      </li>
    </ul>
    <spacer></spacer>
    <el-text><strong>Пол:</strong> {{ trainerData.gender === "MALE" ? "Мужской" : "Женский" }}</el-text>
    <spacer></spacer>
    <el-text><strong>Дата рождения:</strong> {{ formatDate(trainerData.birthday) }}</el-text>
  </div>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { defineProps } from "vue";

defineProps({
  trainerData: {
    type: Object,
    required: true,
  },
});

const formatDate = (date: string): string => {
  return new Date(date).toLocaleDateString("ru-RU", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};
</script>

<style scoped lang="scss">
.trainer-info {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;

  h2 {
    margin-bottom: 20px;
  }

  ul {
    margin: 10px 0;
    padding-left: 20px;

    li {
      margin: 5px 0;
    }
  }
}
</style>
