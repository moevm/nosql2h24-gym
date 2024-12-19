<template>
  <el-dialog
    :model-value="isOpened"
    title="Информация о тренере"
    width="50%"
    @close="emits('close')"
  >
    <TrainerInfo readonly  :trainer-data="trainerData"/>
    <template #footer>
      <el-button @click="emits('close')">Закрыть</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import TrainerInfo from "@/features/sidebar/ui/trainer/components/TrainerInfo.vue";

const props = defineProps({
  trainerData: {
    type: String,
    required: true,
  },
  isOpened: {
    type: Boolean,
    required: true,
  },
});

const emits = defineEmits([ 'close' ]);

</script>

<style scoped lang="scss">

</style>