<template>
  <div class="searchable-select">
    <label>{{ label }} <span v-if="required" style="color:red">*</span></label>
    <div class="input-wrapper">
      <input 
        type="text" 
        v-model="searchQuery" 
        @focus="isOpen = true" 
        @blur="handleBlur" 
        :placeholder="placeholder"
        :class="{ 'invalid': isInvalid }"
      />
      <ul v-if="isOpen && filteredOptions.length > 0" class="dropdown">
        <li v-for="opt in filteredOptions" :key="opt.id" @mousedown.prevent="select(opt)">
          {{ opt.name }}
        </li>
      </ul>
    </div>
    <span v-if="isInvalid" class="error-msg">Không tìm thấy dữ liệu hợp lệ</span>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
const props = defineProps(['options', 'modelValue', 'label', 'placeholder', 'required']);
const emit = defineEmits(['update:modelValue']);

const searchQuery = ref('');
const isOpen = ref(false);

const filteredOptions = computed(() => {
  return props.options.filter(o => o.name.toLowerCase().includes(searchQuery.value.toLowerCase()));
});

// Validate: Nếu có text mà không tìm thấy trong options thì báo lỗi
const isInvalid = computed(() => {
  return props.required && searchQuery.value !== '' && !props.options.find(o => o.name === searchQuery.value);
});

const select = (opt) => {
  searchQuery.value = opt.name;
  emit('update:modelValue', opt.id);
  isOpen.value = false;
};

const handleBlur = () => { setTimeout(() => isOpen.value = false, 200); };
</script>

<style scoped>
.searchable-select { position: relative; margin-bottom: 15px; }
.input-wrapper { position: relative; }
.dropdown { position: absolute; top: 100%; left: 0; right: 0; border: 1px solid #ccc; background: white; z-index: 99; max-height: 150px; overflow-y: auto; list-style: none; padding: 0; }
.dropdown li { padding: 8px; cursor: pointer; }
.dropdown li:hover { background: #eee; }
.invalid { border: 1px solid red; }
.error-msg { color: red; font-size: 12px; }
</style>