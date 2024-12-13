<template>
  <div>
    <el-text size="large" :type="'primary'" style="font-weight: 700">{{ `${trainer.name} ${trainer.surname}` }}</el-text>
    <spacer></spacer>
    <el-text v-if="trainer.sections" :type="'info'">
      Специализация {{ trainer.sections.join(', ') }}
    </el-text>
    <spacer></spacer>
    <el-text :type="'info'">
      Квалификация: {{ trainer.qualification }}
    </el-text>
    <spacer></spacer>
    <el-text :type="'info'">
      Связь {{ trainer.phoneNumber ?? trainer.email }}
    </el-text>
    <spacer></spacer>
  </div>
</template>

<script setup lang="ts">
import Spacer from '@/shared/components/Spacer.vue';
import { PropType } from 'vue';

const props = defineProps({
  trainer: {
    type: Object as PropType<{
      sections: string[],
      phoneNumber?: string,
      email?: string,
      name: string,
      surname: string,
      qualification: string,
    }>,
    required: true
  }
})
</script>

<style scoped lang="scss">

</style>