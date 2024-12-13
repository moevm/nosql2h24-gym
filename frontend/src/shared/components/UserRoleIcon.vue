<template>
  <component :is="icon"/>
</template>

<script setup lang="ts">
import { getIconByRole } from '@/shared/utils/iconsRegistry.ts';
import { computed } from 'vue';

const props = defineProps({
  role: {
    type: String,
    required: true,
  },
});

const icon = computed(() => getIconByRole(props.role));
</script>