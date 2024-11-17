
<template>
  <div :style="{ margin: `${size}px` }"></div>
</template>

<script setup lang="ts">
defineProps({
  size: {
    type: String,
    default: "10", // По умолчанию 10px
  },
});
</script>

<style scoped>
</style>